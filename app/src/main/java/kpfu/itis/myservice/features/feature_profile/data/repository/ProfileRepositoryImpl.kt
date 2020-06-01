package kpfu.itis.myservice.features.feature_profile.data.repository

import com.vk.api.sdk.VK
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.common.Mapper
import kpfu.itis.myservice.data.db.dao.FavoriteDao
import kpfu.itis.myservice.data.db.dao.UserDao
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_profile.data.model.VKUser
import kpfu.itis.myservice.features.feature_profile.data.network.UserFirebase
import kpfu.itis.myservice.features.feature_profile.data.network.VKUsersRequest

class ProfileRepositoryImpl(
    private val dao: UserDao,
    private val favoriteDao: FavoriteDao,
    private val firebase: UserFirebase,
    private val mapper: Mapper
) : ProfileRepository {

    override fun getUser(id: Long): Single<User> =
        firebase.getUser(id)
            .observeOn(Schedulers.io())
            .flatMap { user ->
                getUserLocal(id)
                    .doOnSuccess { updateUserLocal(it) }
                    .doOnError { saveUserLocal(user) }
            }
            .onErrorResumeNext {
                getUserLocal(id)
            }

    private fun getUserLocal(id: Long): Single<User> =
        dao.getUserLocalById(id)

    override fun authUser(id: Long) : Completable =
        Single.fromCallable {
            VK.executeSync(VKUsersRequest(intArrayOf(id.toInt())))
        }
            .observeOn(Schedulers.io())
            .flatMapCompletable { res ->
                firebase.getUser((res[0]).id)
                    .observeOn(Schedulers.io())
                    .flatMapCompletable { user ->
                        firebase.updateUser(mapper.map(user, res[0]))
                            .observeOn(Schedulers.io())
                            .doOnComplete { saveUserLocal(mapper.map(user, res[0])) }
                    }
                    .onErrorResumeNext {
                        firebase.saveUser(mapper.map(res[0]))
                            .observeOn(Schedulers.io())
                            .doOnComplete { saveUserLocal(res[0]) }
                    }
            }
            .onErrorResumeNext {
                Completable.error(it)
            }


    override fun updateUser(user: User) : Completable =
        firebase.updateUser(user)
            .observeOn(Schedulers.io())
            .doOnComplete { dao.updateUserLocal(user) }
            .doOnError { Completable.error(it) }

    private fun updateUserLocal(userLocal: User) =
        dao.run {
            updateUserLocal(userLocal)
            getUserLocalByIdForSup(userLocal.vk_id)
        }

    override fun addDescription(id: Long, description: String) : Completable =
        firebase.updateDescription(id, description)
            .observeOn(Schedulers.io())
            .doOnComplete { dao.updateDescrription(id, description) }
            .doOnError { Completable.error(it) }

    override fun deleteDescription(id: Long) : Completable =
        firebase.deleteDescription(id)
            .observeOn(Schedulers.io())
            .doOnComplete { dao.updateDescrription(id, null) }
            .doOnError { Completable.error(it) }

    override fun exit(id: Long) : Completable =
        Single.just(true)
            .observeOn(Schedulers.io())
            .flatMapCompletable {
                dao.clear(id)
                Completable.complete()
            }

    override fun getServices(id: Long): Single<List<Service>> =
        firebase.getServices(id)

    override fun addFavorite(id: Long, userId: Long): Completable =
        firebase.getService(id, userId)
            .observeOn(Schedulers.io())
            .flatMapCompletable {
                favoriteDao.add(mapper.mapToFavorite(it))
            }

    override fun deleteFavorite(id: Long, userId: Long): Completable =
        Single.just(true)
            .observeOn(Schedulers.io())
            .flatMapCompletable { favoriteDao.delete(id) }

    override fun getFavorites(): Single<List<Favorite>> =
        Single.just(listOf<Favorite>())
            .observeOn(Schedulers.io())
            .flatMap { favoriteDao.get() }

    private fun saveUserLocal(user: VKUser) =
        dao.insertUserLocal(mapper.map(user))

    private fun saveUserLocal(user: User) =
        dao.insertUserLocal(user)

}

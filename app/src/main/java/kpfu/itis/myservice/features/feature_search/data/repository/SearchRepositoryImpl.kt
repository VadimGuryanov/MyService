package kpfu.itis.myservice.features.feature_search.data.repository

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.common.Mapper
import kpfu.itis.myservice.data.db.dao.FavoriteDao
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_search.data.di.SearchDataScope
import kpfu.itis.myservice.features.feature_search.data.network.SearchFirebase
import javax.inject.Inject

@SearchDataScope
class SearchRepositoryImpl @Inject constructor(
    private var firebase: SearchFirebase,
    private var dao: FavoriteDao,
    private var mapper: Mapper
) : SearchRepository {

    override fun getServices(): Single<List<Service>> =
        firebase.getServices()

    override fun getServices(query: String): Single<List<Service>> =
        firebase.getServicesByTitle(query)

    override fun getService(id: Long, userId: Long): Single<Service> =
        firebase.getService(id, userId)

    override fun addFavorite(id: Long, userId: Long): Completable =
        firebase.getService(id, userId)
            .observeOn(Schedulers.io())
            .flatMapCompletable {
                dao.add(mapper.mapToFavorite(it))
            }

    override fun deleteFavorite(id: Long, userId: Long): Completable =
        Single.just(true)
            .observeOn(Schedulers.io())
            .flatMapCompletable { dao.delete(id) }

    override fun getFavorites(): Single<List<Favorite>> =
        Single.just(listOf<Favorite>())
            .observeOn(Schedulers.io())
            .flatMap { dao.get() }

    override fun sendMessage(message: Notification): Completable =
        firebase.sendMessage(message)

    override fun getCount(): Single<Long> =
        firebase.getCount()

    override fun getUser(id: Long): Single<User> =
        firebase.getUser(id)

}

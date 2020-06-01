package kpfu.itis.myservice.features.feature_profile.domain

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.common.exceptions.DatabaseException
import kpfu.itis.myservice.common.exceptions.NetworkException
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_profile.data.repository.ProfileRepository
import kpfu.itis.myservice.features.feature_profile.presentation.edit.dto.UserDto

class ProfileInteractorImpl (
    private val repository: ProfileRepository,
    private val helper: HelperSharedPreferences
) : ProfileInteractor{

    override fun auth() : Completable =
        if (!isAuth()) {
            helper.readID()?.let {
                repository.authUser(it.toLong())
                    .doOnComplete { saveSession() }
            } ?: Completable.error(NetworkException("VK auth error"))
        } else {
            Completable.complete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getUser(id :Long): Single<User> =
        repository.getUser(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun updateUser(userDto: UserDto) : Completable =
            if (isAuth()) {
                userDto.run {
                    var user = User()
                    user.vk_id = helper.readID()?.toLong() ?: -1
                    user.name = name
                    user.lastName = lastName
                    user.city = city
                    user.mobilePhone = mobilePhone
                    user.university = university
                    user.faculty = faculty
                    user.job = job
                    user.socialUrl = socialUrl
                    user.photoURL = photoUrl
                    user.description = description
                    repository.updateUser(user)
                }
            } else {
                Completable.error(Throwable("Вы не авторизованы"))
            }

    override fun addDescription(description: String) : Completable =
        helper.readID()?.let {
            repository.addDescription(it.toLong(), description)
        } ?: Completable.error(Throwable("Вы не авторизованы"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun deleteDescription(): Completable =
        helper.readID()?.let {
            repository.deleteDescription(it.toLong())
        } ?: Completable.error(Throwable("Вы не авторизованы"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun isAuth(): Boolean =
        helper.readSession()?.let {
            helper.readID().let {
                Log.e("id", it)
                it?.toLong() ?: -1 >= 0
            }
        } ?: false

    override fun saveSession() {
        helper.editSession(getID().toString())
    }

    override fun getID() : Long =
        helper.readID()?.toLong() ?: -1

    override fun getServices(id: Long): Single<List<Service>> =
        repository.getServices(id)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())

    override fun addFavorite(id: Long, userId: Long): Completable =
        repository.addFavorite(id, userId)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())

    override fun deleteFavorite(id: Long, userId: Long): Completable =
        repository.deleteFavorite(id, userId)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())

    override fun getFavorites(): Single<List<Favorite>> =
        if (isAuth()) {
            repository.getFavorites()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } else {
            Single.just(listOf())
        }

    override fun exit() : Completable =
        if (isAuth()) {
            repository.exit(helper.readID()?.toLong() ?: -1)
                .doOnComplete { helper.clear() }
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } else {
            Completable.error(Exception("Вы не авторизованы"))
        }

}

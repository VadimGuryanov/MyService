package kpfu.itis.myservice.features.feature_profile.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_profile.presentation.edit.dto.UserDto

interface ProfileInteractor {

    fun auth(): Completable

    fun getUser(id : Long) : Single<User>

    fun addDescription(description: String): Completable

    fun updateUser(userDto: UserDto) : Completable

    fun deleteDescription() : Completable

    fun isAuth() : Boolean

    fun saveSession()

    fun getID() : Long

    fun getServices(id: Long): Single<List<Service>>

    fun addFavorite(id: Long, userId: Long) : Completable

    fun deleteFavorite(id: Long, userId: Long) : Completable

    fun getFavorites(): Single<List<Favorite>>

    fun exit() : Completable

}

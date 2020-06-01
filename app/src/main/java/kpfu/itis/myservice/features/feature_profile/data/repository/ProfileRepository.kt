package kpfu.itis.myservice.features.feature_profile.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User

interface ProfileRepository {

    fun getUser(id : Long) : Single<User>

    fun updateUser(user: User): Completable

    fun authUser(id: Long) : Completable

    fun addDescription(id: Long, description: String): Completable

    fun deleteDescription(id: Long): Completable

    fun exit(id: Long): Completable

    fun getServices(id: Long): Single<List<Service>>

    fun addFavorite(id: Long, userId: Long): Completable

    fun deleteFavorite(id: Long, userId: Long): Completable

    fun getFavorites() : Single<List<Favorite>>

}

package kpfu.itis.myservice.features.feature_search.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User

interface SearchRepository {

    fun getServices(): Single<List<Service>>
    fun getServices(query: String) : Single<List<Service>>
    fun getService(id: Long, userId: Long) : Single<Service>
    fun addFavorite(id: Long, userId: Long): Completable
    fun deleteFavorite(id: Long, userId: Long): Completable
    fun getFavorites() : Single<List<Favorite>>
    fun sendMessage(message: Notification): Completable
    fun getCount(): Single<Long>
    fun getUser(id: Long): Single<User>

}

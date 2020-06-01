package kpfu.itis.myservice.features.feature_search.domain

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User

interface SearchInteractor {

    fun getServices(): Single<List<Service>>
    fun getServices(query: String) : Single<List<Service>>
    fun addFavorite(id: Long, userId: Long) : Completable
    fun deleteFavorite(id: Long, userId: Long) : Completable
    fun isAuth() : Boolean
    fun getFavorites(): Single<List<Favorite>>
    fun getService(id: Long, userId: Long): Single<Service>
    fun isEqualsAuthor(id: Long): Boolean
    fun sendMessage(message: Notification): Completable
    fun getUser(id: Long): Single<User>
    fun getUser(): Single<User>
    fun getId(): Long

}

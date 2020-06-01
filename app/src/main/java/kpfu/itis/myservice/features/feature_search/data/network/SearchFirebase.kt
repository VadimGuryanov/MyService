package kpfu.itis.myservice.features.feature_search.data.network

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User

interface SearchFirebase {

    fun getServices(): Single<List<Service>>
    fun getServicesByTitle(query: String) : Single<List<Service>>
    fun getService(id: Long, userId: Long): Single<Service>
    fun sendMessage(message: Notification): Completable
    fun getCount(): Single<Long>
    fun getUser(id: Long): Single<User>

}

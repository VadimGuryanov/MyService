package kpfu.itis.myservice.features.feature_add_service.data.network

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User

interface ServiceFirebase {

    fun getServices(id: Long) : Single<List<Service>>
    fun addService(id: Long, service: Service) : Completable
    fun updateService(id: Long, service: Service) : Completable
    fun getService(id: Long, serviceId: Long): Single<Service>
    fun getUser(id: Long) : Single<User>
    fun getCount() : Single<Int>
    fun deleteService(id: Long, serviceId: Long) : Completable

}

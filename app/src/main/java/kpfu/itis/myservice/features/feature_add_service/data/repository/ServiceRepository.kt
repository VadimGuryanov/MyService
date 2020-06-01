package kpfu.itis.myservice.features.feature_add_service.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User

interface ServiceRepository {

    fun addService(id: Long, service: Service) : Completable
    fun getServices(id: Long) : Single<List<Service>>
    fun getService(userId: Long, serviceId: Long): Single<Service>
    fun updateService(userId: Long, service: Service): Completable
    fun getUser(id: Long) : Single<User>
    fun getCount() : Single<Int>
    fun deleteService(id: Long, serviceId: Long) : Completable

}

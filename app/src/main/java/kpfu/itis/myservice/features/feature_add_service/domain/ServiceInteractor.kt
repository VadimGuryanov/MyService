package kpfu.itis.myservice.features.feature_add_service.domain

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User

interface ServiceInteractor {

    fun getServices() : Single<List<Service>>
    fun getService(id: Long) : Single<Service>
    fun addService(service: Service) : Completable
    fun isAuth() : Boolean
    fun updateService(service: Service): Completable
    fun getUser() : Single<User>
    fun isContained(userId: Long): Boolean
    fun deleteService(id: Long): Completable

}

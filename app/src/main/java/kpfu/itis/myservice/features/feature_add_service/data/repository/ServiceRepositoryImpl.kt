package kpfu.itis.myservice.features.feature_add_service.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.data.db.dao.ServiceDao
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_add_service.data.network.ServiceFirebase
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private var dao: ServiceDao,
    private var firebase: ServiceFirebase
) : ServiceRepository{

    override fun addService(id: Long, service: Service): Completable =
        firebase.addService(id, service)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnComplete{
                dao.addService(service)
            }

    override fun getServices(id: Long): Single<List<Service>> =
        firebase.getServices(id)
            .observeOn(Schedulers.io())
            .map { if (it.isEmpty()) throw Exception() else it }
            .doOnSuccess { dao.addServices(it) }
            .onErrorResumeNext {
                dao.getServices()
            }

    override fun getService(userId: Long, serviceId: Long): Single<Service> =
        firebase.getService(userId, serviceId)
            .observeOn(Schedulers.io())
            .doOnSuccess { dao.updateService(it) }
            .onErrorResumeNext { dao.getService(serviceId) }

    override fun updateService(userId: Long, service: Service): Completable =
        firebase.updateService(userId, service)
            .observeOn(Schedulers.io())
            .doOnComplete { dao.updateService(service) }

    override fun getUser(id: Long): Single<User> =
        firebase.getUser(id)

    override fun getCount(): Single<Int> =
        firebase.getCount()

    override fun deleteService(id: Long, serviceId: Long): Completable =
        firebase.deleteService(id, serviceId)
            .observeOn(Schedulers.io())
            .doOnComplete { dao.deleteService(serviceId) }

}

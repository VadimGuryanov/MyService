package kpfu.itis.myservice.features.feature_add_service.domain

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_add_service.data.repository.ServiceRepository
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ServiceInteractorImpl @Inject constructor(
    private var repository: ServiceRepository,
    private val helper: HelperSharedPreferences
) : ServiceInteractor {

    private val dateFormat: DateFormat by lazy {
        SimpleDateFormat.getDateInstance()
    }

    override fun getServices(): Single<List<Service>> =
        helper.readID()?.let {
            repository.getServices(it.toLong())
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } ?: Single.error(Exception("Вы не авторизованы"))

    override fun getService(id: Long): Single<Service> =
        helper.readID()?.let {
            repository.getService(it.toLong(), id)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } ?: Single.error(Exception("Вы не авторизованы"))

    override fun addService(service: Service): Completable =
        helper.readID()?.let { id ->
            service.date = getTime()
            service.user_id = id.toLong()
            repository.getCount()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMapCompletable {
                    if (it > 0) {
                        service.ser_id = it.toLong() + 1
                        repository.addService(id.toLong(), service)
                    } else {
                        Completable.error(Exception())
                    }
                }
        } ?: Completable.error(Exception("Вы не авторизованы"))

    override fun isAuth(): Boolean =
        helper.readSession()?.let {
            helper.readID().let {
                it?.toLong() ?: -1 >= 0
            }
        } ?: false

    override fun updateService(service: Service): Completable =
        helper.readID()?.let {
            service.date = getTime()
            service.user_id = it.toLong()
            repository.updateService(it.toLong(), service)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } ?: Completable.error(Exception("Вы не авторизованы"))

    override fun getUser(): Single<User> =
        helper.readID()?.let {
            repository.getUser(it.toLong())
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } ?: Single.error(Exception("Вы не авторизованы"))

    override fun isContained(userId: Long): Boolean =
        helper.readID()?.let {
            userId.toString() == it
        } ?: false

    override fun deleteService(id: Long): Completable =
        helper.readID()?.let {
            repository.deleteService(it.toLong(), id)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } ?: Completable.error(Exception("Вы не авторизованы"))

    private fun getTime() : String = dateFormat.format(Date())

}

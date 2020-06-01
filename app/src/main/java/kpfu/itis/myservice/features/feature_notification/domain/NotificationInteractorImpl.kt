package kpfu.itis.myservice.features.feature_notification.domain

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.features.feature_notification.data.repository.NotificationRepository
import kpfu.itis.myservice.features.feature_notification.domain.di.NotificationDomainScope
import java.lang.Exception
import javax.inject.Inject

@NotificationDomainScope
class NotificationInteractorImpl @Inject constructor(
    private var repository: NotificationRepository,
    private var helper: HelperSharedPreferences
) : NotificationInteractor {

    override fun getNotifications(): Single<List<Notification>> =
        if (isAuth()) {
            repository.getNotifications(helper.readID()?.toLong() ?: -1)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } else {
            Single.error(Exception("Вы не авторизованы"))
        }

    override fun isAuth(): Boolean =
        helper.readSession()?.let {
            helper.readID().let {
                it?.toLong() ?: -1 >= 0
            }
        } ?: false

    override fun read(id: Long): Completable =
        if (isAuth()) {
            repository.read(id, helper.readID()?.toLong() ?: -1)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } else {
            Completable.error(Exception("Вы не авторизованы"))
        }

    override fun getNotification(id: Long): Single<Notification> =
        if (isAuth()) {
            repository.getNotification(id, helper.readID()?.toLong() ?: -1)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } else {
            Single.error(Exception("Вы не авторизованы"))
        }

}

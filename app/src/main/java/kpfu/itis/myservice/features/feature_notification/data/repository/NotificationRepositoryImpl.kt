package kpfu.itis.myservice.features.feature_notification.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.data.db.dao.NotificationDao
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.features.feature_notification.data.di.NotificationDataScope
import kpfu.itis.myservice.features.feature_notification.data.network.NotificationFirebase
import javax.inject.Inject

@NotificationDataScope
class NotificationRepositoryImpl @Inject constructor(
    private var dao: NotificationDao,
    private var firebase: NotificationFirebase
) : NotificationRepository{

    override fun getNotifications(id: Long): Single<List<Notification>> =
        firebase.getNotifications(id)
            .observeOn(Schedulers.io())
            .map { if (it.isEmpty()) throw Exception() else it}
            .doOnSuccess { dao.addNotifications(it) }
            .onErrorResumeNext { dao.getNotifications() }

    override fun read(id: Long, userId: Long): Completable =
        firebase.read(id, userId)
            .observeOn(Schedulers.io())
            .doOnComplete { dao.update(id, true) }

    override fun getNotification(id: Long, userId: Long): Single<Notification> =
        firebase.getNotification(id, userId)
            .observeOn(Schedulers.io())
            .doOnSuccess { dao.addNotification(it) }

}

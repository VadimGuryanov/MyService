package kpfu.itis.myservice.features.feature_notification.data.network

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Notification

interface NotificationFirebase {
    fun getNotifications(id: Long): Single<List<Notification>>
    fun read(id: Long, userId: Long): Completable
    fun getNotification(id: Long, userId: Long): Single<Notification>
}

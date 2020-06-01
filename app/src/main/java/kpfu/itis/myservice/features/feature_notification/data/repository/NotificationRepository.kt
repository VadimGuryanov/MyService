package kpfu.itis.myservice.features.feature_notification.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Notification

interface NotificationRepository {
    fun getNotifications(id: Long): Single<List<Notification>>
    fun read(id: Long, userId: Long): Completable
    fun getNotification(id: Long, userId: Long): Single<Notification>
}

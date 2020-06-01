package kpfu.itis.myservice.features.feature_notification.domain

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Notification

interface NotificationInteractor {

    fun getNotifications() : Single<List<Notification>>
    fun isAuth() : Boolean
    fun read(id: Long): Completable
    fun getNotification(id: Long): Single<Notification>

}

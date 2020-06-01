package kpfu.itis.myservice.data.db.dao

import androidx.room.*
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Notification

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notification")
    fun getNotifications() : Single<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNotification(notification: Notification)

    @Query("UPDATE notification SET isRead =:isRead WHERE mess_id =:id")
    fun update(id: Long, isRead: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNotifications(notifications: List<Notification>)

}

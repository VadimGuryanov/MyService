package kpfu.itis.myservice.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification")
data class Notification(
    @PrimaryKey(autoGenerate = false)
    var mess_id: Long = -1,
    @ColumnInfo(name = "ser_id")
    var ser_id: Long = -1,
    @ColumnInfo(name = "from_user_id")
    var from_user_id: Long = -1,
    @ColumnInfo(name = "to_user_id")
    var to_user_id: Long = -1,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "lastName")
    var lastName: String = "",
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "photo")
    var photoURL: String = "",
    @ColumnInfo(name = "message")
    var message: String = "",
    @ColumnInfo(name = "data")
    var data: String = "",
    @ColumnInfo(name = "isRead")
    var isRead: Boolean = false
)

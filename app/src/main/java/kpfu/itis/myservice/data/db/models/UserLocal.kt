package kpfu.itis.myservice.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserLocal (
    @PrimaryKey(autoGenerate = true)
    var vk_id: Long,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "lastName")
    var lastName: String,
    @ColumnInfo(name = "city")
    var city: String?,
    @ColumnInfo(name = "photo")
    var photoURL: String,
    @ColumnInfo(name = "mobilePhone")
    var mobilePhone: String?,
    @ColumnInfo(name = "university")
    var university: String?,
    @ColumnInfo(name = "faculty")
    var faculty: String?,
    @ColumnInfo(name = "job")
    var job: String?,
    @ColumnInfo(name = "description")
    var description: String?
)

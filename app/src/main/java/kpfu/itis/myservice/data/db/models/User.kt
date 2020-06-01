package kpfu.itis.myservice.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = false)
    var vk_id: Long = -1,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "lastName")
    var lastName: String = "",
    @ColumnInfo(name = "city")
    var city: String? = null,
    @ColumnInfo(name = "photo")
    var photoURL: String = "",
    @ColumnInfo(name = "mobilePhone")
    var mobilePhone: String? = null,
    @ColumnInfo(name = "university")
    var university: String? = null,
    @ColumnInfo(name = "faculty")
    var faculty: String? = null,
    @ColumnInfo(name = "job")
    var job: String? = null,
    @ColumnInfo(name = "description")
    var description: String? = null,
    @ColumnInfo(name = "socialUrl")
    var socialUrl: String? = null
)

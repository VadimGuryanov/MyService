package kpfu.itis.myservice.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite (
    @PrimaryKey(autoGenerate = false)
    var ser_id: Long = -1,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "city")
    var city: String? = null,
    @ColumnInfo(name = "mobilePhone")
    var mobilePhone: String? = null,
    @ColumnInfo(name = "specialty")
    var specialty: String? = null,
    @ColumnInfo(name = "description")
    var description: String? = null,
    @ColumnInfo(name = "socialUrl")
    var socialUrl: String? = null,
    @ColumnInfo(name = "cost")
    var cost: String? = null,
    @ColumnInfo(name = "currancy")
    var currancy: String? = null,
    @ColumnInfo(name = "user_id")
    var user_id: Long = -1,
    @ColumnInfo(name = "date")
    var date: String? = null,
    @ColumnInfo(name = "experience")
    var experience: String? = null,
    @ColumnInfo(name = "author_name")
    var name: String? = null,
    @ColumnInfo(name = "author_last_name")
    var lastName: String? = null
)

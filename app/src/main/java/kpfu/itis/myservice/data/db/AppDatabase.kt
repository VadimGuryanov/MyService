package kpfu.itis.myservice.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import kpfu.itis.myservice.data.db.dao.UserDao
import kpfu.itis.myservice.data.db.models.UserLocal
import javax.inject.Inject

@Database(entities = [UserLocal::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}

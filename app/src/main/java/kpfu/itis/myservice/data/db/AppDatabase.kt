package kpfu.itis.myservice.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import kpfu.itis.myservice.data.db.dao.FavoriteDao
import kpfu.itis.myservice.data.db.dao.NotificationDao
import kpfu.itis.myservice.data.db.dao.ServiceDao
import kpfu.itis.myservice.data.db.dao.UserDao
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User

@Database(entities = [User::class, Service::class, Favorite::class, Notification::class], version = 10)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun serviceDao() : ServiceDao

    abstract fun favoriteDao() : FavoriteDao

    abstract fun notificationDao() : NotificationDao

}

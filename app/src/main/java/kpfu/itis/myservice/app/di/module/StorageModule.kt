package kpfu.itis.myservice.app.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.app.App
import kpfu.itis.myservice.data.db.AppDatabase
import kpfu.itis.myservice.data.db.dao.FavoriteDao
import kpfu.itis.myservice.data.db.dao.NotificationDao
import kpfu.itis.myservice.data.db.dao.ServiceDao
import kpfu.itis.myservice.data.db.dao.UserDao
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(app: App) : SharedPreferences =
        app.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideDb(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, "service.db")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Singleton
    @Provides
    fun provideServiceDao(db: AppDatabase): ServiceDao = db.serviceDao()

    @Singleton
    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()

    @Singleton
    @Provides
    fun provideNotificationDao(db: AppDatabase): NotificationDao = db.notificationDao()

    companion object {
        private const val FILE_NAME = "session"
    }

}

package kpfu.itis.myservice.app.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.data.db.AppDatabase
import kpfu.itis.myservice.data.db.dao.UserDao
import javax.inject.Named
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @Named(TAG_MASTER_KEY) masterKey : String,
        context: Context
    ) : SharedPreferences = EncryptedSharedPreferences.create(
        FILE_NAME,
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    @Provides
    @Singleton
    @Named(TAG_MASTER_KEY)
    fun provideMasterKey() : String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    @Singleton
    @Provides
    fun provideDb(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, "service.db")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    companion object {
        private const val TAG_MASTER_KEY = "tag_master_key"
        private const val FILE_NAME = "session"
    }


}
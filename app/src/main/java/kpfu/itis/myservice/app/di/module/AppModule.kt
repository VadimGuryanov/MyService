package kpfu.itis.myservice.app.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.app.App
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(app : App) : Context = app.applicationContext

}

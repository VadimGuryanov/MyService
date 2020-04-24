package kpfu.itis.myservice.common.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.common.HelperSharedPreferences
import javax.inject.Singleton

@Module
class CommonModule {

    @Provides
    @Singleton
    fun provideHelperSharedPreference(sharedPreferences: SharedPreferences):
            HelperSharedPreferences = HelperSharedPreferences(sharedPreferences)

    @Provides
    @Singleton
    fun provideHelperToastSnackbar(): HelperToastSnackbar = HelperToastSnackbar()

}

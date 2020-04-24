package kpfu.itis.myservice.features.feature_profile.presentation.about_me.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.scope.ViewModelKey
import kpfu.itis.myservice.features.feature_profile.presentation.about_me.AboutMeViewModel
import kpfu.itis.myservice.features.feature_profile.presentation.about_me.di.AboutMeScope

@Module(includes = [ViewModelModule::class])
abstract class AboutMeViewModelModule {

    @IntoMap
    @Binds
    @AboutMeScope
    @ViewModelKey(AboutMeViewModel::class)
    abstract fun bindAboutMeViewModel(
        authViewModel: AboutMeViewModel
    ): ViewModel

}
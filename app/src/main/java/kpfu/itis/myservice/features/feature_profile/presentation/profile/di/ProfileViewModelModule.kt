package kpfu.itis.myservice.features.feature_profile.presentation.profile.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.features.feature_profile.presentation.profile.ProfileViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class ProfileViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(
        profileViewModel: ProfileViewModel
    ): ViewModel

}

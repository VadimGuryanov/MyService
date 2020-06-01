package kpfu.itis.myservice.features.feature_profile.presentation.services.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.features.feature_profile.presentation.services.ProfileServicesViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class ProfileServicesModule {

    @IntoMap
    @Binds
    @ViewModelKey(ProfileServicesViewModel::class)
    abstract fun bindProfileServicesViewModel(
        profileServicesViewModel: ProfileServicesViewModel
    ): ViewModel

}

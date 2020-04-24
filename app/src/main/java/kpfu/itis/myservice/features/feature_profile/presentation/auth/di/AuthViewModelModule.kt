package kpfu.itis.myservice.features.feature_profile.presentation.auth.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.scope.ViewModelKey
import kpfu.itis.myservice.features.feature_profile.presentation.auth.AuthViewModel
import kpfu.itis.myservice.features.feature_profile.presentation.auth.di.AuthScope

@Module(includes = [ViewModelModule::class])
abstract class AuthViewModelModule {

    @IntoMap
    @Binds
    @AuthScope
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(
        authViewModel: AuthViewModel
    ): ViewModel

}

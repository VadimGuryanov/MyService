package kpfu.itis.myservice.features.feature_profile.presentation.auth.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_profile.presentation.auth.AuthFragment

@AuthScope
@Subcomponent(modules = [AuthViewModelModule::class])
interface AuthComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : AuthComponent
    }
    fun inject(authFragment: AuthFragment)
}
package kpfu.itis.myservice.features.feature_profile.presentation.profile.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_profile.presentation.profile.ProfileFragment

@ProfileScope
@Subcomponent(modules = [ProfileViewModelModule::class])
interface ProfileComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : ProfileComponent
    }

    fun inject(profileFragment: ProfileFragment)
}

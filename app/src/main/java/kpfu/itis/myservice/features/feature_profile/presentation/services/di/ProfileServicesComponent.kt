package kpfu.itis.myservice.features.feature_profile.presentation.services.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_profile.presentation.services.ProfileServicesFragment

@ProfileServicesScope
@Subcomponent(modules = [ProfileServicesModule::class])
interface ProfileServicesComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : ProfileServicesComponent
    }

    fun inject(profileServicesFragment: ProfileServicesFragment)

}

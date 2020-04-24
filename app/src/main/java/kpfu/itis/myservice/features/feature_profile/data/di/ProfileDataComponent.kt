package kpfu.itis.myservice.features.feature_profile.data.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_profile.domain.di.ProfileDomainComponent

@ProfileDataScope
@Subcomponent(modules = [ProfileDataModule::class])
interface ProfileDataComponent {

    @Subcomponent.Builder
    interface Builder {
        fun profileDataModule(profileDataModule: ProfileDataModule) : Builder
        fun build() : ProfileDataComponent
    }

    fun plusProfileDomainComponent() : ProfileDomainComponent.Builder

}
package kpfu.itis.myservice.features.feature_profile.domain.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_profile.presentation.about_me.di.AboutMeComponent
import kpfu.itis.myservice.features.feature_profile.presentation.auth.di.AuthComponent
import kpfu.itis.myservice.features.feature_profile.presentation.profile.di.ProfileComponent

@ProfileDomainScope
@Subcomponent(modules = [ProfileDomainModule::class])
interface ProfileDomainComponent {

    @Subcomponent.Builder
    interface Builder {
        fun domainModule(domainModule: ProfileDomainModule) : Builder
        fun build() : ProfileDomainComponent
    }

    fun plusProfileComponent() : ProfileComponent.Builder
    fun plusAuthComponent() : AuthComponent.Builder
    fun plusAboutMeComponent() : AboutMeComponent.Builder

}

package kpfu.itis.myservice.features.feature_profile.presentation.about_me.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_profile.presentation.about_me.AboutMeFragment

@AboutMeScope
@Subcomponent(modules = [AboutMeViewModelModule::class])
interface AboutMeComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : AboutMeComponent
    }

    fun inject(aboutMeFragment: AboutMeFragment)

}

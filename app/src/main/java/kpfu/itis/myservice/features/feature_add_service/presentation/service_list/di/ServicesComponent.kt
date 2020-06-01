package kpfu.itis.myservice.features.feature_add_service.presentation.service_list.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_add_service.presentation.service_list.ServicesFragment

@ServicesScope
@Subcomponent(modules = [ServicesViewModelModule::class])
interface ServicesComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : ServicesComponent
    }

    fun inject(serviceFragment: ServicesFragment)

}

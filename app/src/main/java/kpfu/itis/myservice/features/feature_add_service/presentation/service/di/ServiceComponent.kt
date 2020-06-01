package kpfu.itis.myservice.features.feature_add_service.presentation.service.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_add_service.presentation.service.ServiceFragment

@Subcomponent(modules = [ServiceViewModelModule::class])
interface ServiceComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : ServiceComponent
    }

    fun inject(addServiceFragment: ServiceFragment)

}

package kpfu.itis.myservice.features.feature_add_service.domain.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_add_service.presentation.add.di.AddServiceComponent
import kpfu.itis.myservice.features.feature_add_service.presentation.service.di.ServiceComponent
import kpfu.itis.myservice.features.feature_add_service.presentation.service_list.di.ServicesComponent

@ServiceDomainScope
@Subcomponent(modules = [ServiceDomainModule::class])
interface ServiceDomainComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : ServiceDomainComponent
    }

    fun plusServicesComponent() : ServicesComponent.Builder
    fun plusAddServiceComponent() : AddServiceComponent.Builder
    fun plusServiceComponent() : ServiceComponent.Builder

}

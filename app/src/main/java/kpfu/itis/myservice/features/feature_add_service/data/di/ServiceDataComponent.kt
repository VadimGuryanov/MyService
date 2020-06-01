package kpfu.itis.myservice.features.feature_add_service.data.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_add_service.domain.di.ServiceDomainComponent

@ServiceDataScope
@Subcomponent(modules = [ServiceDataModule::class])
interface ServiceDataComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : ServiceDataComponent
    }

    fun plusServiceDomainComponent() : ServiceDomainComponent.Builder

}

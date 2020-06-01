package kpfu.itis.myservice.app.di.injectors

import kpfu.itis.myservice.app.di.component.AppComponent
import kpfu.itis.myservice.features.feature_add_service.data.di.ServiceDataComponent
import kpfu.itis.myservice.features.feature_add_service.domain.di.ServiceDomainComponent
import kpfu.itis.myservice.features.feature_add_service.presentation.add.di.AddServiceComponent
import kpfu.itis.myservice.features.feature_add_service.presentation.service.di.ServiceComponent
import kpfu.itis.myservice.features.feature_add_service.presentation.service_list.di.ServicesComponent

object ServiceInjector {

    lateinit var appComponent: AppComponent
    private var serviceDataComponent: ServiceDataComponent? = null
    private var serviceDomainComponent : ServiceDomainComponent? = null
    private var servicesComponent: ServicesComponent? = null
    private var addServicesComponent: AddServiceComponent? = null
    private var serviceComponent: ServiceComponent? = null

    fun init(app: AppComponent) {
        appComponent = app
    }

    fun plusServiceDataComponent(): ServiceDataComponent = serviceDataComponent
        ?: appComponent
            .plusServiceDataComponent()
            .build().also {
                serviceDataComponent = it
            }

    fun clearServiceDataComponent() {
        serviceDataComponent = null
    }

    fun plusServiceDomainComponent(): ServiceDomainComponent = serviceDomainComponent
        ?: plusServiceDataComponent()
            .plusServiceDomainComponent()
            .build().also {
                serviceDomainComponent = it
            }

    fun clearServiceDomainComponent() {
        serviceDomainComponent = null
    }

    fun plusServicesComponent(): ServicesComponent = servicesComponent
        ?: plusServiceDomainComponent()
            .plusServicesComponent()
            .build().also {
                servicesComponent = it
            }

    fun clearServicesComponent() {
        servicesComponent = null
    }

    fun plusAddServiceComponent(): AddServiceComponent = addServicesComponent
        ?: plusServiceDomainComponent()
            .plusAddServiceComponent()
            .build().also {
                addServicesComponent = it
            }

    fun clearAddServiceComponent() {
        addServicesComponent = null
    }

    fun plusServiceComponent() : ServiceComponent = serviceComponent
        ?: plusServiceDomainComponent()
            .plusServiceComponent()
            .build().also {
                serviceComponent = it
            }

    fun clearServiceComponent() {
        serviceComponent = null
    }

}

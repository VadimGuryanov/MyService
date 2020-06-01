package kpfu.itis.myservice.app.di.injectors

import kpfu.itis.myservice.app.di.component.AppComponent
import kpfu.itis.myservice.features.feature_search.data.di.SearchDataComponent
import kpfu.itis.myservice.features.feature_search.domain.di.SearchDomainComponent
import kpfu.itis.myservice.features.feature_search.presentation.message.di.MessageFormComponent
import kpfu.itis.myservice.features.feature_search.presentation.search.di.SearchComponent
import kpfu.itis.myservice.features.feature_search.presentation.service.di.ServiceDetailComponent

object SearchInjector {

    lateinit var appComponent: AppComponent
    private var searchDataComponent: SearchDataComponent? = null
    private var searchDomainComponent : SearchDomainComponent? = null
    private var searchComponent: SearchComponent? = null
    private var serviceDetailComponent: ServiceDetailComponent? = null
    private var messageFormComponent: MessageFormComponent? = null

    fun init(app: AppComponent) {
        appComponent = app
    }

    fun plusSearchDataComponent(): SearchDataComponent = searchDataComponent
        ?: appComponent
            .plusSearchDataComponent()
            .build().also {
                searchDataComponent = it
            }

    fun clearSearchDataComponent() {
        searchDataComponent = null
    }

    fun plusSearchDomainComponent(): SearchDomainComponent = searchDomainComponent
        ?: plusSearchDataComponent()
            .plusSearchDomainComponent()
            .build().also {
                searchDomainComponent = it
            }

    fun clearSearchDomainComponent() {
        searchDomainComponent = null
    }

    fun plusSearchComponent(): SearchComponent = searchComponent
        ?: plusSearchDomainComponent()
            .plusSearchComponent()
            .build().also {
                searchComponent = it
            }

    fun clearSearchComponent() {
        searchComponent = null
    }

    fun plusServiceDetailComponent(): ServiceDetailComponent = serviceDetailComponent
        ?: plusSearchDomainComponent()
            .plusServiceDetailComponent()
            .build().also {
                serviceDetailComponent = it
            }

    fun clearServiceDetailComponent() {
        serviceDetailComponent = null
    }

    fun plusMessageFormComponent(): MessageFormComponent = messageFormComponent
        ?: plusSearchDomainComponent()
            .plusMessageFormComponent()
            .build().also {
                messageFormComponent = it
            }

    fun clearMessageFormComponent() {
        messageFormComponent = null
    }

}

package kpfu.itis.myservice.features.feature_search.domain.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_search.presentation.message.di.MessageFormComponent
import kpfu.itis.myservice.features.feature_search.presentation.search.di.SearchComponent
import kpfu.itis.myservice.features.feature_search.presentation.service.di.ServiceDetailComponent

@SearchDomainScope
@Subcomponent(modules = [SearchDomainModule::class])
interface SearchDomainComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : SearchDomainComponent
    }

    fun plusSearchComponent() : SearchComponent.Builder
    fun plusServiceDetailComponent() : ServiceDetailComponent.Builder
    fun plusMessageFormComponent() : MessageFormComponent.Builder

}

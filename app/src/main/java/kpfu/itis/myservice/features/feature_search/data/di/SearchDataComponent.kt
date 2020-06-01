package kpfu.itis.myservice.features.feature_search.data.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_search.domain.di.SearchDomainComponent

@SearchDataScope
@Subcomponent(modules = [SearchDataModule::class])
interface SearchDataComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : SearchDataComponent
    }

    fun plusSearchDomainComponent() : SearchDomainComponent.Builder

}

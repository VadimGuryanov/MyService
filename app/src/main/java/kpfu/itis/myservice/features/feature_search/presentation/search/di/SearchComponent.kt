package kpfu.itis.myservice.features.feature_search.presentation.search.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceViewHolder
import kpfu.itis.myservice.features.feature_search.presentation.search.SearchFragment

@SearchScope
@Subcomponent(modules = [SearchViewModelModule::class])
interface SearchComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : SearchComponent
    }

    fun inject(searchFragment: SearchFragment)

}

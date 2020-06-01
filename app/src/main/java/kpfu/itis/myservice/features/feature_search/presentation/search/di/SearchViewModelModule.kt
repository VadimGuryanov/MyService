package kpfu.itis.myservice.features.feature_search.presentation.search.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.features.feature_search.presentation.search.SearchViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class SearchViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindAddSearchViewModel(
        searchViewModel: SearchViewModel
    ): ViewModel

}

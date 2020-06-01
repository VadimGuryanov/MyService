package kpfu.itis.myservice.features.feature_search.domain.di

import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.features.feature_add_service.data.repository.ServiceRepository
import kpfu.itis.myservice.features.feature_search.data.repository.SearchRepository
import kpfu.itis.myservice.features.feature_search.domain.SearchInteractor
import kpfu.itis.myservice.features.feature_search.domain.SearchInteractorImpl

@Module
class SearchDomainModule {

    @Provides
    @SearchDomainScope
    fun provideSearchInteractor(
        repository: SearchRepository,
        helper: HelperSharedPreferences
    ) : SearchInteractor =
        SearchInteractorImpl(repository, helper)

}

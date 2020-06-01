package kpfu.itis.myservice.features.feature_favorite_service.domain.di

import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.features.feature_favorite_service.data.repository.FavoritesRepository
import kpfu.itis.myservice.features.feature_favorite_service.domain.FavoritesInteractor
import kpfu.itis.myservice.features.feature_favorite_service.domain.FavoritesInteractorImpl

@Module
class FavoritesDomainModule {

    @Provides
    @FavoritesDomainScope
    fun provideFavoritesInteractor(
        repository: FavoritesRepository,
        helper: HelperSharedPreferences
    ) : FavoritesInteractor =
        FavoritesInteractorImpl(repository, helper)

}

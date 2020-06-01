package kpfu.itis.myservice.features.feature_favorite_service.data.di

import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.data.db.dao.FavoriteDao
import kpfu.itis.myservice.features.feature_favorite_service.data.repository.FavoritesRepository
import kpfu.itis.myservice.features.feature_favorite_service.data.repository.FavoritesRepositoryImpl

@Module
class FavoritesDataModule {

    @Provides
    @FavoritesDataScope
    fun provideServiceRepository(
        dao: FavoriteDao
    ) : FavoritesRepository = FavoritesRepositoryImpl(dao)

}

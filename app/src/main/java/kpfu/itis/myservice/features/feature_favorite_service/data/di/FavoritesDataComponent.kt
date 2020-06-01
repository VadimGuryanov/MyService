package kpfu.itis.myservice.features.feature_favorite_service.data.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_favorite_service.domain.di.FavoritesDomainComponent

@FavoritesDataScope
@Subcomponent(modules = [FavoritesDataModule::class])
interface FavoritesDataComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : FavoritesDataComponent
    }

    fun plusFavoritesDomainComponent() : FavoritesDomainComponent.Builder

}

package kpfu.itis.myservice.features.feature_favorite_service.domain.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_favorite_service.presenter.di.FavoritesComponent

@FavoritesDomainScope
@Subcomponent(modules = [FavoritesDomainModule::class])
interface FavoritesDomainComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : FavoritesDomainComponent
    }

    fun plusFavoritesComponent() : FavoritesComponent.Builder

}

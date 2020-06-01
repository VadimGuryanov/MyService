package kpfu.itis.myservice.features.feature_favorite_service.presenter.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_favorite_service.presenter.favorites.FavoritesFragment

@FavoritesScope
@Subcomponent(modules = [FavoritiesViewModelModule::class])
interface FavoritesComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : FavoritesComponent
    }

    fun inject(favoritesFragment: FavoritesFragment)

}

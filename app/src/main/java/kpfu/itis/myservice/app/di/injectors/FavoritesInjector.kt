package kpfu.itis.myservice.app.di.injectors

import kpfu.itis.myservice.app.di.component.AppComponent
import kpfu.itis.myservice.features.feature_favorite_service.data.di.FavoritesDataComponent
import kpfu.itis.myservice.features.feature_favorite_service.domain.di.FavoritesDomainComponent
import kpfu.itis.myservice.features.feature_favorite_service.presenter.di.FavoritesComponent

object FavoritesInjector {

    lateinit var appComponent: AppComponent
    private var favoritesDataComponent: FavoritesDataComponent? = null
    private var favoritesDomainComponent : FavoritesDomainComponent? = null
    private var favoritesComponent: FavoritesComponent? = null

    fun init(app: AppComponent) {
        appComponent = app
    }

    fun plusFavoritesDataComponent(): FavoritesDataComponent = favoritesDataComponent
        ?: appComponent
            .plusFavoritesDataComponent()
            .build().also {
                favoritesDataComponent = it
            }

    fun clearFavoritesDataComponent() {
        favoritesDataComponent = null
    }

    fun plusFavoritesDomainComponent(): FavoritesDomainComponent = favoritesDomainComponent
        ?: plusFavoritesDataComponent()
            .plusFavoritesDomainComponent()
            .build().also {
                favoritesDomainComponent = it
            }

    fun clearFavoritesDomainComponent() {
        favoritesDomainComponent = null
    }

    fun plusFavoritesComponent(): FavoritesComponent = favoritesComponent
        ?: plusFavoritesDomainComponent()
            .plusFavoritesComponent()
            .build().also {
                favoritesComponent = it
            }

    fun clearFavoritesComponent() {
        favoritesComponent = null
    }

}

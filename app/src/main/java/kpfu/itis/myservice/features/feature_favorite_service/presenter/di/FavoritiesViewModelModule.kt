package kpfu.itis.myservice.features.feature_favorite_service.presenter.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.features.feature_favorite_service.presenter.favorites.FavoritesViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class FavoritiesViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(FavoritesViewModel::class)
    abstract fun bindFavoriteViewModel(
        favoritesViewModel: FavoritesViewModel
    ): ViewModel

}

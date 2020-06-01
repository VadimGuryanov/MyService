package kpfu.itis.myservice.features.feature_favorite_service.domain

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.features.feature_favorite_service.data.repository.FavoritesRepository
import kpfu.itis.myservice.features.feature_favorite_service.domain.di.FavoritesDomainScope
import javax.inject.Inject

@FavoritesDomainScope
class FavoritesInteractorImpl @Inject constructor(
    private var repository: FavoritesRepository,
    private var helper: HelperSharedPreferences
) : FavoritesInteractor {

    override fun isAuth(): Boolean =
        helper.readSession()?.let {
            helper.readID().let {
                it?.toLong() ?: -1 >= 0
            }
        } ?: false

    override fun deleteFavorite(id: Long) : Completable =
        repository.deleteFavorite(id)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())

    override fun getFavorites(): Single<List<Favorite>> =
        repository.getFavorites()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())

}

package kpfu.itis.myservice.features.feature_favorite_service.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.data.db.dao.FavoriteDao
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.features.feature_favorite_service.data.di.FavoritesDataScope
import javax.inject.Inject

@FavoritesDataScope
class FavoritesRepositoryImpl @Inject constructor(
    private var dao: FavoriteDao
) : FavoritesRepository {

    override fun deleteFavorite(id: Long): Completable =
        Single.just(true)
            .observeOn(Schedulers.io())
            .flatMapCompletable { dao.delete(id) }

    override fun getFavorites(): Single<List<Favorite>> =
        Single.just(listOf<Favorite>())
            .observeOn(Schedulers.io())
            .flatMap { dao.get() }

}

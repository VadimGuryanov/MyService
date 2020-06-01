package kpfu.itis.myservice.features.feature_favorite_service.domain

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Favorite

interface FavoritesInteractor {

    fun isAuth(): Boolean

    fun deleteFavorite(id: Long) : Completable

    fun getFavorites() : Single<List<Favorite>>

}

package kpfu.itis.myservice.features.feature_favorite_service.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Favorite

interface FavoritesRepository {

    fun deleteFavorite(id: Long) : Completable

    fun getFavorites() : Single<List<Favorite>>

}

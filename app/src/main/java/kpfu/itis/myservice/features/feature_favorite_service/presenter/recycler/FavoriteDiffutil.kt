package kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler.FavoriteViewHolder.Companion.KEY_TITLE
import kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler.FavoriteViewHolder.Companion.KEY_NAME
import kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler.FavoriteViewHolder.Companion.KEY_URL
import kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler.FavoriteViewHolder.Companion.KEY_TEL
import kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler.FavoriteViewHolder.Companion.KEY_LASTNAME
import kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler.FavoriteViewHolder.Companion.KEY_CURRANCY
import kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler.FavoriteViewHolder.Companion.KEY_COST
import kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler.FavoriteViewHolder.Companion.KEY_CITY

object FavoriteDiffutil : DiffUtil.ItemCallback<Favorite>() {

    override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: Favorite, newItem: Favorite): Any? {
        val diffBundle = Bundle()
        if (oldItem.title != newItem.title) {
            diffBundle.putString(KEY_TITLE, newItem.title)
        }
        if (oldItem.name != newItem.name) {
            diffBundle.putString(KEY_NAME, newItem.name)
        }
        if (oldItem.lastName != newItem.lastName) {
            diffBundle.putString(KEY_LASTNAME, newItem.lastName)
        }
        if (oldItem.city != newItem.city) {
            diffBundle.putString(KEY_CITY, newItem.city)
        }
        if (oldItem.cost != newItem.cost) {
            diffBundle.putString(KEY_COST, newItem.cost)
        }
        if (oldItem.currancy != newItem.currancy) {
            diffBundle.putString(KEY_CURRANCY, newItem.currancy)
        }
        if (oldItem.mobilePhone != newItem.mobilePhone) {
            diffBundle.putString(KEY_TEL, newItem.mobilePhone)
        }
        if (oldItem.socialUrl != newItem.socialUrl) {
            diffBundle.putString(KEY_URL, newItem.socialUrl)
        }
        return if (diffBundle.isEmpty) null else diffBundle
    }

}

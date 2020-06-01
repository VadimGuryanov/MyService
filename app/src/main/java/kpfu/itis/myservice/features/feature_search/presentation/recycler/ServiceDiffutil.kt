package kpfu.itis.myservice.features.feature_search.presentation.recycler

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceViewHolder.Companion.KEY_CITY
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceViewHolder.Companion.KEY_COST
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceViewHolder.Companion.KEY_CURRANCY
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceViewHolder.Companion.KEY_DATE
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceViewHolder.Companion.KEY_DESCRIPTION
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceViewHolder.Companion.KEY_LASTNAME
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceViewHolder.Companion.KEY_NAME
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceViewHolder.Companion.KEY_TITLE

object ServiceDiffutil : DiffUtil.ItemCallback<Service>() {

    override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: Service, newItem: Service): Any? {
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
        if (oldItem.description != newItem.description) {
            diffBundle.putString(KEY_DESCRIPTION, newItem.description)
        }
        if (oldItem.cost != newItem.cost) {
            diffBundle.putString(KEY_COST, newItem.cost)
        }
        if (oldItem.currancy != newItem.currancy) {
            diffBundle.putString(KEY_CURRANCY, newItem.currancy)
        }
        if (oldItem.date != newItem.date) {
            diffBundle.putString(KEY_DATE, newItem.date)
        }
        if (oldItem.city != newItem.city) {
            diffBundle.putString(KEY_CITY, newItem.city)
        }
        return if (diffBundle.isEmpty) null else diffBundle
    }

}

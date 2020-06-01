package kpfu.itis.myservice.features.feature_search.presentation.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_search_service.*
import kotlinx.android.synthetic.main.item_search_service.view.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.data.db.models.Service

class SearchServiceViewHolder (
    override val containerView: View,
    private var currantId: Long,
    private var addFavorite: (Long, Long, Int) -> Unit,
    private var deleteFavorite: (Long, Long, Int) -> Unit,
    private var sendMessage: (Long, Long, String) -> Unit,
    private var click: (Long, Long, Boolean) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    companion object {

        const val KEY_TITLE = "title"
        const val KEY_NAME = "name"
        const val KEY_LASTNAME = "lastname"
        const val KEY_COST = "cost"
        const val KEY_CURRANCY = "currancy"
        const val KEY_DESCRIPTION = "description"
        const val KEY_DATE = "date"
        const val KEY_CITY = "city"

        fun create(
            parent: ViewGroup,
            currantId: Long,
            addfavorite: (Long, Long, Int) -> Unit,
            deleteFavorite: (Long, Long, Int) -> Unit,
            sendMessage: (Long, Long, String) -> Unit,
            click: (Long, Long, Boolean) -> Unit
        ) =
            SearchServiceViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_search_service, parent, false),
                currantId,
                addfavorite,
                deleteFavorite,
                sendMessage,
                click
            )
    }

    fun bind(service: Service, position: Int) {
        containerView.apply {
            service.let {
                tv_title.text = if (it.title?.length ?: -1 < 60) {
                    it.title
                } else {
                    it.title?.substring(0, 53) + "..."
                }
                if (it.cost != null && it.cost != "") {
                    tv_price.text = "${it.cost} ${it.currancy}"
                } else {
                    tv_price.text = "Стоимость не указана"
                }
                tv_author.text = "${it.name} ${it.lastName}"
                tv_description.text = if (it.description?.length ?: -1 < 300) {
                    it.description
                } else {
                    it.description?.substring(0, 300) + "..."
                }
                tv_date.text = it.date
                tv_city.text = it.city ?: ""
                if (it.isFavorite) {
                    iv_star_empty.visibility = View.INVISIBLE
                    iv_star.visibility = View.VISIBLE
                } else {
                    iv_star_empty.visibility = View.VISIBLE
                    iv_star.visibility = View.INVISIBLE
                }
                iv_star_empty.setOnClickListener {
                    addFavorite(service.ser_id, service.user_id, position)
                    iv_star_empty.visibility = View.GONE
                    iv_star.visibility = View.VISIBLE
                }
                iv_star.setOnClickListener {
                    deleteFavorite(service.ser_id, service.user_id, position)
                    iv_star_empty.visibility = View.VISIBLE
                    iv_star.visibility = View.GONE
                }
                if (currantId == service.user_id || currantId < 0) {
                    btn_send.visibility = View.GONE
                } else {
                    btn_send.setOnClickListener {
                        sendMessage(service.ser_id, service.user_id, service.title.toString())
                    }
                }
            }
        }
        containerView.setOnClickListener { click(service.ser_id, service.user_id, iv_star.isVisible) }
    }

    fun updateFromBundle(bundle: Bundle?, position: Int) {
        bundle?.apply {
            bind(
                Service(
                    title = getString(KEY_TITLE) ?: "empty",
                    description = getString(KEY_DESCRIPTION) ?: "",
                    name = getString(KEY_NAME) ?: "",
                    lastName = getString(KEY_LASTNAME) ?: "",
                    cost = getString(KEY_COST) ?: "",
                    currancy = getString(KEY_CURRANCY) ?: "",
                    date = getString(KEY_DATE) ?: "",
                    city = getString(KEY_CITY) ?: ""
                ),
                position
            )
        }
    }

}

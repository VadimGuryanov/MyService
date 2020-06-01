package kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_favorite.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.data.db.models.Favorite

class FavoriteViewHolder (
    override val containerView: View,
    private var deleteFavorite: (Long, Int) -> Unit,
    private var click: (Long, Long, Boolean) -> Unit,
    private var clickTel: (String) -> Unit,
    private var clickUrl: (String) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    companion object {

        const val KEY_TITLE = "title"
        const val KEY_NAME = "name"
        const val KEY_LASTNAME = "lastname"
        const val KEY_COST = "cost"
        const val KEY_CURRANCY = "currancy"
        const val KEY_CITY = "city"
        const val KEY_TEL = "tel"
        const val KEY_URL = "url"

        fun create(
            parent: ViewGroup,
            deleteFavorite: (Long, Int) -> Unit,
            click: (Long, Long, Boolean) -> Unit,
            clickTel: (String) -> Unit,
            clickUrl: (String) -> Unit
        ) =
            FavoriteViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_card_favorite, parent, false),
                deleteFavorite, click, clickTel, clickUrl
            )
    }

    fun bind(favorite: Favorite, position: Int) {
        containerView.apply {
            favorite.let {
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
                tv_city.text = it.city ?: ""
                iv_star.setOnClickListener {
                    deleteFavorite(favorite.ser_id, position)
                }
                if (it.mobilePhone == null || it.mobilePhone == "") {
                    visible(tv_tel, iv_tel, true, null)
                } else {
                    visible(tv_tel, iv_tel, false, it.mobilePhone)
                    tv_tel.setOnClickListener{view ->
                        clickTel(it.mobilePhone.toString())
                    }
                    iv_tel.setOnClickListener {view ->
                        clickTel(it.mobilePhone.toString())
                    }
                }
                if (it.socialUrl == null || it.socialUrl == "") {
                    visible(tv_url, iv_url, true, null)
                } else {
                    visible(tv_url, iv_url, false, it.socialUrl)
                    tv_url.setOnClickListener { view ->
                        clickUrl(it.socialUrl.toString())
                    }
                    iv_url.setOnClickListener { view ->
                        clickUrl(it.socialUrl.toString())
                    }
                }
            }
        }
        containerView.setOnClickListener { click(favorite.ser_id, favorite.user_id, true) }
    }

    fun updateFromBundle(bundle: Bundle?, position: Int) {
        bundle?.apply {
            bind(
                Favorite(
                    title = getString(KEY_TITLE) ?: "empty",
                    name = getString(KEY_NAME) ?: "",
                    lastName = getString(KEY_LASTNAME) ?: "",
                    cost = getString(KEY_COST) ?: "",
                    currancy = getString(KEY_CURRANCY) ?: "",
                    mobilePhone = getString(KEY_TEL) ?: "",
                    socialUrl = getString(KEY_URL) ?: "",
                    city = getString(KEY_CITY) ?: ""
                ),
                position
            )
        }
    }

    private fun visible(tv: TextView, iv: ImageView, isNull: Boolean, str: String?) {
        if (isNull) {
            tv.visibility = View.GONE
            iv.visibility = View.GONE
        } else {
            tv.text = "$str"
            tv.visibility = View.VISIBLE
            iv.visibility = View.VISIBLE
        }
    }

}

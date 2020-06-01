package kpfu.itis.myservice.features.feature_add_service.presentation.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_my_service.view.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.data.db.models.Service

class ServiceViewHolder (
    override val containerView: View,
    private var navigateTo: (Long) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    companion object {
        fun create(parent: ViewGroup, navigateTo: (Long) -> Unit) =
            ServiceViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_card_service, parent, false),
                navigateTo
            )
    }

    fun bind(service: Service) {
        containerView.apply {
            tv_title.text = service.title
            tv_cost.text = if (service.cost != null && service.cost != "") {
                "${service.cost} ${service.currancy}"
            } else {
                "не указано"
            }
            tv_city.text = service.city
            this.setOnClickListener {navigateTo(service.ser_id)}
        }
    }

}

package kpfu.itis.myservice.features.feature_add_service.presentation.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kpfu.itis.myservice.data.db.models.Service

class ServiceAdapter(
    private var dataSource: List<Service>,
    private var navigateTo: (Long) -> Unit
) : RecyclerView.Adapter<ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder.create(parent, navigateTo)

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) =
        holder.bind(dataSource[position])

}

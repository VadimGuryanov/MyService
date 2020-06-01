package kpfu.itis.myservice.features.feature_search.presentation.recycler

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kpfu.itis.myservice.data.db.models.Service

class SearchServiceAdapter (
    private var dataSource: List<Service>,
    private var currantId: Long,
    private var addFavorite: (Long, Long, Int) -> Unit,
    private var deleteFavorite: (Long, Long, Int) -> Unit,
    private var sendMessage: (Long, Long, String) -> Unit,
    private var click: (Long, Long, Boolean) -> Unit
) : ListAdapter<Service, SearchServiceViewHolder>(ServiceDiffutil)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchServiceViewHolder =
        SearchServiceViewHolder.create(parent, currantId, addFavorite, deleteFavorite, sendMessage, click)

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: SearchServiceViewHolder, position: Int) =
        holder.bind(dataSource[position], position)

    override fun onBindViewHolder(
        holder: SearchServiceViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val bundle = payloads[0] as? Bundle
            holder.updateFromBundle(bundle, position)
        }
    }

    fun update(newList: List<Service>) {
        val result = DiffUtil.calculateDiff(
            DiffUtilCallback(
                dataSource,
                newList
            ), true)
        result.dispatchUpdatesTo(this)
        val temp = dataSource.toMutableList()
        temp.clear()
        temp.addAll(newList)
        dataSource = temp.toList()
    }

}

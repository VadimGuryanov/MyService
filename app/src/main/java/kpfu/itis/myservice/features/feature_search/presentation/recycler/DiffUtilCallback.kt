package kpfu.itis.myservice.features.feature_search.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import kpfu.itis.myservice.data.db.models.Service

class DiffUtilCallback (
    private var oldItems: List<Service>,
    private var newItems: List<Service>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].title == newItems[newItemPosition].title

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition] == newItems[newItemPosition]

}

package kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kpfu.itis.myservice.data.db.models.Favorite

class FavoriteAdapter (
    private var dataSource: List<Favorite>,
    private var deleteFavorite: (Long, Int) -> Unit,
    private var clickTel: (String) -> Unit,
    private var clickUrl: (String) -> Unit,
    private var click: (Long, Long, Boolean) -> Unit
) : ListAdapter<Favorite, FavoriteViewHolder>(FavoriteDiffutil)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder =
        FavoriteViewHolder.create(parent, deleteFavorite, click, clickTel, clickUrl)

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) =
        holder.bind(dataSource[position], position)

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
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

    fun update(newList: List<Favorite>) {
        dataSource = newList
        submitList(dataSource)
        notifyDataSetChanged()
    }

    fun delete(position: Int) {
        val list = dataSource.toMutableList()
        list.removeAt(position)
        update(list)
    }

    fun getSize() : Int = dataSource.size

}

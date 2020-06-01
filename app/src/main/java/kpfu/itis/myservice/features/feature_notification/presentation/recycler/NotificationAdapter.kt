package kpfu.itis.myservice.features.feature_notification.presentation.recycler

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kpfu.itis.myservice.data.db.models.Notification

class NotificationAdapter(
    private var dataSource: List<Notification>,
    private var downloadPhoto: (ImageView, String) -> Unit,
    private var click: (Long, Long, Boolean) -> Unit
) : RecyclerView.Adapter<NotificationViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
        NotificationViewHolder.create(parent, downloadPhoto, click)

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) =
        holder.bind(dataSource[position])

}

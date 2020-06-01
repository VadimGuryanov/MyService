package kpfu.itis.myservice.features.feature_notification.presentation.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_notification.*
import kotlinx.android.synthetic.main.item_notification.view.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.data.db.models.Notification

class NotificationViewHolder (
    override val containerView: View,
    private var downloadPhoto: (ImageView, String) -> Unit,
    private var click: (Long, Long, Boolean) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    companion object {

        fun create(
            parent: ViewGroup,
            downloadPhoto: (ImageView, String) -> Unit,
            click: (Long, Long, Boolean) -> Unit
        ) = NotificationViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false),
                downloadPhoto, click
            )
    }

    fun bind(notification: Notification) {
        containerView.apply {
            notification.let {
                if (it.name.length + it.lastName.length > 30) {
                    tv_name.text = ("${it.name} ${it.lastName}".substring(0, 27) + "...")
                    tv_lastname.isVisible = false
                } else {
                    tv_name.text = it.name
                    tv_lastname.text = it.lastName
                }
                downloadPhoto(iv_photo, it.photoURL)
                tv_message.text = if (it.message.length > 30) {
                    it.message.substring(0, 27) + "..."
                } else {
                    it.message
                }
                if (it.isRead) {
                    iv_is_read.visibility = View.GONE
                } else {
                    iv_is_read.visibility = View.VISIBLE
                }
            }
        }
        containerView.setOnClickListener {
            click(notification.mess_id,
                notification.from_user_id,
                iv_is_read.isVisible
            )
        }
    }
}

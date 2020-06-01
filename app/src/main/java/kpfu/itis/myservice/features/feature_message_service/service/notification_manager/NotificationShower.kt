package kpfu.itis.myservice.features.feature_message_service.service.notification_manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.data.db.models.Notification
import kotlin.random.Random

object NotificationShower {

    private const val CHANNEL_ID = "127"
    const val EXTRA_ACTION = "action"
    const val VALUE_MESSAGE = "message"

    fun show(notificationManager: NotificationManager, context: Context, message: Notification) {
        if (!message.isRead) {
            var notification = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("${message.name} ${message.lastName}")
                .setContentText(message.message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(navigateTo(context, message.mess_id))
                .build()
            var id = Random.nextInt(9999 - 1000) + 1000
            notificationManager.notify(id, notification)
        }
    }

    fun showChanel(notificationManager: NotificationManager, context: Context, message: Notification) {
        if (!message.isRead) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "${message.name} ${message.lastName}"
                val descriptionText = message.message
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = notificationManager.getNotificationChannel(CHANNEL_ID) ?: NotificationChannel(
                    CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                notificationManager.createNotificationChannel(channel)
            }
            val notification =
                NotificationCompat.Builder(context,
                    CHANNEL_ID
                )
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("${message.name} ${message.lastName}")
                    .setContentText(message.message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(navigateTo(context, message.mess_id))
                    .build()
            var id = Random.nextInt(9999 - 1000) + 1000
            notificationManager.notify(id, notification)
        }
    }

    private fun navigateTo(context: Context, id: Long) : PendingIntent {
        var intent = MainActivity.createIntent(context, id)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(EXTRA_ACTION, VALUE_MESSAGE)
        return PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

}


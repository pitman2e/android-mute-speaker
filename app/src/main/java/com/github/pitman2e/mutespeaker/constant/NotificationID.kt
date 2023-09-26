package com.github.pitman2e.mutespeaker.constant

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.github.pitman2e.mutespeaker.R

object NotificationID {
    const val NOTIFICATION_CHANNEL_MISC = "NOTIFICATION_CHANNEL_MISC"
    const val MUTE_SERVICE_RUNNING = 1
    fun createNotificationChannel(context: Context?, notificationId: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.getNotificationChannel(notificationId) != null) {
                return
            }
            when (notificationId) {
                NOTIFICATION_CHANNEL_MISC -> {
                    val notificationChannel = NotificationChannel(
                        NOTIFICATION_CHANNEL_MISC,
                        context.getString(R.string.notification_channel_miscellaneous),
                        NotificationManager.IMPORTANCE_NONE
                    )
                    notificationChannel.lightColor = Color.BLUE
                    notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                    notificationManager.createNotificationChannel(notificationChannel)
                }
            }
        }
    }
}
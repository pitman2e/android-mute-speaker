package com.github.pitman2e.mutespeaker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.github.pitman2e.mutespeaker.constant.NotificationID
import com.github.pitman2e.mutespeaker.receivers.MuteServiceToggleBroadcastReceiver
import com.github.pitman2e.mutespeaker.services.HeadsetStateService

object MuteServiceToggle {
    fun enforceByPref(context: Context) {
        if (Prefs.getIsEnableNotification(context)) {
            setEnable(context)
        } else {
            setDisable(context)
        }
    }

    fun setEnable(context: Context) {
        Prefs.setIsEnableNotification(context, true)
        HeadsetStateService.startService(context)
        if (!Util.isHeadphonesPlugged(context)) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
        }
    }

    fun setDisable(context: Context) {
        Prefs.setIsEnableNotification(context, false)
        HeadsetStateService.stopService(context)
        createDisableMuteSpeakerNotification(context)
    }

    private fun createDisableMuteSpeakerNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val enableMuteIntent = Intent(context, MuteServiceToggleBroadcastReceiver::class.java)
        enableMuteIntent.putExtra(
            MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE, true
        )
        val enableMutePendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            enableMuteIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val actionButtonText: CharSequence = context.getString(R.string.notification_action_enable)
        val titleText: CharSequence = context.getString(R.string.notification_mute_service_disabled)
        NotificationID.createNotificationChannel(context, NotificationID.NOTIFICATION_CHANNEL_MISC)
        val isPrefPersistDisabledNotification = PreferenceManager.getDefaultSharedPreferences(
            context
        ).getBoolean(context.getString(R.string.PREFERENCES_ID_DISABLE_NOTIFICATION_PERSIST), false)
        val notificationBuilder =
            NotificationCompat.Builder(context, NotificationID.NOTIFICATION_CHANNEL_MISC)
                .setSmallIcon(R.drawable.ic_volume_up_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_LOW).setContentTitle(titleText)
                .setOngoing(isPrefPersistDisabledNotification).addAction(
                    R.drawable.ic_volume_off_black_24dp, actionButtonText, enableMutePendingIntent
                )
        val notification = notificationBuilder.build()
        notificationManager.notify(NotificationID.MUTE_SERVICE_RUNNING, notification)
    }
}
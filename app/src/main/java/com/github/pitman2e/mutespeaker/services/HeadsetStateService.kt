package com.github.pitman2e.mutespeaker.services

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.github.pitman2e.mutespeaker.R
import com.github.pitman2e.mutespeaker.VolumeSettingContentObserver
import com.github.pitman2e.mutespeaker.constant.NotificationID
import com.github.pitman2e.mutespeaker.receivers.HeadsetPlugIntentReceiver
import com.github.pitman2e.mutespeaker.receivers.MuteServiceToggleBroadcastReceiver

class HeadsetStateService : Service() {
    companion object {
        fun startService(context: Context) {
            val intent = Intent(context, HeadsetStateService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, HeadsetStateService::class.java)
            context.stopService(intent)
        }
    }

    private lateinit var mVolumeSettingContentObserver: VolumeSettingContentObserver
    private lateinit var receiver: BroadcastReceiver

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        registerIntent()
        mVolumeSettingContentObserver = VolumeSettingContentObserver(this, Handler())
        applicationContext.contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI, true, mVolumeSettingContentObserver
        )
        createEnableMuteSpeakerNotification()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        unregisterIntent()
        applicationContext.contentResolver.unregisterContentObserver(mVolumeSettingContentObserver)
        super.onDestroy()
    }

    private fun createEnableMuteSpeakerNotification() {
        val enableMuteIntent = Intent(this, MuteServiceToggleBroadcastReceiver::class.java)
        enableMuteIntent.putExtra(
            MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE, false
        )
        val disableMutePendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            enableMuteIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val disableMuteIntentWithVolume =
            Intent(this, MuteServiceToggleBroadcastReceiver::class.java).putExtra(
                MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE,
                false
            ).putExtra(
                MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE_WITH_VOLUME,
                true
            )
        val disableMuteWithVolumePendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            disableMuteIntentWithVolume,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        //Note that if the Request Code is the same, android will consider these intent is the same.
        val titleText: CharSequence = this.getString(R.string.notification_mute_service_enabled)
        NotificationID.createNotificationChannel(this, NotificationID.NOTIFICATION_CHANNEL_MISC)
        val notificationBuilder =
            NotificationCompat.Builder(this, NotificationID.NOTIFICATION_CHANNEL_MISC)
                .setSmallIcon(R.drawable.ic_volume_off_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_LOW).setContentTitle(titleText)
                .setOngoing(true).addAction(
                    R.drawable.ic_volume_up_black_24dp,
                    getString(R.string.notification_action_disable),
                    disableMutePendingIntent
                ).addAction(
                    R.drawable.ic_volume_up_black_24dp,
                    getString(R.string.notification_action_disable_with_volume),
                    disableMuteWithVolumePendingIntent
                )
        notificationBuilder.setOngoing(true)
        val notification = notificationBuilder.build()
        this.startForeground(NotificationID.MUTE_SERVICE_RUNNING, notification)
    }

    private fun registerIntent() {
        val receiverFilter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
        receiver = HeadsetPlugIntentReceiver()
        this.registerReceiver(receiver, receiverFilter)
    }

    private fun unregisterIntent() {
        unregisterReceiver(receiver)
    }
}
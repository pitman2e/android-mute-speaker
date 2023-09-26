package com.github.pitman2e.mutespeaker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.preference.PreferenceManager

class MuteServiceToggleBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRA_IS_ENABLED_MUTE_SERVICE = "EXTRA_IS_ENABLED_MUTE_SERVICE"
        const val EXTRA_IS_ENABLED_MUTE_SERVICE_WITH_VOLUME =
            "EXTRA_IS_ENABLED_MUTE_SERVICE_WITH_VOLUME"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Prefs.setIsEnableNotification(
            context, intent.getBooleanExtra(EXTRA_IS_ENABLED_MUTE_SERVICE, true)
        )
        MuteServiceToggle.enforceByPref(context)
        if (intent.getBooleanExtra(EXTRA_IS_ENABLED_MUTE_SERVICE_WITH_VOLUME, false)) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val intVolumePercentage = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(context.getString(R.string.PREFERENCES_ID_DISABLE_SPEAKER_VOLUME), 20)
            val volumePercentage = intVolumePercentage.toDouble() / 100
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volumePercentage).toInt(),
                0
            )
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_UNMUTE,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
            )
        }
    }
}
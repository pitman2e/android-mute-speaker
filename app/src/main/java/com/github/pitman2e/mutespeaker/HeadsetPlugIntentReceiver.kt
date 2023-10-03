package com.github.pitman2e.mutespeaker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log

class HeadsetPlugIntentReceiver : BroadcastReceiver() {
    companion object {
        private val TAG = HeadsetPlugIntentReceiver::class.java.simpleName
        private const val HEADSET_STATE_UNPLUGGED = 0
        private const val HEADSET_STATE_PLUGGED = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (!Prefs.getIsEnableNotification(context)) {
            return
        }
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when (intent.action) {
            Intent.ACTION_HEADSET_PLUG -> {
                when (intent.extras?.getInt("state")) {
                    HEADSET_STATE_UNPLUGGED -> {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
                        audioManager.adjustStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_MUTE,
                            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
                        )
                        Log.d(TAG, "Headset unplugged")
                    }

                    HEADSET_STATE_PLUGGED -> {
                        audioManager.adjustStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_UNMUTE,
                            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
                        )
                        Log.d(TAG, "Headset plugged")
                    }
                }
            }
        }
    }
}
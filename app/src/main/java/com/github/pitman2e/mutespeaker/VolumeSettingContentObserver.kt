package com.github.pitman2e.mutespeaker

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler

class VolumeSettingContentObserver

internal constructor(
    private val mContext: Context, handler: Handler?) : ContentObserver(handler) {

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        val audioManager =
            mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Prefs.getIsEnableNotification(mContext) && !Util.isHeadphonesPlugged(mContext)) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_MUTE,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
            )
        }
    }
}
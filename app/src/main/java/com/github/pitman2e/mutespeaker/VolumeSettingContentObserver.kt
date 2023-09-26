package com.github.pitman2e.mutespeaker

import android.content.Context
import android.database.ContentObserver
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Handler

class VolumeSettingContentObserver

internal constructor(
    private val mContext: Context, handler: Handler?) : ContentObserver(handler) {

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        val audioManager =
            mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Prefs.getIsEnableNotification(mContext) && !isHeadphonesPlugged) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_MUTE,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
            )
        }
    }

    private val isHeadphonesPlugged: Boolean
        get() {
            val audioManager =
                mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            for (deviceInfo in audioDevices) {
                //Device types:
                //https://developer.android.com/reference/android/media/AudioDeviceInfo#summary
                if (deviceInfo.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES || deviceInfo.type == AudioDeviceInfo.TYPE_USB_HEADSET || deviceInfo.type == AudioDeviceInfo.TYPE_WIRED_HEADSET || deviceInfo.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO || deviceInfo.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP || deviceInfo.type == AudioDeviceInfo.TYPE_AUX_LINE || deviceInfo.type == AudioDeviceInfo.TYPE_BLE_HEADSET) {
                    return true
                }
            }
            return false
        }
}
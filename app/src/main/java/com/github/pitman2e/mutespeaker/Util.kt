package com.github.pitman2e.mutespeaker

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager

class Util {
    companion object {
        fun isHeadphonesPlugged(context: Context): Boolean {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            for (deviceInfo in audioDevices) {
                //Device types:
                //https://developer.android.com/reference/android/media/AudioDeviceInfo#summary
                if (deviceInfo.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES ||
                    deviceInfo.type == AudioDeviceInfo.TYPE_USB_HEADSET ||
                    deviceInfo.type == AudioDeviceInfo.TYPE_WIRED_HEADSET ||
                    deviceInfo.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO ||
                    deviceInfo.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP ||
                    deviceInfo.type == AudioDeviceInfo.TYPE_AUX_LINE ||
                    deviceInfo.type == AudioDeviceInfo.TYPE_BLE_HEADSET) {
                    return true
                }
            }
            return false
        }
    }
}
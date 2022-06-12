package com.pitman2e.mutespeaker;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;

public class VolumeSettingContentObserver extends ContentObserver {
    //private int mPreviousVolume;
    private Context mContext;

    VolumeSettingContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;

        //AudioManager audio = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //mPreviousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager == null) {
            return;
        }

        if (Prefs.getIsEnableNotification(mContext) && !isHeadphonesPlugged()) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }

    }

    private boolean isHeadphonesPlugged(){
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager == null) {
            return false;
        }

        AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
        for (AudioDeviceInfo deviceInfo : audioDevices) {
            //Device types:
            //https://developer.android.com/reference/android/media/AudioDeviceInfo#summary
            if (deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                    || deviceInfo.getType() == AudioDeviceInfo.TYPE_USB_HEADSET
                    || deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET
                    || deviceInfo.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO
                    || deviceInfo.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP
                    || deviceInfo.getType() == AudioDeviceInfo.TYPE_AUX_LINE
                    || deviceInfo.getType() == AudioDeviceInfo.TYPE_BLE_HEADSET
            ) {
                return true;
            }
        }
        return false;
    }
}
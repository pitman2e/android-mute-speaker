package com.pitman2e.mutespeaker;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;

public class VolumeSettingContentObserver extends ContentObserver {
    //private int mPreviousVolume;
    Context mContext;

    public VolumeSettingContentObserver(Context context, Handler handler) {
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

        /*
        Determinate the volume is increased or decreased. <For reference only>
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        int delta = mPreviousVolume - currentVolume;

        if(delta > 0)
        {
            mPreviousVolume = currentVolume;
        }
        else if(delta < 0)
        {
            mPreviousVolume = currentVolume;
        }
        */

        if (Prefs.getIsEnableNotification(mContext) && !isHeadphonesPlugged()) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }

    }

    private boolean isHeadphonesPlugged(){
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
            for (AudioDeviceInfo deviceInfo : audioDevices) {
                if (deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                        || deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                    return true;
                }
            }
            return false;
        } else {
            return audioManager.isWiredHeadsetOn();
        }
    }
}
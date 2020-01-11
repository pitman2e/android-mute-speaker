package com.pitman2e.mutespeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.preference.PreferenceManager;

public class MuteServiceToggleBroadcastReceiver extends BroadcastReceiver {
    public static final String EXTRA_IS_ENABLED_MUTE_SERVICE = "EXTRA_IS_ENABLED_MUTE_SERVICE";
    public static final String EXTRA_IS_ENABLED_MUTE_SERVICE_WITH_VOLUME = "EXTRA_IS_ENABLED_MUTE_SERVICE_WITH_VOLUME";

    @Override
    public void onReceive(Context context, Intent intent) {
        Prefs.setIsEnableNotification(context, intent.getBooleanExtra(EXTRA_IS_ENABLED_MUTE_SERVICE, true));
        MuteServiceToggle.EnforceByPref(context);

        if (intent.getBooleanExtra(EXTRA_IS_ENABLED_MUTE_SERVICE_WITH_VOLUME, false)) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int intVolumePercentage = PreferenceManager.getDefaultSharedPreferences(context).getInt(context.getString(R.string.PREFERENCES_ID_DISABLE_SPEAKER_VOLUME), 20);
            double volumePercentage = ((double)intVolumePercentage / 100);

            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,  (int)(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volumePercentage), 0);
        }
    }
}

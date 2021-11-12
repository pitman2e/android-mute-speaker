package com.pitman2e.mutespeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

public class HeadsetPlugIntentReceiver extends BroadcastReceiver {
    private static final String TAG = HeadsetPlugIntentReceiver.class.getSimpleName();
    private static final int HEADSET_STATE_UNPLUGGED = 0;
    private static final int HEADSET_STATE_PLUGGED = 1;

    public static void registerIntent(Context context) {
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        HeadsetPlugIntentReceiver receiver = new HeadsetPlugIntentReceiver();
        context.registerReceiver(receiver, receiverFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Prefs.getIsEnableNotification(context)) {
            return;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager == null) {
            return;
        }

        switch (intent.getAction()) {
            case Intent.ACTION_HEADSET_PLUG:
                int headsetState = intent.getExtras().getInt("state");
                switch (headsetState) {
                    case HEADSET_STATE_UNPLUGGED:
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        Log.d(TAG, "Headset unplugged");
                        break;
                    case HEADSET_STATE_PLUGGED:
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        Log.d(TAG, "Headset plugged");
                        break;
                }
                break;
        }
    }
}

package com.pitman2e.mutespeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootBroadcastReceiver extends BroadcastReceiver {
    String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Broadcast Received");

        if (Prefs.getIsEnableNotification(context)) {
            MuteServiceToggle.setEnable(context);
        } else {
            MuteServiceToggle.setDisable(context);
        }
    }
}

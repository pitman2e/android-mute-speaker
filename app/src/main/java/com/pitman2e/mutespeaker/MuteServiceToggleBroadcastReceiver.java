package com.pitman2e.mutespeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MuteServiceToggleBroadcastReceiver extends BroadcastReceiver {
    public static final String EXTRA_IS_ENABLED_MUTE_SERVICE = "EXTRA_IS_ENABLED_MUTE_SERVICE";

    @Override
    public void onReceive(Context context, Intent intent) {
        MuteServiceToggle.EnforceByPref(context);
    }
}

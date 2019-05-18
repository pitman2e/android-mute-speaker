package com.pitman2e.mutespeaker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class Prefs {
    public static boolean getIsEnableNotification(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.PREFERENCES_ID_ENABLE_MUTE_SERVICE), true);
    }

    public static void setIsEnableNotification(Context context, boolean isEnable) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(context.getString(R.string.PREFERENCES_ID_ENABLE_MUTE_SERVICE), isEnable);
        editor.apply();
    }
}

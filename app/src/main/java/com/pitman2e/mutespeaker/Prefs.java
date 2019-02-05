package com.pitman2e.mutespeaker;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String PREFERENCES_NAME = "Prefs";
    private static final String PREFERENCES_ID_ENABLE_NOTIFICATION = "ENABLE_NOTIFICATION";

    private static SharedPreferences getPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences;
    }

    public static boolean getIsEnableNotification(Context context) {
        return getPref(context).getBoolean(PREFERENCES_ID_ENABLE_NOTIFICATION, true);
    }

    public static void setIsEnableNotification(Context context, boolean isEnable) {
        SharedPreferences preferences = getPref(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFERENCES_ID_ENABLE_NOTIFICATION, isEnable);
        editor.apply();
    }
}

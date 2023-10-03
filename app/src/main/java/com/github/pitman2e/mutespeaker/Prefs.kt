package com.github.pitman2e.mutespeaker

import android.content.Context
import androidx.preference.PreferenceManager

object Prefs {
    fun getIsEnableNotification(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            context.getString(R.string.PREFERENCES_ID_ENABLE_MUTE_SERVICE), true
        )
    }

    fun setIsEnableNotification(context: Context, isEnable: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(context.getString(R.string.PREFERENCES_ID_ENABLE_MUTE_SERVICE), isEnable)
        editor.apply()
    }
}
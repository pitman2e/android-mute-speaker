package com.github.pitman2e.mutespeaker

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class PreferencesFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragmemt_preferences, rootKey)
        PreferenceManager.getDefaultSharedPreferences(this.activity)
            .registerOnSharedPreferenceChangeListener(this)
        MuteServiceToggle.enforceByPref(this.activity)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        MuteServiceToggle.enforceByPref(this.activity)
        if (getString(R.string.PREFERENCES_ID_ENABLE_MUTE_SERVICE) == key || getString(R.string.PREFERENCES_ID_DISABLE_NOTIFICATION_PERSIST) == key) {
            val switchPreference =
                findPreference<SwitchPreference>(getString(R.string.PREFERENCES_ID_ENABLE_MUTE_SERVICE))
            if (switchPreference != null) {
                switchPreference.isChecked = Prefs.getIsEnableNotification(this.activity)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        PreferenceManager.getDefaultSharedPreferences(this.activity)
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}
package com.pitman2e.mutespeaker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import static com.pitman2e.mutespeaker.Prefs.getIsEnableNotification;

public class PreferencesFragment  extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragmemt_preferences, rootKey);
        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        MuteServiceToggle.EnforceByPref(this.getActivity());

        if (getString(R.string.PREFERENCES_ID_ENABLE_MUTE_SERVICE).equals(key)) {
            SwitchPreference switchPreference = findPreference(key);
            if (switchPreference != null) {
                switchPreference.setChecked(getIsEnableNotification(this.getActivity()));
            }
        }
    }
}

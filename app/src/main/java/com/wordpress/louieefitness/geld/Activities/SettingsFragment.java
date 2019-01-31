package com.wordpress.louieefitness.geld.Activities;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
         Preference preference = findPreference(key);
            if (!(preference == null)){
                if (!(preference instanceof CheckBoxPreference)) {
                    String Value = sharedPreferences.getString(preference.getKey(), "");
                    setPreferenceSummary(preference, Value);
                }
            }
    }

    @Override
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        super.setPreferenceScreen(preferenceScreen);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_unn_info);
        getPreferenceScreen().setLayoutResource(R.layout.activity_settings);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        //get access to all preferences in SharedPreferences
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        //get the total number of preferences
        int count = preferenceScreen.getPreferenceCount();
//
        for (int i = 0; i < count; i++) {
            Preference p = preferenceScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)) {
                String Value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, Value);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setPreferenceSummary(Preference p, String value){
        if (p instanceof ListPreference){
             ListPreference listPreference = (ListPreference)p;
             int prefIndex = listPreference.findIndexOfValue(value);
             if (prefIndex >= 0){
                 listPreference.setSummary(listPreference.getEntries()[prefIndex]);
             }
        }
    }
}
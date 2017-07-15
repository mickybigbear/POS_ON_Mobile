package com.example.sin.projectone.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.sin.projectone.R;

/**
 * Created by naki_ on 7/13/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        String settings = getArguments().getString("settings");
//        if ("notifications".equals(settings)) {
            addPreferencesFromResource(R.xml.pref_payment_method);
//        }
//        else if ("sync".equals(settings)) {
//            addPreferencesFromResource(R.xml.settings_sync);
//        }
    }

}
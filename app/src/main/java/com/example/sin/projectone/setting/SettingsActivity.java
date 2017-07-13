package com.example.sin.projectone.setting;

import android.preference.PreferenceActivity;

import com.example.sin.projectone.R;

import java.util.List;

/**
 * Created by naki_ on 7/13/2017.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preferences_settings, target);
    }
}

package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.preference.PreferenceActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //add prefrences
        addPreferencesFromResource(R.xml.preferences);

    }
}

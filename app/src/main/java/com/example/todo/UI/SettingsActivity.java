package com.example.todo.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.preference.PreferenceActivity;
import android.os.Bundle;

import com.example.todo.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //add prefrences
        addPreferencesFromResource(R.xml.preferences);

    }
}

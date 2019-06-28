package com.example.todoudou;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity{

    public static String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText editUrl = findViewById(R.id.editUrl);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String prefsString = settings.getString("URL", "http://tomnab.fr/todo-api");

        editUrl.setText(prefsString);

        Button btn = findViewById(R.id.btnUrl);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                String url = editUrl.getText().toString();
                editor.putString("URL", url);
                editor.apply();
                Toast.makeText(SettingsActivity.this, "URL Changed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
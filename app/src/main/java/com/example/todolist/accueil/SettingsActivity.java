package com.example.todolist.accueil;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;

/**
 * Définition de la classe SettingsActivity.
 * Cette classe représente l'activité Settings Activity
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * Fonction onCreate appelée lors de le création de l'activité
     * Lie l'activité à son layout et récupère les éléments avec lesquels on peut intéragir
     *
     * @param savedInstanceState données à récupérer si l'activité est réinitialisée après avoir planté
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.i("Settings", "onCreate: Settings");
    }
}

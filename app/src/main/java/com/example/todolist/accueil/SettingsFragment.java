package com.example.todolist.accueil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.todolist.R;

/**
 * Définition de la classe Settings Fragment.
 * Cette classe permet d'utiliser des fragments pour nos préférences (à la place d'une PreferenceActivity)
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    /* Le champ texte pour éditer l'URL de base de l'API */
    EditTextPreference url;

    /**
     * Fonction qui permet de fournir les préférences pour le fragment lors de sa création
     *
     * @param savedInstanceState contient l'état du fragment s'il est re-créé à partir d'un état antérieur
     * @param rootKey
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference pseudoPreference = findPreference("Pseudo");
        pseudoPreference.setTitle(preferences.getString("pseudo", ""));

        url = (EditTextPreference) findPreference("url");
        url.setSummary(preferences.getString("url", "http://tomnab.fr/todo-api"));

        /* On met un écouteur sur les changements de l'URL */
        preferences.registerOnSharedPreferenceChangeListener(this);

    }

    /**
     * Permet de mettre à jour le champ "summary" de l'URL
     *
     * @param sharedPreferences les préférences de l'application
     * @param key               la clé pour rechercher dans les préférences
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("url")) {
            Log.i("url", "Preference value was updated to: " + sharedPreferences.getString(key, ""));

            url.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    /**
     * Fonction onResume appelée lors de la reprise de l'activité après mise en pause
     * pour cause d'appel à une autre activité
     * Permet d'écouter et d'enregistrer les changements d'URL de base de l'API
     */
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Fonction onPause appelée lors de la mise en pause de l'activité courante
     * pour cause d'appel à une autre activité
     * Permet d'arrêter d'écouter et d'enregistrer les changements d'URL de base de l'API
     */
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}

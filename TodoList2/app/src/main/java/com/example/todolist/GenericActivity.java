package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class GenericActivity extends AppCompatActivity {

    protected SharedPreferences mSharedPreferences;
    protected String mUserName;
    protected String mApiUrl;
    protected String mHash;

    // ------------------------------------------------------------------------------------------ //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialisation du fichier des préférences, du nom de l'utilisateur et de son profil
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserName = mSharedPreferences.getString("userName","");
        mApiUrl = mSharedPreferences.getString("apiUrl","http://tomnab.fr/todo-api/");
        mHash = mSharedPreferences.getString("hash","");
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // création du menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            // retourne l'activité des préférences
            Intent toSettings = new Intent(this, SettingsActivity.class);
            startActivity(toSettings);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    // ------------------------------------------------------------------------------------------ //
    protected void toLists() {
        // accés aux liste de l'utilisateur
        Intent toLists = new Intent(this, ListsActivity.class);
        startActivity(toLists);
    }

    // ------------------------------------------------------------------------------------------ //
    protected void toItems(int idList) {
        // accés aux items de la to-do liste selectionnéee
        Intent toItems = new Intent(this, ItemsActivity.class);
        toItems.putExtra("list", idList);
        startActivity(toItems);
    }

    // ------------------------------------------------------------------------------------------ //
    protected boolean connectedToNetwork()
    {
        // vérification de la disponibilité du réseau
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnected());
    }
}

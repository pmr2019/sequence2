package com.example.todopmr.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.room.Room;

import com.example.todopmr.R;
import com.example.todopmr.ReponsesRetrofit.ReponseHash;
import com.example.todopmr.Room.AppDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
Cette activité gère la page d'accueil, l'accès aux paramètres, et l'entrée d'un pseudo.
 */
public class MainActivity extends GenericActivity {

    //Views du layout
    private EditText edtPseudo;
    private EditText edtPassword;
    private Button btnOK;
    private Button btnHorsWifi;

    //Attributs de sauvegarde des informations
    private String pseudo;
    private String password;
    private String hash;
    private String urlAPI;
    private Boolean reseau;

    //Langue de l'application
    private String languageToLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRetrofit();

        //Mise à jour de la langue de l'application selon les préférences, et récupération de l'URL de l'API
        SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(this);
        languageToLoad = settings.getString("langue", "");
        actualiserLangue(languageToLoad);
        urlAPI = settings.getString("urlAPI", "http://tomnab.fr/todo-api/");

        setContentView(R.layout.activity_main);

        btnOK = findViewById(R.id.btnOK);
        btnHorsWifi = findViewById(R.id.btnHorsWifi);
        edtPseudo = findViewById(R.id.edtPseudo);
        edtPassword = findViewById(R.id.edtPassword);

        //On vérifie la connexion au réseau
        //Le bouton n'est pas actif si l'utilisateur n'est pas connecté au réseau
        reseau = verifReseau();
        btnOK.setEnabled(reseau);
        btnHorsWifi.setEnabled(!reseau);

        // Lors du clic sur le champ de saisi du pseudo
        edtPseudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerter(getString(R.string.set_pseudo));
            }
        });
        edtPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerter(getString(R.string.set_password));
            }
        });

        // Lors du clic sur le bouton OK
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pseudo = edtPseudo.getText().toString();
                password = edtPassword.getText().toString();

                //Si le pseudo est vide
                if (pseudo.isEmpty()) {
                    alerter(getString(R.string.pseudo_vide));
                }

                //Si le mdp est vide
                else if (password.isEmpty()) {
                    alerter(getString(R.string.password_vide));
                }
                //Si tout va bien : connexion à l'API
                // ATTENTION : ne peuvent se connecter que les utilisateurs déja crées
                else {
                    toDoInterface.authenticate(pseudo, password).enqueue(new Callback<ReponseHash>() {
                        @Override
                        public void onResponse(Call<ReponseHash> call, Response<ReponseHash> response) {
                            if (response.isSuccessful() && response.body().success) {
                                //Récupération du hash
                                hash = response.body().hash;
                                afficherPseudos(hash);

                                //Mise à jour des SharedPreferences
                                SharedPreferences newSettings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                final SharedPreferences.Editor editor = newSettings.edit();
                                editor.putString("pseudo", pseudo);
                                editor.putString("hash", hash);
                                editor.putBoolean("reseau", reseau);
                                editor.putString("langue", languageToLoad);
                                editor.putString("urlAPI", urlAPI);
                                editor.commit();

                                //Vers CheckListActivity
                                Intent accesCheckList = new Intent(MainActivity.this, CheckListActivity.class);
                                startActivity(accesCheckList);
                            }
                            else {
                                alerter("L'utilisateur n'existe pas, reessayer..");
                            }
                        }

                        @Override
                        public void onFailure(Call<ReponseHash> call, Throwable t) {
                            alerter("Il y a un problème : authentification");
                        }
                    });
                }
            }
        });

        btnHorsWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pseudo = edtPseudo.getText().toString();
                password = edtPassword.getText().toString();

                //Si le pseudo est vide
                if (pseudo.isEmpty()) {
                    alerter(getString(R.string.pseudo_vide));
                }

                //Si le mdp est vide
                else if (password.isEmpty()) {
                    alerter(getString(R.string.password_vide));
                }
                else {
                    SharedPreferences newSettings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    final SharedPreferences.Editor editor = newSettings.edit();
                    editor.putString("pseudo", pseudo);
                    editor.putBoolean("reseau", reseau);
                    editor.putString("langue", languageToLoad);
                    editor.commit();

                    //Vers CheckListActivity
                    Intent accesCheckList = new Intent(MainActivity.this, CheckListActivity.class);
                    startActivity(accesCheckList);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Affichage du pseudo précédent après récupération dans les préférences.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        edtPseudo.setText(settings.getString("pseudo",""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Une seule option dans le menu : préférences

        //Mise à jour des SharedPreferences
        if (reseau) {
            afficherPseudos(hash);
        }
        SharedPreferences newSettings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor editor = newSettings.edit();
        editor.putString("pseudo", pseudo);
        editor.putString("hash", hash);
        editor.putBoolean("reseau", reseau);
        editor.putString("langue", languageToLoad);
        editor.commit();

        //Vers SettingsActivity
        Intent toSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(toSettingsActivity);
        return super.onOptionsItemSelected(item);
    }
}

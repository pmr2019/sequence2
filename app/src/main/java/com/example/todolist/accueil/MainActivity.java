package com.example.todolist.accueil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;
import com.example.todolist.api.TodoApiService;
import com.example.todolist.api.TodoApiServiceFactory;
import com.example.todolist.api.response_class.User;
import com.example.todolist.recycler_activities.ChoixListActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Définition de la classe MainActivity.
 * Cette classe représente l'activité principale de l'application
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /* Le champ texte dans lequel l'utilisateur va saisir son pseudo */
    EditText editTextPseudo;
    EditText password;
    String url;
    Button btnOk;
    private Call<User> call;
    SharedPreferences preferences;
    TodoApiService todoApiService;
    private Button btnCache;
    private ConnectivityManager connectivityManager;
    private NetworkInfo activeNetworkInfo;
    private boolean estConnecte;
    private ConnectivityManager.NetworkCallback connectivityCallback;

    /**
     * Fonction onCreate appelée lors de le création de l'activité
     *
     * @param savedInstanceState données à récupérer si l'activité est réinitialisée après
     *                           avoir planté
     *                           Lie l'activité à son layout et récupère les éléments avec lesquels on peut intéragir
     *                           On initialise le Connectivity Manager pour cette activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOk = findViewById(R.id.btnOk);
        btnCache = findViewById(R.id.btnCache);
        editTextPseudo = findViewById(R.id.editTextPseudo);
        password = findViewById(R.id.password);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        btnOk.setOnClickListener(this);
        btnCache.setOnClickListener(this);

        connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        estConnecte = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        btnCache.setVisibility((estConnecte) ? View.GONE : View.VISIBLE);
        btnOk.setEnabled(estConnecte);

        //Définition du callback du ConnectivityManager
        connectivityCallback
                = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                estConnecte = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        btnOk.setEnabled(estConnecte);
                        btnCache.setVisibility(View.GONE);
                    }
                });
                Log.i("PMR", "INTERNET CONNECTED");

            }

            @Override
            public void onLost(Network network) {
                estConnecte = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        btnOk.setEnabled(estConnecte);
                        btnCache.setVisibility(View.VISIBLE);
                    }
                });
                Log.i("PMR", "INTERNET LOST");

            }
        };

        // Définition du Connectivity Manager
        connectivityManager.registerNetworkCallback(
                new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build(), connectivityCallback);

    }


    /**
     * Fonction onResume appelée lors de la reprise de l'activité principale après mise en pause
     * pour cause d'appel à une autre activité
     * Permet de remplir par défaut le champ pseudo avec le dernier pseudo rentré
     * Le pseudo sera ainsi rafraichit à chaque fois
     * Permet aussi d'activer/désactiver le bouton OK en fonction de l'état du réseau,
     * et de récupérer l'URL de l'API
     */
    @Override
    protected void onResume() {
        super.onResume();
        /* Affichage du dernier pseudo saisi */
        editTextPseudo.setText(preferences.getString("pseudo", ""));
        verfierUrl();

    }

    /**
     * Création de la ToolBar au démarrage de l'activité
     *
     * @param menu le menu de la ToolBar qui contient les différents items
     * @return true pour que le menu soit affiché
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Traitement du clic sur un item de la ToolBar
     *
     * @param item l'item du menu de la ToolBar sélectionné
     * @return true
     * Ici, un seul item est disponible, le clic ouvre l'activité Settings Activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
        return true;
    }


    /**
     * Fonction par défaut de l'interface View.OnClickListener, appelée lors du clic sur la vue
     *
     * @param v la vue cliquée
     *          Ici, lors du clic sur le bouton OK, on ouvre l'activité ChoixListe Activity et on sauvegarde
     *          le pseudo dans les préférences
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                sauverPseudo();
                sync();
                break;
            case R.id.btnCache:
                // Securisation de la connexion hors-ligne
                if (preferences.getString("passe", "dfshiuo45641519684684doijziojxijFJK"
                        + "HEKZJFDJIZENFNEJIZNFJKJAOADZDaoijdzijxozaj45661511zixjaopidsza5d465zaq498ed"
                        + "456z4a8d489aAZDAZDADZDz49s4a4ds894s854az4").equals(password.getText().toString())
                        && preferences.getString("pseudo", "jéçu!à'ndkf:,;,qijjoeçéhzpsgZKOGUZ9"
                        + "83267T0zhqhfé!39E7Fb").equals(editTextPseudo.getText().toString()))
                    ouvrirChoixListeActivity();
        }
    }

    /**
     * Permet d'ouvrir l'activité ChoixListe Activity
     * Fournit le pseudo rentré par l'utilisateur à cette nouvelle activité
     */
    private void ouvrirChoixListeActivity() {
        Intent choixListeActivity = new Intent(MainActivity.this,
                ChoixListActivity.class);
        choixListeActivity.putExtra("pseudo", editTextPseudo.getText().toString());
        startActivity(choixListeActivity);
    }

    /**
     * Permet de sauvegarder le pseudo dans les préférences de l'application
     * Le pseudo est saisi par l'utilisateur
     */
    private void sauverPseudo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pseudo", editTextPseudo.getText().toString());
        editor.apply();
        editor.commit();
    }

    /**
     * Permet de vérifier l'URL de connexion à l'API, et de la changer le cas échéant
     * Récupère l'URL à partir des préférences de l'application
     * Si l'URL est vide, remplisssage avec ue adresse par défaut
     */
    private void verfierUrl() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences.getString("url", "").equals("")) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("url", "http://tomnab.fr/todo-api/");
            editor.apply();
            editor.commit();
        }
        url = preferences.getString("url", "");
        TodoApiServiceFactory.changeApiBaseUrl(url);
    }

    /**
     * Fonction principale de l'activité
     * Permet de lancer une requête de connexion à l'API, et de récupérer le cas échéant le hash
     * d'identification
     * Ouvre une nouvelle activité (ChoixListeActivity) en cas de succès de la requête
     */
    private void sync() {

        todoApiService = TodoApiServiceFactory.createService(TodoApiService.class);

        call = todoApiService.connexion(editTextPseudo.getText().toString(),
                password.getText().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    /* On stocke le hash dans les préférences */
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("hash", response.body().hash);
                    editor.putString(("passe"), password.getText().toString());
                    editor.apply();
                    editor.commit();


                    Log.i("Main", "onResponse: " + response.body().hash);

                    /* On ouvre la prochaine activité (nouvel intent) */
                    ouvrirChoixListeActivity();


                } else {
                    Log.d("TAG", "onResponse: " + response.code());
                    Toast.makeText(MainActivity.this, "Error code : " +
                            response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error code : ",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "onFailure() called with: call = [" + call +
                        "], t = [" + t + "]");
            }
        });

    }

    /**
     * Fonction appelée lors de l'arrêt de l'activité
     * On "vide" la file d'appels de requêtes vers l'API
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (call != null)
            call.cancel();
    }
}

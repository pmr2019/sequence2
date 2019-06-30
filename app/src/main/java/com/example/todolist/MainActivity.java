package com.example.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import api.ReponseApi;
import api.ToDoApi;
import modele.ItemToDo;
import modele.ListeToDo;
import modele.ProfilListeToDo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Float.valueOf;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TEST" ;
    public static String USER;
    String url;
    EditText edt_pseudo;
    EditText edt_mdp;
    Button btn_connexion;
    Button btn_connexionApi;
    ProfilListeToDo p;
    Intent choixListe;


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(this);
        edt_pseudo = findViewById(R.id.iEdt_pseudo);
        edt_pseudo.setText(settings.getString("pseudo",""));
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("urlApi","http://tomnab.fr/todo-api");
        editor.apply();
        url = settings.getString("urlApi", "http://tomnab.fr/todo-api");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_connexion = findViewById(R.id.iBtn_SignIn);
        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_pseudo = findViewById(R.id.iEdt_pseudo);
                String login = edt_pseudo.getText().toString();
                int btn_id = v.getId();

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("pseudo",edt_pseudo.getText().toString());
                editor.apply();

                choixListe = new Intent(MainActivity.this, ChoixListActivity.class);
                Bundle extras = new Bundle();
                extras.putString("LOGIN", login);
                extras.putInt("ID", btn_id);
                choixListe.putExtras(extras);
                startActivity(choixListe);

            }
        });

        btn_connexionApi = findViewById(R.id.iBtn_SignInApi);
        btn_connexionApi.setEnabled(verifReseau());
        if (!btn_connexionApi.isEnabled()){
            btn_connexionApi.setBackgroundColor(0x37003BFF);
        }
        btn_connexionApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                /*
                ToDoApi interface_toDo = retrofit.create(ToDoApi.class);

                interface_toDo.authenticate("tom", "web").enqueue(new Callback<ReponseApi>() {
                    @Override
                    public void onResponse(Call<ReponseApi> call, Response<ReponseApi> response) {
                        if (response.isSuccessful() && response.body().success) {
                            Log.i("TAG" ,"" + response.body().hash);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReponseApi> call, Throwable t) {

                    }
                });

                String login = edt_pseudo.getText().toString();
                String mdp = edt_mdp.getText().toString();
                int id_btn = v.getId();

                choixListe = new Intent(MainActivity.this, ChoixListActivity.class);
                Bundle extras = new Bundle();
                extras.putString("LOGIN", login);
                extras.putString("MDP", mdp);
                extras.putInt("ID", id_btn);

                startActivity(choixListe);
                */
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.iIt_test :
                Toast.makeText(this, "Clique sur l'item Test", Toast.LENGTH_SHORT).show();

            case R.id.iIt_pref :
                Toast.makeText(this, "Gérer ses préférences", Toast.LENGTH_SHORT).show();
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean verifReseau() {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();
        String str_toast = "Aucun réseau détecté";
        boolean bool_Statut = false;
        if (netInfo != null) {
            NetworkInfo.State netState = netInfo.getState();
            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                bool_Statut = true;
                int netType = netInfo.getType();
                switch (netType) {
                    case ConnectivityManager.TYPE_MOBILE:
                        str_toast = "Réseau mobile détecté";
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        str_toast = "Réseau wifi détecté";
                        break;
                }

            }
        }
        Toast.makeText(this, str_toast, Toast.LENGTH_SHORT).show();
        return bool_Statut;
    }

}

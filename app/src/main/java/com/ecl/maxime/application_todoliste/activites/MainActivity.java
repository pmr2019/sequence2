package com.ecl.maxime.application_todoliste.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.ecl.maxime.application_todoliste.R;
import com.ecl.maxime.application_todoliste.api_request.Hashcode;
import com.ecl.maxime.application_todoliste.api_request.ServiceFactory;
import com.ecl.maxime.application_todoliste.api_request.Services;
import com.ecl.maxime.application_todoliste.classes.ProfileListeToDo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private EditText edt_pseudo;
    private EditText edt_password;
    private Button btn_ok;
    private Call<Hashcode> call;
    public static final String HASH = "hash";
    public static String BASE_URL;
    private MainActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_pseudo=findViewById(R.id.pseudo_edittext);
        edt_password=findViewById(R.id.password_edt);
        btn_ok=findViewById(R.id.btn_ok);

        this.activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        BASE_URL = sharedPreferences.getString("api","");

        btn_ok.setEnabled(false);

        if (verifReseau())
            btn_ok.setEnabled(true);

        if (!verifReseau()){
            AlertDialog.Builder myPopup = new AlertDialog.Builder(activity);
            myPopup.setTitle("Pas d'internet");
            myPopup.setMessage("Voulez-vous manipuler les donnéees en cache ?");
            myPopup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent dao = new Intent(MainActivity.this, DaoActivity.class);
                    startActivity(dao);
                }
            });
            myPopup.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });


        }


        // Mise en place de l'écouteur
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifReseau()) {
                    String password = edt_password.getText().toString();
                    String pseudo = edt_pseudo.getText().toString();

                    seConnecter(pseudo, password);
                }
            }
        });
    }

    public boolean verifReseau()
    {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

        String sType = "Aucun réseau détecté";
        Boolean bStatut = false;
        if (netInfo != null)
        {
            NetworkInfo.State netState = netInfo.getState();

            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0)
            {
                bStatut = true;
                int netType= netInfo.getType();
                switch (netType)
                {
                    case ConnectivityManager.TYPE_MOBILE :
                        sType = "Réseau mobile détecté"; break;
                    case ConnectivityManager.TYPE_WIFI :
                        sType = "Réseau wifi détecté"; break;
                }

            }
        }

        this.alerter(sType);
        return bStatut;
    }

    public void alerter(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void seConnecter(final String login, final String mdp){
        Services service = ServiceFactory.createService(Services.class);
        call = service.connexion(login, mdp);
        call.enqueue(new Callback<Hashcode>() {
            @Override
            public void onResponse(Call<Hashcode> call, Response<Hashcode> response) {
                if (response.body() != null) {
                    ProfileListeToDo user = new ProfileListeToDo(login, mdp);
                    Intent i = new Intent(MainActivity.this, ChoixListActivity.class);
                    i.putExtra(HASH, response.body().hash);
                    i.putExtra(USER_SERVICE, user);
                    startActivity(i);
                }
                else {
                    Toast.makeText(MainActivity.this, "Mot de passe incorrect", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Hashcode> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Mot de passe incorrect", Toast.LENGTH_LONG).show();
            }
        });
    }


}

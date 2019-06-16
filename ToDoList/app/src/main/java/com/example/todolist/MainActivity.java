package com.example.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnOK = null;
    private EditText edtPseudo = null;
    private EditText edtPassword = null;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private String pseudo;
    private String password;
    private String mHash;
    private AsyncTask task;
    private Intent toChoixListActivity;
    private String myUrl;
    public Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOK = findViewById(R.id.btnOK);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        data = new Bundle();
        myUrl = "http://tomnab.fr/todo-api";
        data.putString("myUrl",myUrl);
        //on vérifie la connection au réseau
        if(verifReseau()){
            btnOK.setVisibility(View.VISIBLE);
        }
        edtPseudo = findViewById(R.id.edt_pseudo);
        edtPassword = findViewById(R.id.edt_password);
        btnOK.setOnClickListener(this);
        toChoixListActivity = new Intent(this, ChoixListActivity.class);
        toChoixListActivity.putExtras(data);
    }

    @Override
    public void onClick(View v) {
        /// Gestion avec API Rest :
        if(v.getId()==R.id.btnOK){
            mHash=null;
            pseudo=edtPseudo.getText().toString();
            password=edtPassword.getText().toString();
            //Asynctack pour trouver si la connection est autorisée
            task = new PostAsyncTask();
            task.execute();
        }
    }


    protected void onStart() {
        super.onStart();
        /*
        myUrl = mSettings.getString("url","");
        Log.i("url API sauvegardé =",myUrl);
        */
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings) {
            Intent toSettings = new Intent(this,SettingsActivity.class);
            startActivity(toSettings);
        }
        return super.onOptionsItemSelected(item);
    }



    /////////////////// gestion reseau/////////////////////
    public boolean verifReseau(){
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

        Log.i("DETECTION:",sType);
        return bStatut;
    }


    public class PostAsyncTask extends AsyncTask<Object,Void,String> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progess).setVisibility(View.VISIBLE);
        }

        @Override protected String doInBackground(Object... objects) {
            return  (new DataProvider()).getHash(myUrl,pseudo,password,"POST");
        }

        @Override protected void onPostExecute(String hash) {
            super.onPostExecute(hash);
            mHash=hash;
            if(mHash!="" && mHash!=null && mHash!="erreur avec l'objet Json"){
                Log.i("Connection","réussie ! ");
                Log.i("mHash=",mHash);
                //On stock le hash et la liste de Lists dans les préférences
                mEditor = mSettings.edit();
                mEditor.clear();
                mEditor.putString("mHash", mHash);
                mEditor.commit();
                //On lance l'activité suivante
                startActivity(toChoixListActivity);
            }
            else if (mHash=="" || mHash=="erreur avec l'objet Json"){
                Toast toast = Toast.makeText(getApplicationContext(), "Mauvais Pseudo/mdp", Toast.LENGTH_SHORT);
                toast.show();
            }
            findViewById(R.id.progess).setVisibility(View.GONE);
        }
    }
}

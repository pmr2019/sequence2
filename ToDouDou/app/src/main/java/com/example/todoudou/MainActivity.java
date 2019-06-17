package com.example.todoudou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity {

    private Button btn_Ok = null;
    private EditText edt_pseudo = null;
    private EditText edt_pass = null;

    // méthode appelée pour un debug dans l'appli
    private void alerter(String s) {
        Log.i("Debug",s);
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This 2 lines sets the toolbar as the app bar for the activity
        // cf https://developer.android.com/training/appbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_main);
        setSupportActionBar(myToolbar);

        btn_Ok = findViewById(R.id.btn_ok);
        edt_pseudo = findViewById(R.id.edt_pseudo);
        edt_pass = findViewById(R.id.edt_pass);

        // Lors du clic sur le bouton OK, le pseudo est sauvegardé dans les préférences
        // partagées de l’application et l’activité ChoixListActivity s’affiche
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pseudo
                String pseudo = String.valueOf(edt_pseudo.getText());
                String pass = String.valueOf(edt_pass.getText());
                if(pseudo.length() != 0 & pass.length() != 0){
                    sauvegardeLastPseudoPass(pseudo, pass);

                    PostAsyncTask ast = new PostAsyncTask();
                    ast.execute(pseudo, pass);

                }
                else{
                    alerter("Veuillez rentrer un pseudo et un mot de passe");
                }


            }
        });
        edt_pseudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerter("Entrez votre pseudo");
            }
        });
        edt_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { alerter("Entrez votre mot de passe");
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
//        alerter("onStart");

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        // on vérifie l'état du réseau, et on affiche ou non le bouton en conséquence
        boolean reseauDispo =  new Reseau(this).verifReseau();

        // si le réseau est disponible, et que l'utilisateur s'était déjà connecté, on se connecte directement
        if(reseauDispo){
            if(settings.contains("pseudo") && settings.contains("pass")){
                String pseudo = settings.getString("pseudo", "");
                String pass = settings.getString("pass", "");
                if(pseudo.length() != 0 & pass.length() != 0){
                    new PostAsyncTask().execute(pseudo, pass);
                }
            }
        }

        if(reseauDispo) btn_Ok.setVisibility(View.VISIBLE);
        else btn_Ok.setVisibility(View.INVISIBLE);



        edt_pseudo.setText(settings.getString("pseudo",""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    // https://developer.android.com/training/appbar/actions.html
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_deconnexion:
                alerter("Déconnexion");
//                Intent toSettings = new Intent(this,SettingsActivity.class);
//                startActivity(toSettings);
                return true;
            case R.id.action_settings:
                alerter("Settings");
                Intent toSettings = new Intent(this,SettingsActivity.class);
                startActivity(toSettings);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    private void sauvegardeLastPseudoPass(String pseudo, String pass){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = settings.edit();
//        editor.clear();
        editor.putString("pseudo", pseudo);
        editor.putString("pass", pass);
        editor.commit();
    }
    private void sauvegardeHash(String hash){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = settings.edit();
//        editor.clear();
        editor.putString("hash", hash);
        editor.commit();
    }

    private void test(){
        startActivity(new Intent(MainActivity.this, ChoixListActivity.class));
    }


    class PostAsyncTask  extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String pseudo = strings[0];
            String pass = strings[1];
            Reseau res = new Reseau(getBaseContext());
            String url = "/authenticate?user=" + pseudo + "&password=" + pass;
            String reponse = res.executePost(url, "");
            return reponse;
        }
        protected void onPostExecute(String result) {

            if(result!=null){
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(result,JsonObject.class);
                String hash = resp.get("hash").getAsString();
//                alerter(hash);
                sauvegardeHash(hash);
                startActivity(new Intent(MainActivity.this, ChoixListActivity.class));
            }
            else{
                alerter("Pseudo ou mot de pass incorrect");
                return;
            }
        }
    }
}

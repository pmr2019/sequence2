package com.example.todoudou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoudou.database.DataProvider;

public class MainActivity extends AppCompatActivity {

    private Button btn_Ok = null;
    private EditText edt_pseudo = null;
    private EditText edt_pass = null;

    private boolean dataRequest = false;
    private DataProvider data = null;

    // méthode appelée pour un debug dans l'appli
    private void alerter(String s) {
        Log.i("debug2",s);
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
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_white_24dp);
        myToolbar.setOverflowIcon(drawable);

//        // initialise le dataProvider
//        data = DataProvider.getInstance(this);
//        data.setCustomObjectListener(new DataProvider.DataListener() {
//            @Override
//            public void onDataReady(int type, Object... data) {
//                dataProcessing(type, data);
//            }
//        });

        btn_Ok = findViewById(R.id.btn_ok);
        edt_pseudo = findViewById(R.id.edt_pseudo);
        edt_pass = findViewById(R.id.edt_pass);

        // Lors du clic sur le bouton OK, le pseudo est sauvegardé dans les préférences
        // partagées de l’application et l’activité ChoixListActivity s’affiche
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pseudo = String.valueOf(edt_pseudo.getText());
                String pass = String.valueOf(edt_pass.getText());
//                if(pseudo.length() != 0 & pass.length() != 0){
//                    new Connexion_API_AsyncTask().execute(pseudo, pass);
//                }
//                else{
//                    alerter("Veuillez rentrer un pseudo et un mot de passe");
//                }

                manageData(Constant.CONNEXION, pseudo, pass);

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


        // initialise le dataProvider
        data = DataProvider.getInstance(this);
        data.setCustomObjectListener(new DataProvider.DataListener() {
            @Override
            public void onDataReady(int type, Object... data) {
                dataProcessing(type, data);
            }
        });

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        // si l'utilisateur s'était déjà connecté, on se connecte directement

        // TODO vérifier le réseau
        //  si reseau passer par la connexion par l'api pour récupérer le hash (qui est censé changer à chaque fois)
        //  sinon vérifier que le pseudo mot de passe des shared preferences est dans la base de donnée avec pseudo mot de passe et si oui lancer l'acitivity
//        String pseudo = settings.getString("pseudo", "");
//        String pass = settings.getString("pass", "");
//        if(pseudo.length() != 0 & pass.length() != 0){
//            startActivity(new Intent(MainActivity.this, ChoixListActivity.class));
//        }

        // on vérifie l'état du réseau, et on affiche ou non le bouton en conséquence
        if(new Reseau(this).verifReseau()) btn_Ok.setText("Ok");
        else btn_Ok.setText("Ok\nMode hors ligne");

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


    private void manageData(int type, Object... dataProvide){
        if(!dataRequest){
            dataRequest = true;
            if (type == Constant.CONNEXION)
                data.getConnexionUser((String) dataProvide[0], (String) dataProvide[1]);
        }
        else{
            alerter("Veuillez attendre la fin du traitement de l'action précédente");
        }
    }

    private void dataProcessing(int type, Object... data) {
        Log.i("debug2", "dataprocessing, object : " + data.toString());

        if (type == Constant.CONNEXION){
            alerter("Connexion réussie !");
            sauvegardePreferenceUser((String) data[0], (String) data[1]);
            startActivity(new Intent(MainActivity.this, ChoixListActivity.class));
        }
        if (type == Constant.CONNEXION_ECHEC){
            alerter("Pseudo ou mot de pass incorrect");
        }
        dataRequest = false;
    }

    private void sauvegardePreferenceUser(String pseudo, String pass){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pseudo", pseudo);
        editor.putString("pass", pass);
        editor.commit();
    }



}

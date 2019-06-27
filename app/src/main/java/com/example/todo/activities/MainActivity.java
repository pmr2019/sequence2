package com.example.todo.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.API_models.RetroMain;
import com.example.todo.API_models.TodoInterface;
import com.example.todo.R;
import com.example.todo.database.MyDatabase;
import com.example.todo.models.DataProvider;
import com.example.todo.models.InternetCheck;
import com.example.todo.models.ProfilListeToDo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, InternetCheck.Consumer {
    private static final String TAG = "MainActivity";

    //Widgets
    private EditText edtPseudo;
    private EditText edtPassword;
    private Button btnOk;


    // DataProvider
    DataProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find widgets
        edtPseudo = findViewById(R.id.edtPseudo);
        edtPassword = findViewById(R.id.edtPassword);
        btnOk = findViewById(R.id.btnOk);

        //Attach listener
        edtPseudo.setOnEditorActionListener(this);
        btnOk.setOnClickListener(this);

        // Init databse and executor
        dataProvider = new DataProvider(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Recuperation du dernier pseudo
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        edtPseudo.setText(settings.getString("pseudo",""));
        new InternetCheck(this); // Check internet connexion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings: {
                Intent toSettings = new Intent(this, SettingsActivity.class);
                startActivity(toSettings);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk :
                Log.d(TAG, "onClick: clicked on : Ok.");
                final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                final String pseudo = edtPseudo.getText().toString();
                String password = edtPassword.getText().toString();

                dataProvider.authenticate(pseudo, password, new DataProvider.PostsListener() {
                    @Override
                    public void onSuccess(DataProvider.DataResponse dataResponse) {
                        MainActivity.this.dataProvider.stop();

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("pseudo", pseudo);
                        editor.putString("hash", dataResponse.getHash());
                        editor.commit();

                        //Intent to ChoixListActivity
                        Intent toSecondAct = new Intent(MainActivity.this, ChoixListActivity.class);
                        Bundle data = new Bundle();
                        data.putString("pseudo", pseudo);
                        toSecondAct.putExtras(data);
                        startActivity(toSecondAct);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }

    //Button validate on the on-screen keyboard = click on btnOk.
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            btnOk.performClick();
        }
        return false;
    }

    /**
     * Implementation of the interface Consumer from InternetCheck.
     * When we receive the result from an InternetCheck instance, this method is run.
     * If the app has an access to internet, we enable the button ok, else it disables it.
     * @param internet boolean
     */
    @Override
    public void isConnectedToInternet(Boolean internet) {
        if (!internet){
            btnOk.setEnabled(false);
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Aucune connexion internet");
            builder.setMessage("Voulez-vous manipuler les données en cache ?");
            // add the buttons
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    btnOk.setEnabled(true);
                }
            });
            builder.setNegativeButton("Cancel", null);
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            btnOk.setEnabled(true);
        }
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isConnectedToInternet", internet);
        editor.commit();
    }
}

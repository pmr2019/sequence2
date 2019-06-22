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

    //Retrofit API
    private static Retrofit retrofit;

    // Database
    MyDatabase myDatabase;
    Executor executor;

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
        myDatabase = MyDatabase.getInstance(this);
        executor = command -> new Thread(command).start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Recuperation du dernier pseudo
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        edtPseudo.setText(settings.getString("pseudo",""));
        new InternetCheck(this); // Check internet connexion


        // Test
//        myDatabase = MyDatabase.getInstance(this);
//        executor = command -> new Thread(command).start();
//        executor.execute(() -> {
//                ProfilListeToDo profilListeToDo = new ProfilListeToDo();
//                profilListeToDo.setLogin("PremierLogin");
//                myDatabase.profilListeToDoDao().addProfilListeToDo(profilListeToDo);
//        });
//        executor.execute(() -> {
//            ProfilListeToDo profilListeToDo = myDatabase.profilListeToDoDao().getProfilListeToDoByLogin();
//            Log.d(TAG, "onStart: profil recup="+profilListeToDo.toString());
//        });
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

                if (settings.getBoolean("isConnectedToInternet", false)) {
                    //Verify pseudo & password with the API
                    String baseUrl = settings.getString("APIurl", "http://tomnab.fr/");
                    TodoInterface service = retrofitPrettyBuilder(baseUrl).create(TodoInterface.class);
                    Call<RetroMain> call = service.authenticate(pseudo, password);
                    call.enqueue(new Callback<RetroMain>() {
                        @Override
                        public void onResponse(Call<RetroMain> call, Response<RetroMain> response) {
                            if (response.body() != null) {
                                RetroMain retroMain = response.body();
                                if (retroMain.isSuccess()) {
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("pseudo", pseudo);
                                    editor.putString("hash", retroMain.getHash());
                                    editor.commit();

                                    //Intent to ChoixListActivity
                                    Intent toSecondAct = new Intent(MainActivity.this, ChoixListActivity.class);
                                    Bundle data = new Bundle();
                                    data.putString("pseudo", pseudo);
                                    toSecondAct.putExtras(data);
                                    startActivity(toSecondAct);
                                } else {
                                    Log.d(TAG, "onResponse: http code : " + retroMain.getStatus());
                                }
                            } else {
                                Log.d(TAG, "onResponse: empty response. HTTP CODE : " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<RetroMain> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.toString());
                        }
                    });
                } else {
                    executor.execute(() -> {
                        ProfilListeToDo profilListeToDo = myDatabase.profilListeToDoDao().getProfilListeToDoByLogin(pseudo);
                        Log.d(TAG, "onClick: profil recup = "+profilListeToDo.toString());
                        if (profilListeToDo != null) {
                            //Intent to ChoixListActivity
                            Intent toSecondAct = new Intent(MainActivity.this, ChoixListActivity.class);
                            Bundle data = new Bundle();
                            data.putString("pseudo", pseudo);
                            toSecondAct.putExtras(data);
                            startActivity(toSecondAct);
                        }
                    });
                    // Si aucun profil correspondant n'a été trouve en cache
                    Log.i(TAG, "onClick: pseudo unknown.");
                    Toast.makeText(this, "Mode hors ligne : pseudo inconnu.", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void isConnectedToInternet(Boolean internet) {
        if (!internet){
            btnOk.setEnabled(false);
//            Toast.makeText(this, "Aucune connexion internet.", Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(this, "Connexion internet établie.", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isConnectedToInternet", internet);
        editor.commit();
    }

    /**
     * Build a retrofit object with a special Gson object.
     * @param baseUrl : String
     * @return
     */
    public Retrofit retrofitPrettyBuilder(String baseUrl){
        if (retrofit==null){
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}

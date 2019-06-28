package com.example.todo.activities;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.todo.R;
import com.example.todo.models.DataProvider;
import com.example.todo.models.InternetCheck;
import com.example.todo.models.ListeToDo;
import com.example.todo.models.ProfilListeToDo;
import com.example.todo.ui.RecyclerViewAdapterList;

import java.util.ArrayList;

public class ChoixListActivity extends AppCompatActivity implements View.OnClickListener, InternetCheck.Consumer {
    private static final String TAG = "ChoixListActivity";

    //Widgets
    private EditText edtListTitle;
    private Button btnAddList;

    //Var
    private String pseudo;
    private ArrayList<ListeToDo> mList = new ArrayList<ListeToDo>(); //Liste des listes to do
    private ProfilListeToDo profil;

    // DataProvider
    DataProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_list);
        Log.d(TAG, "onCreate: started.");

        // Init dataProvider
        dataProvider = new DataProvider(this);

        //Get pseudo active
        Bundle data = this.getIntent().getExtras();
        assert data != null;
        pseudo = data.getString("pseudo", "inconnu");
        Log.d(TAG, "pseudo : " + pseudo);

        //Init widgets
        edtListTitle = findViewById(R.id.edtListTitle);
        btnAddList = findViewById(R.id.btnAddList);

        //Add listener
        btnAddList.setOnClickListener(this);

        initRecyclerView();
        new InternetCheck(this); // To load the data, cf. isConnectedToInternet()
        // Get todo lists from the pseudo = load profile from the API & update BDD : done in isConnectedToInternet
        //Init reclyclerView (done in loadProfile_API if it is with the API because of the thread)

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddList:
                Log.d(TAG, "onClick: clicked on : Ajouter.");
                String title = edtListTitle.getText().toString();
                edtListTitle.setText("");
                if (!title.isEmpty()) {
                    addListeToDo(title);
                }
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init RecyclerView");
        RecyclerView recyclerView = findViewById(R.id.reclycler_view_list);
        RecyclerViewAdapterList adapter = new RecyclerViewAdapterList(this, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void refreshRecyclerView() {
        mList.clear();
        mList.addAll(profil.getMesListeToDo());
        RecyclerView recyclerView = findViewById(R.id.reclycler_view_list);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void addListeToDo(String title) {
        ListeToDo listeToDo = new ListeToDo(profil.getLogin(), title);
        profil.ajouteListe(listeToDo);
        Log.d(TAG, "addListeToDo: "+listeToDo.toString());
        dataProvider.addListeToDo(this, listeToDo);
        Log.d(TAG, "addListeToDo: " + profil.toString());
        refreshRecyclerView();
    }

    private void loadProfilToDo(){
        dataProvider.getProfilToDo(pseudo, new DataProvider.PostsListener() {
            @Override
            public void onSuccess(DataProvider.DataResponse dataResponse) {
//                    ChoixListActivity.this.dataProvider.stop();
                profil = dataResponse.getProfilListeToDo();
                refreshRecyclerView();
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "onErrorFromDataProvider: "+error);
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(ChoixListActivity.this);
                builder.setTitle("Error");
                builder.setMessage("Error unknown.");
                // add the buttons
                builder.setNeutralButton("Ok", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void isConnectedToInternet(Boolean internet) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isConnectedToInternet = settings.getBoolean("isConnectedToInternet",false);
        if (isConnectedToInternet == false && internet == true){
            Log.d(TAG, "Connexion internet detected.");
            // Modif offline will erase the online, only for the lists concerned.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connexion internet détectée");
            builder.setMessage("Voulez-vous vous connecter ?");
            // add the buttons
            builder.setPositiveButton("Oui", (dialog, which) -> {
                SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(ChoixListActivity.this);
                SharedPreferences.Editor editor = settings1.edit();
                editor.putBoolean("isConnectedToInternet", internet);
                editor.commit();
                Intent toSecondAct = new Intent(ChoixListActivity.this, MainActivity.class);
                startActivity(toSecondAct);
            });
            builder.setNegativeButton("Non", (dialog, which) -> loadProfilToDo());
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if ((isConnectedToInternet == true && internet == true) || (isConnectedToInternet == false && internet == false)) {
            loadProfilToDo();
        }
        else if (isConnectedToInternet == true && internet == false) {
            Log.d(TAG, "Connexion internet lost.");
            // Ask if the user wants to modify offline (as in the MainActivity).
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connexion perdue");
            builder.setMessage("Voulez-vous manipuler les données en cache ?");
            // add the buttons
            builder.setPositiveButton("Oui", (dialog, which) -> loadProfilToDo());
            builder.setNegativeButton("Non", (dialog, which) -> {
                SharedPreferences settings12 = PreferenceManager.getDefaultSharedPreferences(ChoixListActivity.this);
                SharedPreferences.Editor editor = settings12.edit();
                editor.putBoolean("isConnectedToInternet", internet);
                editor.commit();
                Intent toSecondAct = new Intent(ChoixListActivity.this, MainActivity.class);
                startActivity(toSecondAct);
            });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
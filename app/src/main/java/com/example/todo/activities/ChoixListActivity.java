package com.example.todo.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.todo.API_models.RetroMain;
import com.example.todo.API_models.TodoInterface;
import com.example.todo.R;
import com.example.todo.database.MyDatabase;
import com.example.todo.models.DataProvider;
import com.example.todo.models.InternetCheck;
import com.example.todo.models.ListeToDo;
import com.example.todo.models.ProfilListeToDo;
import com.example.todo.ui.RecyclerViewAdapterList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChoixListActivity extends AppCompatActivity implements View.OnClickListener, InternetCheck.Consumer {
    private static final String TAG = "ChoixListActivity";

    //Widgets
    private EditText edtListTitle;
    private Button btnAddList;

    //Var
    private String pseudo;
    private String hash;
    private ArrayList<ListeToDo> mList = new ArrayList<ListeToDo>(); //Liste des listes to do
    private ProfilListeToDo profil;
    private TodoInterface service;
    public ArrayList<Integer> indices = new ArrayList<>(); //Key : position of the list in the recylcerview, value : id of the list

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

        // Initialize hash & service variables
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String baseUrl = settings.getString("APIurl", "http://tomnab.fr/");
        hash = settings.getString("hash", "");
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
        service = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TodoInterface.class);

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
                Log.d(TAG, "onClick: clicked on : Ok.");
                String title = edtListTitle.getText().toString();
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
        profil.ajouteListe(new ListeToDo(title));
        addListeToDo_API(new ListeToDo(title));
        Log.d(TAG, "addListeToDo: " + profil.toString());
        refreshRecyclerView();
    }

    /**
     * Add the liste to the user profile in the api.
     * @param listeToDo
     */
    private void addListeToDo_API(ListeToDo listeToDo){
        Call<RetroMain> call = service.addList(hash, listeToDo.getTitreListeToDo());
        call.enqueue(new Callback<RetroMain>() {
            @Override
            public void onResponse(Call<RetroMain> call, Response<RetroMain> response) {
                if (response.body() != null) {
                    RetroMain retroMain = response.body();
                    if (retroMain.isSuccess()) {
                        Log.d(TAG, "onResponse: success adding a new listeToDo.");
                        RetroMain rList = retroMain.getList();
                        indices.add(rList.getId());
                    } else {
                        Log.d(TAG, "onResponse: http code : "+retroMain.getStatus());
                    }
                } else {
                    Log.d(TAG, "onResponse: empty response. HTTP CODE : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<RetroMain> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }

    @Override
    public void isConnectedToInternet(Boolean internet) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isConnectedToInternet = settings.getBoolean("isConnectedToInternet",false);
        if (isConnectedToInternet == false && internet == true){
            // Modif offline will erase the online, only for the lists concerned.
        } else if ((isConnectedToInternet == true && internet == true) || (isConnectedToInternet == false && internet == false)) {
            dataProvider.loadListeToDo(pseudo, hash, new DataProvider.PostsListener() {
                @Override
                public void onSuccess(DataProvider.DataResponse dataResponse) {
                    ChoixListActivity.this.dataProvider.stop();
                    profil = dataResponse.getProfilListeToDo();
                    refreshRecyclerView();
                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, "onErrorFromDataProvider: "+error);
                }
            });
        } else if (isConnectedToInternet == true && internet == false) {
            // Ask if the user wants to modify offline (as in the MainActivity).

        }
    }
}
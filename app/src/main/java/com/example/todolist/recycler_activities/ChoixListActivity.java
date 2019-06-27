package com.example.todolist.recycler_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.api.TodoApiService;
import com.example.todolist.api.TodoApiServiceFactory;
import com.example.todolist.api.response_class.ListResponse;
import com.example.todolist.api.response_class.Lists;
import com.example.todolist.api.response_class.UneListe;
import com.example.todolist.modele.ListeToDo;
import com.example.todolist.modele.ProfilListeToDo;
import com.example.todolist.recycler_activities.adapter.ItemAdapterList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Définition de la classe ChoixListActivity.
 * Cette classe représente l'activité ChoixListe Activity de l'application
 */
public class ChoixListActivity extends Library implements View.OnClickListener,
        ItemAdapterList.onClickListListener {

    /* Le titre de la nouvelle ToDoList à ajouter, saisi par l'utilisateur dans l'activité courante */
    private EditText ajouterListe;
    /* L'adapteur associé à la Recycler View de l'activité courante */
    private ItemAdapterList itemAdapterList;
    /* La Recycle View de l'activité courante */
    private RecyclerView recyclerView;
    /* Le hash d'identification auprès de l'API */
    private String hash;
    /* L'interface de connexion auprès de l'API */
    TodoApiService todoApiService;
    /* La file des requêtes auprès de l'API */
    private Call<Lists> call;
    /* Les préférences de l'application */
    private SharedPreferences preferences;
    /* La liste de ToDoLists associée à l'utilisateur courant */
    private List<ListeToDo> data;
    /* Le bouton d'ajout d'une ToDoList */
    private Button btnOk;


    /**
     * Fonction onCreate appelée lors de le création de l'activité
     *
     * @param savedInstanceState données à récupérer si l'activité est réinitialisée après avoir planté
     *                           Lie l'activité à son layout et récupère les éléments avec lesquels on peut intéragir
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixliste);

        /* Récupération du pseudo depuis les préférences de l'application */
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        hash = preferences.getString("hash", "");
        Log.i("PMR", "onCreate: " + hash);


        /* Traitement de l'ajout d'une ToDoList au profil */
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        ajouterListe = findViewById(R.id.ajouterListe);
    }

    /**
     * Fonction onResume appelée après la création de l'activité et à chaque retour sur l'activité courante
     * Permet de générer la RecyclerView associée à la liste des ToDoLists en récupérant les données
     * depuis l'API si on possède une connexiion réseau, depuis la BDD sinon
     */
    @Override
    protected void onResume() {
        super.onResume();
        estConnecte = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        btnOk.setEnabled(estConnecte);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (estConnecte)
                    sync();
                else
                    recupListesDB();
            }
        });
    }


    /**
     * Fonction appelée lors de la destruction de l'activité, permet de libérer le Connectivity Manager
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityManager.unregisterNetworkCallback(connectivityCallback);
    }

    /**
     * Fonction par défaut de l'interface View.OnClickListener, appelée lors du clic sur la vue
     *
     * @param v la vue cliquée
     *          Ici, lors du clic sur le bouton OK, on ajoute la ToDoList dans l'API et dans la RecyclerView
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                String label = ajouterListe.getText().toString();
                ajoutListe(label);
                Log.i("ChoixListe", "onClick: " + itemAdapterList.getItemCount());
                break;
            default:
        }
    }

    /**
     * Permet d'ouvrir l'activité ShowList Activity lors du clic sur une des ToDoLists
     *
     * @param position l'indice où se trouve la ToDoList dans la liste des ToDoLists
     */
    @Override
    public void clickList(int position) {
        Intent showListIntent = new Intent(this, ShowListActivity.class);
        showListIntent.putExtra("idListe", data.get(position).getId());
        startActivity(showListIntent);
    }


    /**
     * Permet de récupérer la liste des ToDoLists associée à l'utilisateur courant
     * La réponse de la requête vers l'API met à jour la liste data en cas de succès, ainsi que la BDD
     */
    private void sync() {

        todoApiService = TodoApiServiceFactory.createService(TodoApiService.class);

        call = todoApiService.recupereListes(hash);

        call.enqueue(new Callback<Lists>() {
            @Override
            public void onResponse(Call<Lists> call, final Response<Lists> response) {

                if (response.isSuccessful()) {

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            //stocke les listes
                            List<UneListe> lists = response.body().listeDeListes;
                            data = new ArrayList<ListeToDo>();
                            for (UneListe x : lists) {
                                data.add(new ListeToDo(x.titreListeToDO, x.id));
                                Log.i("TAG", "onResponse: " + x.id + " " + x.titreListeToDO);
                                x.hash = hash;
                            }
                            database.listeDao().insertAll(lists);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    /* Mise en place de la Recycler View sur la liste des ToDoLists associée au profil*/
                                    recyclerView = findViewById(R.id.recyclerView);
                                    itemAdapterList = new ItemAdapterList(data, ChoixListActivity.this);
                                    recyclerView.setAdapter(itemAdapterList);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(ChoixListActivity.this));

                                }
                            });
                        }
                    });


                } else {
                    Log.d("TAG", "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Lists> call, Throwable t) {
                Toast.makeText(ChoixListActivity.this, "Error code : ", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

    }

    /**
     * Permet d'ajouter une ToDoList à la RecyclerView et dans l'API
     *
     * @param label la description associée à la nouvelle ToDoList
     */
    private void ajoutListe(String label) {
        todoApiService = TodoApiServiceFactory.createService(TodoApiService.class);
        Call<ListResponse> call = todoApiService.ajoutListe(hash, label);
        call.enqueue(new Callback<ListResponse>() {
            @Override
            public void onResponse(Call<ListResponse> call, Response<ListResponse> response) {
                if (response.isSuccessful()) {
                    UneListe x = response.body().list;
                    ListeToDo item = new ListeToDo(x.titreListeToDO, x.id);
                    data.add(item);
                    itemAdapterList.notifyItemInserted(data.size() - 1);
                    Log.i("TAG", "onResponse: nice");
                } else {
                    Log.d("TAG", "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ListResponse> call, Throwable t) {
                Toast.makeText(ChoixListActivity.this, "Error code : ", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });
    }


    /**
     * Permet de récupérer l'ensemble des ToDoLists de l'utiilisateur auprès de la BDD
     * Cette fonction est appelée hors-ligne
     */
    private void recupListesDB() {
        Log.i("PMR", "recupListesDB");
        List<UneListe> lists = database.listeDao().getAll(hash);
        data = new ArrayList<ListeToDo>();
        for (UneListe x : lists) {
            data.add(new ListeToDo(x.titreListeToDO, x.id));
            Log.i("TAG", "onResponse: " + x.id + " " + x.titreListeToDO);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("PMR", "run: afficher adapter");
                recyclerView = findViewById(R.id.recyclerView);
                itemAdapterList = new ItemAdapterList(data, ChoixListActivity.this);
                recyclerView.setAdapter(itemAdapterList);
                recyclerView.setLayoutManager(new LinearLayoutManager(ChoixListActivity.this));
            }
        });

    }
}

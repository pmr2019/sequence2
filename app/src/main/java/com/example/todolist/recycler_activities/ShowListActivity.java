package com.example.todolist.recycler_activities;

/** Définition de la classe ShowListActivity.
 * Cette classe représente l'activité ShowList Activity de l'application
 */

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
import com.example.todolist.api.response_class.ItemResponse;
import com.example.todolist.api.response_class.Items;
import com.example.todolist.api.response_class.UnItem;
import com.example.todolist.modele.ItemToDo;
import com.example.todolist.modele.ProfilListeToDo;
import com.example.todolist.recycler_activities.adapter.ItemAdapterItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Définition de la classe ShowListActivity.
 * Cette classe représente l'activité ShowList Activity de l'application
 */
public class ShowListActivity extends Library implements View.OnClickListener,
        ItemAdapterItem.onClickItemListener{

    /* Le pseudo rentré par l'utilisateur dans l'activité principale */
    private String pseudo;
    /* La description du nouvel item à ajouter, saisi par l'utilisateur dans l'activité courante */
    private EditText ajouterItem;
    /* L'adapteur associé à la Recycler View de l'activité courante */
    private ItemAdapterItem itemAdapterItem;
    /* La Recycle View de l'activité courante */
    private RecyclerView recyclerView;
    /* La liste des items associé à la ToDoList courante */
    private ArrayList<ItemToDo> listeItem;
    /* La position (identifiant) de la ToDoList courante */
    private int idListe;
    /* Le hash d'identification auprès de l'API */
    private String hash;
    /* L'interface de connexion auprès de l'API */
    private TodoApiService todoApiService;

    /**
     * Fonction onCreate appelée lors de le création de l'activité
     *
     * @param savedInstanceState données à récupérer si l'activité est réinitialisée après avoir planté
     *         Lie l'activité à son layout et récupère les éléments avec lesquels on peut intéragir
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showliste);

        /* Récupération du pseudo depuis les préférences de l'application */
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        pseudo = preferences.getString("pseudo", "");
        hash = preferences.getString("hash","");
        Bundle b = this.getIntent().getExtras();
        idListe = b.getInt("idListe");

        /* Traitement de l'ajout d'un item à la ToDoList */
        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        ajouterItem = findViewById(R.id.ajouterItem);
    }


    /** Fonction onResume appelée après la création de l'activité et à chaque retour sur l'activité
     *          courante
     * Permet de générer la RecyclerView associée à la liste des items, en la récupérant depuis l'API
     */
    @Override
    protected void onResume() {
        super.onResume();
        recupItems();
    }

   
    /** Fonction par défaut de l'interface View.OnClickListener, appelée lors du clic sur la vue
     * @param v la vue cliquée
     * Ici, lors du clic sur le bouton OK, on ajoute l'item dans l'API et dans la RecyclerView
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOk:
                String label = ajouterItem.getText().toString();
                ajoutItem(label);
                Log.i("ChoixListe", "onClick: " + itemAdapterItem.getItemCount());
                break;
            default:
        }
    }

    /** Permet de changer la valeur du paramètre fait de l'item sélectionné
     * @param position l'indice où se trouve l'item dans la liste des items
     */
    @Override
    public void clickItem(int position) {
        listeItem.get(position).setFait(!listeItem.get(position).isFait());
        itemAdapterItem.notifyItemChanged(position);
        Log.i("TAG", "clickItem: " + listeItem.get(position).getFait());
        Log.i("TAG", "clickItem: "+ listeItem.get(position).getId());
        cocherItem(listeItem.get(position).getId(), listeItem.get(position).getFait());
    }

    /** Permet de récupérer la liste des items associée à la ToDoList d'identifiant idListe de
     *         l'utilisateur courant auprès de l'API
     * Met à jour la RecyclerView en cas de succès de la requête
     */
    private void recupItems() {
        todoApiService = TodoApiServiceFactory.createService(TodoApiService.class);
        Call<Items> call = todoApiService.recupereItems(hash, idListe);

        call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                if(response.isSuccessful()){
                    //stocke les listes
                    List<UnItem> liste = response.body().listeItems;
                    listeItem = new ArrayList<ItemToDo>();
                    for (UnItem x : liste) {
                        listeItem.add(new ItemToDo(x.label,x.checked == 1, x.id));
                        Log.i("TAG", "onResponse: " + x.id + " ");
                    }
                    recyclerView = findViewById(R.id.recyclerView);
                    itemAdapterItem = new ItemAdapterItem(listeItem, ShowListActivity.this);
                    recyclerView.setAdapter(itemAdapterItem);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ShowListActivity.this));

                } else {
                    Log.d("TAG", "onResponse: "+response.code());
                }
            }
            @Override public void onFailure(Call<Items> call, Throwable t) {
                Toast.makeText(ShowListActivity.this,"Error code : " ,Toast.LENGTH_LONG).show();
                Log.d("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

    }

    /** Permet de mettre à jour l'état d'un item auprès de l'API (et donc de conserver en mémoire
     *       les états des items)
     * @param idItem l'identifiant de l'item à mettre à jour
     * @param etat son nouvel état
     */
    private void cocherItem(int idItem, int etat) {
        todoApiService = TodoApiServiceFactory.createService(TodoApiService.class);
        Call<UnItem> call = todoApiService.cocherItem(hash, idListe, idItem, etat);
        call.enqueue(new Callback<UnItem>() {
            @Override
            public void onResponse(Call<UnItem> call, Response<UnItem> response) {
                if(response.isSuccessful()){
                    Log.i("TAG", "onResponse: nice");
                } else {
                    Log.d("TAG", "onResponse: "+response.code());
                }
            }
            @Override public void onFailure(Call<UnItem> call, Throwable t) {
                Toast.makeText(ShowListActivity.this,"Error code : " ,Toast.LENGTH_LONG).show();
                Log.d("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    /** Permet d'ajouter un item à la RecyclerView et dans l'API
     * @param label la description associée au nouvel item
     */
    private void ajoutItem(final String label) {
        todoApiService = TodoApiServiceFactory.createService(TodoApiService.class);
        Call<ItemResponse> call = todoApiService.ajoutItem(hash, idListe,label);
        call.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                if(response.isSuccessful()){
                    UnItem x = response.body().item;
                    ItemToDo item = new ItemToDo(x.label,false,x.id);
                    listeItem.add(item);
                    itemAdapterItem.notifyItemInserted(listeItem.size()-1);
                    Log.i("TAG", "onResponse: nice");
                } else {
                    Log.d("TAG", "onResponse: "+response.code());
                }
            }
            @Override public void onFailure(Call<ItemResponse> call, Throwable t) {
                Toast.makeText(ShowListActivity.this,"Error code : " ,Toast.LENGTH_LONG).show();
                Log.d("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });
    }

}

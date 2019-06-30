package com.example.todopmr.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todopmr.Modele.ItemToDo;
import com.example.todopmr.R;
import com.example.todopmr.RecyclerView.ItemAdapter;
import com.example.todopmr.ReponsesRetrofit.ReponseDeBase;
import com.example.todopmr.ReponsesRetrofit.ReponseItem;
import com.example.todopmr.ReponsesRetrofit.ReponseItems;
import com.example.todopmr.Room.AppDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
Cette activité gère l'affichage et la modification des items associés à la liste cliquée.
 */
public class ShowListActivity extends GenericActivity implements ItemAdapter.ActionListenerItem {

    //Views du layout
    private Button btn_ajout;
    private EditText edt_item;

    //Attributs de sauvegarde des informations
    private String pseudo;
    private String titre;
    private int idListe;

    //Objets en cours
    private ItemAdapter adapterEnCours;
    private ArrayList<ItemToDo> itemsListeCourante = new ArrayList<>();


    //Langue de l'application
    private String languageToLoad;

    private String hash;
    private String historique;
    private Boolean reseau;
    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRetrofit();

        //Mise à jour de la langue de l'application selon les préférences.
        SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(this);
        languageToLoad = settings.getString("langue", "");
        actualiserLangue(languageToLoad);
        database = AppDatabase.getDatabase(this);

        setContentView(R.layout.activity_show_list);

        btn_ajout = findViewById(R.id.btn_ajout);
        edt_item = findViewById(R.id.edt_item);
        TextView txt_liste = findViewById(R.id.txt_liste);

        //Récuperation des SharedPreferences
        hash = settings.getString("hash", "");
        historique = settings.getString("historique", "");
        pseudo = settings.getString("pseudo","");
        idListe = settings.getInt("idListe", -1);
        titre = settings.getString("titreListe", "");
        reseau = settings.getBoolean("reseau", true);

        txt_liste.setText(txt_liste.getText().toString() + " " + titre);

        if (reseau) {
            affichageItems(idListe, hash);

            // Lors du clic sur le bouton d'ajout
            btn_ajout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String description = edt_item.getText().toString();
                    ajouterItem(hash, description);
                }
            });
        }
        else {
            affichageItemsViaSQL();
            btn_ajout.setEnabled(false);
        }

    }

    public void affichageItemsViaSQL() {
        new Thread(){
            public void run() {
                List<ItemToDo> itemsLists = database.itemDao().findbyListId(idListe);
                for (ItemToDo item : itemsLists) {
                    itemsListeCourante.add(item);
                }
            }
        }.start();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowListActivity.this));
        adapterEnCours = new ItemAdapter(itemsListeCourante, ShowListActivity.this);
        recyclerView.setAdapter(adapterEnCours);
        recyclerView.addItemDecoration(new DividerItemDecoration(ShowListActivity.this, LinearLayout.VERTICAL));
    }

    /*
    Affichage des items de la liste dont l'id est sauvegardé, de l'utilisateur courant
     */
    public void affichageItems(final int idListe, String hash) {
        /*
        new Thread(){
            public void run() {
                database.itemDao().clean();
            }
        }.start();
        */
        toDoInterface.recupItems(idListe, hash).enqueue(new Callback<ReponseItems>() {
            @Override
            public void onResponse(Call<ReponseItems> call, Response<ReponseItems> response) {
                if (response.isSuccessful() && response.body().success) {
                    List<ItemToDo> items = response.body().items;
                    for (int i=0 ; i<items.size() ; i++) {
                        final ItemToDo item_i = items.get(i);
                        item_i.setListeId(idListe);
                        itemsListeCourante.add(item_i);
                        new Thread(){
                            public void run() {
                                database.itemDao().createItem(item_i);
                            }
                        }.start();
                    }
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ShowListActivity.this));
                    adapterEnCours = new ItemAdapter(itemsListeCourante, ShowListActivity.this);
                    recyclerView.setAdapter(adapterEnCours);
                    recyclerView.addItemDecoration(new DividerItemDecoration(ShowListActivity.this, LinearLayout.VERTICAL));

                }
            }
            @Override
            public void onFailure(Call<ReponseItems> call, Throwable t) {
                alerter("Il y a un problème : affichage Items");
            }
        });
    }

    /*
    Ajout d'un item à la liste dont l'id est sauvegardé, de l'utilisateur courant
     */
    public void ajouterItem(String hash, String description) {
        toDoInterface.addItem(idListe, hash, description).enqueue(new Callback<ReponseItem>() {
            @Override
            public void onResponse(Call<ReponseItem> call, Response<ReponseItem> response) {
                if (response.isSuccessful() && response.body().success) {
                    ItemToDo list = response.body().item;
                    itemsListeCourante.add(list);
                    adapterEnCours.actualiserAffichage();
                }
            }
            @Override
            public void onFailure(Call<ReponseItem> call, Throwable t) {
                alerter("Il y a un problème : : ajouter Items");
            }
        });
    }

    /*
    En cas de retour vers la page précédente, on envoie les informations actualisées.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //Mise à jour des SharedPreferences
            if (reseau) {
                afficherPseudos(hash);
            }
            SharedPreferences newSettings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = newSettings.edit();
            editor.putString("pseudo", pseudo);
            editor.putString("hash", hash);
            editor.putString("langue", languageToLoad);
            editor.commit();

            //Vers CheckListActivity
            Intent toCheckListActivity = new Intent(this, CheckListActivity.class);
            startActivity(toCheckListActivity);
            return true;
        }
        return false;
    }

    /*
    Affichage de la description de l'item.
     */
    @Override
    public void onItemClickedItem(ItemToDo itemClicked) {
        alerter("Item : " + itemClicked.getDescription());
    }

    /*
    En cas de clic sur l'icon de suppresion d'un item, on supprime et on met à jour les informations.
     */
    @Override
    public void onClickDeleteButtonItem(ItemToDo itemToDelete) {
        int idItem = itemToDelete.getIdItem();
        toDoInterface.supressItem(idListe, idItem, hash).enqueue(new Callback<ReponseDeBase>() {
            @Override
            public void onResponse(Call<ReponseDeBase> call, Response<ReponseDeBase> response) {
                if (response.isSuccessful() && response.body().success) {
                    alerter("Item supprimé");
                }
            }
            @Override
            public void onFailure(Call<ReponseDeBase> call, Throwable t) {
                alerter("Il y a un problème : Item supprimé");
            }
        });
    }

    /*
    En cas de clic sur la checkbox d'un item, on change le statut et on met à jour les informations.
     */
    @Override
    public void onClickCheckButtonItem(ItemToDo itemToCheck) {
        if (reseau) {
            int idItem = itemToCheck.getIdItem();
            int fait = itemToCheck.getFait();
            toDoInterface.checkItem(idListe, idItem, hash, fait).enqueue(new Callback<ReponseItem>() {
                @Override
                public void onResponse(Call<ReponseItem> call, Response<ReponseItem> response) {
                    if (response.isSuccessful() && response.body().success) {
                        alerter("Changement d'état");
                    }
                }
                @Override
                public void onFailure(Call<ReponseItem> call, Throwable t) {
                    alerter("Il y a un problème : Item checked");
                }
            });
        }
        else {
            Toast.makeText(ShowListActivity.this, "Impossible de supprimer une liste hors connexion", Toast.LENGTH_SHORT).show();
        }
    }
}
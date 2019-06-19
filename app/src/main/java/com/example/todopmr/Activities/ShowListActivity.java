package com.example.todopmr.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todopmr.Modele.ItemToDo;
import com.example.todopmr.R;
import com.example.todopmr.RecyclerView.ItemAdapter;
import com.example.todopmr.ReponsesRetrofit.ReponseDeBase;
import com.example.todopmr.ReponsesRetrofit.ReponseItem;
import com.example.todopmr.ReponsesRetrofit.ReponseItems;

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
    private ArrayList<ItemToDo> itemsListeCourante;


    //Langue de l'application
    private String languageToLoad;

    private String hash;
    private String historique;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRetrofit();

        //Mise à jour de la langue de l'application selon les préférences.
        SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(this);
        languageToLoad = settings.getString("langue", "");
        actualiserLangue(languageToLoad);

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

        txt_liste.setText(txt_liste.getText().toString() + " " + titre);

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

    /*
    Affichage des items de la liste dont l'id est sauvegardé, de l'utilisateur courant
     */
    public void affichageItems(int idListe, String hash) {
        toDoInterface.recupItems(idListe, hash).enqueue(new Callback<ReponseItems>() {
            @Override
            public void onResponse(Call<ReponseItems> call, Response<ReponseItems> response) {
                if (response.isSuccessful() && response.body().success) {
                    List<ItemToDo> lists = response.body().items;
                    itemsListeCourante = new ArrayList<>();
                    for (int i=0 ; i<lists.size() ; i++) {
                        itemsListeCourante.add(lists.get(i));
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
                alerter("Il y a un problème...");
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
                alerter("Il y a un problème...");
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
            afficherPseudos(hash);
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
                alerter("Il y a un problème...");
            }
        });
    }

    /*
    En cas de clic sur la checkbox d'un item, on change le statut et on met à jour les informations.
     */
    @Override
    public void onClickCheckButtonItem(ItemToDo itemToCheck) {
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
                alerter("Il y a un problème...");
            }
        });
    }
}
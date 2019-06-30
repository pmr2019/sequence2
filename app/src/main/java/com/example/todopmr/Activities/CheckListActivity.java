package com.example.todopmr.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
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
import androidx.room.Room;

import com.example.todopmr.Modele.ItemToDo;
import com.example.todopmr.Modele.ListeToDo;
import com.example.todopmr.R;
import com.example.todopmr.RecyclerView.ListAdapter;
import com.example.todopmr.ReponsesRetrofit.ReponseDeBase;
import com.example.todopmr.ReponsesRetrofit.ReponseItems;
import com.example.todopmr.ReponsesRetrofit.ReponseList;
import com.example.todopmr.ReponsesRetrofit.ReponseLists;
import com.example.todopmr.Room.AppDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
Cette activité gère l'affichage et la modification des listes associé au pseudo entré.
 */
public class CheckListActivity extends GenericActivity implements ListAdapter.ActionListenerListe {

    //Views du layout
    private Button btn_ajout;
    private EditText edt_liste;

    //Attributs de sauvegarde des informations
    private String pseudo;

    //Objets en cours
    private ListAdapter adapterEnCours;
    private ArrayList<ListeToDo> listesUserCourant = new ArrayList<>();

    //Langue de l'application
    private String languageToLoad;

    private String hash;
    private String historique;
    private AppDatabase database;
    private Boolean reseau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRetrofit();

        //Mise à jour de la langue de l'application selon les préférences.
        SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(this);
        languageToLoad = settings.getString("langue", "");
        actualiserLangue(languageToLoad);
        database = AppDatabase.getDatabase(this);

        setContentView(R.layout.activity_check_list);

        btn_ajout = findViewById(R.id.btn_ajout);
        edt_liste = findViewById(R.id.edt_liste);
        TextView txt_bienvenue = findViewById(R.id.txt_bienvenue);

        //Récupération des SharedPreferences
        hash = settings.getString("hash", "");
        historique = settings.getString("historique", "");
        pseudo = settings.getString("pseudo","");
        reseau = settings.getBoolean("reseau", true);

        txt_bienvenue.setText(txt_bienvenue.getText().toString() + " " + pseudo + " !");

        if (reseau) {
            affichageListes(hash);

            // Lors du clic sur le bouton d'ajout
            btn_ajout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String label = edt_liste.getText().toString();
                    ajouterListe(hash, label);
                }
            });
        }

        else {
            affichageListesViaSQL();
            btn_ajout.setEnabled(false);
        }
    }

    public void affichageListesViaSQL() {
        new Thread(){
            public void run() {
                List<ListeToDo> listeUser = database.listeDao().getAll();
                for (ListeToDo list : listeUser) {
                    listesUserCourant.add(list);
                }
            }
        }.start();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CheckListActivity.this));
        adapterEnCours = new ListAdapter(listesUserCourant, CheckListActivity.this);
        recyclerView.setAdapter(adapterEnCours);
        recyclerView.addItemDecoration(new DividerItemDecoration(CheckListActivity.this, LinearLayout.VERTICAL));
    }

    /*
    On affiche les listes de l'utilisateur courant.
     */
    public void affichageListes(String hash) {
        new Thread(){
            public void run() {
                database.listeDao().clean();
            }
        }.start();
        toDoInterface.recupLists(hash).enqueue(new Callback<ReponseLists>() {
            @Override
            public void onResponse(Call<ReponseLists> call, Response<ReponseLists> response) {
                if (response.isSuccessful() && response.body().success) {
                    List<ListeToDo> allLists = response.body().lists;
                    for (int i=0 ; i<allLists.size() ; i++) {
                        final ListeToDo list_i = allLists.get(i);
                        listesUserCourant.add(allLists.get(i));
                        new Thread(){
                            public void run() {
                                database.listeDao().createListe(list_i);
                            }
                        }.start();
                    }
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CheckListActivity.this));
                    adapterEnCours = new ListAdapter(listesUserCourant, CheckListActivity.this);
                    recyclerView.setAdapter(adapterEnCours);
                    recyclerView.addItemDecoration(new DividerItemDecoration(CheckListActivity.this, LinearLayout.VERTICAL));

                }
            }
            @Override
            public void onFailure(Call<ReponseLists> call, Throwable t) {
                alerter("Il y a un problème : affichage listes");
            }
        });
    }

    /*
    On ajoute une liste à la liste des listes de l'utilisateur courant.
     */
    public void ajouterListe(String hash, String label) {
        toDoInterface.addList(hash, label).enqueue(new Callback<ReponseList>() {
            @Override
            public void onResponse(Call<ReponseList> call, Response<ReponseList> response) {
                if (response.isSuccessful() && response.body().success) {
                    ListeToDo list = response.body().list;
                    listesUserCourant.add(list);
                    adapterEnCours.actualiserAffichage();
                }
            }
            @Override
            public void onFailure(Call<ReponseList> call, Throwable t) {
                alerter("Il y a un problème : ajout liste");
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
            editor.putBoolean("reseau", reseau);
            editor.putString("langue", languageToLoad);
            editor.commit();

            //Vers MainActivity
            Intent toMainActivity = new Intent(this, MainActivity.class);
            startActivity(toMainActivity);
            return true;
        }
        return false;
    }

    /*
    En cas de clic sur une liste, on affiche les items de cette liste.
     */
    @Override
    public void onItemClickedListe(ListeToDo listeClicked) {
        int idListe = listeClicked.getIdListe();
        String titreListe = listeClicked.getTitreListeToDo();

        //Mise à jour des SharedPreferences
        if (reseau) {
            afficherPseudos(hash);
        }
        SharedPreferences newSettings = PreferenceManager.getDefaultSharedPreferences(CheckListActivity.this);
        SharedPreferences.Editor editor = newSettings.edit();
        editor.putInt("idListe", idListe);
        editor.putString("titreListe", titreListe);
        editor.putString("pseudo", pseudo);
        editor.putString("hash", hash);
        editor.putBoolean("reseau", reseau);
        editor.putString("langue", languageToLoad);
        editor.commit();

        //Vers ShowListActivity
        Intent toShowListActivity = new Intent(CheckListActivity.this, ShowListActivity.class);
        startActivity(toShowListActivity);
    }

    /*
    En cas de clic sur l'icon de suppresion d'une liste, on supprime et on met à jour les informations.
     */
    @Override
    public void onClickDeleteButtonListe(ListeToDo listeToDelete) {
        if (reseau) {
            int idListe = listeToDelete.getIdListe();
            toDoInterface.supressList(idListe, hash).enqueue(new Callback<ReponseDeBase>() {
                @Override
                public void onResponse(Call<ReponseDeBase> call, Response<ReponseDeBase> response) {
                    if (response.isSuccessful() && response.body().success) {
                        alerter("Liste supprimée");
                    }
                }
                @Override
                public void onFailure(Call<ReponseDeBase> call, Throwable t) {
                    alerter("Il y a un problème : Liste supprimée");
                }
            });
        }
        else {
            Toast.makeText(CheckListActivity.this, "Impossible de supprimer une liste hors connexion", Toast.LENGTH_SHORT).show();
        }
    }

}

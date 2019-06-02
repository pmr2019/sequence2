package com.example.td_wang_yang_wei;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ChoixListeActivity extends AppCompatActivity {
    //pour recevoir le class ProfileListeToDo et enregistrer les nouveaux donnnes
    private ProfilListeToDo profile;

    //recevoir le EditText Button et RecyclerView
    private EditText edtListe;
    private Button btnListe;
    private RecyclerView recyclerView;

    //Transpoteur de liste de nom de liste
    private List<String> ListeData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_liste);

        edtListe = findViewById(R.id.edtliste);
        btnListe = findViewById(R.id.btnListe);

        //obtenir le Profile selon le nom qui est transmet de MainActicity
        profile=readProfilData(getIntent().getStringExtra("profile"));

        //obtenir la liste de noms des listes dans le profile
        ListeData=getListedeNom(profile);

        //afficher la liste de noms dans le RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListeAdapter(ListeData));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

    }

    //alerter pour savoir le processus de la programme et alerter les utilisateurs
    public void alerter(String s) {
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }

    //la function pour creer une nouvelle liste quand on cliquer le button "creer votre liste"
    public void addnewlist(View v) {
            String liste = edtListe.getText().toString();
            if (liste.equals("")) {
                alerter("tapez la nouvelle liste");
            } else {

                    ListeToDo nouveauList = new ListeToDo(liste);
                    profile.ajouteListe(nouveauList);
                    saveProfileData(profile, profile.getLogin());
                    ListeData = getListedeNom(profile);
                    recyclerView.setAdapter(new ListeAdapter(ListeData));
                    edtListe.setText("");

            }
            }


    //obtenir la liste de noms
    public List<String> getListedeNom(ProfilListeToDo profile){

        List<String> data = new ArrayList<>();
        ListeToDo tmp;
        for (ListeToDo list : profile.getMesListeToDo()){
            tmp = list;
            data.add(tmp.getTitreListeToDo());
        }
        return data;

    }
    //enregistrer le changement de liste
    public void saveProfileData(ProfilListeToDo profile, String pseudo) {
        Gson gson=new Gson();
        String fileContents = gson.toJson(profile);
        SharedPreferences preferences = getSharedPreferences(pseudo, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("content",fileContents);
        editor.commit();
    }

    //obtenir le profile selon le nom de profile
    public ProfilListeToDo readProfilData(String pseudo) {

        ProfilListeToDo profile;
         GsonBuilder builder = new GsonBuilder();
         Gson gson = builder.create();
        //Gson gson=new Gson();
        SharedPreferences profileData = getSharedPreferences(pseudo, MODE_PRIVATE);
        String content=profileData.getString("content","");

        profile = gson.fromJson(content, ProfilListeToDo.class); // cast Profile

        return profile;
    }

    //construire le Adapter de RecyclerView
    class ListeAdapter extends RecyclerView.Adapter<ListeAdapter.MyViewHolder>{

        private final List<String> lists;

        ListeAdapter(List<String> lists) {
            this.lists = lists;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.liste,parent,false);
            return new ListeAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String data = lists.get(position);

            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            if(lists==null)
                return 0;
            else return lists.size();
        }
        //Construire le ViewHolder dans le Adapter
        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private final TextView textView;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.nom_Liste);
                itemView.setOnClickListener(this);
            }

            void bind(String data) {
                textView.setText(data);
            }


            //sauter au ItemActivity quand on clique la liste.
            @Override
            public void onClick(View v) {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Intent i = new Intent(ChoixListeActivity.this, ShowListeActivity.class);
                    i.putExtra("profile", profile.getLogin());
                    i.putExtra("list", lists.get(getAdapterPosition()));
                    startActivity(i);
                }
            }
        }
    }
}

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

    private ProfilListeToDo profile;

    private EditText edtListe;
    private Button btnListe;

    private RecyclerView recyclerView;
    private List<String> ListeData;
    private ListeAdapter listeAdapter;

    private String Cat="ChoixListe";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_liste);


        edtListe = findViewById(R.id.edtliste);
        btnListe = findViewById(R.id.btnListe);




        profile=readProfilData(getIntent().getStringExtra("profile"));
        ListeData=getListedeNom(profile);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListeAdapter(ListeData));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

    }

    public void alerter(String s) {
        Log.i(Cat,s);
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }

    public void addnewlist(View v) {
        String liste = edtListe.getText().toString();
        if (liste.equals("")) {
            alerter("tapez la nouvelle liste");
        } else {
            if(eviterMemeNom(liste)){
                alerter("Déjà existe");
            }else {
            ListeToDo nouveauList = new ListeToDo(liste);
            profile.ajouteListe(nouveauList);
            saveProfileData(profile, profile.getLogin());
            ListeData=getListedeNom(profile);
            recyclerView.setAdapter(new ListeAdapter(ListeData));
            edtListe.setText("");
        }}

    }

    public boolean eviterMemeNom(String nomliste){

        for(String i:ListeData){
            if(i.equals(nomliste))
                return true;
        }
        return false;
    }

    public List<String> getListedeNom(ProfilListeToDo profile){

        List<String> data = new ArrayList<>();
        ListeToDo tmp;
        for (ListeToDo list : profile.getMesListeToDo()){
            tmp = list;
            data.add(tmp.getTitreListeToDo());
        }
        return data;
    }

    public void saveProfileData(ProfilListeToDo profile, String pseudo) {
        Gson gson=new Gson();
        String fileContents = gson.toJson(profile);
        SharedPreferences preferences = getSharedPreferences(pseudo, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("content",fileContents);
        editor.commit();
    }

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

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Intent i = new Intent(ChoixListeActivity.this, ShowListeActivity.class);
                    i.putExtra("profile", profile.getLogin());
                    i.putExtra("liste", lists.get(getAdapterPosition()));
                    startActivity(i);
                }
            }
        }
    }
}

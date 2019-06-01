package com.example.td_wang_yang_wei;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

        recyclerView = findViewById(R.id.recyclerView);


        profile=readProfilData(getIntent().getStringExtra("profile"));
        ListeData=getListedeNom(profile);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        listeAdapter=new ListeAdapter(ListeData);
        recyclerView.setAdapter(listeAdapter);


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
                ListeToDo nouveauList = new ListeToDo(liste);
                profile.ajouteListe(nouveauList);
                saveProfileData(profile, profile.getLogin());
                listeAdapter.AjouterItem(liste);
                edtListe.setText("");
//                    if(eviterMemeNom(liste)){
//                        alerter("Le nom est déjà exist!");
//                    }else{
//                        ListeToDo nouveauList=new ListeToDo(liste);
//                        profil.ajouteListe(nouveauList);
//                        saveProfileData(profil,nomProfil);
//                }

            }
        }
   // }
    public boolean eviterMemeNom(String nomliste){
        List<ListeToDo> listExist=profile.getMesListeToDo();
        for(ListeToDo i:listExist){
            if(i.getTitreListeToDo()==nomliste)
                return true;
        }
        return false;
    }
    public List<String> getListedeNom(ProfilListeToDo profile){
//        List<String> nom = new ArrayList<>();
//        ListeToDo tmp;
//        if(profile.getMesListeToDo()==null)
//        return nom;
//        else {
//        for (ListeToDo list : profile.getMesListeToDo()) {
//            tmp = list;
//            nom.add(tmp.getTitreListeToDo());
//        }
//        return nom;
//        }
        List<String> data = new ArrayList<>();
        ListeToDo tmp;
        for (ListeToDo list : profile.getMesListeToDo()){
            tmp = list;
            data.add(tmp.getTitreListeToDo());
        }
        return data;

    }


//    public void saveProfileData(ProfilListeToDo p, String pseudo){
//        final GsonBuilder builder = new GsonBuilder();
//        final Gson gson = builder.create();
//        JsonObject json = new JsonObject(p);
//        String fileContents = gson.toJson(json);
//        SharedPreferences preferences = getSharedPreferences(pseudo, MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("content",fileContents);
//        editor.commit();
//    }

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

//    public ProfilListeToDo readProfilData(String filename) {
//        StringBuilder jsonRead = new StringBuilder();
//        ProfilListeToDo profile;
//        final GsonBuilder builder = new GsonBuilder();
//        final Gson gson = builder.create();
//        try {
//            FileInputStream inputStream;
//            inputStream = openFileInput(filename);
//            int content;
//            while ((content = inputStream.read()) != -1) {
//                jsonRead.append((char) content);
//            }
//            inputStream.close();
//
//            profile = gson.fromJson(jsonRead.toString(), ProfilListeToDo.class); // cast Profile
//
//            return profile;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new ProfilListeToDo();
//    }

    public class ListeAdapter extends RecyclerView.Adapter<ListeAdapter.MyViewHolder>{

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

        private void AjouterItem(String listname){
            lists.add(0,listname);
            notifyItemInserted(0);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder viewholder, int position) {
            MyViewHolder holder=viewholder;
            String donne=lists.get(position);
            holder.nomDeListe.setText(donne);
        }

        @Override
        public int getItemCount() {
            if(lists==null)
            return 0;
            else return lists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final TextView nomDeListe;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                nomDeListe=findViewById(R.id.nomDeListe);
                itemView.setOnClickListener(this);
            }

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

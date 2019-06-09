package com.example.td_wang_yang_wei;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private SettingAdapter settingAdapter;
    private ListeDeUtilisateur listeDeUtilisateur;
    //recevoir le recyclerView
    private RecyclerView recyclerView;
    private EditText edtUrl;
    private List<String> listeDeNom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listeDeUtilisateur=getProfiles();
        listeDeNom=ListeDeNom(listeDeUtilisateur.getUtilisateurs());
        settingAdapter=new SettingAdapter(listeDeNom);
        //afficher la liste de noms dans le RecyclerView
        recyclerView = findViewById(R.id.setting_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(settingAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        edtUrl=findViewById(R.id.edtUrl);
        edtUrl.setText(listeDeUtilisateur.getUrl());
    }

    public List<String> ListeDeNom(List<Utilisateur> u){
        List<String>data=new ArrayList<>();
        if(!u.isEmpty()){
            for(int i=0;i<u.size();i++){
                data.add(u.get(i).getPseudo());
            }return data;
        }return null;
    }

    public ListeDeUtilisateur getProfiles() {
        SharedPreferences preferences = getSharedPreferences("utilisateurs", MODE_PRIVATE);
        GsonBuilder builder=new GsonBuilder();
        Gson gson = builder.create();
        String stringListUtilisateur = preferences.getString("utilisateurs", gson.toJson(new ListeDeUtilisateur()));
        ListeDeUtilisateur LU=gson.fromJson(stringListUtilisateur,ListeDeUtilisateur.class);
        return LU;
    }

    //construire le Adapter de RecyclerView
    class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {
        private final List<String> profiles;
        SettingAdapter(List<String> profiles) { this.profiles = profiles; }

        @NonNull
        @Override
        public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.setting,parent,false);
            return new SettingAdapter.SettingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
            String data = profiles.get(position);

            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            if(profiles==null)
                return 0;
            else return profiles.size();
        }

        //Construire le ViewHolder dans le Adapter
        class SettingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final TextView textView;

            SettingViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.setting_name);

                itemView.setOnClickListener(this);
            }

            void bind(String data) {
                textView.setText(data);
            }

            //sauter au MainActivity corresponde quand on clique la liste.
            @Override
            public void onClick(View v) {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                    i.putExtra("pseudo", profiles.get(getAdapterPosition()));
                    startActivity(i);
                }
            }
        }
    }


}

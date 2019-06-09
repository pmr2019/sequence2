package com.example.td_wang_yang_wei;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoixListeActivity extends AppCompatActivity {

    //recevoir le EditText Button et RecyclerView
    private EditText edtListe;
    private Button btnListe;

    private RecyclerView recyclerView;
   // private ListeAdapter listAdapter;
    private ListeAdapter listeAdapter;


    //Transpoteur de liste de nom de liste


    private String hash;
    private String url;
    private String pseudo;
    private String userId;
    private requestService requestService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_liste);

        edtListe = findViewById(R.id.edtliste);
        btnListe = findViewById(R.id.btnListe);

        hash = getIntent().getStringExtra("hash");
        url = getIntent().getStringExtra("url");
        pseudo = getIntent().getStringExtra("pseudo");



        requestService = requestServiceFactory.createService(url, requestService.class);

        listeAdapter=new ListeAdapter((new ArrayList<String>()));
        recyclerView = findViewById(R.id.recyclerView);
        //afficher la liste de noms dans le RecyclerView
        recyclerView.setAdapter(listeAdapter);


        //obtenir la liste de noms des listes dans le profile
        getListedeLabel(hash);
        getUserIdconneted(hash,pseudo);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    //alerter pour savoir le processus de la programme et alerter les utilisateurs
    public void alerter(String s) {
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }

    //la function pour creer une nouvelle liste quand on cliquer le button "creer votre liste"
    public void addnewlist(View v) {
        final String liste = edtListe.getText().toString();
        if (liste.equals("")) {
            alerter("tapez la nouvelle liste");
        } else {
            if(listeAdapter.verfierNom(liste)){
                alerter("Déjà existe");
            }else {

                Call<NouveauListe> call = requestService.addList(hash, userId, liste);

                call.enqueue(new Callback<NouveauListe>() {
                    @Override
                    public void onResponse(Call<NouveauListe> call, Response<NouveauListe> response) {
                        if (response.isSuccessful()) {
                            listeAdapter.add(liste);
                            edtListe.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
        }}

    }

    public void getListedeLabel(String hash){

        Call<Lists> call = requestService.getLists(hash);
        call.enqueue(new Callback<Lists>() {
            @Override
            public void onResponse(Call<Lists> call, Response<Lists> response) {
                if (response.isSuccessful()) {
                    if(!response.body().getLists().isEmpty()){
                        for (int i=0;i<response.body().getLists().size();i++) {
                            listeAdapter.add(response.body().getLists().get(i).getLabel());
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                alerter("pas de connexion");
            }
        });

    }
    private void getUserIdconneted(String hash, final String pseudo) {
        Call<Users> call = requestService.getUsers(hash);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    userId = response.body().getUserId(pseudo);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                alerter("pas de connexion");
            }
        });
    }


    //construire le Adapter de RecyclerView
    class ListeAdapter extends RecyclerView.Adapter<ListeAdapter.MyViewHolder>{

        private final List<String> lists;

        ListeAdapter(List<String> lists) {
            this.lists = lists;
        }

        private void add(String list){
            lists.add(list);
            notifyDataSetChanged();
        }
        private Boolean verfierNom(String s){
            if(this.lists.contains(s))
                return true;
            return false;
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
                    Call<Lists> call = requestService.getLists(hash);
                    final String listeCliquee = lists.get(getAdapterPosition());
                    call.enqueue(new Callback<Lists>() {
                        @Override
                        public void onResponse(Call<Lists> call, Response<Lists> response) {
                            if (response.isSuccessful()) {
                                if(!response.body().getLists().isEmpty())
                                for (int i=0;i<response.body().getLists().size();i++) {
                                    if (response.body().getLists().get(i).getLabel().equals(listeCliquee)) {
                                        convertToItems(hash,url,response.body().getLists().get(i).getId());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            alerter("pas de connexion");
                        }
                    });
                }
            }
            public void convertToItems(String hash,String url,String id){
                Intent i = new Intent(ChoixListeActivity.this, ShowListeActivity.class);
                i.putExtra("hash", hash);
                i.putExtra("url", url);
                i.putExtra("listId", id);
                startActivity(i);
            }
        }
    }
}

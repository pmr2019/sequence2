package com.example.td_wang_yang_wei.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.td_wang_yang_wei.DataProvider;
import com.example.td_wang_yang_wei.Database.ToDoListdb;
import com.example.td_wang_yang_wei.Database.model.Listdb;
import com.example.td_wang_yang_wei.R;
import com.example.td_wang_yang_wei.api.Lists;
import com.example.td_wang_yang_wei.api.NouveauListe;
import com.example.td_wang_yang_wei.api.Users;
import com.example.td_wang_yang_wei.api.requestService;
import com.example.td_wang_yang_wei.api.requestServiceFactory;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoixListeActivity extends AppCompatActivity {

    //recevoir le EditText Button et RecyclerView
    private EditText edtListe;
    private Button btnListe;//pas grave car on ajoute une fonction de addnewlist sur ce bouton

    private RecyclerView recyclerView;
   // private ListeAdapter listAdapter;
    private ListeAdapter listeAdapter;

    private String hash;
    private String url;
    private String pseudo;
    private String userId;
    private com.example.td_wang_yang_wei.api.requestService requestService;
    private DataProvider dataProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_liste);
        dataProvider = new DataProvider(this);

        edtListe = findViewById(R.id.edtliste);
        btnListe = findViewById(R.id.btnListe);

        //obtenir les donnees de MainActivity
        hash = getIntent().getStringExtra("hash");
        url = getIntent().getStringExtra("url");
        pseudo = getIntent().getStringExtra("pseudo");

        //créer adapter
        listeAdapter=new ListeAdapter((new ArrayList<String>()));

        //creer un instance de requestService
        requestService = requestServiceFactory.createService(url, requestService.class);

        //obtenir la liste de nom de liste dans cet utilisateur
        sync();
//        getListedeLabel(hash);
        getUserIdconneted(hash,pseudo);

        //afficher la liste de noms dans le RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(listeAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    //alerter pour savoir le processus de la programme et alerter les utilisateurs
    public void alerter(String s) {
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }


//    TODO:完善异步
    private void sync() {
        findViewById(R.id.progess).setVisibility(View.VISIBLE);

        dataProvider.syncLists(hash,new DataProvider.ListsListener() {
            @Override public void onSuccess(List<String> labelslist) {
                for (int i = 0; i< labelslist.size(); i++) {
                    listeAdapter.add(labelslist.get(i));
                }
                Log.d("test",labelslist+"");
                findViewById(R.id.progess).setVisibility(View.GONE);
            }

            @Override public void onError(List<String> labelslistLocal) {
                for (int i = 0; i< labelslistLocal.size(); i++) {
                    listeAdapter.add(labelslistLocal.get(i));
                }
                Log.d("test",labelslistLocal+"");
                findViewById(R.id.progess).setVisibility(View.GONE);
            }
        });
    }


    //la fonction pour creer une nouvelle liste quand on cliquer le button "creer votre liste"
    public void addnewlist(View v) {
        final String liste = edtListe.getText().toString();
        //éviter le cas d'entrée vide
        if (liste.equals("")) {
            alerter("tapez la nouvelle liste");
        } else {
            //éviter le cas de répétition
            if(listeAdapter.verfierNom(liste)){
                alerter("Déjà existe");
            }else {

                //Encapsuler la requête d'après les règles de Interface requestService
                Call<NouveauListe> call = requestService.addList(hash, userId, liste);

                //Envoyer la requête et collecter les résultats
                //si succès ajouter une nouvelle liste
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

    /**
     * obtenir la liste de liste d'utilisateur courant
     * @param hash
     */
    public void getListedeLabel(String hash){

        //Encapsuler la requête d'après les règles de Interface requestService
        Call<Lists> call = requestService.getLists(hash);

        //Envoyer la requête et collecter les résultats
        //si succès récupérer des listes de l'utilisateur courant; le hash peut être fourni en chaîne de requête
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

        //Encapsuler la requête d'après les règles de Interface requestService
        Call<Users> call = requestService.getUsers(hash);

        //Envoyer la requête et collecter les résultats
        //si succès récupérer le ID d'utilisateur courant
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
            return this.lists.contains(s);
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
            private final ImageButton imageButton;
            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.nom_Liste);
                imageButton=itemView.findViewById(R.id.listSupp);
                imageButton.setOnClickListener(this);
                textView.setOnClickListener(this);
            }

            void bind(String data) {
                textView.setText(data);
            }


            //sauter au ItemActivity quand on clique la liste.
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.nom_Liste:
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            //Encapsuler la requête d'après les règles de Interface requestService
                            Call<Lists> call = requestService.getLists(hash);
                            final String listeCliquee = lists.get(getAdapterPosition());

                            //Envoyer la requête et collecter les résultats
                            //si succès entrer dans la liste des Items de Liste courante
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

                        }break;
                    case R.id.listSupp:
                        //Encapsuler la requête d'après les règles de Interface requestService
                        Call<Lists> callTotal = requestService.getLists(hash);
                        final String listCliquee = lists.get(getAdapterPosition());

                        //Envoyer la requête et collecter les résultats
                        //si succès obtenir les listes de cet utilisateur courant
                        callTotal.enqueue(new Callback<Lists>() {
                            @Override
                            public void onResponse(Call<Lists> call, Response<Lists> response) {
                                if (response.isSuccessful()) {
                                    for (Lists.ListsBean l : response.body().getLists()) {
                                        if (l.getLabel().equals(listCliquee)) {
                                            //Encapsuler la requête d'après les règles de Interface requestService
                                            Call<ResponseBody> callSupp = requestService.deleteList(hash, userId, l.getId());

                                            //Envoyer la requête et collecter les résultats
                                            callSupp.enqueue(new Callback() {
                                                @Override
                                                public void onResponse(Call call, Response response) { }

                                                @Override
                                                public void onFailure(Call call, Throwable t) { }
                                            });
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });

                        lists.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        break;
                }

                }
            }

        /**
         * Entrez dans la liste d'Items
         * @param hash
         * @param url
         * @param id
         */
        void convertToItems(String hash, String url, String id){
                Intent i = new Intent(ChoixListeActivity.this, ShowListeActivity.class);
                i.putExtra("hash", hash);
                i.putExtra("url", url);
                i.putExtra("listId", id);
                startActivity(i);
            }
        }
    }


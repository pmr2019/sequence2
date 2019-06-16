package com.example.td_wang_yang_wei;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowListeActivity extends AppCompatActivity {


    //recevoir le EditText Button et RecyclerView
    private RecyclerView recyclerView;

    private EditText edtItem;
    private String Cat="ShowListe";
    private ItemAdapter itemAdapter;
    private String hash;
    private String url;
    private String listId;
    private requestService requestService;


    //alerter pour savoir le processus de la programme et alerter les utilisateurs
    public void alerter(String s) {
        Log.i(Cat,s);
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_liste);

        edtItem = findViewById(R.id.edit_item);

        //obtenir les donnees de ChoixListeActivity
        hash = getIntent().getStringExtra("hash");
        url = getIntent().getStringExtra("url");
        listId = getIntent().getStringExtra("listId");
        alerter(listId);

        //creer Adapter
        itemAdapter = new ItemAdapter(new ArrayList<Item>());

        //creer un instance de requestService
        requestService = requestServiceFactory.createService(url, requestService.class);

        //obtenir les items de ce Liste
        getListedeItem(hash,listId);
        //afficher la liste de noms dans le RecyclerView
        recyclerView = findViewById(R.id.list_show);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    //creer le nouveau class pour afficher
    class Item {
        private String description;
        private Boolean fait;

        Item(String d, String f) {
            this.description = d;
            this.fait = f.equals("1");
        }

        String getDescription() {
            return description;
        }

        Boolean getFait() {
            return fait;
        }
    }

    //ajouter nouveau item
    public void addnewitem(View v) {
        final String item = edtItem.getText().toString();
        //éviter le cas d'entrée vide
        if (item.equals("")) {
            alerter("tapez la nouvelle item");
        } else {
            //éviter le cas de répétition
            if (itemAdapter.verifierNom(item)) {
                alerter("déjà existe");
            } else {
                //Encapsuler la demande d'envoyer d'après les règles de Interface requestService
                Call<NouveauItem> call = requestService.addItem(hash, listId, item);
                //Envoyer la demande d'envoyer et collecter les résultats
                //si succès ajouter un nouveau Item
                call.enqueue(new Callback<NouveauItem>() {
                    @Override
                    public void onResponse(Call<NouveauItem> call, Response<NouveauItem> response) {
                        if (response.isSuccessful()) {
                            itemAdapter.add(item, "0");
                            edtItem.setText("");
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        }
    }

    /**
     * obtenir la liste de Item de Liste courant
     * @param hash
     * @param id
     */
    public void getListedeItem(String hash,String id){

        Call<Items> call = requestService.getItems(hash,id);
        call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                if (response.isSuccessful()) {
                    if(!response.body().getItems().isEmpty()){
                        for (int i=0;i<response.body().getItems().size();i++) {
                            itemAdapter.add(response.body().getItems().get(i).getLabel(),
                                    response.body().getItems().get(i).getChecked());
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


    //construire ItemAdapter
    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>{
        private final List<Item> ItemsData;
        ItemAdapter(List<Item> ItemsData) {
            this.ItemsData = ItemsData;
        }

        private void add(String label,String f){
            ItemsData.add(new Item(label,f));
            notifyDataSetChanged();
        }
        private Boolean verifierNom(String s){
            for(Item i :ItemsData){
                if(i.getDescription().equals(s))
                return true;
            }return false;
        }
        @NonNull
        @Override
        public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.show,parent,false);
            return new ItemAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
            Item data = ItemsData.get(position);

            holder.bind(data);

        }

        @Override
        public int getItemCount() {
            if(ItemsData == null)
                return 0;
            else return ItemsData.size();
        }

        //creer MyviewHolder dans le class Adapter
        class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

            private final CheckBox checkBox;
            private final ImageButton imageButton;

            MyViewHolder(@NonNull View itemView){
                super(itemView);
                checkBox = itemView.findViewById(R.id.CBItem);
                imageButton=itemView.findViewById(R.id.itemSupp);
                imageButton.setOnClickListener(this);
                checkBox.setOnClickListener(this);
            }
            void bind(Item data){
                checkBox.setText(data.getDescription());
                checkBox.setChecked(data.getFait());
            }


            @Override
            public void onClick(View v) {

                switch(v.getId()){
                    case R.id.CBItem:
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            //Encapsuler la demande d'envoyer d'après les règles de Interface requestService
                            Call<Items> callChange = requestService.getItems(hash, listId);
                            final String itemCliquee = ItemsData.get(getAdapterPosition()).getDescription();
                            //Envoyer la demande d'envoyer et collecter les résultats
                            //si succès
                            callChange.enqueue(new Callback<Items>() {
                                @Override
                                public void onResponse(Call<Items> call, Response<Items> response) {
                                    if (response.isSuccessful()) {
                                        if(!response.body().getItems().isEmpty())
                                            for (int i=0;i<response.body().getItems().size();i++) {
                                                if (response.body().getItems().get(i).getLabel().equals(itemCliquee)) {
                                                    Call callSave;
                                                    if (response.body().getItems().get(i).getChecked().equals("0")) {
                                                        callSave = requestService.cliqueItem(hash, listId, response.body().getItems().get(i).getId(), "1");
                                                    } else {
                                                        callSave = requestService.cliqueItem(hash, listId, response.body().getItems().get(i).getId(), "0");
                                                    }
                                                    callSave.enqueue(new Callback() {
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
                                    alerter("pas de connexion");
                                }
                        });break;}

                    case R.id.itemSupp:
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        Call<Items> callTotal = requestService.getItems(hash, listId);
                        final String item_selected = ItemsData.get(getAdapterPosition()).getDescription();

                        callTotal.enqueue(new Callback<Items>() {
                            @Override
                            public void onResponse(Call<Items> call, Response<Items> response) {
                                if (response.isSuccessful()) {
                                    for (Items.ItemsBean i : response.body().getItems()) {
                                        if (i.getLabel().equals(item_selected)) {
                                            Call<ResponseBody> callSupp = requestService.deleteItem(hash, listId, i.getId());

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
                                alerter("pas de connexion");
                            }
                        });
                        ItemsData.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        break;}
                }




            }
        }
    }

}

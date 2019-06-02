package com.example.td_wang_yang_wei;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.TwoStatePreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class ShowListeActivity extends AppCompatActivity {

    //pour recevoir le class ProfileListeToDo et enregistrer les nouveaux donnnes
    private ProfilListeToDo profile;
    private String liste;

    //recevoir le EditText Button et RecyclerView
    private RecyclerView recyclerView;

    private EditText edtItem;
    private String Cat="ShowListe";
    ItemAdapter itemAdapter;

    //Transpoteur de liste de item
    List<Item> ItemsData;

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

        //obtenir le Profile selon le nom qui est transmet de MainActicity
        profile = readProfilData(getIntent().getStringExtra("profile"));

        //obtenir la liste de noms des listes dans le profile
        liste = getIntent().getStringExtra("liste");

        //obtenir list de item
        ItemsData = ItemsData(profile,liste);

        //creer Adapter
        itemAdapter = new ItemAdapter(ItemsData);

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

        Item(String d, Boolean f) {
            this.description = d;
            this.fait = f;
        }

        String getDescription() {
            return description;
        }

        Boolean getFait() {
            return fait;
        }
    }

    //obtenir la lists de Item
    public List<Item> ItemsData(ProfilListeToDo profile, String liste){

        List<Item> data = new ArrayList<>();
        for (ItemToDo tmp : profile.rechercherListe(liste).getLesItems()){
            data.add(new Item(tmp.getDescription(), tmp.getFait()));
        }

        return data;
    }

    //ajouter nouveau item
    public void addnewitem(View v) {
        String item = edtItem.getText().toString();
        if (item.equals("")) {
            alerter("tapez la nouvelle item");
        } else {
            if (EviterMemeNom(item)) {
                alerter("déjà existe");
            } else {
                profile.addItem(liste, new ItemToDo(item));
                saveProfileData(profile, profile.getLogin());
                //ItemsData=ItemsData(profile,liste);
                ItemsData.add(new Item(item, false));
                Log.d("add", "" + ItemsData);
                itemAdapter.notifyItemInserted(ItemsData.size());
                //recyclerView.setAdapter(new ItemAdapter(ItemsData));
                edtItem.setText("");
            }
        }
    }

    //éviter ajouter le item qui a le meme nom avec les autres
    public boolean EviterMemeNom(String nom){
        for(Item i:ItemsData){
            if(nom.equals(i.getDescription()))
                return true;
        }
        return false;
    }

    //sauvegarder le profile
    public void saveProfileData(ProfilListeToDo profile, String pseudo) {
        Gson gson=new Gson();
        String fileContents = gson.toJson(profile);
        SharedPreferences preferences = getSharedPreferences(pseudo, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("content",fileContents);
        editor.commit();
    }

    //obtenir le proflie selon le nom
    public ProfilListeToDo readProfilData(String pseudo) {

        ProfilListeToDo profile;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        SharedPreferences profileData = getSharedPreferences(pseudo, MODE_PRIVATE);
        String content=profileData.getString("content","");

        profile = gson.fromJson(content, ProfilListeToDo.class); // cast Profile

        return profile;
    }
    //construire ItemAdapter
    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>{
        private final List<Item> ItemsData;
        ItemAdapter(List<Item> ItemsData) {
            this.ItemsData = ItemsData;
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
        class MyViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

            private final CheckBox checkBox;

            MyViewHolder(@NonNull View itemView){
                super(itemView);
                checkBox = itemView.findViewById(R.id.CBItem);
                checkBox.setOnCheckedChangeListener(this);
            }
            void bind(Item data){
                checkBox.setText(data.getDescription());
                checkBox.setChecked(data.getFait());
            }

            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                profile.rechercherListe(liste).validerItem(ItemsData.get(getAdapterPosition()).getDescription(), isChecked);
                saveProfileData(profile, profile.getLogin());
            }


            }
        }

}

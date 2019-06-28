package com.ecl.maxime.application_todoliste.activites;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecl.maxime.application_todoliste.adapter.ItemToDoAdapter;
import com.ecl.maxime.application_todoliste.R;
import com.ecl.maxime.application_todoliste.api_request.Item;
import com.ecl.maxime.application_todoliste.api_request.ListeItems;
import com.ecl.maxime.application_todoliste.api_request.ServiceFactory;
import com.ecl.maxime.application_todoliste.api_request.Services;
import com.ecl.maxime.application_todoliste.repos.ItemsDataRepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ItemToDoAdapter mAdapter;
    //private Call<ListeItems> call_items;
    private Call<Void> call_ajout, call_modif;
    private ArrayList<Item> items;
    private ItemsDataRepo itemsDataRepo;
    private String hash;
    private String id;
    private EditText edt_ajout;
    private Button btn_ajout;
    private String titreListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_ajout = findViewById(R.id.ed_nouvel_item);
        btn_ajout = findViewById(R.id.btn_item);
        mRecyclerView = findViewById(R.id.list_itemtodo);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent i = getIntent();
        hash = i.getStringExtra(MainActivity.HASH);
        id = i.getStringExtra(ChoixListActivity.LISTE_ID);
        titreListe = i.getStringExtra(ChoixListActivity.TITRE_LISTE);

        mAdapter = new ItemToDoAdapter(new ArrayList<Item>(0), new ItemToDoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item itemToDo) {
                boolean checked = itemToDo.isChecked();
                itemToDo.setChecked(!checked);
                if (checked)
                    modifItem(itemToDo.getId(), "0");
                else
                    modifItem(itemToDo.getId(), "1");
                itemsDataRepo.updateItem(itemToDo);
            }
        });

        sync();

        mRecyclerView.setAdapter(mAdapter);
        btn_ajout.setEnabled(false);
        if(verifReseau())btn_ajout.setEnabled(true);


        btn_ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = edt_ajout.getText().toString();
                Item new_item = new Item();
                new_item.setLabel(label);
                itemsDataRepo.createItem(new_item);
                ArrayList<Item> items = mAdapter.getLesItems();
                items.add(new_item);
                mAdapter.setLesItems(items);
                ajouterItem(label);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(ShowListActivity.this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void sync(){
        //Services service = ServiceFactory.createService(Services.class);
        //call_items = service.getListeItems(hash, id);
        //call_items.enqueue(new Callback<ListeItems>() {
        //    @Override
        //    public void onResponse(Call<ListeItems> call, Response<ListeItems> response) {
        //        mAdapter.setLesItems(response.body().items);
        //    }

        //    @Override
        //    public void onFailure(Call<ListeItems> call, Throwable t) {
        //        Toast.makeText(ShowListActivity.this,"Erreur",Toast.LENGTH_LONG).show();
        //    }
        //});

        items = itemsDataRepo.getItems(titreListe);
        mAdapter.setLesItems(items);
    }

    private void ajouterItem(final String label){
        Services services = ServiceFactory.createService(Services.class);
        call_ajout = services.addItem(hash, id, label);
        call_ajout.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                edt_ajout.setText("");
                Toast.makeText(ShowListActivity.this,"L'ajout de l'item "+label+" a été effectué !",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ShowListActivity.this,"L'ajout a échoué",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void modifItem(String id_item, String check){
        Services service = ServiceFactory.createService(Services.class);
        call_modif = service.modifyItem(hash, id, id_item, check);
        call_modif.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ShowListActivity.this,"Item modifié",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ShowListActivity.this,"Erreur",Toast.LENGTH_LONG).show();
            }
        });
    }
    public boolean verifReseau()
    {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

        String sType = "Aucun réseau détecté";
        Boolean bStatut = false;
        if (netInfo != null)
        {
            NetworkInfo.State netState = netInfo.getState();

            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0)
            {
                bStatut = true;
                int netType= netInfo.getType();
                switch (netType)
                {
                    case ConnectivityManager.TYPE_MOBILE :
                        sType = "Réseau mobile détecté"; break;
                    case ConnectivityManager.TYPE_WIFI :
                        sType = "Réseau wifi détecté"; break;
                }

            }
        }

        this.alerter(sType);
        return bStatut;
    }
    public void alerter(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.show();
    }
}

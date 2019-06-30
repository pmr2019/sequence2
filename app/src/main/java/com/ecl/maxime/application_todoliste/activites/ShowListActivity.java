package com.ecl.maxime.application_todoliste.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecl.maxime.application_todoliste.adapters.Converter;
import com.ecl.maxime.application_todoliste.data.DataProvider;
import com.ecl.maxime.application_todoliste.data.Item;
import com.ecl.maxime.application_todoliste.adapters.ItemToDoAdapter;
import com.ecl.maxime.application_todoliste.R;
import com.ecl.maxime.application_todoliste.api_request.ItemResponse;
import com.ecl.maxime.application_todoliste.api_request.ItemResponseList;
import com.ecl.maxime.application_todoliste.api_request.ServiceFactory;
import com.ecl.maxime.application_todoliste.api_request.Services;
import com.ecl.maxime.application_todoliste.data.Liste;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ItemToDoAdapter mAdapter;
    private Call<ItemResponseList> call_items;
    private Call<Void> call_ajout, call_modif;
    private String hash;
    private int id;
    private EditText edt_ajout;
    private Button btn_ajout;
    private DataProvider mDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDataProvider = new DataProvider(this);
        edt_ajout = findViewById(R.id.ed_nouvel_item);
        btn_ajout = findViewById(R.id.btn_item);
        mRecyclerView = findViewById(R.id.list_itemtodo);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent i = getIntent();
        hash = i.getStringExtra(MainActivity.HASH);
        id = i.getIntExtra(ChoixListActivity.LISTE_ID,0);

        mAdapter = new ItemToDoAdapter(new ArrayList<Item>(0), new ItemToDoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item itemToDo) {
                boolean checked = itemToDo.isChecked();
                itemToDo.setChecked(!checked);
                if (checked)
                    modifItem(itemToDo.getId(), "0");
                else
                    modifItem(itemToDo.getId(), "1");
            }
        });

        sync();

        mRecyclerView.setAdapter(mAdapter);

        btn_ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = edt_ajout.getText().toString();
                Item new_itemResponse = new Item();
                new_itemResponse.setLabel(label);
                List<Item> itemResponses = mAdapter.getLesItems();
                itemResponses.add(new_itemResponse);
                mAdapter.setLesItems(itemResponses);
                ajouterItem(label);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds mItemResponses to the action bar if it is present.
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
        mDataProvider.syncItems(new DataProvider.ItemsListener() {
            @Override
            public void onSuccess(List<Item> items) {
                mAdapter.setLesItems(items);
            }
            @Override
            public void onError() {
            }
        }, hash, id);
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

    private void modifItem(long id_item, String check){
        Services service = ServiceFactory.createService(Services.class);
        call_modif = service.modifyItem(hash, id, id_item, check);
        call_modif.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ShowListActivity.this,"ItemResponse modifié",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ShowListActivity.this,"Erreur",Toast.LENGTH_LONG).show();
            }
        });
    }
}

package com.ecl.maxime.application_todoliste.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecl.maxime.application_todoliste.data.DataProvider;
import com.ecl.maxime.application_todoliste.data.Liste;
import com.ecl.maxime.application_todoliste.adapters.ListeToDoAdapter;
import com.ecl.maxime.application_todoliste.R;
import com.ecl.maxime.application_todoliste.api_request.ListeResponseList;
import com.ecl.maxime.application_todoliste.api_request.ServiceFactory;
import com.ecl.maxime.application_todoliste.api_request.Services;
import com.ecl.maxime.application_todoliste.data.MyRoomDB;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoixListActivity extends AppCompatActivity {

    private String hash;
    private ListeToDoAdapter mAdapter;
    private Call<Void> call_ajout;
    private RecyclerView mRecyclerView;
    private EditText edt_ajout;
    private Button btn_ajout;
    private DataProvider mDataProvider;
    public static final String LISTE_ID = "liste_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDataProvider = new DataProvider(this);

        Intent intent = getIntent();
        hash = intent.getStringExtra(MainActivity.HASH);
        edt_ajout = findViewById(R.id.ed_nouvelle_liste);
        btn_ajout = findViewById(R.id.btn_liste);
        mRecyclerView = findViewById(R.id.list_listetodo);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ListeToDoAdapter(new ArrayList<Liste>(0), new ListeToDoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Liste listeToDo, int position) {
                int p = listeToDo.getId();
                Intent i = new Intent(ChoixListActivity.this, ShowListActivity.class);
                i.putExtra(MainActivity.HASH, hash);
                i.putExtra(LISTE_ID, p);
                startActivity(i);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        btn_ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom_liste = edt_ajout.getText().toString();
                Liste new_listeResponse = new Liste();
                new_listeResponse.setLabel(nom_liste);
                List<Liste> lesListeResponses = mAdapter.getLesListes();
                lesListeResponses.add(new_listeResponse);
                mAdapter.setLesListes(lesListeResponses);
                ajouterListe(nom_liste);
            }
        });
    }

    private void sync() {

        mDataProvider.syncLists(new DataProvider.ListesListener() {
            @Override
            public void onSuccess(List<Liste> lists) {
                mAdapter.setLesListes(lists);
            }
            @Override
            public void onError() {
            }
        }, hash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sync();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDataProvider.stop();
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
            Intent i = new Intent(ChoixListActivity.this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }



    private void ajouterListe(final String label) {
        Services services = ServiceFactory.createService(Services.class);
        call_ajout = services.addListe(hash, label);
        call_ajout.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                edt_ajout.setText("");
                Toast.makeText(ChoixListActivity.this,"L'ajout de la liste "+label+" a été effectué !",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChoixListActivity.this,"L'ajout a échoué",Toast.LENGTH_LONG).show();
            }
        });
    }

}

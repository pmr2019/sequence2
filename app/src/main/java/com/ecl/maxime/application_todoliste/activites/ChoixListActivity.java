package com.ecl.maxime.application_todoliste.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecl.maxime.application_todoliste.adapter.ListeToDoAdapter;
import com.ecl.maxime.application_todoliste.R;
import com.ecl.maxime.application_todoliste.api_request.Liste;
import com.ecl.maxime.application_todoliste.api_request.ListeDeListes;
import com.ecl.maxime.application_todoliste.api_request.ServiceFactory;
import com.ecl.maxime.application_todoliste.api_request.Services;
import com.ecl.maxime.application_todoliste.classes.ListeToDo;
import com.ecl.maxime.application_todoliste.classes.ProfileListeToDo;
import com.ecl.maxime.application_todoliste.repos.ListesDataRepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoixListActivity extends AppCompatActivity {

    private String hash;
    private ProfileListeToDo user;
    private ListeToDoAdapter mAdapter;
    // On peut maintenant appeler toutes les listes en local
    // private Call<ListeDeListes> call_liste;
    private ArrayList<Liste> listes;
    private Call<Void> call_ajout;
    private RecyclerView mRecyclerView;
    private EditText edt_ajout;
    private Button btn_ajout;
    public static final String LISTE_ID = "liste_id";
    public static final String TITRE_LISTE = "titre_liste";
    private ListesDataRepo listesDataRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_ajout = findViewById(R.id.ed_nouvelle_liste);
        btn_ajout = findViewById(R.id.btn_liste);
        mRecyclerView = findViewById(R.id.list_listetodo);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        hash = intent.getStringExtra(MainActivity.HASH);

        mAdapter = new ListeToDoAdapter(new ArrayList<Liste>(0), new ListeToDoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Liste listeToDo, int position) {
                String id = listeToDo.getId();
                String titreListe = listeToDo.getLabel();
                Intent i = new Intent(ChoixListActivity.this, ShowListActivity.class);
                i.putExtra(MainActivity.HASH, hash);
                i.putExtra(LISTE_ID, id);
                i.putExtra(TITRE_LISTE, titreListe);
                startActivity(i);
            }
        });

        sync();

        mRecyclerView.setAdapter(mAdapter);
        btn_ajout.setEnabled(false);
        if (verifReseau()) btn_ajout.setEnabled(true);
        btn_ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom_liste = edt_ajout.getText().toString();
                Liste new_liste = new Liste();
                new_liste.setLabel(nom_liste);
                listesDataRepo.createItem(new_liste);
                ArrayList<Liste> lesListes = mAdapter.getLesListes();
                lesListes.add(new_liste);
                mAdapter.setLesListes(lesListes);
                ajouterListe(nom_liste);
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
            Intent i = new Intent(ChoixListActivity.this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void sync() {
        //Services service = ServiceFactory.createService(Services.class);
        //call_liste = service.getListes(hash);
        //call_liste.enqueue(new Callback<ListeDeListes>() {



        //    @Override
        //    public void onResponse(Call<ListeDeListes> call, Response<ListeDeListes> response) {
        //       if(response.isSuccessful()){
        //            mAdapter.setLesListes(response.body().lists);
        //        }
        //    }

        //    @Override
        //    public void onFailure(Call<ListeDeListes> call, Throwable t) {
        //        Toast.makeText(ChoixListActivity.this,"Erreur",Toast.LENGTH_LONG).show();
        //    }
        //});
        String login = user.getLogin();
        String mdp = user.getMdp();
        listes = listesDataRepo.getListes(login, mdp);
        if (listes != null) mAdapter.setLesListes(listes);
        else Toast.makeText(ChoixListActivity.this,"Erreur",Toast.LENGTH_LONG).show();

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

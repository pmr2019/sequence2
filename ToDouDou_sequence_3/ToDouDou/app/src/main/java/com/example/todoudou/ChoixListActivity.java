package com.example.todoudou;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoudou.database.DataProvider;
import com.example.todoudou.database.ListeToDo;

import java.util.ArrayList;


public class ChoixListActivity extends AppCompatActivity implements ListAdapter.ActionListener {

    private ArrayList<ListeToDo> listToDo = new ArrayList<>();
    private ListAdapter myAdapter = null;


    private boolean dataRequest = false;
    private DataProvider dataProvider = null;

    private void alerter(String s) {
        Log.i("debug2",s);
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_list);
        Toolbar myToolbar = findViewById(R.id.toolbarList);
        setSupportActionBar(myToolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_white_24dp);
        myToolbar.setOverflowIcon(drawable);

        // initialise le recyclerView en le liant avec listToDo
        final RecyclerView recyclerView = findViewById(R.id.list_recycler_view);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new ListAdapter(listToDo,this);
        recyclerView.setAdapter(myAdapter);


        // on ajoute une liste si le bouton + est cliqué
        findViewById(R.id.btnAjouterListe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.edtAjouterListe);
                String description = editText.getText().toString();
                editText.setText("");
                if (description.length() != 0) {
                    if( manageData(Constant.POST_LIST, new ListeToDo(description)) )
                        editText.setText("");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // initialise le dataProvider
        dataProvider = DataProvider.getInstance(this);
        dataProvider.setCustomObjectListener(new DataProvider.DataListener() {
            @Override
            public void onDataReady(int type, Object... data) {
                dataProcessing(type, data[0]);
            }
        });

        // on récupère, puis affiche les lists
        manageData(Constant.GET_LIST, null);

    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    // https://developer.android.com/training/appbar/actions.html
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_deconnexion:
                alerter("Déconnexion");
                dataProvider.deconnexion();
                // on retroune à l'activité de connexion
                Intent toDeco = new Intent(this,MainActivity.class);
                startActivity(toDeco);
                return true;
            case R.id.action_settings:
                alerter("Settings");
                Intent toSettings = new Intent(this,SettingsActivity.class);
                startActivity(toSettings);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }



    // méthode appelée lors du clic sur la liste
    public void onItemClicked(ListeToDo liste) {
        Log.i("debug2", "liste cliqué " + liste.toString());
        // lancement de l'activité affichant les items de la liste cliqué
        Bundle myBdl = new Bundle();
        myBdl.putInt("indiceList", liste.getId());
        Intent intent = new Intent(ChoixListActivity.this, ShowListActivity.class);
        intent.putExtras(myBdl);
        startActivity(intent);

    }

    // méthode appelée lors d'un clic sur l'icone de suppression de la liste
    public void onItemDelete(ListeToDo liste) {
        manageData(Constant.DELETE_LIST, liste);
    }


    // demande la dataProvider voulue au dataProvider si on ne lui en demande pas déjà
    // Renvoie true la demande a bien été envoyé, false sinon
    private boolean manageData(int type, Object data){
        if(!dataRequest){
            dataRequest = true;
            if (type == Constant.GET_LIST)
                dataProvider.getListToDo();
            if (type == Constant.POST_LIST)
                dataProvider.postListToDo((ListeToDo) data);
            if (type == Constant.DELETE_LIST)
                dataProvider.deleteListToDo((ListeToDo) data);
            return true;
        }
        else{
            alerter("Veuillez attendre la fin du traitement de l'action précédente");
            return false;
        }
    }


    public void dataProcessing(int type, Object data){
        if (type == Constant.GET_LIST)
            new Converter().copyToFromList(listToDo, (ArrayList<ListeToDo>) data);
        if (type == Constant.POST_LIST)
            listToDo.add((ListeToDo) data);
        if (type == Constant.DELETE_LIST)
            removeList_id((Integer) data);
        if (type == Constant.ACTION_IMPOSSIBLE)
            alerter("Action non autorisée en mode hors ligne");
        if (type == Constant.ECHEC_LIAISON_API)
            alerter("Echec liaison API");
        myAdapter.notifyDataSetChanged();

        dataRequest = false;
    }

    // retire la liste d'identifiant égal à @id de la liste de liste listToDo
    private void removeList_id(int id){
        int index = -1;
        for(int k = 0 ; k < listToDo.size() ; k++){
            if(listToDo.get(k).getId() == id)
                index = k;
        }
        if(index != -1)
            listToDo.remove(index);
    }


}
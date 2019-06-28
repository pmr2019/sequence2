package com.example.todoudou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.todoudou.database.ItemToDo;
import com.example.todoudou.database.ListeToDo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ShowListActivity extends AppCompatActivity implements ItemAdapter.ActionListener {

    private int idListCourante;
    private ArrayList<ItemToDo> itemToDo = new ArrayList<>();
    private ItemAdapter myAdapter = null;

    private boolean dataRequest = false;
    private DataProvider dataProvider = null;

    private void alerter(String s) {
        Log.i("debug2",s);
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }
    private void deb(String s){
        Log.i("debug2", s);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        Toolbar myToolbar = findViewById(R.id.toolbarItem);
        setSupportActionBar(myToolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_white_24dp);
        myToolbar.setOverflowIcon(drawable);

        // itnitialise le recyclerView en le liant avec itemToDo
        final RecyclerView recyclerView = findViewById(R.id.item_recycler_view);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new ItemAdapter(itemToDo,this);
        recyclerView.setAdapter(myAdapter);

        // on récupère l'identifiant de la liste sur lequel l'utilisateur a cliqué dans l'activité précédente
        Bundle b = this.getIntent().getExtras();
        idListCourante = b.getInt("indiceList");

        findViewById(R.id.btnAjouterTache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = findViewById(R.id.edtAjouterTache);
                String description = editText.getText().toString();
                editText.setText("");
                if (description.length() != 0) {
                    deb("+ item");
                    manageData(Constant.POST_ITEM, new ItemToDo(description, idListCourante));
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
        manageData(Constant.GET_ITEM, idListCourante);

    }

    private boolean manageData(int type, Object data) {
        if(!dataRequest){
            dataRequest = true;
            if (type == Constant.GET_ITEM){
                dataProvider.getItem(idListCourante);
            }
            if (type == Constant.POST_ITEM){
                dataProvider.postItem((ItemToDo) data);
            }
            if (type == Constant.DELETE_ITEM){
                dataProvider.deleteItem((ItemToDo) data);
            }
            if (type == Constant.CHECK_ITEM){
                dataProvider.putItem((ItemToDo) data);
            }
            return true;
        }
        else{
            alerter("Veuillez attendre la fin du traitement de l'action précédente");
            return false;
        }
    }

    private void dataProcessing(int type, Object data) {
        if (type == Constant.GET_ITEM){
            new Converter().copyToFromItem(itemToDo, (List<ItemToDo>) data);
        }
        if (type == Constant.POST_ITEM){
            itemToDo.add((ItemToDo) data);
        }
        if (type == Constant.DELETE_ITEM){
            removeItem_id(((ItemToDo)data).getUid());
        }
        if (type == Constant.CHECK_ITEM){
            ItemToDo item = (ItemToDo)data;
            item.setFait(!item.getFait());
        }
        if (type == Constant.ACTION_IMPOSSIBLE)
            alerter("Action non autorisée en mode hors ligne");
        if (type == Constant.ECHEC_LIAISON_API)
            alerter("Echec liaison API");

        deb("itemtodo : " + itemToDo);

        myAdapter.notifyDataSetChanged();

        dataRequest = false;
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


    // méthode appelée lors du clic sur la checkbox de l'item
    public void onItemChecked(ItemToDo item) {
//        Log.i("debug2", "item cliqué coche " + item.toString());
//        alerter("Check item");
        deb("on check pre" + item.toString());
        item.setIdliste(idListCourante);
//        item.setFait(!item.getFait());
        deb("on check post" + item.toString());
        manageData(Constant.CHECK_ITEM, item);
    }
    // méthode appelée lors d'un clic sur l'icone de suppression de l'item
    public void onItemDelete(ItemToDo item) {
//        Log.i("debug2", "item cliqué " + item.toString());

//        alerter("Supression de l'item");
        item.setIdliste(idListCourante);
        manageData(Constant.DELETE_ITEM, item);
    }




    // dans la liste itemToDo, renvoie l'index de l'item d'identifiant @id s'il existe, -1 sinon
    private int findIndexItemById(int uid){
        int index = -1;
        for(int k = 0 ; k < itemToDo.size() ; k++){
            if(itemToDo.get(k).getUid() == uid){
                index = k;
            }
        }
        return index;
    }

    // retire l'item d'identifiant égal à @id de la liste d'item itemToDo
    private void removeItem_id(int id){
        int index = findIndexItemById(id);
        if(index != -1) itemToDo.remove(index);
    }



}
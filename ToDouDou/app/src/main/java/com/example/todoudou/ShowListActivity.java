package com.example.todoudou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ShowListActivity extends AppCompatActivity implements ItemAdapter.ActionListener {

    private int idListCourante;
    private ArrayList<ItemToDo> itemToDo = new ArrayList<>();
    private ItemAdapter myAdapter = null;

    private void alerter(String s) {
        Log.i("debug2",s);
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarItem);
        setSupportActionBar(myToolbar);

        // on récupère l'identifiant de la liste sur lequel l'utilisateur a cliqué dans l'activité précédente
        Bundle b = this.getIntent().getExtras();
        idListCourante = b.getInt("indiceList");

        new GetItemAsyncTask().execute();

        findViewById(R.id.btnAjouterTache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = findViewById(R.id.edtAjouterTache);
                String description = editText.getText().toString();
                editText.setText("");
                if (description.length() != 0) {
                    new PostItemAsyncTask().execute(description);
                }
            }
        });
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
                // on efface le mot de passe enregistré afin d'éviter que la prochaine fois l'utilisateur se connecte directement
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = settings.edit();
                if(settings.contains("pass")){
                    editor.remove("pass");
                    editor.commit();
                }
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
        int checked = 0;
        if(!item.getFait()) checked = 1;
        new PutItemAsyncTask().execute(String.valueOf(item.getId()), String.valueOf(checked) );
    }
    // méthode appelée lors d'un clic sur l'icone de suppression de l'item
    public void onItemDelete(ItemToDo item) {
//        Log.i("debug2", "item cliqué " + item.toString());
        alerter("Supression de l'item");
        new DeleteItemAsyncTask().execute(String.valueOf(item.getId()));
    }

    // à partir des SharedPreferences, récupère le hash de connexion de l'utilisateur
    private String getHash(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getString("hash","");
    }
    // à partir de l'objet JSON item renvoyé par l'API, créé un nouvel item
    private ItemToDo newItemToDo(JsonObject item){
        int id = item.get("id").getAsInt();
        String description = item.get("label").getAsString();
        boolean fait = true;
        if(item.get("checked").getAsInt() == 0) fait = false;
        return new ItemToDo(description, fait, id);
    }
    // dans la liste itemToDo, renvoie l'index de l'item d'identifiant @id s'il existe, -1 sinon
    private int findIndexItemById(int id){
        int index = -1;
        for(int k = 0 ; k < itemToDo.size() ; k++){
            if(itemToDo.get(k).getId() == id){
                index = k;
            }
        }
        return index;
    }

    // async task permettant d'exécuter la requete GET permettant de récupérer la liste des items d'une liste
    class GetItemAsyncTask  extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String hash = getHash();
            Reseau res = new Reseau(getBaseContext());
            String reponse = "";
            if( !hash.equals("") && res.verifReseau()){
                String url = "/lists/" + idListCourante + "/items?hash=" + hash;
                reponse = res.requete(url);
            }
            return reponse;
        }
        protected void onPostExecute(String result) {
            if(result!=null) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonArray tab = gson.fromJson(result, JsonObject.class).get("items").getAsJsonArray();
                // on initialise notre itemToDo avec la réponse de l'API
                for(int k = 0 ; k < tab.size() ; k++){
                    JsonObject list = tab.get(k).getAsJsonObject();
                    itemToDo.add(newItemToDo(list));
                }
                initialiseRecyclerView();
            }
        }
    }
    // itnitialise le recyclerView en le liant avec itemToDo
    private void initialiseRecyclerView(){
        final RecyclerView recyclerView = findViewById(R.id.item_recycler_view);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new ItemAdapter(itemToDo,this);
        recyclerView.setAdapter(myAdapter);
    }

    // async task permettant d'exécuter la requete POST permettant de créer un nouvel item
    class PostItemAsyncTask  extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String hash = getHash();
            String description = strings[0];
            Reseau res = new Reseau(getBaseContext());
            String reponse = "";
            if(!hash.equals("") && res.verifReseau()){
                String url = "/lists/" + idListCourante + "/items?label=" + description + "&check=0&hash=" + hash;
                reponse = res.executePost(url, "");
            }
            return reponse;
        }
        protected void onPostExecute(String result) {
            if(result!=null) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(result, JsonObject.class);
                if(resp.get("success").getAsBoolean()){
                    JsonObject item = resp.get("item").getAsJsonObject();
                    itemToDo.add(newItemToDo(item));
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    // async task permettant d'exécuter la requete DELETE permettant de supprimer un item
    class DeleteItemAsyncTask  extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String hash = getHash();
            String idItem = strings[0];
            Reseau res = new Reseau(getBaseContext());
            String reponse = "";
            if(!hash.equals("")  && res.verifReseau()){
                String url = "/lists/" + idListCourante + "/items/" + idItem + "?hash=" + hash;
                reponse = res.executeDelete(url);
            }
            return idItem + "&" + reponse; //
        }
        protected void onPostExecute(String result) {
            String[] results = result.split("&");
            int idItem = Integer.parseInt(results[0]);
            String reponse = results[1];
            if(result!=null) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(reponse, JsonObject.class);
                if(resp.get("success").getAsBoolean()){
                    removeItem_id(idItem);
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    // retire l'item d'identifiant égal à @id de la liste d'item itemToDo
    private void removeItem_id(int id){
        int index = findIndexItemById(id);
        if(index != -1) itemToDo.remove(index);
    }

    // async task permettant d'exécuter la requete PUT permettant de cocher l'item
    class PutItemAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String hash = getHash();
            String idItem = strings[0];
            String checked = strings[1];
            Reseau res = new Reseau(getBaseContext());
            String reponse = "";
            if(!hash.equals("")  && res.verifReseau()){
                String url = "/lists/" + idListCourante + "/items/" + idItem + "?check=" + checked + "&hash=" + hash;
//                Log.i("debug2", "url du PUT : " + url);
                reponse = res.executePut(url);
            }
//            Log.i("debug2", "La reponse : " + reponse);
            return reponse; //
        }
        protected void onPostExecute(String result) {
            if(result!=null && !result.equals("")) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(result, JsonObject.class);
                if(resp.get("success").getAsBoolean()){
                    checkedItem_id(resp.get("item").getAsJsonObject());
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    // Permet de mettre à jour la liste d'item itemToDo en cochant l'item qui a été coché
    private void checkedItem_id(JsonObject item){
        int id = item.get("id").getAsInt();
        boolean fait = true;
        if(item.get("checked").getAsInt() == 0) fait = false;
        int index = findIndexItemById(id);
        if(index != -1) itemToDo.get(index).setFait(fait);
    }
}

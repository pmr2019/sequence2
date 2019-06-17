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

public class ChoixListActivity extends AppCompatActivity implements ListAdapter.ActionListener {

    private ArrayList<ListeToDo> listToDo = new ArrayList<>();
    private ListAdapter myAdapter = null;

    private void alerter(String s) {
        Log.i("Debug",s);
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarList);
        setSupportActionBar(myToolbar);

        new GetListAsyncTask().execute();

        findViewById(R.id.btnAjouterListe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.edtAjouterListe);
                String description = editText.getText().toString();
                editText.setText("");
                if (description.length() != 0) {
                    new PostListAsyncTask().execute(description);
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

    // méthode appelée lors du clic sur la liste
    public void onItemClicked(ListeToDo liste) {
//        Log.i("debug2", "liste cliqué " + liste.toString());

        // lancement de l'activité affichant les items de la liste cliqué
        Bundle myBdl = new Bundle();
        myBdl.putInt("indiceList", liste.getId());
        Intent intent = new Intent(ChoixListActivity.this, ShowListActivity.class);
        intent.putExtras(myBdl);
        startActivity(intent);
    }
    // méthode appelée lors d'un clic sur l'icone de suppression de la liste
    public void onItemDelete(ListeToDo liste) {
//        Log.i("debug2", "delete : " + liste.getId());
        alerter("id : " + liste.getId());
        new DeleteListAsyncTask().execute(String.valueOf(liste.getId()));
    }


    // à partir des SharedPreferences, récupère le hash de connexion de l'utilisateur
    private String getHash(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getString("hash","");
    }
    // à partir de l'objet JSON "list" renvoyé par l'API, créé une nouvelle liste
    private ListeToDo newListToDo(JsonObject list){
        int id = list.get("id").getAsInt();
        String description = list.get("label").getAsString();
        return new ListeToDo(description, id);
    }

    // async task permettant d'exécuter la requete GET permettant de récupérer la liste des listes d'un utilisateur
    class GetListAsyncTask  extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String hash = getHash();
            Reseau res = new Reseau(getBaseContext());
            String reponse = "";
            if( !hash.equals("") && res.verifReseau()){
                String url = "/lists?hash=" + hash;
                reponse = res.requete(url);
            }
            return reponse;
        }
        protected void onPostExecute(String result) {
            if(result!=null) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonArray tab = gson.fromJson(result, JsonObject.class).get("lists").getAsJsonArray();
                // on initialise notre listToDo avec la réponse de l'API
                for(int k = 0 ; k < tab.size() ; k++){
                    JsonObject list = tab.get(k).getAsJsonObject();
                    listToDo.add(newListToDo(list));
                }
                initialiseRecyclerView();
            }
        }
    }
    // initialise le recyclerView en le liant avec listToDo
    private void initialiseRecyclerView(){
        final RecyclerView recyclerView = findViewById(R.id.list_recycler_view);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new ListAdapter(listToDo,this);
        recyclerView.setAdapter(myAdapter);
    }

    // async task permettant d'exécuter la requete POST permettant de créer une nouvelle liste
    class PostListAsyncTask  extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String hash = getHash();
            String description = strings[0];
            Reseau res = new Reseau(getBaseContext());
            String reponse = "";
            if(!hash.equals("") && res.verifReseau()){
                String url = "/lists?label=" + description + "&hash=" + hash;
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
                    JsonObject list = resp.get("list").getAsJsonObject();
                    listToDo.add(newListToDo(list));
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    // async task permettant d'exécuter la requete DELETE permettant de supprimer une liste
    class DeleteListAsyncTask  extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String hash = getHash();
            String idList = strings[0];
            Reseau res = new Reseau(getBaseContext());
            String reponse = "";
            if(!hash.equals("")  && res.verifReseau()){
                String url = "/lists/" + idList + "&hash=" + hash;
                reponse = res.executeDelete(url);
            }
            return idList + "&" + reponse; //
        }
        protected void onPostExecute(String result) {
            String[] results = result.split("&");
            int idList = Integer.parseInt(results[0]);
            String reponse = results[1];
            if(result!=null) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(reponse, JsonObject.class);
                if(resp.get("success").getAsBoolean()){
                    removeList_id(idList);
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    // retire la liste d'identifiant égal à @id de la liste de liste listToDo
    private void removeList_id(int id){
        int index = -1;
        for(int k = 0 ; k < listToDo.size() ; k++){
            if(listToDo.get(k).getId() == id){
                index = k;
            }
        }
        if(index != -1) listToDo.remove(index);
    }
}


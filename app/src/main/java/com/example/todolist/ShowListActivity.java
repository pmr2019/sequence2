package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class ShowListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PseudoItemTask mAdapter;
    private EditText edtAjouterTask;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button btnAjouterTask;
    private SharedPreferences mSettings;
    private Bundle data;
    private int idList;
    private int idItem;
    private String mHash;
    private JSONArray myJsonItems;
    private List<ItemToDo> myListeItems;
    private int laPosition;
    private String description;
    private String myUrl;
    private Boolean connexion;
    private DataProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        btnAjouterTask = findViewById(R.id.btnAjouterTask);
        edtAjouterTask = findViewById(R.id.textAjouterTask);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        data = this.getIntent().getExtras();
        idList = data.getInt("idList");
        myUrl = data.getString("myUrl");
        connexion = data.getBoolean("connexion");

        mHash = mSettings.getString("mHash","");

        dataProvider = new DataProvider(this);

        myListeItems = new ArrayList<>();

        if(connexion) {
            new PostAsyncTask().execute();
        }
        else{
            new PostAsyncTask4().execute();
        }
        // on mets en place le recyclerView spécial pour les Todoliste (sans checkbox)
        mRecyclerView =  findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        btnAjouterTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getId()==R.id.btnAjouterTask && connexion){
                    description = edtAjouterTask.getText().toString();
                    new PostAsyncTask3().execute();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Vous n'êtes pas connecté à Internet", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


    }
    ///////quand on est connecté, on question l'API et on met a jour la BDD/////////
    public class PostAsyncTask extends AsyncTask<Object,Void, JSONArray> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override protected JSONArray doInBackground(Object... objects) {
            myJsonItems = dataProvider.getItems(myUrl, mHash,idList);
            //on ajoute a liste de todos (qu'on enverra à l'adapteur) chacune
            // des todos envoyés par le serveur. On a crée un nouvel attribut dans la classe ListeTodo
            //et on a crée un nouveau constructeur.
            try {
                for(int i=0; i<myJsonItems.length();i++){
                    myListeItems.add(new ItemToDo(myJsonItems.getJSONObject(i).getInt("id"),
                            myJsonItems.getJSONObject(i).getString("label"),
                            myJsonItems.getJSONObject(i).getString("checked"),
                            idList));

                    dataProvider.insertItem(new ItemToDo(myJsonItems.getJSONObject(i).getInt("id"),
                            myJsonItems.getJSONObject(i).getString("label"),
                            myJsonItems.getJSONObject(i).getString("checked"),
                            idList));
                }
            } catch (JSONException e) {e.printStackTrace();}
            return  myJsonItems;
        }

        @Override protected void onPostExecute(JSONArray myJson) {
            super.onPostExecute(myJson);
            // on ajoute a l'adapter les todolist propre au pseudo précédemment séléctionné
            mAdapter = new PseudoItemTask(myListeItems);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnClickListener(new PseudoItemTask.OnItemClickListener() {
                @Override
                public void onItemClick(int position, List<ItemToDo> mList) {}

                @Override
                public void onCheckBoxClick(int position) {
                    laPosition=position;
                    new PostAsyncTask2().execute();
                }
            });
        }
    }

    //////Quand on est connecté et qu'on clique sur la checkbox/////////
    public class PostAsyncTask2 extends AsyncTask<String, Void, String> {
        // Params, Progress, Result

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (!myListeItems.get(laPosition).getFait()) {
                myListeItems.get(laPosition).setFait(true);
                return dataProvider.requete(myUrl + "/lists/"+ idList +"/items/"+ myListeItems.get(laPosition).getIdItem() +"?check=1&hash="+mHash,"PUT");
            }
            else {
                return dataProvider.requete(myUrl + "/lists/"+ idList +"/items/"+ myListeItems.get(laPosition).getIdItem() +"?check=0&hash=" + mHash,"PUT");
            }
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
    }

    //////Seulement quand on est connecté, le bouton pour ajouter des list est actif///////
    public class PostAsyncTask3 extends AsyncTask<String, Void, String> {
        // Params, Progress, Result

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return dataProvider.requete(myUrl + "/lists/"+ idList +"/items?label="+ description + "&hash="+mHash,"POST");
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
    }

    //////Quand on est pas connecté, on va cherche les infos dnas la BDD///////
    public class PostAsyncTask4 extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            myListeItems = dataProvider.loadItem(idList);
            return "";
        }
        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            mAdapter = new PseudoItemTask(myListeItems);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnClickListener(new PseudoItemTask.OnItemClickListener() {
                @Override
                public void onItemClick(int position, List<ItemToDo> mList) {}

                @Override
                public void onCheckBoxClick(int position) {
                    laPosition=position;
                    idItem=myListeItems.get(position).getIdItem();
                    new PostAsyncTask5().execute();
                }
            });
        }
    }

    ///////Quand on est pas connecté et que l'utilisateur clique sur une checkbox /////////
    public class PostAsyncTask5 extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(!dataProvider.getFaitBdd(idItem)){
                dataProvider.updateItem(true,idItem);
            }
            else {
                dataProvider.updateItem(false,idItem);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
    }
}

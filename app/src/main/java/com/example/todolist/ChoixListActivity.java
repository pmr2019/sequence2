package com.example.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;


public class ChoixListActivity extends AppCompatActivity{

    // le fichier myJsonStringListeTodo correspond au fichier Json associé à la liste des profils "lesListTodo"

    private RecyclerView mRecyclerView;
    private PseudoItem mAdapter;
    private EditText edtAjouterTodo;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button btnAjouterTodo;
    private SharedPreferences mSettings;
    private Intent toShowListActivity;
    private String mHash;
    private Bundle data;
    private JSONArray myJsonList;
    private List<ListeToDo> myListeTodo;
    private String titreListeTodo;
    private String myUrl;
    private DataProvider dataProvider;
    private Boolean connexion;
    private int idUser;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_list);
        btnAjouterTodo = findViewById(R.id.btnAjouterTodo);
        edtAjouterTodo = findViewById(R.id.textAjouterTodo);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        //myUrl = mSettings.getString("url","");
        data = this.getIntent().getExtras();
        dataProvider = new DataProvider(this);
        myUrl = data.getString("myUrl");
        connexion = data.getBoolean("connexion");
        idUser = data.getInt("idUser");
        myListeTodo = new ArrayList<>();
        mHash = mSettings.getString("mHash","");

        //Si la connexion est établi, on fait avec API comme d'hab
        if(connexion) {
            (new PostAsyncTask()).execute();
        }
        //Sinon on reste sur la BDD
        else {
            (new PostAsyncTask3()).execute();
        }

        // on mets en place le recyclerView spécial pour les Todoliste (sans checkbox)
        mRecyclerView =  findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);

        btnAjouterTodo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getId()==R.id.btnAjouterTodo && connexion){
                    titreListeTodo = edtAjouterTodo.getText().toString();
                    (new PostAsyncTask2()).execute();
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
            myJsonList = dataProvider.getLists(myUrl,mHash);
            //on ajoute a liste de todos (qu'on enverra à l'adapteur) chacune
            // des todos envoyés par le serveur. On a crée un nouvel attribut dans la classe ListeTodo
            //et on a crée un nouveau constructeur.
            //On ajoute également à la BDD les listes en question avec l'idUser
            try {
                for(int i=0; i<myJsonList.length();i++){
                    //avec la gestion réseau
                    myListeTodo.add(new ListeToDo(myJsonList.getJSONObject(i).getString("label"),
                            myJsonList.getJSONObject(i).getInt("id")));

                    //on rempli la bdd
                    dataProvider.insertList(new ListeToDo(myJsonList.getJSONObject(i).getString("label"),
                            myJsonList.getJSONObject(i).getInt("id"),
                            idUser));
                }
            } catch (JSONException e) {e.printStackTrace();}
            return  myJsonList;
        }

        @Override protected void onPostExecute(JSONArray myJson) {
            super.onPostExecute(myJson);
            // on ajoute a l'adapter les todolist propre au pseudo précédemment séléctionné
            mAdapter = new PseudoItem(myListeTodo);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnClickListener(new PseudoItem.OnItemClickListener() {
                @Override
                public void onItemClick(int position, List<ListeToDo> mList) {
                    Intent toShowListActivity = new Intent(ChoixListActivity.this,ShowListActivity.class);
                    //on transmet l'id de la TodoList sélectionnée
                    data.putInt("idList",myListeTodo.get(position).getIdList());
                    toShowListActivity.putExtras(data);
                    startActivity(toShowListActivity);
                }
            });

        }
    }


    //////Seulement quand on est connecté, le bouton pour ajouter des list est actif///////
    public class PostAsyncTask2 extends AsyncTask<String, Void, String> {
        // Params, Progress, Result

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return dataProvider.requete(myUrl + "/lists?label="+ titreListeTodo + "&hash="+mHash,"POST");
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
    public class PostAsyncTask3 extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            myListeTodo = dataProvider.loadList(idUser);
            return "";
        }
        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            mAdapter = new PseudoItem(myListeTodo);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnClickListener(new PseudoItem.OnItemClickListener() {
                @Override
                public void onItemClick(int position, List<ListeToDo> mList) {
                    Intent toShowListActivity = new Intent(ChoixListActivity.this,ShowListActivity.class);
                    //on transmet l'id de la TodoList sélectionnée
                    data.putInt("idList",myListeTodo.get(position).getIdList());
                    toShowListActivity.putExtras(data);
                    startActivity(toShowListActivity);
                }
            });
        }
    }
}

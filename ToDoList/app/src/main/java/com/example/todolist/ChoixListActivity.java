package com.example.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private PostAsyncTask task;
    private List<ListeToDo> myListeTodo;
    private PostAsyncTask2 task2;
    private String titreListeTodo;
    private String myUrl;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_list);
        btnAjouterTodo = findViewById(R.id.btnAjouterTodo);
        edtAjouterTodo = findViewById(R.id.textAjouterTodo);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        //myUrl = mSettings.getString("url","");
        data = this.getIntent().getExtras();
        myUrl = data.getString("myUrl");
        myListeTodo = new ArrayList<>();
        mHash = mSettings.getString("mHash","");
        task = new PostAsyncTask();
        task2= new PostAsyncTask2();
        task.execute();

        // on mets en place le recyclerView spécial pour les Todoliste (sans checkbox)
        mRecyclerView =  findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        btnAjouterTodo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getId()==R.id.btnAjouterTodo){
                    titreListeTodo = edtAjouterTodo.getText().toString();
                    task2.execute();
                }
            }
        });
     }

    public class PostAsyncTask extends AsyncTask<Object,Void, JSONArray> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override protected JSONArray doInBackground(Object... objects) {
            return  (new DataProvider()).getLists(myUrl,mHash);
        }

        @Override protected void onPostExecute(JSONArray myJson) {
            super.onPostExecute(myJson);
            myJsonList = myJson;
            //on ajoute a liste de todos (qu'on enverra à l'adapteur) chacune
            // des todos envoyés par le serveur. On a crée un nouvel attribut dans la classe ListeTodo
            //et on a crée un nouveau constructeur.
            try {
                for(int i=0; i<myJsonList.length();i++){
                    myListeTodo.add(new ListeToDo(myJsonList.getJSONObject(i).getString("id"),
                            myJsonList.getJSONObject(i).getString("label")));
                }
            } catch (JSONException e) {e.printStackTrace();}
            // on ajoute a l'adapter les todolist propre au pseudo précédemment séléctionné
            mAdapter = new PseudoItem(myListeTodo);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnClickListener(new PseudoItem.OnItemClickListener() {
                @Override
                public void onItemClick(int position, List<ListeToDo> mList) {
                    Intent toShowListActivity = new Intent(ChoixListActivity.this,ShowListActivity.class);
                    //on transmet l'id de la TodoList sélectionnée
                    data.putString("idList",myListeTodo.get(position).getId());
                    toShowListActivity.putExtras(data);
                    startActivity(toShowListActivity);
                }
            });

        }
    }



    class PostAsyncTask2 extends AsyncTask<String, Void, String> {
        // Params, Progress, Result

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return (new DataProvider()).requete(myUrl + "/lists?label="+ titreListeTodo + "&hash="+mHash,"POST");
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

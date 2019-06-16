package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Button;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
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
    private String idList;
    private String mHash;
    private JSONArray myJsonItems;
    private List<ItemToDo> myListeItems;
    private PostAsyncTask task;
    private PostAsyncTask2 task2;
    private PostAsyncTask3 task3;
    private int laPosition;
    private String description;
    private String myUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        btnAjouterTask = findViewById(R.id.btnAjouterTask);
        edtAjouterTask = findViewById(R.id.textAjouterTask);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        data = this.getIntent().getExtras();
        idList = data.getString("idList");
        myUrl = data.getString("myUrl");
        mHash = mSettings.getString("mHash","");


        myListeItems = new ArrayList<>();
        task = new PostAsyncTask();
        task2= new PostAsyncTask2();
        task3 = new PostAsyncTask3();
        task.execute();

        // on mets en place le recyclerView spécial pour les Todoliste (sans checkbox)
        mRecyclerView =  findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        btnAjouterTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getId()==R.id.btnAjouterTask){
                    description = edtAjouterTask.getText().toString();
                    task3.execute();
                }
            }
        });


    }

    public class PostAsyncTask extends AsyncTask<Object,Void, JSONArray> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override protected JSONArray doInBackground(Object... objects) {
            return  (new DataProvider()).getItems(myUrl, mHash,idList);
        }

        @Override protected void onPostExecute(JSONArray myJson) {
            super.onPostExecute(myJson);
            myJsonItems = myJson;
            //on ajoute a liste de todos (qu'on enverra à l'adapteur) chacune
            // des todos envoyés par le serveur. On a crée un nouvel attribut dans la classe ListeTodo
            //et on a crée un nouveau constructeur.
            try {
                for(int i=0; i<myJsonItems.length();i++){
                    myListeItems.add(new ItemToDo(myJsonItems.getJSONObject(i).getString("id"),
                            myJsonItems.getJSONObject(i).getString("label"),
                            myJsonItems.getJSONObject(i).getString("checked")));
                }
            } catch (JSONException e) {e.printStackTrace();}

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
                    task2.execute();
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
            if (!myListeItems.get(laPosition).getFait()) {
                myListeItems.get(laPosition).setFait(true);
                return (new DataProvider()).requete(myUrl + "/lists/"+ idList +"/items/"+ myListeItems.get(laPosition).getId() +"?check=1&hash="+mHash,"PUT");
            }
            else {
                return (new DataProvider()).requete(myUrl + "/lists/"+ idList +"/items/"+ myListeItems.get(laPosition).getId() +"?check=0&hash=" + mHash,"PUT");
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

    class PostAsyncTask3 extends AsyncTask<String, Void, String> {
        // Params, Progress, Result

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return (new DataProvider()).requete(myUrl + "/lists/"+ idList +"/items?label="+ description + "&hash="+mHash,"POST");
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

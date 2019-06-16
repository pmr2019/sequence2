package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ChoixListActivity extends AppCompatActivity implements ItemAdapterList.ActionListener {
    private EditText edtNewList;
    private Button btnAddList;
    private String token;
    private ItemAdapterList adapterList;
    private SharedPreferences settings;
    private ItemAdapterList.ActionListener al;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_list);
        btnAddList = (Button) findViewById(R.id.btnAddList);
        edtNewList = (EditText) findViewById(R.id.edtNewList);
        al = this;
        context = this;

        //Recovering the preferences and the token
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        token = settings.getString("token", "");

        //Recovering the lists
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/lists?hash=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        JsonObject responseJson = gson.fromJson(response, JsonObject.class);
                        Type type = new TypeToken<List<ListeToDo>>() {
                        }.getType();
                        List<ListeToDo> lists = gson.fromJson(responseJson.getAsJsonArray("lists"), type);


                        //adapter parameters
                        adapterList = new ItemAdapterList(lists, al);
                        final RecyclerView recyclerView = findViewById(R.id.lists);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adapterList);
                        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));
                        recyclerView.addItemDecoration((new DividerItemDecoration(context, LinearLayout.VERTICAL)));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Web", "error");
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        // Click listener for Add button
        btnAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listTitle = edtNewList.getText().toString();
                if (listTitle.equals("")) {
                    Toast myToast = Toast.makeText(getApplicationContext(), " Please name your list !", Toast.LENGTH_LONG);
                    myToast.show();
                } else {
                    final ListeToDo[] newList = new ListeToDo[1];
                    //Add it to the DB
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/lists?label=" + listTitle + "&hash=" + token;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Gson gson = new Gson();
                                    JsonObject responseJson = gson.fromJson(response, JsonObject.class);
                                    Type type = new TypeToken<ListeToDo>() {
                                    }.getType();
                                    newList[0] = gson.fromJson(responseJson.getAsJsonObject("list"), type);

                                    //Add it to the adapter
                                    adapterList.AddToList(newList[0]);

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Web", "error");
                            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                        }
                    });
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);

                }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //List clicked
    public void onListClicked(Integer idListe) {
        //Create an intent to move to ShowListActivity
        Intent toSecondAct = new Intent(ChoixListActivity.this, ShowListActivity.class);
        Bundle data_list = new Bundle();
        data_list.putInt("idList", idListe);
        toSecondAct.putExtras(data_list);
        startActivity(toSecondAct);
    }

}

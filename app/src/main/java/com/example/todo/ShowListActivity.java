package com.example.todo;

import android.content.Context;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ShowListActivity extends AppCompatActivity implements ItemAdapterItem.ActionListener {

    private EditText edtNewItem;
    private Button btnAddItem;
    private Integer idList;
    private ItemAdapterItem adapterItem;
    private SharedPreferences settings;
    private String token;
    private ItemAdapterItem.ActionListener al;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
        edtNewItem = (EditText) findViewById(R.id.edtNewItem);
        al = this;
        context = this;

        //Recovering the bundle
        Bundle b = this.getIntent().getExtras();
        idList = b.getInt("idList");

        //Recover the token from preferences
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        token = settings.getString("token", "");

        //Recovering the items
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/lists/" + idList + "/items?hash=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Web", response);
                        Gson gson = new Gson();
                        JsonObject responseJson = gson.fromJson(response, JsonObject.class);
                        Type type = new TypeToken<List<ItemToDo>>() {
                        }.getType();
                        List<ItemToDo> items = gson.fromJson(responseJson.getAsJsonArray("items"), type);

                        Log.i("Web", items.toString());


                        //adapter parameters
                        adapterItem = new ItemAdapterItem(items, al);
                        final RecyclerView recyclerView = findViewById(R.id.items);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adapterItem);
                        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));
                        recyclerView.addItemDecoration((new DividerItemDecoration(context, LinearLayout.VERTICAL)));

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallbackItem(adapterItem));
                        itemTouchHelper.attachToRecyclerView(recyclerView);

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
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemTitle = edtNewItem.getText().toString();
                if (itemTitle.equals("")) {
                    Toast myToast = Toast.makeText(getApplicationContext(), " Please name your item !", Toast.LENGTH_LONG);
                    myToast.show();
                } else {
                    final ItemToDo[] newItem = new ItemToDo[1];
                    //Add it to the DB
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/lists/" + idList + "/items?label=" + itemTitle + "&hash=" + token;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Gson gson = new Gson();
                                    JsonObject responseJson = gson.fromJson(response, JsonObject.class);
                                    Type type = new TypeToken<ItemToDo>() {
                                    }.getType();
                                    newItem[0] = gson.fromJson(responseJson.getAsJsonObject("item"), type);

                                    //Add it to the adapter
                                    adapterItem.AddToList(newItem[0]);

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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Item clicked
    public void onCheckBoxClicked(Integer idItem, int state) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/lists/" + idList + "/items/" + idItem + "?check=" + state + "&hash=" + token;
        Log.i("Web",url);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Web",response);
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


    //Deleting
    @Override
    public void onItemRemoved(Integer id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/lists/" + idList + "/items/" + id + "&hash=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Web", "error");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onUndoDelete(String label, final Integer state, final Integer position) {
        final ItemToDo[] newItem = new ItemToDo[1];
        //Add it to the DB
        final RequestQueue queue = Volley.newRequestQueue(context);
        final String[] url = {settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/lists/" + idList + "/items?label=" + label + "&hash=" + token};
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url[0],
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        JsonObject responseJson = gson.fromJson(response, JsonObject.class);
                        Type type = new TypeToken<ItemToDo>() {}.getType();
                        newItem[0] = gson.fromJson(responseJson.getAsJsonObject("item"), type);

                        //Add it to the adapter
                        newItem[0].setChecked(state);
                        adapterItem.addToPosition(newItem[0], position);
                        adapterItem.notifyItemInserted(position);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

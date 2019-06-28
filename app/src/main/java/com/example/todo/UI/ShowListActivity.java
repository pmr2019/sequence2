package com.example.todo.UI;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.todo.adapters.ItemAdapterItem;
import com.example.todo.database.RoomProductHuntDb;
import com.example.todo.database.dao.ItemDao;
import com.example.todo.database.dao.RequestDao;
import com.example.todo.model.ItemToDo;
import com.example.todo.R;
import com.example.todo.adapters.SwipeToDeleteCallbackItem;
import com.example.todo.model.WaitingRequest;
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

        if(verifReseau()) {
            //Updating
            final RequestDao requestDao;
            requestDao = RoomProductHuntDb.getDatabase(context).requestDao();
            final List<WaitingRequest> waitingRequests = requestDao.getRequests();
            RequestQueue queue = Volley.newRequestQueue(this);
            for (final WaitingRequest r:waitingRequests) {
                final String url = r.getUrl();
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                requestDao.delete(url);
                                Log.i("BlaBla",response.toString());
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


            //Recovering the items
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

                            //Dao
                            final ItemDao itemDao;
                            itemDao = RoomProductHuntDb.getDatabase(context).itemDao();
                            //Saving
                            for (ItemToDo item :items) {
                                item.setIdListe(idList);
                            }
                            itemDao.save(items);

                            Log.i("BDD_sqlit", itemDao.getItems(idList).toString());

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
        }
        else{
            final ItemDao itemDao;
            itemDao = RoomProductHuntDb.getDatabase(context).itemDao();
            List<ItemToDo> items = itemDao.getItems(idList);
            adapterItem = new ItemAdapterItem(items, al);
            final RecyclerView recyclerView = findViewById(R.id.items);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapterItem);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));
            recyclerView.addItemDecoration((new DividerItemDecoration(context, LinearLayout.VERTICAL)));

        }

        // Click listener for Add button
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifReseau()){
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
                                        newItem[0].setIdListe(idList);

                                        //Add it to the adapter
                                        adapterItem.AddToList(newItem[0]);

                                        //Add to the database
                                        final ItemDao itemDao;
                                        itemDao = RoomProductHuntDb.getDatabase(context).itemDao();
                                        //Saving
                                        itemDao.add(newItem[0]);

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
                else{
                    Toast.makeText(context, "There is no internet connection", Toast.LENGTH_LONG).show();
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
        if (verifReseau()) {
            final ItemToDo[] newItem = new ItemToDo[1];
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/lists/" + idList + "/items/" + idItem + "?check=" + state + "&hash=" + token;
            Log.i("Web", url);
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                    new Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            JsonObject responseJson = gson.fromJson(response, JsonObject.class);
                            Type type = new TypeToken<ItemToDo>() {
                            }.getType();
                            newItem[0] = gson.fromJson(responseJson.getAsJsonObject("item"), type);
                            newItem[0].setIdListe(idList);
                            //Add to the database
                            final ItemDao itemDao;
                            itemDao = RoomProductHuntDb.getDatabase(context).itemDao();
                            //Saving
                            itemDao.add(newItem[0]);
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
        else{
           Toast.makeText(context, "item will be updated once there is internet connection", Toast.LENGTH_LONG).show();
            final ItemDao itemDao;
            itemDao = RoomProductHuntDb.getDatabase(context).itemDao();
            //get the item
            ItemToDo item = itemDao.getItem(idItem);
            //change it
            item.setChecked(state);
            //store it in the database
            itemDao.add(item);
            //store a request in the database
            String url = settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/lists/" + idList + "/items/" + idItem + "?check=" + state + "&hash=" + token;
            WaitingRequest mWaitingRequest = new WaitingRequest(url);

            final RequestDao requestDao;
            requestDao = RoomProductHuntDb.getDatabase(context).requestDao();
            requestDao.add(mWaitingRequest);

            Log.i("BlaBla",requestDao.getRequests().toString());

        }

    }


    //Deleting
    @Override
    public void onItemRemoved(Integer id) {
        final Integer idItem=id;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/lists/" + idList + "/items/" + id + "&hash=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final ItemDao itemDao;
                        itemDao = RoomProductHuntDb.getDatabase(context).itemDao();
                        itemDao.delete(idItem);
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

                        newItem[0].setIdListe(idList);
                        //Add to the database
                        final ItemDao itemDao;
                        itemDao = RoomProductHuntDb.getDatabase(context).itemDao();
                        //Saving
                        itemDao.add(newItem[0]);

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



    public boolean verifReseau() {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

        String sType = "Aucun réseau détecté";
        Boolean bStatut = false;
        if (netInfo != null) {
            NetworkInfo.State netState = netInfo.getState();
            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                bStatut = true;
                int netType = netInfo.getType();
                switch (netType) {
                    case ConnectivityManager.TYPE_MOBILE:
                        sType = "Réseau mobile détecté";
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        sType = "Réseau wifi détecté";
                        break;
                }
            }
        }

        Log.i("Web", sType);
        return bStatut;
    }
}

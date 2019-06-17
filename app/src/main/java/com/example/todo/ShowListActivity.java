package com.example.todo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.todo.API_models.RetroMain;
import com.example.todo.API_models.TodoInterface;
import com.example.todo.models.ItemToDo;
import com.example.todo.models.ListeToDo;
import com.example.todo.models.ProfilListeToDo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ShowListActivity";

    //Var
    private ArrayList<ItemToDo> mItem = new ArrayList<ItemToDo>();
    private ListeToDo listeToDo = new ListeToDo();
    private int idList;
    private ArrayList<Integer> indices = new ArrayList<>();
    private String hash;
    private TodoInterface service;

    //Widgets
    private EditText edtItemDesc;
    private Button btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        //Getting the data of the intent
        Bundle data = this.getIntent().getExtras();
        assert data != null;
        idList = data.getInt("idList", 9999);
        Log.d(TAG, "idList id : " + idList);

        // Init hash & service.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String baseUrl = settings.getString("APIurl", "http://tomnab.fr/");
        hash = settings.getString("hash", "");
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
        service = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TodoInterface.class);

        //Init variables
        getListe();

        //Init widgets
        edtItemDesc = findViewById(R.id.edtItemDesc);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(this);

        //Init reclyclerView done in getListe() because of the mutlithread.
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddItem:
                Log.d(TAG, "onClick: clicked on : add item");
                String desc = edtItemDesc.getText().toString();
                if (!desc.isEmpty()) {
                    addItem(desc);
                }
        }
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init RecyclerView");
        RecyclerView recyclerView = findViewById(R.id.reclycler_view_item);
        RecyclerViewAdapterItem adapter = new RecyclerViewAdapterItem(this, mItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void refreshRecyclerView(){
        mItem.clear();
        mItem.addAll(listeToDo.getLesItems());
        Log.d(TAG, "refreshRecyclerView: "+mItem.toString());
        RecyclerView recyclerView = findViewById(R.id.reclycler_view_item);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void addItem_API(ItemToDo itemToDo) {
        Call<RetroMain> call = service.addItem(hash, Integer.toString(idList),itemToDo.getDescription(), "noUrlForNow");
        call.enqueue(new Callback<RetroMain>() {
            @Override
            public void onResponse(Call<RetroMain> call, Response<RetroMain> response) {
                if (response.body() != null) {
                    RetroMain retroMain = response.body();
                    if (retroMain.isSuccess()) {
                        Log.d(TAG, "onResponse: success adding a new itemToDo.");
                        RetroMain rItem = retroMain.getItem();
                        indices.add(rItem.getId());
                    } else {
                        Log.d(TAG, "onResponse: http code : "+retroMain.getStatus());
                    }
                } else {
                    Log.d(TAG, "onResponse: empty response. HTTP CODE : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<RetroMain> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }

    /**
     * Delete the item at the position i in the recyclerView in the API.
     * @param i : Integer.
     */
    private void delItem_API(final int i) {
        Call<RetroMain> call = service.delItem(hash, Integer.toString(idList), Integer.toString(indices.get(i)));
        call.enqueue(new Callback<RetroMain>() {
            @Override
            public void onResponse(Call<RetroMain> call, Response<RetroMain> response) {
                if (response.body() != null) {
                    RetroMain retroMain = response.body();
                    if (retroMain.isSuccess()) {
                        Log.d(TAG, "onResponse: success delete the itemToDo.");
                        // Refresh indices list.
                        ArrayList<Integer> tmp = new ArrayList<>();
                        for (int j : indices) if (j!=i) tmp.add(j);
                        indices.clear();
                        indices.addAll(tmp);
                    } else {
                        Log.d(TAG, "onResponse: http code : "+retroMain.getStatus());
                    }
                } else {
                    Log.d(TAG, "onResponse: empty response. HTTP CODE : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<RetroMain> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }

    /**
     * i is the position of the item in the recyclerView.
     * @param i
     */
    private void checkItem_API(int i, int isCheck) {
        Call<RetroMain> call = service.checkItem(hash,
                Integer.toString(idList),
                Integer.toString(indices.get(i)),
                Integer.toString(isCheck));
        call.enqueue(new Callback<RetroMain>() {
            @Override
            public void onResponse(Call<RetroMain> call, Response<RetroMain> response) {
                if (response.body() != null) {
                    RetroMain retroMain = response.body();
                    if (retroMain.isSuccess()) {
                        Log.d(TAG, "onResponse: success check the itemToDo.");
                    } else {
                        Log.d(TAG, "onResponse: http code : "+retroMain.getStatus());
                    }
                } else {
                    Log.d(TAG, "onResponse: empty response. HTTP CODE : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<RetroMain> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }

    /**
     * Get the ListeToDo from the API with the idList of this class and the hash of the session.
     * @return ListeToDo
     */
    private void getListe(){
        Call<RetroMain> call = service.getItems(hash, idList);
        Log.d(TAG, "getListe: "+call.request().toString());
        call.enqueue(new Callback<RetroMain>() {
            @Override
            public void onResponse(Call<RetroMain> call, Response<RetroMain> response) {
                if (response.body() != null) {
                    RetroMain retroMain = response.body();
                    if (retroMain.isSuccess()) {
                        ArrayList<RetroMain> rItems = retroMain.getItems();
                        for (RetroMain item : rItems){
                            listeToDo.ajouteItem(new ItemToDo(item.getLabel(), (item.getChecked()==1)));
                            indices.add(item.getId());
                        }
                        initRecyclerView();
                        refreshRecyclerView();
                    } else {
                        Log.d(TAG, "onResponse: http code : "+retroMain.getStatus());
                    }
                } else {
                    Log.d(TAG, "onResponse: empty response. HTTP CODE : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<RetroMain> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }

    private void addItem(String desc){
        ItemToDo item = new ItemToDo(desc);
        listeToDo.ajouteItem(item);
        addItem_API(item);
        Log.d(TAG, "addItem: item ajouté : "+item.toString());
        refreshRecyclerView();
    }

    /**
     * Delete the ItemToDo at the position i in the recyclerView.
     * @param i : Integer
     */
    public void delItem(int i){
        // Delete the item in the API.
        delItem_API(i);
        // Delete the item in the view.
        listeToDo.getLesItems().remove(i);
        refreshRecyclerView();
    }

    /**
     * Check the item if it is not checked, or uncheck the item otherwise.
     * Then, it refresh the recyclerview. i is the position of the item in the recyclerView.
     * @param i
     */
    public void checkItem(int i, boolean isCheck){
        ItemToDo item = listeToDo.getLesItems().get(i);
        item.setFait(isCheck);
        checkItem_API(i, (isCheck ? 1 : 0));
        refreshRecyclerView();
    }

}

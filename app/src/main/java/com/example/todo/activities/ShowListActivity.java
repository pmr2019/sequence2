package com.example.todo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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
import com.example.todo.R;
import com.example.todo.models.DataProvider;
import com.example.todo.models.InternetCheck;
import com.example.todo.models.ItemToDo;
import com.example.todo.models.ListeToDo;
import com.example.todo.ui.RecyclerViewAdapterItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowListActivity extends AppCompatActivity implements View.OnClickListener, InternetCheck.Consumer {
    private static final String TAG = "ShowListActivity";

    //Var
    private ArrayList<ItemToDo> mItem = new ArrayList<ItemToDo>();
    private ListeToDo listeToDo = new ListeToDo();
    private int idList;
    private String pseudo;

    //Data
    DataProvider dataProvider;

    //Widgets
    private EditText edtItemDesc;
    private Button btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        //Init widgets
        edtItemDesc = findViewById(R.id.edtItemDesc);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(this);

        //Getting the data of the intent
        Bundle data = this.getIntent().getExtras();
        assert data != null;
        idList = data.getInt("idList", 9999);
        pseudo = data.getString("pseudo", "");

        Log.d(TAG, "idList id : " + idList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Init variables
//        getListe();
        dataProvider = new DataProvider(this);
        new InternetCheck(this);

        //Init reclyclerView
        initRecyclerView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddItem:
                Log.d(TAG, "onClick: clicked on : add item");
                String desc = edtItemDesc.getText().toString();
                edtItemDesc.setText("");
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

    private void loadListeToDo(){
        dataProvider.getListeToDo(pseudo, idList, new DataProvider.PostsListener() {
            @Override
            public void onSuccess(DataProvider.DataResponse dataResponse) {
                listeToDo = dataResponse.getListeToDo();
                refreshRecyclerView();
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "onErrorFromDataProvider: "+error);
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowListActivity.this);
                builder.setTitle("Error");
                builder.setMessage("Error unknown.");
                // add the buttons
                builder.setNeutralButton("Ok", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void addItem(String desc){
        ItemToDo item = new ItemToDo(desc);
        listeToDo.ajouteItem(item);
        dataProvider.addItemToDo(this, item);
        Log.d(TAG, "addItem: item ajouté : "+item.toString());
        refreshRecyclerView();
    }

    /**
     * Delete the ItemToDo at the position i in the recyclerView.
     * @param i : Integer
     */
    public void delItem(ItemToDo itemToDo, int pos){
        // Delete the item in the API.
        dataProvider.delItemToDo(this, itemToDo);
        // Delete the item in the view.
        listeToDo.getLesItems().remove(pos);
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
        dataProvider.updateItemToDo(this, item);
        refreshRecyclerView();
    }

    @Override
    public void isConnectedToInternet(Boolean internet) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isConnectedToInternet = settings.getBoolean("isConnectedToInternet",false);
        if (isConnectedToInternet == false && internet == true){
            Log.d(TAG, "isConnectedToInternet: connexion internet detected.");
            // Modif offline will erase the online, only for the lists concerned.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connexion internet détectée");
            builder.setMessage("Voulez-vous vous connecter ?");
            // add the buttons
            builder.setPositiveButton("Oui", (dialog, which) -> {
                SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(ShowListActivity.this);
                SharedPreferences.Editor editor = settings1.edit();
                editor.putBoolean("isConnectedToInternet", internet);
                editor.commit();
                Intent toSecondAct = new Intent(ShowListActivity.this, MainActivity.class);
                startActivity(toSecondAct);
            });
            builder.setNegativeButton("Non", (dialog, which) -> loadListeToDo());
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if ((isConnectedToInternet == true && internet == true) || (isConnectedToInternet == false && internet == false)) {
            loadListeToDo();
        }
        else if (isConnectedToInternet == true && internet == false) {
            // Ask if the user wants to modify offline (as in the MainActivity).
            Log.d(TAG, "isConnectedToInternet: Connexion internet lost.");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connexion perdue");
            builder.setMessage("Voulez-vous manipuler les données en cache ?");
            // add the buttons
            builder.setPositiveButton("Oui", (dialog, which) -> loadListeToDo());
            builder.setNegativeButton("Non", (dialog, which) -> {
                SharedPreferences settings12 = PreferenceManager.getDefaultSharedPreferences(ShowListActivity.this);
                SharedPreferences.Editor editor = settings12.edit();
                editor.putBoolean("isConnectedToInternet", internet);
                editor.commit();
                Intent toSecondAct = new Intent(ShowListActivity.this, MainActivity.class);
                startActivity(toSecondAct);
            });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}

package com.example.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.todolist.adapter.ItemsAdapter;
import com.example.todolist.data.API.GetDataService;
import com.example.todolist.data.API.ResponseBasic;
import com.example.todolist.data.API.ResponseItem;
import com.example.todolist.data.API.ResponseItems;
import com.example.todolist.data.API.RetrofitClient;
import com.example.todolist.data.Convert;
import com.example.todolist.data.Item;
import com.example.todolist.data.database.AppDatabase;
import com.example.todolist.data.database.DatabaseItem;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsActivity extends GenericActivity implements ItemsAdapter.ActionListener {

    private EditText mNewItemTE;
    private ItemsAdapter mItemsAdapter;
    private long idList;
    private List<Item> mListItem = new ArrayList<>();
    private AppDatabase mDb;

    // ------------------------------------------------------------------------------------------ //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        mNewItemTE = findViewById(R.id.newItemTE);

        Intent intent = getIntent();
        idList = intent.getLongExtra("list", 0);

        mItemsAdapter = new ItemsAdapter(mListItem, this);

        // récupération des items de la to-do liste
        getItems();

        // création d'un recycler view pour afficher les items
        RecyclerView recyclerView = findViewById(R.id.showListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(mItemsAdapter);
    }



    // ------------------------------------------------------------------------------------------ //
    public void getItems() {
        if(connectedToNetwork()) {
            GetDataService service = RetrofitClient.createService(mApiUrl, GetDataService.class);
            Call<ResponseItems> call = service.getItems(idList, mHash);

            call.enqueue(new Callback<ResponseItems>() {
                @Override
                public void onResponse(Call<ResponseItems> call, final Response<ResponseItems> response) {
                    if (response.isSuccessful() && response.body().success) {
                        mListItem.addAll(response.body().items);
                        mItemsAdapter.notifyDataSetChanged();

                        // on enregistre les données en local
                        new Thread() {
                            @Override
                            public void run() {
                                mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db").fallbackToDestructiveMigration().build();
                                Convert c = new Convert();
                                mDb.ItemDAO().insertItems(c.itemList(response.body(), idList));
                            }
                        }.start();

                    } else {

                        Toast.makeText(getBaseContext(), String.valueOf(idList), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseItems> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Un problème a été rencontré...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {

            final Handler mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    Toast.makeText(getBaseContext(), "Les données attendues n'ont pas pu être récupérées", Toast.LENGTH_SHORT).show();
                }
            };

            new Thread() {
                @Override
                public void run() {
                    mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db").fallbackToDestructiveMigration().build();
                    List<DatabaseItem> databaseItems = mDb.ItemDAO().getItems(idList);

                    if (databaseItems != null) {
                        Convert c = new Convert();
                        mListItem.addAll(c.itemListDB(databaseItems));
                        mItemsAdapter.notifyDataSetChanged();
                    } else {
                        Message message = mHandler.obtainMessage();
                        message.sendToTarget();
                    }
                }
            }.start();

        }
    }

    // ------------------------------------------------------------------------------------------ //
    public void addItem(View view) {
        String itemDescription = mNewItemTE.getText().toString();

        if (itemDescription.matches("")) {
            // en cas de description vide
            Toast.makeText(this, "Veuillez ajouter une description à votre item", Toast.LENGTH_SHORT).show();
        }
        else {
            mNewItemTE.setText("");

            GetDataService service = RetrofitClient.createService(mApiUrl, GetDataService.class);
            Call<ResponseItem> call = service.postItem(idList, mHash, itemDescription);

            call.enqueue(new Callback<ResponseItem>() {
                @Override
                public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                    if(response.isSuccessful() && response.body().success) {
                        Item item = response.body().item;
                        mListItem.add(item);
                        mItemsAdapter.notifyItemInserted(mListItem.size() - 1);
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Les données attendues n'ont pas été reçues", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseItem> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Un problème a été rencontré...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public void onCheckedClicked(final int position, final boolean isChecked) {

        int i = 0;
        if(isChecked) {
            i = 1;
        }
        // petite astuce de filou
        final int a = i;

        if(connectedToNetwork()) {
            GetDataService service = RetrofitClient.createService(mApiUrl, GetDataService.class);
            Call<ResponseItem> call = service.putCheck(idList, mListItem.get(position).getId(), mHash, i);

            call.enqueue(new Callback<ResponseItem>() {
                @Override
                public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {

                }

                @Override
                public void onFailure(Call<ResponseItem> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Un problème a été rencontré...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            new Thread() {
                @Override
                public void run() {
                    mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db").fallbackToDestructiveMigration().build();
                    mDb.ItemDAO().updateStatus(a, mListItem.get(position).getId());
                }
            }.start();

        }
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public void onDeleteClicked(final int position) {

        // création d'une fenêtre de dialogue pour confirmer la suppression d'un item
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Suppression d'un item");
        builder.setMessage("Êtes-vous sûr de supprimer cet item ?");
        builder.setNegativeButton("Non", null);
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                GetDataService service = RetrofitClient.createService(mApiUrl, GetDataService.class);
                Call<ResponseBasic> call = service.deleteItem(idList, mListItem.get(position).getId(), mHash);

                call.enqueue(new Callback<ResponseBasic>() {
                    @Override
                    public void onResponse(Call<ResponseBasic> call, Response<ResponseBasic> response) {
                        if(response.isSuccessful() && response.body().success) {
                            mListItem.remove(mListItem.get(position));
                            mItemsAdapter.notifyItemRemoved(position);
                        }
                        else {
                            Toast.makeText(getBaseContext(), "Les données attendues n'ont pas été reçues", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBasic> call, Throwable t) {
                        Toast.makeText(getBaseContext(), "Un problème a été rencontré...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.show();
    }
}

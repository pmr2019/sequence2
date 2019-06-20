package com.example.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.todolist.adapter.ItemsAdapter;
import com.example.todolist.data.API.GetDataService;
import com.example.todolist.data.API.ResponseBasic;
import com.example.todolist.data.API.ResponseItem;
import com.example.todolist.data.API.ResponseItems;
import com.example.todolist.data.API.ResponseTodoList;
import com.example.todolist.data.API.ResponseTodoLists;
import com.example.todolist.data.API.RetrofitClient;
import com.example.todolist.data.Item;
import com.example.todolist.data.TodoList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private int idList;
    private List<Item> mListItem = new ArrayList<>();

    // ------------------------------------------------------------------------------------------ //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        mNewItemTE = findViewById(R.id.newItemTE);

        Intent intent = getIntent();
        idList = intent.getIntExtra("list", 0);

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
        GetDataService service = RetrofitClient.createService(mApiUrl, GetDataService.class);
        Call<ResponseItems> call = service.getItems(idList, mHash);

        call.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                if(response.isSuccessful() && response.body().success) {
                    mListItem.addAll(response.body().items);
                    mItemsAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getBaseContext(), "Les données attendues n'ont pas été reçues", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Un problème a été rencontré...", Toast.LENGTH_SHORT).show();
            }
        });
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

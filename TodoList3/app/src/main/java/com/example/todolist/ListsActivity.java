package com.example.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.todolist.adapter.ListsAdapter;
import com.example.todolist.data.API.GetDataService;
import com.example.todolist.data.API.ResponseBasic;
import com.example.todolist.data.API.ResponseTodoList;
import com.example.todolist.data.API.ResponseTodoLists;
import com.example.todolist.data.API.RetrofitClient;
import com.example.todolist.data.Convert;
import com.example.todolist.data.TodoList;
import com.example.todolist.data.database.AppDatabase;
import com.example.todolist.data.database.DatabaseTodoList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListsActivity extends GenericActivity implements ListsAdapter.ActionListener {

    private EditText mNewListTE;
    private ListsAdapter mListsAdapter;
    private List<TodoList> mListTodoList = new ArrayList<>();
    private AppDatabase mDb;
    
    // ------------------------------------------------------------------------------------------ //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        mNewListTE = findViewById(R.id.newListTE);

        mListsAdapter = new ListsAdapter(mListTodoList, this);

        // récupération des listes de l'utilisateur
        getLists();

        // création d'un recycler view pour afficher les listes
        RecyclerView recyclerView = findViewById(R.id.choiceListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(mListsAdapter);
    }

    // ------------------------------------------------------------------------------------------ //
    public void getLists() {

        if(connectedToNetwork()) {
            GetDataService service = RetrofitClient.createService(mApiUrl, GetDataService.class);
            Call<ResponseTodoLists> call = service.getTodoLists(mHash);

            call.enqueue(new Callback<ResponseTodoLists>() {
                @Override
                public void onResponse(Call<ResponseTodoLists> call, final Response<ResponseTodoLists> response) {
                    if (response.isSuccessful() && response.body().success) {
                        mListTodoList.addAll(response.body().lists);
                        mListsAdapter.notifyDataSetChanged();

                        // on enregistre les données en local
                        new Thread() {
                            @Override
                            public void run() {
                                mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db").fallbackToDestructiveMigration().build();
                                Convert c = new Convert();
                                mDb.TodoListDAO().insertTodoLists(c.todoListList(response.body(), mIdUser));
                            }
                        }.start();

                    } else {
                        Toast.makeText(getBaseContext(), "Les données attendues n'ont pas été reçues", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseTodoLists> call, Throwable t) {
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
                    List<DatabaseTodoList> databaseTodoList = mDb.TodoListDAO().getTodoLists(mIdUser);

                    if (databaseTodoList != null) {
                        Convert c = new Convert();
                        mListTodoList.addAll(c.todoListListDB(databaseTodoList));
                        mListsAdapter.notifyDataSetChanged();
                    } else {
                        Message message = mHandler.obtainMessage();
                        message.sendToTarget();
                    }
                }
            }.start();


        }
    }

    // ------------------------------------------------------------------------------------------ //
    public void addList(View view) {
        String listTitle = mNewListTE.getText().toString();

        if (listTitle.matches("")) {
            // en cas de titre vide
            Toast.makeText(this, "Veuillez ajouter un nom à votre todo liste", Toast.LENGTH_SHORT).show();
        }
        else {
            // ajout de la liste
            GetDataService service = RetrofitClient.createService(mApiUrl, GetDataService.class);
            Call<ResponseTodoList> call = service.postTodoList(mHash, listTitle);

            call.enqueue(new Callback<ResponseTodoList>() {
                @Override
                public void onResponse(Call<ResponseTodoList> call, Response<ResponseTodoList> response) {
                    if(response.isSuccessful() && response.body().success) {
                        TodoList todoList = response.body().list;
                        toItems(todoList.getId());
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Les données attendues n'ont pas été reçues", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseTodoList> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Un problème a été rencontré...", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public void onItemClicked(long idList) {
        toItems(idList);
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public void onDeleteClicked(final int position) {

        // création d'une fenêtre de dialogue pour confirmer la suppression d'une to-do liste
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        builder.setTitle("Suppression d'une todo liste");
        builder.setMessage("Êtes-vous sûr de supprimer cette todo liste ?");
        builder.setNegativeButton("Non", null);
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                GetDataService service = RetrofitClient.createService(mApiUrl, GetDataService.class);
                Call<ResponseBasic> call = service.deleteTodoList(mListTodoList.get(position).getId(), mHash);

                call.enqueue(new Callback<ResponseBasic>() {
                    @Override
                    public void onResponse(Call<ResponseBasic> call, Response<ResponseBasic> response) {
                        if(response.isSuccessful() && response.body().success) {
                            mListTodoList.remove(mListTodoList.get(position));
                            mListsAdapter.notifyItemRemoved(position);
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

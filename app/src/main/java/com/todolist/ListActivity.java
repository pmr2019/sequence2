package com.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.todolist.MyRetrofit.Lists;
import com.todolist.MyRetrofit.NewListInfo;
import com.todolist.MyRetrofit.TodoListService;
import com.todolist.MyRetrofit.TodoListServiceFactory;
import com.todolist.MyRetrofit.Users;
import com.todolist.MyTouchHelper.ItemTouchHelperAdapter;
import com.todolist.MyTouchHelper.ItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
    EditText edt_list;
    Button btn_list;
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    String hash;
    String url;
    String pseudo;
    String userId;
    TodoListService todoListService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        edt_list = findViewById(R.id.edt_list);
        btn_list = findViewById(R.id.btn_list);

        hash = getIntent().getStringExtra("hash");
        url = getIntent().getStringExtra("url");
        pseudo = getIntent().getStringExtra("pseudo");

        todoListService = TodoListServiceFactory.createService(
                url, TodoListService.class);

        recyclerView = findViewById(R.id.list_activity);
        listAdapter = new ListAdapter(new ArrayList<DataEntity>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(listAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerViewConfig(hash);
        getCurrentUserId(hash, pseudo);
    }

    class DataEntity {
        private String listName;
        private String id;

        DataEntity(String listName, String id) {
            this.listName = listName;
            this.id = id;
        }

        String getListName() {
            return listName;
        }

        String getListId() {
            return id;
        }
    }

    private void recyclerViewConfig(String hash) {
        Call<Lists> call = todoListService.getLists(hash);

        call.enqueue(new Callback<Lists>() {
            @Override
            public void onResponse(Call<Lists> call, Response<Lists> response) {
                if (response.isSuccessful()) {
                    for (Lists.ListsBean l : response.body().getLists()) {
                        listAdapter.addData(new DataEntity(l.getLabel(), l.getId()));
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(ListActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentUserId(String hash, final String pseudo) {
        Call<Users> call = todoListService.getUsers(hash);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    userId = response.body().getUserId(pseudo);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    // Function bind to the "OK" button as onClick event for a new list
    public void newList(View v) {
        final String list_name = edt_list.getText().toString();
        if (list_name.isEmpty()){
            Toast.makeText(this, "Please enter list name", Toast.LENGTH_SHORT).show();
        } else {
            Call<NewListInfo> call = todoListService.addList(hash, userId, list_name);

            call.enqueue(new Callback<NewListInfo>() {
                @Override
                public void onResponse(Call<NewListInfo> call, Response<NewListInfo> response) {
                    if (response.isSuccessful()) {
                        listAdapter.addData(new DataEntity(list_name, response.body().getList().getId()));
                        edt_list.setText("");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
        }
    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> implements ItemTouchHelperAdapter {
        private final List<DataEntity> lists;
        ListAdapter(List<DataEntity> lists) {
            this.lists = lists;
        }

        void addData(DataEntity list) {
            lists.add(list);
            notifyItemInserted(lists.size());
        }

        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.list,parent,false);
            return new ListAdapter.ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            String data = lists.get(position).getListName();

            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return lists == null? 0 : lists.size();
        }

        // Function declared in the interface "ItemTouchHelperAdapter"
        // Triggered when item swiped to left
        @Override
        public void onItemDissmiss(int position) {
            String list_selected = lists.get(position).getListId();
            Call<ResponseBody> call = todoListService.deleteList(hash, userId, list_selected);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call call, Response response) {

                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });

            lists.remove(position);
            notifyItemRemoved(position);
        }

        // Function declared in the interface "ItemTouchHelperAdapter"
        // Triggered when item dragged to other position
        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            DataEntity tmp = lists.get(fromPosition);
            lists.remove(fromPosition);
            lists.add(toPosition > fromPosition ? toPosition - 1 : toPosition, tmp);
            notifyItemMoved(fromPosition,toPosition);
        }

        class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final TextView textView;

            ListViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.list_name);

                itemView.setOnClickListener(this);
            }

            void bind(String data) {
                textView.setText(data);
            }

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    String list_selected = lists.get(getAdapterPosition()).getListId();
                    Intent i = new Intent(ListActivity.this, ItemActivity.class);
                    i.putExtra("hash", hash);
                    i.putExtra("url", url);
                    i.putExtra("listId", list_selected);
                    startActivity(i);
                }
            }
        }
    }
}

package com.todolist;

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

import com.todolist.MyRetrofit.Label;
import com.todolist.MyRetrofit.Lists;
import com.todolist.MyRetrofit.TodoListService;
import com.todolist.MyRetrofit.TodoListServiceFactory;
import com.todolist.MyTouchHelper.ItemTouchHelperAdapter;
import com.todolist.MyTouchHelper.ItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
    EditText edt_list;
    Button btn_list;
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    String hash;
    int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        edt_list = findViewById(R.id.edt_list);
        btn_list = findViewById(R.id.btn_list);

        hash = getIntent().getStringExtra("hash");

        recyclerViewConfig(hash);

        userId = getCurrentUserId(getIntent().getStringExtra("pseudo"));
    }

    private void recyclerViewConfig(String hash) {
        TodoListService todoListService = TodoListServiceFactory.createService(
                        getIntent().getStringExtra("url"),
                        TodoListService.class);

        Call call = todoListService.getLists(hash);

        call.enqueue(new Callback<Lists>() {
            @Override
            public void onResponse(Call<Lists> call, Response<Lists> response) {
                recyclerView = findViewById(R.id.list_activity);
                listAdapter = new ListAdapter(getLabels(response.body()));

                recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this, RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(listAdapter);

                ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(listAdapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(recyclerView);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(ListActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<String> getLabels(Lists lists) {
        List<String> labels = new ArrayList<>();

        for (Label l : lists.labels) {
            labels.add(l.label);
        }
        return labels;
    }

    private int getCurrentUserId(String pseudo) {
        int id = 0;

        //TODO get user ID by searching pseudo

        return id;
    }

    // Function bind to the "OK" button as onClick event for a new list
    public void newList(View v) {
        String list_name = edt_list.getText().toString();
        if (list_name.isEmpty()){
            Toast.makeText(this, "Please enter list name", Toast.LENGTH_SHORT).show();
        } else {
//            profile.addList(new TodoList(list_name));
//            saveProfilData(profile, profile.getLogin());
//            listAdapter.addData(list_name);
//            edt_list.setText("");
        }
    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> implements ItemTouchHelperAdapter {
        private final List<String> lists;
        ListAdapter(List<String> lists) {
            this.lists = lists;
        }

        void addData(String list) {
            lists.add(list);
            notifyItemInserted(lists.size());
        }

        void removeData(int position) {
            lists.remove(position);
            notifyItemRemoved(position);
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
            String data = lists.get(position);

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
//            profile.removeList(position);
//            removeData(position);
//            saveProfilData(profile, profile.getLogin());
        }

        // Function declared in the interface "ItemTouchHelperAdapter"
        // Triggered when item dragged to other position
        @Override
        public void onItemMove(int fromPosition, int toPosition) {
//            String tmp = labels.get(fromPosition);
//            labels.remove(fromPosition);
//            labels.add(toPosition > fromPosition ? toPosition - 1 : toPosition, tmp);
//            profile.swapList(fromPosition, toPosition);
//            saveProfilData(profile, profile.getLogin());
//            notifyItemMoved(fromPosition,toPosition);
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
//                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
//                    Intent i = new Intent(ListActivity.this, ItemActivity.class);
//                    i.putExtra("profile", profile.getLogin());
//                    i.putExtra("list", labels.get(getAdapterPosition()));
//                    startActivity(i);
//                }
            }
        }
    }
}

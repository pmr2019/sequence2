package com.todolist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.todolist.DataClass.Item;
import com.todolist.DataClass.Profile;
import com.todolist.MyRetrofit.Items;
import com.todolist.MyRetrofit.Lists;
import com.todolist.MyRetrofit.NewItemInfo;
import com.todolist.MyRetrofit.TodoListService;
import com.todolist.MyRetrofit.TodoListServiceFactory;
import com.todolist.MyTouchHelper.ItemTouchHelperAdapter;
import com.todolist.MyTouchHelper.ItemTouchHelperCallback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemActivity extends AppCompatActivity {
    EditText edt_item;
    Button btn_item;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    String hash;
    String url;
    String listId;
    TodoListService todoListService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        edt_item = findViewById(R.id.edt_item);
        btn_item = findViewById(R.id.btn_item);

        hash = getIntent().getStringExtra("hash");
        url = getIntent().getStringExtra("url");
        listId = getIntent().getStringExtra("listId");

        todoListService = TodoListServiceFactory.createService(
                url, TodoListService.class);

        recyclerView = findViewById(R.id.item_activity);
        itemAdapter = new ItemAdapter(new ArrayList<DataEntity>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(itemAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerViewConfig(hash, listId);
    }

    private void recyclerViewConfig(String hash, String listId) {
        Call call = todoListService.getItems(hash, listId);

        call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                if (response.isSuccessful()) {
                    for (Items.ItemsBean i: response.body().getItems()) {
                        itemAdapter.addData(new DataEntity(i.getLabel(), i.getChecked()));
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    // Class contains item name and check status
    class DataEntity {
        private String itemName;
        private Boolean checked;

        DataEntity(String itemName, String checked) {
            this.itemName = itemName;
            if (checked.equals("1")) {
                this.checked = true;
            } else {
                this.checked = false;
            }
        }

        String getItemName() {
            return itemName;
        }

        Boolean getChecked() {
            return checked;
        }
    }

    // Function bind to the "OK" button as onClick event for a new item
    public void newItem(View v) {
        final String item_name = edt_item.getText().toString();
        if (item_name.isEmpty()) {
            Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
        } else {
            Call call = todoListService.addItem(hash, listId, item_name);

            call.enqueue(new Callback<NewItemInfo>() {
                @Override
                public void onResponse(Call<NewItemInfo> call, Response<NewItemInfo> response) {
                    if (response.isSuccessful()) {
                        itemAdapter.addData(new DataEntity(item_name, "0"));
                        edt_item.setText("");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {
        private final List<DataEntity> data;
        ItemAdapter(List<DataEntity> data) {
            this.data = data;
        }

        void addData(DataEntity dataEntity) {
            data.add(dataEntity);
            notifyItemInserted(data.size());
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            String data1 = data.get(position).getItemName();
            boolean data2 = data.get(position).getChecked();

            holder.bind(data1, data2);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        // Function declared in the interface "ItemTouchHelperAdapter"
        // Triggered when item swiped to left
        @Override
        public void onItemDissmiss(int position) {
//            data.remove(position);
//            profile.removeItem(list_name_selected, position);
//            notifyItemRemoved(position);
//            saveProfilData(profile, profile.getLogin());
        }

        // Function declared in the interface "ItemTouchHelperAdapter"
        // Triggered when item dragged to other position
        @Override
        public void onItemMove(int fromPosition, int toPosition) {
//            DataEntity tmp = data.get(fromPosition);
//            data.remove(fromPosition);
//            data.add(toPosition > fromPosition ? toPosition - 1 : toPosition, tmp);
//            profile.swapItem(fromPosition, toPosition, list_name_selected);
//            saveProfilData(profile, profile.getLogin());
//            notifyItemMoved(fromPosition,toPosition);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
            private final CheckBox checkBox;

            ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.item_name);
            }

            void bind(String data1, boolean data2) {
                checkBox.setText(data1);
                checkBox.setChecked(data2);
                checkBox.setOnCheckedChangeListener(this);
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                profile.getListByName(list_name_selected).setItemStatus(data.get(getAdapterPosition()).getItemName(), isChecked);
//                saveProfilData(profile, profile.getLogin());
            }
        }
    }
}

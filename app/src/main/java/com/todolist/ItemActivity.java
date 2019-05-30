package com.todolist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.todolist.TouchHelper.ItemTouchHelperAdapter;
import com.todolist.TouchHelper.ItemTouchHelperCallback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity {
    private Profile profile;
    String list_name_selected;
    EditText edt_item;
    Button btn_item;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    List<String> data_item;
    List<Boolean> checked_item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        edt_item = findViewById(R.id.edt_item);
        btn_item = findViewById(R.id.btn_item);

        profile = readProfilData(getIntent().getStringExtra("profile"));
        list_name_selected = getIntent().getStringExtra("list");

        recyclerView = findViewById(R.id.item_activity);
        data_item = data_item(profile, list_name_selected);
        checked_item = checked_item(profile, list_name_selected);
        itemAdapter = new ItemAdapter(data_item, checked_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(itemAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private List<String> data_item(Profile profile, String list_name_selected) {
        List<String> data = new ArrayList<>();
        List<Item> items = profile.getListByName(list_name_selected).getLesItems();
        for (Item i : items) {
            data.add(i.getDescription());
        }
        return data;
    }

    private List<Boolean> checked_item(Profile profile, String list_name_selected) {
        List<Item> items = profile.getListByName(list_name_selected).getLesItems();
        List<Boolean> data = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            data.add(items.get(i).getFait());
        }
        return data;
    }

    public void newItem(View v) {
        String item_name = edt_item.getText().toString();
        if (item_name.isEmpty()) {
            Toast.makeText(this, "Please enter item name", Toast.LENGTH_LONG).show();
        } else {
            profile.addItem(list_name_selected, item_name);
//            recyclerView.setAdapter(new ItemAdapter(data_item(profile, list_name_selected), checked_item(profile, list_name_selected)));
            edt_item.setText("");
            saveProfilData(profile, profile.getLogin());
            data_item.add(item_name);
            checked_item.add(false);
            itemAdapter.notifyItemInserted(data_item.size());
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {
        private final List<String> items;
        private final List<Boolean> checked;
        ItemAdapter(List<String> items, List<Boolean> checked) {
            this.items = items;
            this.checked = checked;
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
            String data1 = items.get(position);
            boolean data2 = checked.get(position);

            holder.bind(data1, data2);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onItemDissmiss(int position) {
            items.remove(position);
            profile.removeItem(list_name_selected, position);
            notifyItemRemoved(position);
            saveProfilData(profile, profile.getLogin());
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            String tmp = items.get(fromPosition);
            items.remove(fromPosition);
            items.add(toPosition > fromPosition ? toPosition - 1 : toPosition, tmp);
            profile.swapItem(fromPosition, toPosition, list_name_selected);
            saveProfilData(profile, profile.getLogin());
            notifyItemMoved(fromPosition,toPosition);
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
                profile.getListByName(list_name_selected).setItemStatus(items.get(getAdapterPosition()), isChecked);
                saveProfilData(profile, profile.getLogin());
            }
        }
    }

    public void saveProfilData(Profile profile, String pseudo) {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        String fileContents = gson.toJson(profile);
        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = openFileOutput(pseudo, Context.MODE_PRIVATE);
            fileOutputStream.write(fileContents.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Profile readProfilData(String filename) {
        StringBuilder jsonRead = new StringBuilder();
        Profile profile;
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        try {
            FileInputStream inputStream;
            inputStream = openFileInput(filename);
            int content;
            while ((content = inputStream.read()) != -1) {
                jsonRead.append((char) content);
            }
            inputStream.close();

            profile = gson.fromJson(jsonRead.toString(), Profile.class); // cast Profile

            return profile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Profile();
    }
}

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
import com.todolist.MyTouchHelper.ItemTouchHelperAdapter;
import com.todolist.MyTouchHelper.ItemTouchHelperCallback;

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
    List<DataEntity> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        edt_item = findViewById(R.id.edt_item);
        btn_item = findViewById(R.id.btn_item);

        profile = readProfilData(getIntent().getStringExtra("profile"));
        list_name_selected = getIntent().getStringExtra("list");

        recyclerView = findViewById(R.id.item_activity);
        data = data(profile, list_name_selected);
        itemAdapter = new ItemAdapter(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(itemAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    // Class contains item name and check status
    class DataEntity {
        private String itemName;
        private Boolean checked;

        DataEntity(String itemName, Boolean checked) {
            this.itemName = itemName;
            this.checked = checked;
        }

        String getItemName() {
            return itemName;
        }

        Boolean getChecked() {
            return checked;
        }
    }

    // Return a list of DataEntity in a profile.list class
    // to dispaly in the Recycler view
    private List<DataEntity> data(Profile profile, String list_name_selected) {
        List<DataEntity> data = new ArrayList<>();
        List<Item> items = profile.getListByName(list_name_selected).getLesItems();
        for (Item i : items) {
            data.add(new DataEntity(i.getDescription(), i.getFait()));
        }
        return data;
    }

    // Function bind to the "OK" button as onClick event for a new item
    public void newItem(View v) {
        String item_name = edt_item.getText().toString();
        if (item_name.isEmpty()) {
            Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
        } else {
            profile.addItem(list_name_selected, item_name);
//            recyclerView.setAdapter(new ItemAdapter(data_item(profile, list_name_selected), checked_item(profile, list_name_selected)));
            edt_item.setText("");
            saveProfilData(profile, profile.getLogin());
//            data_item.add(item_name);
//            checked_item.add(false);
            data.add(new DataEntity(item_name, false));
            itemAdapter.notifyItemInserted(data.size());
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {
        private final List<DataEntity> data;
        ItemAdapter(List<DataEntity> data) {
            this.data = data;
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
            data.remove(position);
            profile.removeItem(list_name_selected, position);
            notifyItemRemoved(position);
            saveProfilData(profile, profile.getLogin());
        }

        // Function declared in the interface "ItemTouchHelperAdapter"
        // Triggered when item dragged to other position
        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            DataEntity tmp = data.get(fromPosition);
            data.remove(fromPosition);
            data.add(toPosition > fromPosition ? toPosition - 1 : toPosition, tmp);
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
                profile.getListByName(list_name_selected).setItemStatus(data.get(getAdapterPosition()).getItemName(), isChecked);
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

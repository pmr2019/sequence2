package com.todolist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemActivity extends AppCompatActivity {
    private Profile profile;
    String list_name_selected;
    EditText edt_item;
    Button btn_item;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        edt_item = findViewById(R.id.edt_item);
        btn_item = findViewById(R.id.btn_item);

        profile = (Profile) getIntent().getSerializableExtra("profile");
        list_name_selected = getIntent().getStringExtra("list");

        recyclerView = findViewById(R.id.item_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ItemAdapter(data_item(profile, list_name_selected)));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    private List<String> data_item(Profile profile, String list_name_selected) {
        List<String> data = new ArrayList<>();
        List<Item> items = profile.getListByName(list_name_selected).getLesItems();
        for (int i = 0; i < items.size(); i++) {
            data.add(items.get(i).getDescription());
        }
        return data;
    }

    public void newItem(View v) {
        String item_name = edt_item.getText().toString();
        if (item_name.isEmpty()) {
            Toast toast = new Toast(this);
            toast.makeText(this, "Please enter item name", Toast.LENGTH_LONG).show();
        } else {
            profile.addItem(list_name_selected, item_name);
            recyclerView.setAdapter(new ItemAdapter(data_item(profile, list_name_selected)));
            edt_item.setText("");
            saveProfilData(profile, profile.getLogin());
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
        private final List<String> items;
        public ItemAdapter(List<String> items) {this.items = items;}

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            String data = items.get(position);

            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final CheckBox checkBox;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.item_name);
            }

            public void bind(String data) {
                checkBox.setText(data);
            }

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    //TODO save checkbox checked
                    Toast t = new Toast(ItemActivity.this);
                    t.makeText(ItemActivity.this, "Item " + items.get(getAdapterPosition()), Toast.LENGTH_LONG);
                }
            }
        }
    }

    public void saveProfilData(Profile profile, String pseudo) {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        String filename = pseudo;
        String fileContents = gson.toJson(profile);
        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            fileOutputStream.write(fileContents.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

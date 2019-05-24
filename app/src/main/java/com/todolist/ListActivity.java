package com.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private Profile profile;
    EditText edt_list;
    Button btn_list;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        edt_list = findViewById(R.id.edt_list);
        btn_list = findViewById(R.id.btn_list);

        profile = readProfilData(getIntent().getStringExtra("profile"));

        recyclerView = findViewById(R.id.list_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListAdapter(data_list(profile)));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    @Override
    protected void onStart() {
        super.onStart();
        profile = readProfilData(getIntent().getStringExtra("profile"));
        recyclerView.setAdapter(new ListAdapter(data_list(profile)));
    }

    private List<String> data_list(Profile profile) {
        List<String> data = new ArrayList<>();
        TodoList tmp;
        for (TodoList list : profile.getListe()) {
            tmp = list;
            data.add(tmp.getTitreListeToDo());
        }
        return data;
    }

    public void newList(View v) {
        String list_name = edt_list.getText().toString();
        if (list_name.isEmpty()){
            Toast.makeText(this, "Please enter list name", Toast.LENGTH_LONG).show();
        } else {
            profile.addList(new TodoList(list_name));
            recyclerView.setAdapter(new ListAdapter(data_list(profile)));
            edt_list.setText("");
            saveProfilData(profile, profile.getLogin());
        }
    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{
        private final List<String> lists;
        ListAdapter(List<String> lists) {
            this.lists = lists;
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
            return lists.size();
        }

        class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
                    Intent i = new Intent(ListActivity.this, ItemActivity.class);
                    i.putExtra("profile", profile.getLogin());
                    i.putExtra("list", lists.get(getAdapterPosition()));
                    startActivity(i);
                }
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

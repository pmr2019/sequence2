package com.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.todolist.DataClass.Setting;
import com.todolist.DataClass.User;
import com.todolist.MyTouchHelper.ItemTouchHelperAdapter;
import com.todolist.MyTouchHelper.ItemTouchHelperCallback;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SettingAdapter settingAdapter;
    Setting setting;
    EditText edt_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setting = readPreference();
        edt_url = findViewById(R.id.edt_url);
        edt_url.setText(setting.getUrl());

        settingAdapter = new SettingAdapter(setting.getUsers());
        recyclerView = findViewById(R.id.setting_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(settingAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(settingAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public void newURL(View v) {
        final String url = edt_url.getText().toString();
        if (url.isEmpty()){
            Toast.makeText(this, "Please enter list name", Toast.LENGTH_SHORT).show();
        } else {
            setting.setUrl(url);
            savePreference(setting);
        }
    }

    // Save setting into sharedpreferences
    public void savePreference(Setting setting) {
        SharedPreferences preferences = getSharedPreferences("setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        String setting_serialized = gson.toJson(setting);
        editor.putString("setting", setting_serialized);

        editor.apply();
        editor.commit();
    }

    // Return setting class
    public Setting readPreference() {
        SharedPreferences preferences = getSharedPreferences("setting", MODE_PRIVATE);

        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        String setting_serialized = preferences.getString("setting", gson.toJson(new Setting()));
        return gson.fromJson(setting_serialized, Setting.class);
    }

    class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> implements ItemTouchHelperAdapter {
        private final List<User> users;
        SettingAdapter(List<User> users) { this.users = users; }

        @NonNull
        @Override
        public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.setting,parent,false);
            return new SettingAdapter.SettingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
            String data = users.get(position).getPseudo();

            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        @Override
        public void onItemDissmiss(int postion) {
            users.remove(postion);
            notifyItemRemoved(postion);
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {

        }

        class SettingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final TextView textView;

            SettingViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.setting_name);

                itemView.setOnClickListener(this);
            }

            void bind(String data) { textView.setText(data); }

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Intent i = new Intent(SettingActivity.this, MainActivity.class);
                    i.putExtra("profile", users.get(getAdapterPosition()).getPseudo());
                    i.putExtra("password", users.get(getAdapterPosition()).getPassword());
                    startActivity(i);
                }
            }
        }
    }
}

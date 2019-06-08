package com.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.todolist.MyTouchHelper.ItemTouchHelperAdapter;
import com.todolist.MyTouchHelper.ItemTouchHelperCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    List<String> profiles;
    RecyclerView recyclerView;
    SettingAdapter settingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        profiles = readPreference();

        settingAdapter = new SettingAdapter(profiles);
        recyclerView = findViewById(R.id.setting_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(settingAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(settingAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public void savePreference(List<String> profiles) {
        SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("profile_number", profiles.size());
        for (int i = 0; i < profiles.size(); i++) {
            editor.putString("" + i, profiles.get(i));
        }

        editor.apply();
        editor.commit();
    }

    public List<String> readPreference() {
        SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);
        List<String> profiles = new ArrayList<>();
        int profile_number = preferences.getInt("profile_number", 0);
        for (int i = 0; i < profile_number; i++) {
            profiles.add(preferences.getString("" + i, ""));
        }
        return profiles;
    }

    class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> implements ItemTouchHelperAdapter {
        private final List<String> profiles;
        SettingAdapter(List<String> profiles) { this.profiles = profiles; }

        @NonNull
        @Override
        public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.setting,parent,false);
            return new SettingAdapter.SettingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
            String data = profiles.get(position);

            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return profiles.size();
        }

        @Override
        public void onItemDissmiss(int postion) {
            File f = new File(getFilesDir() + "/" + profiles.get(postion));
            f.delete();
            profiles.remove(postion);
            savePreference(profiles);
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
                    i.putExtra("profile", profiles.get(getAdapterPosition()));
                    startActivity(i);
                }
            }
        }
    }
}

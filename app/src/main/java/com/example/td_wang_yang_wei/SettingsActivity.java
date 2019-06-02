package com.example.td_wang_yang_wei;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    //Transpoteur de liste de nom de profile
    List<String> profiles;

    //recevoir le recyclerView
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //obtenir la liste de noms de profiles
        profiles = readPreference();

        //afficher la liste de noms dans le RecyclerView
        recyclerView = findViewById(R.id.setting_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SettingAdapter(profiles));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    //function d'obtenir la liste de noms de profiles
    public List<String> readPreference() {
        SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);
        List<String> profiles = new ArrayList<>();
        int profile_number = preferences.getInt("nbProfile", 0);
        for (int i = 0; i < profile_number; i++) {
            profiles.add(preferences.getString("" + i, ""));
        }
        return profiles;
    }

    //construire le Adapter de RecyclerView
    class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {
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
            if(profiles==null)
                return 0;
            else return profiles.size();
        }

        //Construire le ViewHolder dans le Adapter
        class SettingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final TextView textView;

            SettingViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.setting_name);

                itemView.setOnClickListener(this);
            }

            void bind(String data) {
                textView.setText(data);
            }

            //sauter au MainActivity corresponde quand on clique la liste.
            @Override
            public void onClick(View v) {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                    i.putExtra("profile", profiles.get(getAdapterPosition()));
                    startActivity(i);
                }
            }
        }
    }


}

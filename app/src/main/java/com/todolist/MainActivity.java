package com.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText edt_pseudo;
    Button btn_pseudo;
    ArrayList<Profile> list_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_pseudo = findViewById(R.id.edt_pseudo);
        btn_pseudo = findViewById(R.id.btn_pseudo);
        list_profile = new ArrayList<>();

        List<String> profiles = readPreference();
        for (String profile : profiles) {
            list_profile.add(readProfilData(profile));
        }
        if (list_profile.size() > 0) {
            edt_pseudo.setText(list_profile.get(list_profile.size() - 1).getLogin());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        myIntent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(myIntent);
        return true;
    }

    public void openListActivity(View v) {
        String pseudo = edt_pseudo.getText().toString();
        boolean flag = true;
        Intent i;
        i = new Intent(MainActivity.this, ListActivity.class);

        for (Profile tmp : list_profile) {
            if (pseudo.equals(tmp.getLogin())) {
                i.putExtra("profile", tmp);
                saveProfilData(tmp, pseudo);
                flag = false;
                break;
            }
        }
        if (flag) {
            Profile p = new Profile(pseudo, new ArrayList<TodoList>());
            list_profile.add(p);
            i.putExtra("profile", p);
            saveProfilData(p, pseudo);
            savePreference(list_profile);
        }

        if (pseudo.isEmpty()){
            Toast.makeText(this, "Please enter your pseudo", Toast.LENGTH_LONG).show();
        } else {
            startActivity(i);
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

    public void savePreference(ArrayList<Profile> profiles) {
        SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("profile_number", profiles.size());
        for (int i = 0; i < profiles.size(); i++) {
            editor.putString("" + i, profiles.get(i).getLogin());
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
}

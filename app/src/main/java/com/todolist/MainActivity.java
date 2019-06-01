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
import com.todolist.DataClass.Profile;
import com.todolist.DataClass.TodoList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText edt_pseudo;
    Button btn_pseudo;
    List<String> profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_pseudo = findViewById(R.id.edt_pseudo);
        btn_pseudo = findViewById(R.id.btn_pseudo);

        profiles = readPreference();
        if (profiles.size() > 0) {
            edt_pseudo.setText(profiles.get(profiles.size() - 1));
        }
    }

    // Initialize the preference menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Show the profile selected by user when redirect to this activity
    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getStringExtra("profile") != null) {
            edt_pseudo.setText(getIntent().getStringExtra("profile"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        myIntent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(myIntent);
        return true;
    }

    // Open an existing list activity or create a new one
    // regarding if the pseudo exist or not in the preference
    public void openListActivity(View v) {
        String pseudo = edt_pseudo.getText().toString();

        if (pseudo.isEmpty()){
            Toast.makeText(this, "Please enter your pseudo", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(MainActivity.this, ListActivity.class);
            if (!profiles.contains(pseudo)) {
                Profile p = new Profile(pseudo, new ArrayList<TodoList>());
                profiles.add(pseudo);
                saveProfilData(p, pseudo);
                savePreference(profiles);
            }
            i.putExtra("profile", pseudo);
            startActivity(i);
        }
    }

    // Function to save data as json file
    // Each Profile class has its own json file named by its login
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

    // Read data and return a Profile class by using its login
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

    // Save logins and number as preference
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

    // Return an array list of profiles' logins;
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

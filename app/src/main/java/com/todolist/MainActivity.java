package com.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.todolist.DataClass.Setting;
import com.todolist.DataClass.User;
import com.todolist.MyRetrofit.TodoListServiceFactory;
import com.todolist.MyRetrofit.Hash;
import com.todolist.MyRetrofit.TodoListService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText edt_pseudo;
    EditText edt_password;
    Setting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_pseudo = findViewById(R.id.edt_pseudo);
        edt_password = findViewById(R.id.edt_password);

        setting = readPreference();
        if (setting.getLastUser() != null) {
            edt_pseudo.setText(setting.getLastUser().getPseudo());
            edt_password.setText(setting.getLastUser().getPassword());
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
        String pseudo = getIntent().getStringExtra("profile");
        String password = getIntent().getStringExtra("password");

        if (pseudo != null) {
            edt_pseudo.setText(pseudo);
        }
        if (password != null) {
            edt_password.setText(password);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        myIntent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(myIntent);
        return true;
    }

    // Login as an existing user in the setting, or authenticate from API and get hash code
    // regarding if the pseudo exist or not in the preference
    public void login(View v) {
        final String pseudo = edt_pseudo.getText().toString();
        final String password = edt_password.getText().toString();

        if (pseudo.isEmpty()){
            Toast.makeText(this, "Please enter your pseudo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (setting.hasUser(pseudo)) {
            User user = setting.getUser(pseudo);
            if (user.verify(password)) {
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra("hash", user.getHash());
                i.putExtra("url", setting.getUrl());
                i.putExtra("pseudo", pseudo);
                startActivity(i);
            } else {
                Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
            }
        } else {
            TodoListService todoListService = TodoListServiceFactory.createService(setting.getUrl(), TodoListService.class);

            Call<Hash> call = todoListService.authenticate(pseudo, password);

            call.enqueue(new Callback<Hash>() {
                @Override
                public void onResponse(Call<Hash> call, Response<Hash> response) {
                    if (response.isSuccessful()) {
                        setting.addUser(new User(pseudo, password, response.body().hash));
                        savePreference(setting);
                        Intent i = new Intent(MainActivity.this, ListActivity.class);
                        i.putExtra("hash", response.body().hash);
                        i.putExtra("url", setting.getUrl());
                        i.putExtra("pseudo", pseudo);
                        startActivity(i);
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void signin(View v) {
        final String pseudo = edt_pseudo.getText().toString();
        final String password = edt_password.getText().toString();

        if (pseudo.isEmpty()){
            Toast.makeText(this, "Please enter your pseudo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (setting.getLastUser() == null) {
            Toast.makeText(this, "You have no right to sign in", Toast.LENGTH_SHORT).show();
            return;
        }

        if (setting.hasUser(pseudo)) {
            Toast.makeText(this, "User already exist", Toast.LENGTH_SHORT).show();
            return;
        }

        TodoListService todoListService = TodoListServiceFactory.createService(setting.getUrl(), TodoListService.class);

        Call<ResponseBody> call = todoListService.signin(setting.getLastUser().getHash(), pseudo, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Success to sign in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Fail to sign in", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
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
}

package fr.syned.sequence1_todolist;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "fr.syned.sequence1_todolist.USERNAME";
    public static final String EXTRA_PROFILE = "fr.syned.sequence1_todolist.PROFILE";
    static final int PICK_CONTACT_REQUEST = 1;
    private static final String TAG = "ToDoList Application";

    private List<Profile> profilesList;
    private ArrayAdapter<String> autoCompleteAdapter;
    private String filename = "profiles.JSON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilesList = getProfilesFromFile();

        setupAutoComplete();

    }

//    private ArrayList<Profile> getFromJson() {
//        Type profilesListType = new TypeToken<ArrayList<Profile>>(){}.getType();
//        ArrayList<Profile> profilesList = gson.fromJson(filename, profilesListType);
//        if (profilesList != null) {
//            for (Profile p : profilesList) {
//                autoCompleteAdapter.add(p.getUsername());
//            }
//        }
//        return profilesList;
//    }

    private List<Profile> getProfilesFromFile() {
        Gson gson = new Gson();
        //BufferedReader reader;
        String json = "";
        List<Profile> profileList = null;
        try {
            FileInputStream inputStream = openFileInput(filename);
            int content;
            while ((content = inputStream.read()) != -1) json += (char) content;
            inputStream.close();
            profileList = gson.fromJson(json, new TypeToken<List<Profile>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (profilesList != null) {
            Log.i(TAG, "getProfilesFromFile: ");
            for (Profile p : profilesList) {
                autoCompleteAdapter.add(p.getUsername());
            }
        }
        return profileList;
    }

    private void setupAutoComplete() {
        AutoCompleteTextView textView = findViewById(R.id.text_view_pseudo);
        autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        if (profilesList != null) {
            for (Profile p : profilesList) {
                autoCompleteAdapter.add(p.getUsername());
            }
        }
        textView.setAdapter(autoCompleteAdapter);
        textView.setThreshold(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickOkBtn(View view) {
        TextView textView = findViewById(R.id.text_view_pseudo);
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        Profile usernameProfile = null;
        // if textViewToDoListName is empty
        String username = textView.getText().toString();
        if (textView.getText().toString().matches("")) {
            Toast.makeText(this, "Please enter your pseudo", Toast.LENGTH_LONG).show();
        } else if (profilesList != null) {
            for (Profile p : profilesList) {
                if (p.getUsername().equals(username)) {
                    usernameProfile = p;
                }
            }
        } else if (profilesList == null) profilesList = new ArrayList<>();
        if (usernameProfile == null) {
            usernameProfile = new Profile(username);
            profilesList.add(usernameProfile);
            autoCompleteAdapter.add(usernameProfile.getUsername());
        }
        intent.putExtra(EXTRA_PROFILE, usernameProfile);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Profile returnedProfile = (Profile)data.getSerializableExtra(EXTRA_PROFILE);
        for (Profile p : profilesList) {
            if (p.getUsername().equals(returnedProfile.getUsername())) {
                p.setToDoLists(returnedProfile.getToDoLists());
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fileContent = gson.toJson(profilesList);
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
            Log.i(TAG, "Saving JSON file.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


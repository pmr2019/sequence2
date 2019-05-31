package fr.syned.sequence1_todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_PROFILE;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_USERNAME;
import static fr.syned.sequence1_todolist.CustomApplication.TAG;
import static fr.syned.sequence1_todolist.CustomApplication.profilesList;

public class MainActivity extends BaseActivity {

//    private List<Profile> profilesList;
    private ArrayAdapter<String> autoCompleteAdapter;
//    private String filename = "profiles.JSON";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        profilesList = getProfilesFromFile();
//        if (profilesList != null) {
//            Log.i(TAG, "getProfilesFromFile: ");
//            for (Profile p : profilesList) {
//                p.onDeserialization();
//            }
//        }

        setupAutoComplete();
    }

//    private List<Profile> getProfilesFromFile() {
//        Gson gson = new Gson();
//        String json = "";
//        List<Profile> profileList = null;
//        try {
//            FileInputStream inputStream = openFileInput(filename);
//            BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream), StandardCharsets.UTF_8));
//            profileList = gson.fromJson(br, new TypeToken<List<Profile>>() {}.getType());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return profileList;
//    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void onClickOkBtn(View view) {
        Log.i(TAG, "onClickOkBtn: ");
        TextView textView = findViewById(R.id.text_view_pseudo);

//        Profile usernameProfile = null;
        String username = textView.getText().toString();
        if (textView.getText().toString().matches("")) {
            Toast.makeText(this, "Please enter your pseudo", Toast.LENGTH_LONG).show();
        } else if (profilesList != null) {
            boolean userExists = false;
            for (Profile p : profilesList) {
                if (p.getUsername().equals(username)) {
//                    usernameProfile = p;
                    userExists = true;
                }
            }
            if(!userExists) {
                profilesList.add(new Profile(username));
                autoCompleteAdapter.add(username);
            }
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(EXTRA_USERNAME, username);
            startActivity(intent);
        }
//        else if (profilesList == null) profilesList = new ArrayList<>();
//        if (usernameProfile == null) {
//            usernameProfile = new Profile(username);
//            profilesList.add(usernameProfile);
//            autoCompleteAdapter.add(usernameProfile.getUsername());
//        }


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data != null) {
//            Profile returnedProfile = (Profile) data.getSerializableExtra(EXTRA_PROFILE);
//            for (Profile p : profilesList) {
//                if (p.getUsername().equals(returnedProfile.getUsername())) {
//                    p.setToDoLists(returnedProfile.getToDoLists());
//                }
//            }
//
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String fileContent = gson.toJson(profilesList);
//            FileOutputStream outputStream;
//
//            try {
//                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
//                outputStream.write(fileContent.getBytes());
//                outputStream.close();
//                Log.i(TAG, "Saving JSON file.");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}


package fr.syned.sequence1_todolist.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import fr.syned.sequence1_todolist.R;

import static fr.syned.sequence1_todolist.CustomApplication.TAG;
import static fr.syned.sequence1_todolist.CustomApplication.filename;
import static fr.syned.sequence1_todolist.CustomApplication.profilesList;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected abstract int getLayoutResourceId();

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
//        saveToJson();
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
            Intent I = new Intent(this, SettingsActivity.class);
            startActivity(I);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveToJson() {
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

package com.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void openListActivity(View v) {
        //Bundle myBdl = new Bundle();

        //TODO get the pseudo name for the list activity

        //myBdl.putString();

        Intent myIntent;
        myIntent = new Intent(MainActivity.this, ListActivity.class);

        //myIntent.putExtras(myBdl);

        startActivity(myIntent);
    }
}

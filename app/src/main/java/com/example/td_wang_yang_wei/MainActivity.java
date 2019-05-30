package com.example.td_wang_yang_wei;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnOk=null;
    private EditText edtPseudo=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOk=findViewById(R.id.btnOK);
        edtPseudo=findViewById(R.id.edtPseudo);
        btnOk.setOnClickListener(this);
        edtPseudo.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(this);

        edtPseudo.setText(settings.getString("pseudo",""));
    }

    @Override
    public void onClick(View v) {

        //afficher des informations lors on clique les elements
        switch (v.getId()){
            //cliquer le button
            case R.id.btnOK:
            String pseudo = edtPseudo.getText().toString();
            Intent OK=new Intent(MainActivity.this,ChoixListeActivity.class);
            startActivity(OK);
            //cliquer le champ de saisie
            case R.id.edtPseudo:


         }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


                Intent account=new Intent(this,SettingsActivity.class);
                startActivity(account);



        return super.onOptionsItemSelected(item);
    }
}

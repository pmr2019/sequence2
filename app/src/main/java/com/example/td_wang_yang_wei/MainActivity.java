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

    public final String Cat="toDoList";
    private Button btnOk=null;
    private EditText edtPseudo=null;

    private void alerter(String s) {
        Log.i(Cat,s);
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }


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
        alerter("onStart");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //cliquer le button
            case R.id.btnOK:
            String pseudo = edtPseudo.getText().toString();
            if(pseudo.equals("")){
                alerter("tapez votre nom");
            }else {
            alerter("Pseudo: " + pseudo);
            Intent liste=new Intent(this,ChoixListeActivity.class);
            startActivity(liste);}
            break;
            //cliquer le champ de saisie
            case R.id.edtPseudo:
                alerter("saisir ton pseudo");
            break;
         }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

                alerter("Menu Compte");
                Intent account=new Intent(this,SettingsActivity.class);
                Bundle data = new Bundle();
                data.putString("pseudo",edtPseudo.getText().toString());
                account.putExtras(data);
                startActivity(account);

        return super.onOptionsItemSelected(item);
    }
}

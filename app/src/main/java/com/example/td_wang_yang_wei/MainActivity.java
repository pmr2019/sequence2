package com.example.td_wang_yang_wei;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final String Cat="toDoList";
    private Button btnOk=null;
    private EditText edtPseudo=null;
    public List <String>profiles;


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
        profiles=getProfiles();
        if(profiles.size()>0){
            edtPseudo.setText(profiles.get(profiles.size()-1));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnOK:

                String pseudo = edtPseudo.getText().toString();

                if(pseudo.equals("")){
                    alerter("tapez votre nom");
                }
                else {
                    alerter("Pseudo: " + pseudo);
                    if(!profiles.contains(pseudo)){
                        setProfile(pseudo);
                        ProfilListeToDo profile=new ProfilListeToDo(pseudo);
                        saveProfileData(profile,pseudo);
                    }
                    ConvertToListe(pseudo);
                }
            break;

            case R.id.edtPseudo:
                alerter("saisir ton pseudo");
            break;
         }

    }
    public void ConvertToListe(String pseudo){
        Intent liste=new Intent(this,ChoixListeActivity.class);
        liste.putExtra("profile",pseudo);
        startActivity(liste);

    }

    public void setProfile(String pseudo){
        profiles.add(pseudo);
        SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("nbProfile", profiles.size());
        for (int i = 0; i < profiles.size(); i++) {
            editor.putString("" + i, profiles.get(i));
        }
        editor.apply();
        editor.commit();
    }
    public List<String> getProfiles() {
        SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);
        List<String> profiles = new ArrayList<>();
        int nbProfile = preferences.getInt("nbProfile", 0);
        for (int i = 0; i < nbProfile; i++) {
            profiles.add(preferences.getString("" + i, ""));
        }
        return profiles;
    }

    public void saveProfileData(ProfilListeToDo p, String pseudo){
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        String fileContents = gson.toJson(p);
        SharedPreferences preferences = getSharedPreferences(pseudo, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("content",fileContents);
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

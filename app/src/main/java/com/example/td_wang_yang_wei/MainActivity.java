package com.example.td_wang_yang_wei;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémenter l'interface principale de l'application
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Initialisation de paramètres
    public final String Cat = "toDoList";
    private Button btnOk = null;
    private EditText edtPseudo = null;
    public List <String>profiles;

    //Alerter pour savoir le processus de la programme et alerter les utilisateurs
    public void alerter(String s) {
        Log.i(Cat,s);
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connection les layouts avec des backends
        btnOk=findViewById(R.id.btnOK);
        edtPseudo=findViewById(R.id.edtPseudo);

        btnOk.setOnClickListener(this);
        edtPseudo.setOnClickListener(this);

        //Le dernier pseudo saisi est automatiquement renseigné dans le champ de saisie
        profiles=getProfiles();
        if(profiles.size()>0){
            edtPseudo.setText(profiles.get(profiles.size()-1));
        }
    }


    @Override
    protected void onStart() {

        super.onStart();
        alerter("onStart");

        //Lorsque nous cliquons sur un élément dans les comptes
        // nous revenons à MainActivity à partir de SettingsActivity
        // et affichons le profil sur lequel vous avez cliqué dans le champ de saisie
        if (getIntent().getStringExtra("profile") != null) {
            edtPseudo.setText(getIntent().getStringExtra("profile"));
        }
    }

    /**
     * Quand on appuie sur le bouton OK, on ajoute un nouveau profil non existant
     * Quand on appuie sur le le champ de saisie, on affiche un avertissement
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnOK:

                String pseudo = edtPseudo.getText().toString();

                //éviter le cas d'entrée vide
                if(pseudo.equals("")){
                    alerter("tapez votre nom");
                }
                else {
                    alerter("Pseudo: " + pseudo);

                    //créez un nouveau profil
                    if(!profiles.contains(pseudo)){
                        setProfile(pseudo);
                        ProfilListeToDo profile=new ProfilListeToDo(pseudo,new ArrayList<ListeToDo>());
                        saveProfileData(profile,pseudo);
                    }

                    //entrez un profil existant
                    ConvertToListe(pseudo);
                }
            break;

            case R.id.edtPseudo:
                alerter("saisir ton pseudo");
            break;
         }
    }

    /**
     * entrez un profil existant
     * @param pseudo
     */
    public void ConvertToListe(String pseudo){
        Intent liste=new Intent(this,ChoixListeActivity.class);
        liste.putExtra("profile",pseudo);
        startActivity(liste);
    }

    /**
     * ajouter un nouveau profil
     * @param pseudo
     * @return null
     */
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

    /**
     *générer une liste de profils
     * @return profiles
     */
    public List<String> getProfiles() {
        SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);
        List<String> profiles = new ArrayList<>();
        int nbProfile = preferences.getInt("nbProfile", 0);
        for (int i = 0; i < nbProfile; i++) {
            profiles.add(preferences.getString("" + i, ""));
        }
        return profiles;
    }

    /**
     *Remettre le profil modifié à la Préférence
     * @param p
     * @param pseudo
     */
    public void saveProfileData(ProfilListeToDo p, String pseudo){
        Gson gson=new Gson();
        String fileContents = gson.toJson(p);
        SharedPreferences preferences = getSharedPreferences(pseudo, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("content",fileContents);
        editor.commit();
    }


    /**
     * Créez le menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Entrez un élément dans le menu
     * Dans notre cas, accounts uniquement
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

                alerter("Menu Compte");
                Intent account=new Intent(this,SettingsActivity.class);
                startActivity(account);

        return super.onOptionsItemSelected(item);
    }
}

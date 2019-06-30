package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import modele.ListeToDo;
import modele.ProfilListeToDo;

public class ChoixListActivity extends AppCompatActivity {

    private static final String TAG = "TEST";
    public static String PSEUDO;
    public static String LISTE;
    ProfilListeToDo p;
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    String nomFichier;
    String login;
    ListView lv_liste;
    TextView tv_bvnListe;
    Button btn_ajoutListe;
    EditText edt_ajoutListe;
    ArrayAdapter listesAdapter;
    ArrayList<ListeToDo> listes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_list);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        login = extras.getString("LOGIN");

        tv_bvnListe = findViewById(R.id.iTv_bienvenue);
        tv_bvnListe.setText("Listes de " + login);

        nomFichier = login + ".txt";
        try {
            FileInputStream fluxIn_p = openFileInput(nomFichier);
            StringBuffer sb = new StringBuffer();
            int value;
            while ((value =fluxIn_p.read()) != -1 ){
                sb.append((char) value);
            }
            fluxIn_p.close();
            String sJson = sb.toString();
            p = gson.fromJson(sJson, ProfilListeToDo.class );
        } catch (FileNotFoundException e) {
            p = new ProfilListeToDo(login);
            try {
                FileOutputStream fluxOut_p = openFileOutput(nomFichier, MODE_PRIVATE);
                String json_p = gson.toJson(p);
                fluxOut_p.write(json_p.getBytes());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lv_liste = findViewById(R.id.iLv_Liste);
        listes = new ArrayList<>();
        for(ListeToDo ls : p.getMesListesToDo()){
            listes.add(ls);
        }
        listesAdapter = new ArrayAdapter<>(this, R.layout.item_liste, listes);
        lv_liste.setAdapter(listesAdapter);

        btn_ajoutListe = findViewById(R.id.iBtn_ajoutListe);
        btn_ajoutListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_ajoutListe = findViewById(R.id.iEdt_ajoutListe);
                String titreListe = edt_ajoutListe.getText().toString();
                p.ajouterListe(new ListeToDo(titreListe));
                edt_ajoutListe.setText("");

                try {
                    FileOutputStream fluxOut_p = openFileOutput(nomFichier, MODE_PRIVATE);
                    fluxOut_p.write(gson.toJson(p).getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                listesAdapter.add(new ListeToDo(titreListe));
                Toast.makeText(ChoixListActivity.this, titreListe, Toast.LENGTH_SHORT).show();
            }
        });

        lv_liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv_itemListe = (TextView) view;
                String titre = tv_itemListe.getText().toString();
                Toast.makeText(ChoixListActivity.this, "Id de l'item: " + id + "\nTexte:" + titre , Toast.LENGTH_SHORT).show();

                Intent showListe = new Intent(ChoixListActivity.this, ShowListActivity.class);
                Bundle extras = new Bundle();
                extras.putString("PSEUDO", login);
                extras.putString("LISTE", titre);
                showListe.putExtras(extras);
                startActivity(showListe);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.iIt_test :
                Toast.makeText(this, "Clique sur l'item Test", Toast.LENGTH_SHORT).show();

            case R.id.iIt_pref :
                Toast.makeText(this, "Gérer ses préférences", Toast.LENGTH_SHORT).show();
                Intent settings = new Intent(ChoixListActivity.this, SettingsActivity.class);
                startActivity(settings);
        }

        return super.onOptionsItemSelected(item);
    }
}

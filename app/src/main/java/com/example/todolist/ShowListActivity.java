package com.example.todolist;

import android.content.ClipData;
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
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import modele.ItemToDo;
import modele.ListeToDo;
import modele.ProfilListeToDo;

public class ShowListActivity extends AppCompatActivity {

    private static final String TAG = "TEST";
    ProfilListeToDo p;
    ListeToDo l;
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    String nomFichier;
    String login;
    String titre;
    Button btn_ajoutItem;
    ListView lv_item;
    TextView tv_bvnItem;
    EditText edt_ajoutItem;
    ArrayList<ItemToDo> items;
    ArrayAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        login = extras.getString("PSEUDO");
        titre = extras.getString("LISTE");

        tv_bvnItem = findViewById(R.id.iTv_bvnItem);
        tv_bvnItem.setText("Items de " + titre + " de l'utilisateur " + login);

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
            p = gson.fromJson(sJson, ProfilListeToDo.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Récup du profil" + p);
        Log.i(TAG, p.getMesListesToDo().toString());

        for (ListeToDo liste : p.getMesListesToDo()){
            Log.i(TAG, liste.getTitreListeToDo());
            if (liste.getTitreListeToDo().equals(titre)) {
                Log.i(TAG, "C'est cette liste!");
                l = liste;
            }
        }

        lv_item = findViewById(R.id.iLv_item);
        items = new ArrayList<>();
        for (ItemToDo it : l.getLesItems()) {
            items.add(it);
        }
        itemsAdapter = new ArrayAdapter<>(this, R.layout.item_item, items);
        lv_item.setAdapter(itemsAdapter);

        btn_ajoutItem = findViewById(R.id.iBtn_ajoutItem);
        btn_ajoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_ajoutItem = findViewById(R.id.iEdt_ajoutItem);
                String description = edt_ajoutItem.getText().toString();
                l.ajouterItem(new ItemToDo(description));
                edt_ajoutItem.setText("");

                try {
                    FileOutputStream fluxOut_p = openFileOutput(nomFichier, MODE_PRIVATE);
                    fluxOut_p.write(gson.toJson(p).getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                itemsAdapter.add(new ItemToDo(description));
                Toast.makeText(ShowListActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView Ctv_itemItem = (CheckedTextView) view;
                if (Ctv_itemItem.isChecked()) {
                    Ctv_itemItem.setChecked(false);
                }else {
                    Ctv_itemItem.setChecked(true);
                }
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
                Intent settings = new Intent(ShowListActivity.this, SettingsActivity.class);
                startActivity(settings);
        }

        return super.onOptionsItemSelected(item);
    }
}

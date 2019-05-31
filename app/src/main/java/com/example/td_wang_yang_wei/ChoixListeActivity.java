package com.example.td_wang_yang_wei;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ChoixListeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_liste);

        EditText edtListe = findViewById(R.id.edtliste);
        Button btnListe = findViewById(R.id.btnListe);
        RecyclerView recyclerView = findViewById(R.id.list);


    }
}

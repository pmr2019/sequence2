package com.example.td_wang_yang_wei;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChoixListeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtListe;
    private Button btnListe;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_liste);

        edtListe = findViewById(R.id.edtliste);
        btnListe = findViewById(R.id.btnListe);
        recyclerView = findViewById(R.id.list);
        btnListe.setOnClickListener(this);

        //recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnListe:
                String liste = edtListe.getText().toString();

        }

    }
}

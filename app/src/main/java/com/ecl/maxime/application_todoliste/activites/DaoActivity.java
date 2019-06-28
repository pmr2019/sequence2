package com.ecl.maxime.application_todoliste.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ecl.maxime.application_todoliste.R;
import com.ecl.maxime.application_todoliste.classes.ListeToDo;
import com.ecl.maxime.application_todoliste.database.dao.Listesdao;

import java.util.ArrayList;

public class DaoActivity extends AppCompatActivity {

    private EditText edt_pseudo;
    private EditText edt_password;
    private Button btn_ok;
    private String login;
    private String mdp;
    private ArrayList<ListeToDo> listes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_pseudo=findViewById(R.id.pseudo_edittext);
        edt_password=findViewById(R.id.password_edt);
        btn_ok=findViewById(R.id.btn_ok);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = edt_pseudo.getText().toString();
                mdp = edt_password.getText().toString();
                }
        });
    }
}

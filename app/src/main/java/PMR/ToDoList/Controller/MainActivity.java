package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import PMR.ToDoList.R;

public class MainActivity extends AppCompatActivity {

    private EditText edtPseudo;
    private Button btnPseudo;
    private TextView txtPseudo;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPseudo = (EditText) findViewById(R.id.edtPseudo);
        btnPseudo = (Button) findViewById(R.id.btnPseudo);
        txtPseudo = (TextView) findViewById(R.id.txtPseudo);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnPseudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToDoListActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menupseudo, menu);
        return true;
    }



}

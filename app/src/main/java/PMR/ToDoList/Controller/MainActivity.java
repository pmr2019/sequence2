package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import PMR.ToDoList.Model.User;
import PMR.ToDoList.R;

public class MainActivity extends AppCompatActivity {

    private EditText edtPseudo;
    private Button btnPseudo;
    private TextView txtPseudo;
    private androidx.appcompat.widget.Toolbar toolbar;
    private User user;
    private FileOutputStream outputStream; //permet de sérialiser correctement user

    private void alerter(String s) {
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }

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
                user = new User(edtPseudo.getText().toString());
                sauvegarderUserToJsonFile();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_settings:

                Intent toSettings = new Intent(MainActivity.this,SettingsActivity.class);
                sauvegarderUserToJsonFile();
                startActivity(toSettings);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //Partie GSON
    //Ecrire des données dans la mémoire interne du téléphone

    public void sauvegarderUserToJsonFile(){

        final GsonBuilder builder = new GsonBuilder(); //assure la qualité des données Json
        final Gson gson = builder.create();
        String fileName = "pseudos"; //nom du fichier Json
        String fileContents = gson.toJson(this.user);


        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
            Log.i("TODO_Romain", "Sauvegarde du fichier Json");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}

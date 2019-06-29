package PMR.ToDoList.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import PMR.ToDoList.R;
import static PMR.ToDoList.ui.MainActivity.urlApi;


/*
Classe affichant l'URL de l'API utilisé par l'application.
 */
public class SettingsURL extends AppCompatActivity {

    private EditText etUrlApi;
    private Toolbar toolbar;
    private Button btnModifUrlApi;
    private Button btnRestaurationUrlApi;

    // METHODE POUR LES TOASTS
    public void alerter(String s) {
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_url);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etUrlApi=findViewById(R.id.urlApi);
        try {
            etUrlApi.setText(getUrlApiFromJson());
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnModifUrlApi=findViewById(R.id.btnModifUrlApi);
        btnRestaurationUrlApi=findViewById(R.id.btnRestaurationUrlApi);


        btnModifUrlApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlApi= String.valueOf(etUrlApi.getText());
                sauvegarderUrlApiToJsonFile(urlApi);
                alerter("Url modifiée avec succès");
            }
        });

        btnRestaurationUrlApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlApi= "http://tomnab.fr/todo-api/";
                sauvegarderUrlApiToJsonFile(urlApi);
                etUrlApi.setText("http://tomnab.fr/todo-api/");
                alerter("Url modifiée avec succès");
            }
        });
    }

    //Partie GSON
    //Ecrire des données dans la mémoire interne du téléphone

    public void sauvegarderUrlApiToJsonFile(String urlApi) {

        final GsonBuilder builder = new GsonBuilder(); //assure la qualité des données Json
        final Gson gson = builder.setPrettyPrinting().create();
        String fileName = "UrlApi"; //nom du fichier Json
        FileOutputStream outputStream; //permet de sérialiser correctement user

        try {
            outputStream = openFileOutput("UrlApi", Context.MODE_PRIVATE);
            outputStream.write(urlApi.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fonction recréant à chaque ouverture de l'appli une liste de users
    private String getUrlApiFromJson() throws IOException {
        InputStream in = openFileInput("UrlApi");
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            return reader.readLine();

        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

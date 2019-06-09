package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import PMR.ToDoList.R;

public class SettingsURL extends AppCompatActivity {

    private EditText urlApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_url);

        urlApi=findViewById(R.id.urlApi);

        String s=urlApi.getText().toString();
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.show();
    }
}

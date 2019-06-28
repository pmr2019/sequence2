package com.example.todo.UI;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.todo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnOK = null;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView edtLogin;
    private EditText edtPassword;
    private SharedPreferences settings;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOK = findViewById(R.id.buttonOK);
        btnOK.setOnClickListener(this);
        context=this;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        edtLogin = (AutoCompleteTextView) findViewById(R.id.edtLogin);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        //calling settings
        settings = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //get login from preferences
        edtLogin.setText(settings.getString("login", "").toString());

    }

    @Override // To add the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override //Menu event listener
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent myInt = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(myInt);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOK:
                if (verifReseau()) {
                    adapter.add(edtLogin.getText().toString());
                    edtLogin.setAdapter(adapter);
                    //Token request
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = settings.getString("api_url", "http://tomnab.fr/todo-api").toString() + "/authenticate?user=" + edtLogin.getText() + "&password=" + edtPassword.getText();
                    Log.i("Web", url);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("Web", response);
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<HashMap<String, String>>() {
                                    }.getType();
                                    HashMap<String, String> responseJson = gson.fromJson(response, type);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("token", responseJson.get("hash"));
                                    Log.i("Web", responseJson.get("hash"));

                                    //save login in preferences
                                    //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                                    editor.putString("login", edtLogin.getText().toString());
                                    editor.commit();

                                    //Create an intent to change activity
                                    Intent toSecondAct = new Intent(MainActivity.this, ChoixListActivity.class);
                                    //Bundle data = new Bundle();
                                    //data.putString("login", edtLogin.getText().toString());
                                    //toSecondAct.putExtras(data);
                                    startActivity(toSecondAct);


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Web", "error");
                            Toast.makeText(context, "Verify your login or your password", Toast.LENGTH_LONG).show();
                        }
                    });
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);

                } else {
                    Toast.makeText(this, "Accessing offline mode", Toast.LENGTH_LONG).show();
                    Intent toSecondAct = new Intent(MainActivity.this, ChoixListActivity.class);
                    startActivity(toSecondAct);
                }
                break;

        }

    }


    public boolean verifReseau() {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

        String sType = "Aucun réseau détecté";
        Boolean bStatut = false;
        if (netInfo != null) {
            NetworkInfo.State netState = netInfo.getState();
            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                bStatut = true;
                int netType = netInfo.getType();
                switch (netType) {
                    case ConnectivityManager.TYPE_MOBILE:
                        sType = "Réseau mobile détecté";
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        sType = "Réseau wifi détecté";
                        break;
                }
            }
        }

        Log.i("Web", sType);
        return bStatut;
    }


}


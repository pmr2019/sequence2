package fr.syned.sequence1_todolist.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import fr.syned.sequence1_todolist.activities.network.RequestQueueInstance;
import fr.syned.sequence1_todolist.model.Profile;
import fr.syned.sequence1_todolist.R;

import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_HASH;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_ID;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_USERNAME;
import static fr.syned.sequence1_todolist.CustomApplication.TAG;
import static fr.syned.sequence1_todolist.CustomApplication.profilesList;

public class MainActivity extends BaseActivity {

    private ArrayAdapter<String> autoCompleteAdapter;
    private RequestQueueInstance instance;
    private String username;

    @Override
    public void onResume(){
        super.onResume();
        setupAutoComplete();
    }
    @Override
    public void onStart(){
        super.onStart();
        final Button ok_btn = findViewById(R.id.ok_btn);
        AutoCompleteTextView textView = findViewById(R.id.username_text_view);

        ok_btn.setEnabled(false);
        final Handler handler = new Handler();
        final int delay = 1000; // milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(checkNetwork()){
                    ok_btn.setEnabled(true);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);

        instance = RequestQueueInstance.getInstance(getApplicationContext());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    private void setupAutoComplete() {
        AutoCompleteTextView textView = findViewById(R.id.username_text_view);
        autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        if (profilesList != null) {
            for (Profile p : profilesList) {
                autoCompleteAdapter.add(p.getUsername());
            }
        }
        textView.setAdapter(autoCompleteAdapter);
        textView.setThreshold(1);
    }

    public void onClickOkBtn(View view) {
        Log.i(TAG, "onClickOkBtn: ");
        TextView textView = findViewById(R.id.username_text_view);
        TextView passwordView = findViewById(R.id.password_text_view);

        username = textView.getText().toString();
        String password = passwordView.getText().toString();

        // SEQUENCE 1
//        if (textView.getText().toString().matches("")) {
//            Toast.makeText(this, "Please enter your pseudo", Toast.LENGTH_LONG).show();
//        } else if (profilesList != null) {
//            boolean userExists = false;
//            for (Profile p : profilesList) {
//                if (p.getUsername().equals(username)) userExists = true;
//            }
//            if(!userExists) {
//                profilesList.add(new Profile(username));
//                autoCompleteAdapter.add(username);
//            }
//            Intent intent = new Intent(this, ProfileActivity.class);
//            intent.putExtra(EXTRA_USERNAME, username);
//
//            startActivity(intent);
//        }
        // SEQUENCE 2
        if (textView.getText().toString().matches("")) {
            Toast.makeText(this, "Please enter your pseudo", Toast.LENGTH_LONG).show();
        } else {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://tomnab.fr/todo-api/authenticate?user="+username+"&password=" + password,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("TODO", "onResponse: ");
                    String hash;
                    String id;
                        try {
                            if (response.has("hash")) {
                                hash = response.get("hash").toString();
//                                id = response.get("id").toString();
//                                id = hash;
                                startProfileActivity(hash);
//                                startProfileActivity(hash, id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            },
                    new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("TODO", "onErrorResponse: " + error.toString());
//                    Toast: Failed Connection
                }
            });

            instance.addToRequestQueue(request);
            autoCompleteAdapter.add(username);
        }
    }
    private boolean checkNetwork()
    {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

        String sType = "Aucun réseau détecté";
        Boolean bStatut = false;
        if (netInfo != null)
        {

            NetworkInfo.State netState = netInfo.getState();

            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0)
            {
                bStatut = true;
                int netType= netInfo.getType();
                switch (netType)
                {
                    case ConnectivityManager.TYPE_MOBILE :
                        sType = "Réseau mobile détecté"; break;
                    case ConnectivityManager.TYPE_WIFI :
                        sType = "Réseau wifi détecté"; break;
                }

            }
        }
        return bStatut;
    }

    public void startProfileActivity(String hash) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra(EXTRA_HASH, hash);
        intent.putExtra(EXTRA_USERNAME, username);
        startActivity(intent);
    }
}


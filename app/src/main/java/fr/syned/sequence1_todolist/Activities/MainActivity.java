package fr.syned.sequence1_todolist.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.syned.sequence1_todolist.Model.Profile;
import fr.syned.sequence1_todolist.R;

import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_USERNAME;
import static fr.syned.sequence1_todolist.CustomApplication.TAG;
import static fr.syned.sequence1_todolist.CustomApplication.profilesList;

public class MainActivity extends BaseActivity {

    private ArrayAdapter<String> autoCompleteAdapter;

    @Override
    public void onResume(){
        super.onResume();
        setupAutoComplete();
    }
    @Override
    public void onStart(){
        super.onStart();
        final Button bOk = findViewById(R.id.btn_ok);
        bOk.setEnabled(false);
        AutoCompleteTextView textView = findViewById(R.id.text_view_pseudo);
        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(checkNetwork()){
                    bOk.setEnabled(true);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    private void setupAutoComplete() {
        AutoCompleteTextView textView = findViewById(R.id.text_view_pseudo);
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
        TextView textView = findViewById(R.id.text_view_pseudo);
        TextView passwordView = findViewById(R.id.text_view_password);

        String username = textView.getText().toString();
        if (textView.getText().toString().matches("")) {
            Toast.makeText(this, "Please enter your pseudo", Toast.LENGTH_LONG).show();
        } else if (profilesList != null) {
            boolean userExists = false;
            for (Profile p : profilesList) {
                if (p.getUsername().equals(username)) userExists = true;
            }
            if(!userExists) {
                profilesList.add(new Profile(username));
                autoCompleteAdapter.add(username);
            }
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(EXTRA_USERNAME, username);

            startActivity(intent);
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
}


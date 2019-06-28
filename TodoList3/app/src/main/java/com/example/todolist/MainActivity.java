package com.example.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.room.Room;

import com.example.todolist.data.API.GetDataService;
import com.example.todolist.data.API.ResponseHash;
import com.example.todolist.data.API.RetrofitClient;
import com.example.todolist.data.Convert;
import com.example.todolist.data.database.AppDatabase;
import com.example.todolist.data.database.DatabaseItem;
import com.example.todolist.data.database.DatabaseProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends GenericActivity {

    private EditText mUserNameTE;
    private EditText mUserPassTE;
    private Button mOkButton;
    private AppDatabase mDb;

    // ------------------------------------------------------------------------------------------ //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialisation des champs textes et du bouton
        mUserNameTE = findViewById(R.id.userNameEditText);
        mUserPassTE = findViewById(R.id.userPassEditText);
        mOkButton = findViewById(R.id.okButton);

        mUserNameTE.setText(mUserName);
        if(!connectedToNetwork()) {
            mOkButton.setText("Manipuler les données en cache");
        }
    }

    // ------------------------------------------------------------------------------------------ //
    public void toUserLists(View view) {

        if(connectedToNetwork()) {
            mOkButton.setText("OK");
        } else {
            mOkButton.setText("Manipuler les données en cache");
        }

        String userName = mUserNameTE.getText().toString();
        final String userPass = mUserPassTE.getText().toString();
        
        if (userName.matches("") || userPass.matches("")) {
            // en cas d'absence du nom utilisateur ou du mot de passe
            Toast.makeText(this, "Veuillez entrer votre nom ou un mot de passe", Toast.LENGTH_SHORT).show();
        }
        else {
            // en cas de présence du nom utilisateur et du mot de passe
            mUserName = userName;

            findViewById(R.id.progess).setVisibility(View.VISIBLE);

            if(connectedToNetwork()) {
                // si on est connecté, on récupère les données avec l'API
                GetDataService service = RetrofitClient.createService(mApiUrl, GetDataService.class);
                Call<ResponseHash> call = service.authenticate(userName, userPass);

                call.enqueue(new Callback<ResponseHash>() {
                    @Override
                    public void onResponse(Call<ResponseHash> call, Response<ResponseHash> response) {

                        if (response.isSuccessful() && response.body().success) {

                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putString("userName", mUserName);
                            editor.putString("hash", response.body().hash);
                            editor.apply();

                            new Thread() {
                                @Override
                                public void run() {
                                    mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db").fallbackToDestructiveMigration().build();
                                    Convert c = new Convert();
                                    mIdUser = mDb.UserProfileDAO().insertUser(c.profile(mUserName, userPass));

                                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                                    editor.putLong("idUser", mIdUser);
                                    editor.apply();
                                }
                            }.start();

                            toLists();
                        } else {
                            Toast.makeText(getBaseContext(), "Les données attendues n'ont pas été reçues", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseHash> call, Throwable t) {
                        Toast.makeText(getBaseContext(), "Un problème a été rencontré...", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {

                final Handler mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Toast.makeText(getBaseContext(), "Les données attendues n'ont pas pu être récupérées", Toast.LENGTH_SHORT).show();
                    }
                };

                new Thread() {
                    @Override
                    public void run() {
                        mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db").fallbackToDestructiveMigration().build();
                        DatabaseProfile user = mDb.UserProfileDAO().getUser(mUserName, userPass);

                        if (user != null) {
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putLong("idUser", user.id);
                            editor.apply();

                            toLists();
                        } else {
                            Message message = mHandler.obtainMessage();
                            message.sendToTarget();
                        }
                    }
                }.start();


            }

        }
    }
}
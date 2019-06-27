package com.example.todolist.recycler_activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.api.TodoApiService;
import com.example.todolist.api.TodoApiServiceFactory;
import com.example.todolist.api.response_class.UnItem;
import com.example.todolist.api.response_class.UneListe;
import com.example.todolist.bdd.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Définition de la classe Library.
 * Cette classe contient des méthodes communes aux classes ChoixListActivity et ShowListActivity
 */
abstract public class Library extends AppCompatActivity {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    AppDatabase database;
    private SharedPreferences preferences;
    private String hash;
    private TodoApiService todoApiService;
    ConnectivityManager connectivityManager;
    NetworkInfo activeNetworkInfo;
    boolean estConnecte;
    ConnectivityManager.NetworkCallback connectivityCallback;

    /**
     * Fonction onCreate appelée lors de le création de l'activité
     * On initialise le Connectivity Manager
     *
     * @param savedInstanceState données à récupérer si l'activité est réinitialisée après
     *                           avoir planté
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        hash = preferences.getString("hash", "");

        connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        estConnecte = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        connectivityCallback
                = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                if (!estConnecte)
                    majAPI();
                estConnecte = true;
                Log.i("PMR", "INTERNET CONNECTED");

            }

            @Override
            public void onLost(Network network) {
                estConnecte = false;
                Log.i("PMR", "INTERNET LOST");

            }
        };

        connectivityManager.registerNetworkCallback(
                new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build(), connectivityCallback);

        database = AppDatabase.getInstance(getApplicationContext());
    }

    /**
     * Permet de mettre à jour l'API (méthode appelée lors de la récupération du réseau
     */
    void majAPI() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<UneListe> lesListes = database.listeDao().getAll(hash);
                for (UneListe liste : lesListes) {
                    List<UnItem> lesItems = database.itemDao().getAll(liste.id);
                    for (UnItem item : lesItems) {
                        todoApiService = TodoApiServiceFactory.createService(TodoApiService.class);
                        Call<UnItem> call = todoApiService.cocherItem(hash, liste.id, item.id, item.checked);
                        call.enqueue(new Callback<UnItem>() {
                            @Override
                            public void onResponse(Call<UnItem> call, Response<UnItem> response) {
                                if (response.isSuccessful()) {
                                    Log.i("TAG", "onResponse: nice");
                                } else {
                                    Log.d("TAG", "onResponse: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<UnItem> call, Throwable t) {
                                Log.d("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                            }
                        });
                    }

                }
            }
        });
    }
}

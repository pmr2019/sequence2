package com.example.todo.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.UiThread;
import android.util.Log;

import com.example.todo.API_models.RetroMain;
import com.example.todo.API_models.TodoInterface;
import com.example.todo.Utils;
import com.example.todo.activities.ChoixListActivity;
import com.example.todo.database.MyDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataProvider {

    private static final String TAG = "DataProvider";

    /////////////////
    private List<Future> futures = new ArrayList<>();
    private final Handler uiHandler;
    //////////////////////


    private MyDatabase myDatabase;
    private TodoInterface service;
    private boolean isConnectedToInternet;

    public DataProvider(Context context) {
        uiHandler = new Handler(context.getMainLooper());
        myDatabase = MyDatabase.getInstance(context);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        this.isConnectedToInternet = settings.getBoolean("isConnectedToInternet",false);
        String baseUrl = settings.getString("APIurl", "http://tomnab.fr/");
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
        service = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TodoInterface.class);
    }

    /**
     * Return a DataResponse with a profilListeToDo initialized with only its login
     * from the API or the DB, and the hash if the data comes from the API. To choose the database (API or SQL), the function checks
     * the preference isConnectedToInternet.
     * @param pseudo
     * @param password
     * @param listener
     */
    public void authenticate(String pseudo, String password, final PostsListener listener){
        Future future = Utils.BACKGROUND.submit(() -> {
            if (isConnectedToInternet && internetCheck()) {
                //Verify pseudo & password with the API
                Call<RetroMain> call = service.authenticate(pseudo, password);
                call.enqueue(new Callback<RetroMain>() {
                    @Override
                    public void onResponse(Call<RetroMain> call, Response<RetroMain> response) {
                        if (response.body() != null) {
                            RetroMain retroMain = response.body();
                            if (retroMain.isSuccess()) {
                                DataResponse dR = new DataResponse();
                                dR.setHash(response.body().getHash());
                                ProfilListeToDo profil = new ProfilListeToDo();
                                profil.setLogin(pseudo);
                                dR.setProfilListeToDo(profil);
                                uiHandler.post(() -> {
                                    listener.onSuccess(dR);
                                });
                            } else {
                                Log.d(TAG, "onResponse: http code : "+retroMain.getStatus());
                                uiHandler.post(()-> {
                                    listener.onError("onResponse: http code : "+retroMain.getStatus());
                                });
                            }
                        } else {
                            Log.d(TAG, "onResponse: empty response. HTTP CODE : "+response.code());
                            uiHandler.post(() -> {
                                listener.onError("onResponse: empty response. HTTP CODE : "+response.code());
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<RetroMain> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.toString());
                        uiHandler.post(() -> {
                            listener.onError("onFailure from API call.");
                        });
                    }
                });
            } else if (!isConnectedToInternet) {
                ProfilListeToDo profil;
                profil = myDatabase.profilListeToDoDao().getProfilListeToDo(pseudo);
                if (profil != null) {
                    DataResponse dR = new DataResponse();
                    dR.setProfilListeToDo(profil);
                    uiHandler.post(() -> {
                        listener.onSuccess(dR);
                    });
                } else {
                    uiHandler.post(() -> {
                        listener.onError("Pseudo unknown in cache.");
                    });
                }
            }
        });
        futures.add(future);
    }

    /**
     * Return a DataResponse with a profilListeToDo initialized with all the listeToDo it contains
     * from the API or the DB. To choose the database (API or SQL), the function checks
     * the preference isConnectedToInternet.
     * @param pseudo
     * @param hash
     * @param listener
     */
    public void loadListeToDo(String pseudo, String hash, final PostsListener listener){
        Future future = Utils.BACKGROUND.submit(() -> {
            if (isConnectedToInternet && internetCheck()) {
                Call<RetroMain> call = service.getLists(hash);
                call.enqueue(new Callback<RetroMain>() {
                    @Override
                    public void onResponse(Call<RetroMain> call, Response<RetroMain> response) {
                        if (response.body() != null) {
                            RetroMain retroMain = response.body();
                            if (retroMain.isSuccess()) {
                                ProfilListeToDo profil = new ProfilListeToDo();
                                ArrayList<ListeToDo> listeToDos = new ArrayList<>();
                                for (RetroMain r : retroMain.getLists()) {
                                    listeToDos.add(new ListeToDo(r.getId(), profil.getLogin(), r.getLabel(), new ArrayList<ItemToDo>()));
                                }
                                profil.setMesListeToDo(listeToDos);
                                // Updating the database
                                myDatabase.profilListeToDoDao().addProfilListeToDo(profil);
                                for (ListeToDo listeToDo : profil.getMesListeToDo()){
                                    myDatabase.listeToDoDao().addListeToDo(listeToDo);
                                }

                                DataResponse dR = new DataResponse();
                                dR.setProfilListeToDo(profil);
                                uiHandler.post(() -> {
                                    listener.onSuccess(dR);
                                });

                            } else {
                                Log.d(TAG, "onResponse: http code : "+retroMain.getStatus());
                                uiHandler.post(()-> {
                                    listener.onError("onResponse: http code : "+retroMain.getStatus());
                                });
                            }
                        } else {
                            Log.d(TAG, "onResponse: empty response. HTTP CODE : "+response.code());
                            uiHandler.post(() -> {
                                listener.onError("onResponse: empty response. HTTP CODE : "+response.code());
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<RetroMain> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.toString());
                        uiHandler.post(() -> {
                            listener.onError("onFailure from API call.");
                        });
                    }
                });
            } else if (!isConnectedToInternet){
                ProfilListeToDo profil;
                profil = myDatabase.profilListeToDoDao().getProfilListeToDo(pseudo);
                if (profil != null) {
                    List<ListeToDo> listeToDos = myDatabase.listeToDoDao().getAllListeToDo(profil.getLogin());
                    profil.setMesListeToDo(new ArrayList<>(listeToDos));
                    DataResponse dR = new DataResponse();
                    dR.setProfilListeToDo(profil);
                    uiHandler.post(() -> {
                        listener.onSuccess(dR);
                    });
                } else{
                    uiHandler.post(() -> {
                        listener.onError("Pseudo unknown in cache.");
                    });
                }
            }
        });
        futures.add(future);
    }

    public void updateAPIfromDB(String pseudo, String hash, final PostsListener listener) {
        if (isConnectedToInternet && internetCheck()) {
            ProfilListeToDo profil = myDatabase.profilListeToDoDao().getProfilListeToDo(pseudo);
            profil.setMesListeToDo(new ArrayList<>(myDatabase.listeToDoDao().getAllListeToDo(profil.getLogin())));
        } else {
            uiHandler.post(() -> {
                listener.onError("No Internet connection.");
            });
        }
    }


    public void stop() {
        for (Future future : futures) {
            if(!future.isDone()){
                future.cancel(true);
            }
        }
        futures.clear();
    }

    /**
     * Return true if connected to internet, false otherwise.
     * BE CAREFUL, this function can only be launched in a different
     * thread than the UI thread.
     * @return
     */
    public boolean internetCheck(){
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public interface PostsListener {
        @UiThread
        public void onSuccess(DataResponse dataResponse);
        @UiThread
        public void onError(String error);
    }

    public class DataResponse {

        private String hash="";
        private ProfilListeToDo profilListeToDo = new ProfilListeToDo();

        public DataResponse() {
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public ProfilListeToDo getProfilListeToDo() {
            return profilListeToDo;
        }

        public void setProfilListeToDo(ProfilListeToDo profilListeToDo) {
            this.profilListeToDo = profilListeToDo;
        }
    }
}

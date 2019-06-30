package com.example.todopmr.Activities;


import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.todopmr.Modele.ListeToDo;
import com.example.todopmr.Modele.ProfilListeToDo;
import com.example.todopmr.ReponsesRetrofit.ReponseDeBase;
import com.example.todopmr.ReponsesRetrofit.ReponseHash;
import com.example.todopmr.ReponsesRetrofit.ReponseItem;
import com.example.todopmr.ReponsesRetrofit.ReponseItems;
import com.example.todopmr.ReponsesRetrofit.ReponseList;
import com.example.todopmr.ReponsesRetrofit.ReponseLists;
import com.example.todopmr.ReponsesRetrofit.ReponseUsers;
import com.example.todopmr.Room.AppDatabase;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class GenericActivity extends AppCompatActivity {

    public ToDoApiInterface toDoInterface;
    private String urlAPI;
    private String hash;
    private List<ProfilListeToDo> users;
    private List<ListeToDo> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(this);
        hash = settings.getString("hash", "");
        urlAPI = settings.getString("urlAPI", "http://tomnab.fr/todo-api/");
    }

    /*
    Affiche l'information dans le Logcat et dans un Toast.
     */
    public void alerter(String s) {
        Log.i("PMR",s);
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }

    /*
    Créé une liste de tous les pseudos utilisés à partir du JSON,
    pour faciliter l'autocomplétion et l'affichage des pseudos dans Settings.
     */
    public void afficherPseudos(String hash) {
        toDoInterface.recupUsers(hash).enqueue(new Callback<ReponseUsers>() {
            public void onResponse(Call<ReponseUsers> call, Response<ReponseUsers> response) {
                if (response.isSuccessful() && response.body().success) {
                    String histo = "";
                    for (int i=0 ; i < response.body().users.size() ; i++) {
                        histo += response.body().users.get(i).getLogin() + "; ";
                    }
                    SharedPreferences newSettings = PreferenceManager.getDefaultSharedPreferences(GenericActivity.this);
                    final SharedPreferences.Editor editor = newSettings.edit();
                    editor.putString("historique", histo);
                    editor.commit();
                }
            }
            @Override
            public void onFailure(Call<ReponseUsers> call, Throwable t) {
                alerter("Il y a un problème : afficher pseudos");
            }
        });
    }

    /*
    Actualise la langue de l'application.
    Trois choix sont possibles : Francais, Anglais, Espagnol.
     */
    public void actualiserLangue(String langue) {
        Locale locale = new Locale(langue);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    /*
    Verifie l'état du réseau internet, renvoie un booléen correspondant à une connexion reussie ou non.
     */
    public boolean verifReseau()
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

        this.alerter(sType);
        return bStatut;
    }

    /*
    Initialisation de Retrofit.
     */
    public void initRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        toDoInterface = retrofit.create(ToDoApiInterface.class);
    }

    public interface ToDoApiInterface {
        @POST("authenticate")
        Call<ReponseHash> authenticate(@Query("user") String user, @Query("password") String password);

        @GET("users")
        //Renvoie version, success, status, users[]
        Call<ReponseUsers> recupUsers(@Query("hash") String hash);

        @GET("lists")
        //Renvoie version, success, status, lists[] (utilisateur courant)
        Call<ReponseLists> recupLists(@Query("hash") String hash);

        @GET("lists/{idListe}/items")
        //Renvoie version, success, status, items []
        Call<ReponseItems> recupItems(@Path("idListe") int idListe, @Query("hash") String hash);

        @POST("lists")
        //Renvoie version, success, status, list[id, label]
        Call<ReponseList> addList(@Query("hash") String hash, @Query("label") String label);

        @POST("lists/{idListe}/items")
        //Renvoie version, succes, status, items[]
        Call<ReponseItem> addItem(@Path("idListe") int idListe, @Query("hash") String hash, @Query("label") String label);

        @PUT("lists/{idListe}/items/{idItem}")
        //Renvoie version, succes, status, item[]
        //Check = 1 veut dire oui
        Call<ReponseItem> checkItem(@Path("idListe") int idListe, @Path("idItem") int idItem, @Query("hash") String hash, @Query("check") int etat);

        @DELETE("lists/{idListe}")
        //Renvoie version, success, status
        Call<ReponseDeBase> supressList(@Path("idListe") int idListe, @Query("hash") String hash);

        @DELETE("lists/{idListe}/items/{idItem}")
        //Renvoie version, success, status
        Call<ReponseDeBase> supressItem(@Path("idListe") int idListe, @Path("idItem") int idItem, @Query("hash") String hash);

    }
}

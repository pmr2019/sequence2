package fr.syned.sequence1_todolist;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.syned.sequence1_todolist.activities.database.AppDatabase;
import fr.syned.sequence1_todolist.activities.database.User;
import fr.syned.sequence1_todolist.activities.network.RequestQueueInstance;
import fr.syned.sequence1_todolist.model.Profile;

public class CustomApplication extends Application {

    public static List<Profile> profilesList;
    public static String filename = "profiles.JSON";
    public static final String EXTRA_USERNAME = "fr.syned.sequence1_todolist.USERNAME";
    public static final String EXTRA_HASH = "fr.syned.sequence1_todolist.HASH";
    public static final String EXTRA_PROFILE = "fr.syned.sequence1_todolist.PROFILE";
    public static final String EXTRA_TODOLIST = "fr.syned.sequence1_todolist.TODOLIST";
    public static final String EXTRA_TASK = "fr.syned.sequence1_todolist.TASK";
    public static final String EXTRA_UUID = "fr.syned.sequence1_todolist.UUID";
    public static final String EXTRA_ID = "fr.syned.sequence1_todolist.ID";

    public static final int PICK_CONTACT_REQUEST = 1;
    public static final String TAG = "ToDoList Application";

    public static AppDatabase database;
    public static Executor executor = Executors.newSingleThreadExecutor();
    public static HashMap<Integer, Pair<Integer, Boolean>> changedCheckboxes = new HashMap<>();
    public static String hash;
    public static HashMap<UUID, Pair<Integer, String>> addedTasks = new HashMap<>();

    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Custom App");
        hash=null;
//        profilesList = getProfilesFromFile();
//
//        // TODO: chercher un moyen de faire ça dans le constructeur par défaut de Profile
//        if (profilesList != null) {
//            for (Profile p : profilesList) {
//                p.onDeserialization();
//                for (ToDoList t : p.getToDoLists()) {
//                    t.onDeserialization();
//                }
//            }
//        }

        // get or build database.

        final Context context = getApplicationContext();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database = Room.databaseBuilder(context, AppDatabase.class, "users").build();
            }
        });

        //new DatabaseAsyncTask(getApplicationContext()).execute();
        final Handler handler = new Handler();
        final int delay = 10000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(hash!=null){
                    if (checkNetwork(getApplicationContext())) {

                        for (Map.Entry entry : addedTasks.entrySet()) {
                            String url = "http://tomnab.fr/todo-api/lists/" + ((Pair) entry.getValue()).first + "/items?label=" + ((Pair) entry.getValue()).second;

                            StringRequest request = new StringRequest(Request.Method.POST, url, null, null) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("hash", hash);

                                    return params;
                                }
                            };
                            RequestQueueInstance instance = RequestQueueInstance.getInstance(getApplicationContext());
                            instance.addToRequestQueue(request);
                            Log.i(TAG, "run: Added Task!");
                        }

                        addedTasks = new HashMap<>();

                        for (Map.Entry entry : changedCheckboxes.entrySet()) {
                            String url = "http://tomnab.fr/todo-api/lists/" + ((Pair) entry.getValue()).first + "/items/" + entry.getKey() + "?check=" + ((Boolean) ((Pair) entry.getValue()).second ? 1 : 0);

                            StringRequest request = new StringRequest(Request.Method.PUT, url, null, null) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("hash", hash);

                                    return params;
                                }
                            };
                            RequestQueueInstance instance = RequestQueueInstance.getInstance(getApplicationContext());
                            instance.addToRequestQueue(request);
                            Log.i(TAG, "run: Changed Checkbox!");
                        }

                        changedCheckboxes = new HashMap<>();
                    }
                }
                //do something
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    private List<Profile> getProfilesFromFile() {
        Gson gson = new Gson();
        List<Profile> profilesList = new ArrayList<>();
        try {
            FileInputStream inputStream = openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream), StandardCharsets.UTF_8));
            profilesList = gson.fromJson(br, new TypeToken<List<Profile>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profilesList;
    }

//    private static class DatabaseAsyncTask extends AsyncTask<String, Void, String> {
//        private Context context;
//
//        public DatabaseAsyncTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            database = Room.databaseBuilder(context, AppDatabase.class, "users").build();
//            return "";
//        }
//
//        @Override
//        protected void onPostExecute(String str) {
//
//        }
//    }
public boolean checkNetwork(Context context)
{
    // On vérifie si le réseau est disponible,
    // si oui on change le statut du bouton de connexion
    ConnectivityManager cnMngr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
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

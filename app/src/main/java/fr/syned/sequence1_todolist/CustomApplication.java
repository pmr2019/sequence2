package fr.syned.sequence1_todolist;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.syned.sequence1_todolist.activities.database.AppDatabase;
import fr.syned.sequence1_todolist.activities.database.User;
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

    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Custom App");
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
}

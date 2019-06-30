package fr.syned.sequence1_todolist.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Pair;

import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.syned.sequence1_todolist.CustomApplication;
import fr.syned.sequence1_todolist.activities.MainActivity;
import fr.syned.sequence1_todolist.activities.database.AppDatabase;
import fr.syned.sequence1_todolist.activities.database.User;
import fr.syned.sequence1_todolist.activities.network.RequestQueueInstance;

import static fr.syned.sequence1_todolist.CustomApplication.TAG;
import static fr.syned.sequence1_todolist.CustomApplication.executor;
import static fr.syned.sequence1_todolist.CustomApplication.profilesList;
import static fr.syned.sequence1_todolist.activities.ProfileActivity.profile;

public class Task implements Serializable {

    private UUID id;
    private int JsonId;
    private int toDoListJsonId;
    private String hash;
    private String name;
    private String description;
    private String label;
    private boolean isDone;
    private Date creationDate;
    private Date doneDate;

    public Task() {
        this.id = UUID.randomUUID();
        this.isDone = false;
        this.creationDate = Calendar.getInstance().getTime();
    }

    public Task(String name){
        this();
        this.name = name;
    }

    public Task(String taskName, String checked) {
        this(taskName);
        this.isDone = checked.equals("1");
    }

    public Task(String taskId, String taskName, String checked, String toDoListJsonId, String hash) {
        this(taskName);
        this.JsonId = Integer.parseInt(taskId);
        this.toDoListJsonId = Integer.parseInt(toDoListJsonId);
        this.hash = hash;
        this.isDone = checked.equals("1");
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public boolean toggleCheckbox() {
        this.isDone = !isDone;
        if (isDone) doneDate = Calendar.getInstance().getTime();
        else doneDate = null;

        return isDone;
    }

    public boolean toggleCheckbox(final Context context) {
        this.isDone = !isDone;
        if (isDone) doneDate = Calendar.getInstance().getTime();
        else doneDate = null;

        if (checkNetwork(context)) {
            String url = "http://tomnab.fr/todo-api/lists/" + this.toDoListJsonId + "/items/" + this.JsonId + "?check=" + (this.isDone ? 1 : 0);

            StringRequest request = new StringRequest(Request.Method.PUT, url, null, null) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("hash", hash);

                    return params;
                }
            };

            RequestQueueInstance instance = RequestQueueInstance.getInstance(context);
            instance.addToRequestQueue(request);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    CustomApplication.database.userDao().replaceAll(new User(profile));
                }
            });

        } else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    CustomApplication.database.userDao().replaceAll(new User(profile));
                }
            });
            CustomApplication.changedCheckboxes.put(this.JsonId,new Pair<>(this.toDoListJsonId,this.isDone));
        }


        return isDone;
    }

    public boolean isDone() {
        return isDone;
    }

    public int getJSONId() {
        return JsonId;
    }

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

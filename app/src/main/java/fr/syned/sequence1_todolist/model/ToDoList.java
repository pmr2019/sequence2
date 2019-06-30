package fr.syned.sequence1_todolist.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.syned.sequence1_todolist.CustomApplication;
import fr.syned.sequence1_todolist.activities.database.User;
import fr.syned.sequence1_todolist.activities.network.RequestQueueInstance;
import fr.syned.sequence1_todolist.activities.ProfileActivity;

import static fr.syned.sequence1_todolist.CustomApplication.TAG;
import static fr.syned.sequence1_todolist.CustomApplication.executor;
import static fr.syned.sequence1_todolist.activities.ProfileActivity.profile;

public class ToDoList implements Serializable {

    private UUID id;
    private String name;
    private boolean isArchived;
    private ArrayList<Task> taskList;
    private String JSONid;
    private String hash;

    private transient HashMap<UUID, Task> taskMap;

    public ToDoList() {
        this.id = UUID.randomUUID();
        this.isArchived = false;
        this.taskList = new ArrayList<>();
        onDeserialization();
    }

    public ToDoList(String name) {
        this();
        this.name = name;
    }
    public ToDoList(String id, String label, final String hash, Context c){
        this();
        this.name = label;
        final String fhash = hash;
        final String fid = id;
        this.JSONid = id;
        this.hash = hash;
        String url = "http://tomnab.fr/todo-api/lists/"+id+"/items";
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ProfileActivity.completeList((JSONArray)response.get("items"), fid, hash);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("hash", fhash);

                return params;
            }
        };
        RequestQueueInstance instance = RequestQueueInstance.getInstance(c);
        instance.addToRequestQueue(jsonObjectRequest);
    }
    public void onDeserialization() {
        taskMap = new HashMap<>();
        for (Task t : taskList) {
            taskMap.put(t.getId(), t);
        }
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void addTask(Task task) {
        this.taskList.add(task);
        this.taskMap.put(task.getId(), task);
    }

    public void addTask(String name, Context context) {
        final Task task = new Task(name);
        task.setToDoListJsonId(this.JSONid);
        this.taskList.add(task);
        this.taskMap.put(task.getId(), task);

        if (checkNetwork(context)) {
            String url = "http://tomnab.fr/todo-api/lists/" + this.JSONid + "/items?label=" + name;

            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                task.setJsonId(Integer.valueOf(((JSONObject)response.get("items")).get("id").toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("hash", hash);

                    return params;
                }
            };

            RequestQueueInstance instance = RequestQueueInstance.getInstance(context);
            instance.addToRequestQueue(request);
            Log.i(TAG, "addTask: API + DB");

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
            CustomApplication.addedTasks.put(task.getId(), new Pair<>(Integer.valueOf(this.JSONid), name));
            Log.i(TAG, "addTask: DB only");
        }
    }

    public Task getTask(UUID taskId) {
        return taskMap.get(taskId);
    }
    public String getJSONId(){
        return JSONid;
    }
    public void replaceTask(Task oldTask, Task newTask) {
        Collections.replaceAll(taskList, oldTask, newTask);
        taskMap.remove(oldTask.getId());
        taskMap.put(newTask.getId(), newTask);
    }

    public List<Task> getTasks() {
        return taskList;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        onDeserialization();
    }

    public void addTasks(JSONArray items, String toDoListId, String hash) {
        for (int i=0; i< items.length(); i++) {
            String taskId = null;
            String taskName = null;
            String checked = null;
            try {
                taskId = ((JSONObject)items.get(i)).get("id").toString();
                taskName = ((JSONObject)items.get(i)).get("label").toString();
                checked = ((JSONObject)items.get(i)).get("checked").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Task task = new Task(taskId, taskName, checked, toDoListId, hash);
            this.taskList.add(task);
            this.taskMap.put(task.getId(), task);
        }
    }

    public String getHash() {
        return hash;
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

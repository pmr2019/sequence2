package fr.syned.sequence1_todolist.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.syned.sequence1_todolist.Activities.Network.RequestQueueInstance;
import fr.syned.sequence1_todolist.Activities.ProfileActivity;

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

    public boolean toggleCheckbox(Context context) {
        this.isDone = !isDone;
        if (isDone) doneDate = Calendar.getInstance().getTime();
        else doneDate = null;

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

        return isDone;
    }

    public boolean isDone() {
        return isDone;
    }

    public int getJSONId() {
        return JsonId;
    }
}

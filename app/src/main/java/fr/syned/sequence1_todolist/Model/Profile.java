package fr.syned.sequence1_todolist.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.syned.sequence1_todolist.Activities.Network.RequestQueueInstance;
import fr.syned.sequence1_todolist.Activities.ProfileActivity;

public class Profile implements Serializable {
    private String username;
    private ArrayList<ToDoList> toDoLists;

    private transient Map<UUID, ToDoList> toDoListMap;

    public Profile(String username) {
        this.username = username;
        this.toDoLists = new ArrayList<>();
        onDeserialization();
    }
    public Profile(String username, String hash, Context c){
        final String fhash = hash;
        final Context co = c;
        this.username = username;
        this.toDoLists = new ArrayList<>();
        String url = "http://tomnab.fr/todo-api/lists";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("TODO", "onResponse: " + response.get("lists"));
                            ProfileActivity.completeProfile((JSONArray)response.get("lists"), fhash, co);
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
        onDeserialization();
    }

    public void onDeserialization() {
        toDoListMap = new HashMap<>();
        for (ToDoList tdl : toDoLists) {
            toDoListMap.put(tdl.getId(), tdl);
        }
    }

    public void addToDoList(String name) {
        ToDoList toDoList = new ToDoList(name);
        this.toDoLists.add(toDoList);
        this.toDoListMap.put(toDoList.getId(), toDoList);
    }

    public void addToDoList(JSONObject obj, String hash, Context c) {
        Log.i("TODO", "addToDoList: ");
        ToDoList toDoList = null;
        try {
            Log.i("TODO", "addToDoList id: " + obj.get("id").toString());
            Log.i("TODO", "addToDoList label: " + obj.get("label").toString());
            toDoList = new ToDoList(obj.get("id").toString(),obj.get("label").toString(), hash, c);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.toDoLists.add(toDoList);
        this.toDoListMap.put(toDoList.getId(), toDoList);
    }

    public ToDoList getList(String id){
        ToDoList toDoList = null;
        for (ToDoList tdl : toDoLists) {
            if (tdl.getJSONId() == id) toDoList = tdl;
        }
        return toDoList;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<ToDoList> getToDoLists() {
        return toDoLists;
    }


    public ToDoList getToDoList(UUID toDoListId) {
        return toDoListMap.get(toDoListId);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        onDeserialization();
    }
}

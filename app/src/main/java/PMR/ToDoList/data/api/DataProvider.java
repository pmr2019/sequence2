package PMR.ToDoList.data.api;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import PMR.ToDoList.data.Model.Task;
import PMR.ToDoList.data.Model.ToDoList;
import PMR.ToDoList.data.Model.User;

public class DataProvider {

    //PARTIE REQUETE

    //Fonction permettant la conversion d'un Stream en String

    private String convertStreamToString(InputStream in) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Fonction permettant la création d'une requete URL à partir d'une URL String

    public String requete(String qs, String methode) {

        String urlData = "http://tomnab.fr/todo-api/";

        if (qs != null)
        {

            try {
                URL url = new URL(urlData + qs);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(methode);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String txtReponse = convertStreamToString(in);
                urlConnection.disconnect();
                return txtReponse;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return "";
    }

    //PARTIE GET, POST, PUT

    //Fonction permettant de générer la portion d'URL (String) pour récupérer un hash

    public String getHash (String pseudo, String password, String methode) throws JSONException {


        String qs = "authenticate?user=" + pseudo + "&password=" + password;

        String response = requete(qs, methode);

        JSONObject json = new JSONObject(response);

        String hash = json.getString("hash");

        return hash;
    }

    //Fonction permettant de récupérer l'id d'un utilisateur à partir du hash

    public String getId (String pseudo, String hash,String methode) throws JSONException {


        String qs = "users?hash="+hash;

        String response = requete(qs, methode);

        JSONObject json = new JSONObject(response);

        JSONArray users = json.getJSONArray("users");

        for (int i=0; i<users.length(); i++){
            JSONObject user = users.getJSONObject(i);

            int id = user.getInt("id");
            String pseudoUser = user.getString("pseudo");

            if (pseudoUser.equals(pseudo)){
                return (""+id);
            }
        }

        return ""+-1;
    }

    //Fonction permettant de générer la portion d'URL (String) necessaire à la récupération d'une
    //liste de ToDoLists à partir d'un hash

    public ArrayList<ToDoList> getToDoLists (String hash, String methode, int keyUser) throws JSONException {

        String qs = "lists?hash=" + hash;
        String response = requete (qs, methode);

        JSONObject json = new JSONObject(response);
        JSONArray jsonArray = json.getJSONArray("lists");

        ArrayList<ToDoList> myToDoLists = new ArrayList<>();

        for (int i=0; i<jsonArray.length(); i++){
            JSONObject ToDoList = jsonArray.getJSONObject(i);

            int id = ToDoList.getInt("id");
            String label = ToDoList.getString("label");

            ToDoList myToDoList = new ToDoList(id, label,keyUser);
            myToDoLists.add(myToDoList);
        }

        return myToDoLists;
    }

    //Fonction permettant de générer la portion d'URL (String) nécessaire à la récupération d'une
    //liste d'items à partir d'un hash et d'un id de liste

    public ArrayList<Task> getTasks (String hash, int id, String methode) throws JSONException {

        String qs = "lists/" + id + "/items?hash=" + hash;
        String response = requete (qs, methode);

        JSONObject json = new JSONObject(response);
        JSONArray jsonArray = json.getJSONArray("items");

        ArrayList<Task> myTasks = new ArrayList<>();

        for (int i=0; i<jsonArray.length(); i++){
            JSONObject task = jsonArray.getJSONObject(i);

            int idTask = task.getInt("id");
            String labelTask = task.getString("label");
            String url = task.getString("url");
            int checked = task.getInt("checked");

            Task myTask = new Task(idTask, labelTask,id, checked);
            myTasks.add(myTask);
        }

        return myTasks;
    }

    //Fonction permettant de générer la portion d'URL (String) nécessaire à la création d'une
    //nouvelle ToDolist à partir d'un label

    public void postToDoList (String label, String hash, String methode){

        String qs = "/lists?label=" + label + "&hash=" + hash;
        requete (qs, methode);
    }

    //Fonction permettant de générer la portion d'URL (String) nécessaire à la création d'un
    //nouvel item à partir d'un label

    public void postItem (int idList, String label, String hash, String methode){

        String qs = "/lists/" + idList + "/items?label=" + label + "&hash=" + hash;
        requete (qs, methode);
    }

    //Fonction permettant de générer la portion d'URL (String) nécessaire pour cocher ou décocher
    //un item

    public void itemChecked (int idList, int idTask, int checked, String hash, String methode){

        String qs = "/lists/" + idList + "/items/" + idTask + "?check=" + checked + "&hash=" + hash;
        requete (qs, methode);
    }
}

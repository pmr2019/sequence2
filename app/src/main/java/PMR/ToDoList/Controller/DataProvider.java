package PMR.ToDoList.Controller;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

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
import java.util.List;

import PMR.ToDoList.Model.ToDoList;
import PMR.ToDoList.Model.User;

public class DataProvider {

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

    public String getHash (String pseudo, String password, String methode) throws JSONException {


        String qs = "authenticate?user=" + pseudo + "&password=" + password;

        String response = requete(qs, "POST");

        //Gson gson = new Gson();
        //User myUser = gson.fromJson(response, User.class);

        JSONObject json = new JSONObject(response);
        String hash = json.getString("hash");

        return hash;
    }

/*    public List<ToDoList> getToDoLists (String hash, String methode){

        String qs = "lists?hash=" + hash;
        String response = requete (qs, "GET");

        JSONObject json = new JSONObject(response);

    }*/
}

package com.example.todolist;

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

public class DataProvider {

    private String convertStreamToString(InputStream in) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
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

    public String requete(String qs,String methode) {
        if (qs != null)
        {
            try {
                URL url = new URL(qs);
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

    //On renvoie le hash si l'utilisateur existe, sinon on renvoi ""
    public String getHash(String myUrl, String pseudo, String motDePasse, String methode) {
        String URL = myUrl + "/authenticate?user=" + pseudo + "&password=" + motDePasse;
        String sJson = requete(URL,methode);
        JSONObject oJSon = null;
        try {
            oJSon = new JSONObject(sJson);
            if (oJSon.getBoolean("success")) {
                return oJSon.getString("hash");
            }
            else {
                return "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "erreur avec l'objet Json";
        }
    }

    //On renvoie la liste des todolist sous forme de Json
    public JSONArray getLists(String myUrl, String hash){
        String URL = myUrl + "/lists?hash="+ hash;
        String sJson = requete(URL,"GET");
        JSONObject oJSon = null;
        try {
            oJSon = new JSONObject(sJson);
            if (oJSon.getBoolean("success")) {
                return oJSon.getJSONArray("lists");
            }
            else {
                return (new JSONArray());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return (new JSONArray());
        }

    }

    //On renvoie la liste des todolist sous forme de Json
    public JSONArray getItems(String myUrl, String hash, String id){
        String URL = myUrl + "/lists/" + id + "/items?hash="+ hash;
        String sJson = requete(URL,"GET");
        JSONObject oJSon = null;
        try {
            oJSon = new JSONObject(sJson);
            if (oJSon.getBoolean("success")) {
                return oJSon.getJSONArray("items");
            }
            else {
                return (new JSONArray());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return (new JSONArray());
        }

    }
}



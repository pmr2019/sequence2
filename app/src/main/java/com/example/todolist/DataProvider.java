package com.example.todolist;

import android.content.Context;

import com.example.todolist.database.TodoListDataBase;
import com.example.todolist.database.dao.ItemDao;
import com.example.todolist.database.dao.ListDao;
import com.example.todolist.database.dao.UserDao;

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
import java.util.List;

public class DataProvider {

    private UserDao userDao;
    private ListDao listDao;
    private ItemDao itemDao;

    public DataProvider(Context context) {
        userDao = TodoListDataBase.getDatabase(context).userDao();
        listDao = TodoListDataBase.getDatabase(context).listDao();
        itemDao = TodoListDataBase.getDatabase(context).itemDao();
    }

    public List<ProfilListeToDo> loadUsers() {
        return (List<ProfilListeToDo>) userDao.getUser();
    }
    public List<ListeToDo> loadList(int idUser){
        return (List<ListeToDo>) listDao.getLists(idUser);
    }
    public List<ItemToDo> loadItem(int idList) {
        return (List<ItemToDo>) itemDao.getItem(idList);
    }
    public int updateItem(boolean fait, int idItem){
        return (int) itemDao.updateItem(fait,idItem);
    }
    public boolean getFaitBdd(int idItem){
        return (boolean) itemDao.getFaitBdd(idItem);
    }
    public List<ItemToDo> getItemModifie(){
        return (List<ItemToDo>) itemDao.getItemModifie();
    }


    public void insertUser(ProfilListeToDo user){ userDao.insertUser(user); }
    public void insertList(ListeToDo list) {listDao.insertList(list);}
    public void insertItem(ItemToDo item) {itemDao.insertItem(item);}

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

    public JSONArray getItems(String myUrl, String hash, int id){
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

    public JSONArray getListUser(String myUrl, String hash) {
        String URL = myUrl + "/users?hash="+ hash;
        String sJson = requete(URL,"GET");
        JSONObject oJSon = null;
        try {
            oJSon = new JSONObject(sJson);
            if (oJSon.getBoolean("success")) {
                return oJSon.getJSONArray("users");
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



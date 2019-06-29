package com.example.todo.models;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.support.annotation.UiThread;
import android.util.Log;

import com.example.todo.API_models.RetroMain;
import com.example.todo.API_models.TodoInterface;
import com.example.todo.Utils;
import com.example.todo.activities.ChoixListActivity;
import com.example.todo.database.MyDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataProvider {

    private static final String TAG = "DataProvider";

    /////////////////
    private List<Future> futures = new ArrayList<>();
    private final Handler uiHandler;
    //////////////////////


    private MyDatabase myDatabase;
    private TodoInterface service;
    private boolean isConnectedToInternet;

    public DataProvider(Context context) {
        uiHandler = new Handler(context.getMainLooper());
        myDatabase = MyDatabase.getInstance(context);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        this.isConnectedToInternet = settings.getBoolean("isConnectedToInternet",false);
        String baseUrl = settings.getString("APIurl", "http://tomnab.fr/");
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
        service = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TodoInterface.class);
    }

    /// DEBUG ///

    public void insertProfilDB(ProfilListeToDo profilListeToDo){
        Future future = Utils.BACKGROUND.submit(()-> {
            myDatabase.profilListeToDoDao().addProfilListeToDo(profilListeToDo);
            for (ListeToDo listeToDo : profilListeToDo.getMesListeToDo()) {
                myDatabase.listeToDoDao().addListeToDo(listeToDo);
                for (ItemToDo itemToDo : listeToDo.getLesItems()) {
                    myDatabase.itemToDoDao().addItemToDo(itemToDo);
                }
            }
        });
        futures.add(future);
    }

    /////////////

    /**
     * Return a DataResponse with a profilListeToDo initialized with only its login
     * from the API or the DB, and the hash if the data comes from the API. To choose the database (API or SQL), the function checks
     * the preference isConnectedToInternet.
     * @param pseudo
     * @param password
     * @param listener
     */
    public void authenticate(String pseudo, String password, final PostsListener listener){
        Future future = Utils.BACKGROUND.submit(() -> {
            if (isConnectedToInternet && internetCheck()) {
                //Verify pseudo & password with the API
                Call<RetroMain> call = service.authenticate(pseudo, password);
                try {
                    Response<RetroMain> response = call.execute();
                    if (response.body() != null) {
                        RetroMain retroMain = response.body();
                        if (retroMain.isSuccess()) {
                            DataResponse dR = new DataResponse();
                            dR.setHash(retroMain.getHash());
                            ProfilListeToDo profil = new ProfilListeToDo();
                            profil.setLogin(pseudo);
                            dR.setProfilListeToDo(profil);
                            uiHandler.post(() -> {
                                listener.onSuccess(dR);
                            });
                        } else {
                            Log.d(TAG, "onResponse: http code : "+retroMain.getStatus());
                            uiHandler.post(()-> {
                                listener.onError("onResponse: http code : "+retroMain.getStatus());
                            });
                        }
                    } else {
                        Log.d(TAG, "onResponse: empty response. HTTP CODE : "+response.code());
                        uiHandler.post(() -> {
                            listener.onError("onResponse: empty response. HTTP CODE : "+response.code());
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    uiHandler.post(() -> {
                        listener.onError("onFailure from API call.");
                    });
                }
            } else if (!isConnectedToInternet) {
                ProfilListeToDo profil;
                profil = myDatabase.profilListeToDoDao().getProfilListeToDo(pseudo);
                if (profil != null) {
                    DataResponse dR = new DataResponse();
                    dR.setProfilListeToDo(profil);
                    uiHandler.post(() -> {
                        listener.onSuccess(dR);
                    });
                } else {
                    uiHandler.post(() -> {
                        listener.onError("Pseudo unknown in cache.");
                    });
                }
            } else if (isConnectedToInternet && !internetCheck()) {
                uiHandler.post(() -> {
                    listener.onError("Internet connection lost.");
                });
            }
        });
        futures.add(future);
    }

    /**
     * Return a DataResponse with a profilListeToDo initialized from the DB.
     * @param pseudo
     * @param listener
     */
    public void getProfilToDo(String pseudo, final PostsListener listener){
        Future future = Utils.BACKGROUND.submit(() -> {
            DataResponse dR = new DataResponse();
            dR.setProfilListeToDo(getProfileFromDB(pseudo));
            uiHandler.post(() -> {
                listener.onSuccess(dR);
            });
        });
        futures.add(future);
    }

    public void getListeToDo(String pseudo, int idList, final PostsListener listener){
        Future future = Utils.BACKGROUND.submit(() -> {
            DataResponse dR = new DataResponse();
            dR.setListeToDo(getListeToDoFromDB(pseudo, idList));
            uiHandler.post(() -> {
                listener.onSuccess(dR);
            });
        });
        futures.add(future);
    }



    /**
     * Update the profil in the api with the data present in the database (delete and add data).
     * @param context
     * @param pseudo
     * @param listener
     */
    public void updateAPIfromDB(Context context, String pseudo, final PostsListener listener) {
        Future future = Utils.BACKGROUND.submit(() -> {
            if (isConnectedToInternet && internetCheck()) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                String hash = settings.getString("hash","");

                ProfilListeToDo profilDB = getProfileFromDB(pseudo);
                if(profilDB == null){
                    // If authenticate succeed but no data in DB.
                    uiHandler.post(() -> {
                        listener.onSuccess(new DataResponse());
                    });
                    return;
                }
                ProfilListeToDo profilAPI = getProfileFromAPI(hash, pseudo);

                // Adding the data in the API not present
                if (profilAPI == null) {
                    uiHandler.post(() -> {
                        listener.onError("Profil not found in the API.");
                    });
                    return;
                }
                for (ListeToDo listeDB : profilDB.getMesListeToDo()) {

                    ListeToDo listeAPI = null;
                    for (ListeToDo tmp : profilAPI.getMesListeToDo()){
                        if (tmp.getId() == listeDB.getId()){
                            listeAPI = tmp;
                            break;
                        }
                    }
                    if (listeAPI == null){
                        // Add the list in the API
                        addListeInAPI(hash, listeDB);
                        continue;
                    }
                    for (ItemToDo itemDB : listeDB.getLesItems()) {

                        ItemToDo itemAPI = null;
                        for (ItemToDo tmp : listeAPI.getLesItems()) {
                            if (tmp.getId() == itemDB.getId()) {
                                itemAPI = tmp;
                                break;
                            }
                        }
                        if (itemAPI == null) {
                            // Add the item in the API
                            Log.d(TAG, "updateAPIfromDB: addItem="+itemDB.toString());
                            addItemInAPI(hash, itemDB);
                            continue;
                        }
                        if ((!itemDB.getDescription().equals(itemAPI.getDescription())) || (itemDB.isFait() != itemAPI.isFait())) {
                            // if itemAPI and itemDB are differents :
                            updateItemInAPI(hash, itemDB);
                        }
                    }
                }

                uiHandler.post(() -> {
                    listener.onSuccess(null);
                });
            } else {
                uiHandler.post(() -> {
                    listener.onError("No Internet connection.");
                });
            }
        });
        futures.add(future);
    }

    /**
     * update the local profil with the data from the api (delete and add data).
     * @param context
     * @param pseudo
     * @param listener
     */
    public void updateDBfromAPI(Context context, String pseudo, final PostsListener listener){
        Future future = Utils.BACKGROUND.submit(() -> {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            String hash = settings.getString("hash","");

            ProfilListeToDo profilAPI = getProfileFromAPI(hash, pseudo);
            ProfilListeToDo profilDB = getProfileFromDB(profilAPI.getLogin());
            if (profilDB == null) {
                addProfilInDB(profilAPI);
                uiHandler.post(() -> {
                    listener.onSuccess(new DataResponse());
                });
                return;
            }

            // Adding the data in the DB from the API
            if (profilDB == null) {
                profilDB = new ProfilListeToDo(profilAPI.getLogin(), profilAPI.getMesListeToDo());
                myDatabase.profilListeToDoDao().addProfilListeToDo(profilDB);
            }
            for (ListeToDo listeAPI : profilAPI.getMesListeToDo()) {

                ListeToDo listeDB = null;
                for (ListeToDo tmp : profilDB.getMesListeToDo()) {
                    if (tmp.getId() == listeAPI.getId()) {
                        listeDB = tmp;
                        break;
                    }
                }
                if (listeDB == null) {
                    listeDB = new ListeToDo(listeAPI.getId(), listeAPI.getProfilListeToDoId(), listeAPI.getTitreListeToDo(), listeAPI.getLesItems());
                    myDatabase.listeToDoDao().addListeToDo(listeDB);
                }
                for (ItemToDo itemAPI : listeAPI.getLesItems()) {
                    ItemToDo itemDB = null;
                    for (ItemToDo tmp : listeDB.getLesItems()) {
                        if (tmp.getId() == itemAPI.getId()) {
                            itemDB = tmp;
                            break;
                        }
                    }
                    if (itemDB == null) {
                        itemDB = new ItemToDo(itemAPI.getId(), itemAPI.getListeToDoId(), itemAPI.getDescription(), itemAPI.isFait());
                        myDatabase.itemToDoDao().addItemToDo(itemDB);
                        continue;
                    }
                    if ((!itemDB.getDescription().equals(itemAPI.getDescription())) || (itemDB.isFait() != itemAPI.isFait())) {
                        // if itemAPI and itemDB are differents :
                        updateItemInDB(itemAPI);
                    }
                }
            }

            // Deleting the data in the profile not present in the API
            for (ListeToDo listeDB : profilDB.getMesListeToDo()) {

                ListeToDo listeAPI = null;
                for (ListeToDo tmp : profilAPI.getMesListeToDo()) {
                    if (tmp.getId() == listeDB.getId()) {
                        listeAPI = tmp;
                        break;
                    }
                }
                if (listeAPI == null) {
                    // If the listeToDo is present in the DB but not in the API, the listeDB is deleted
                    delListeInDB(listeDB);
                    continue;
                }

                for (ItemToDo itemDB : listeDB.getLesItems()) {
                    ItemToDo itemAPI = null;
                    for (ItemToDo tmp : listeAPI.getLesItems()) {
                        if (tmp.getId() == itemDB.getId()) {
                            itemAPI = tmp;
                            break;
                        }
                    }
                    if (itemAPI == null) {
                        // If the itemToDo is present in the DB but not in the API, the itemDB is deleted
                        delItemInDB(itemDB);
                        continue;
                    }
                }
            }

            uiHandler.post(() -> {
                listener.onSuccess(null);
            });
        });
        futures.add(future);
    }




    public void addListeToDo(Context context, ListeToDo listeToDo){
        Future future = Utils.BACKGROUND.submit(() -> {
            // Try to add to the API
            if (isConnectedToInternet && internetCheck()) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                String hash = settings.getString("hash","");
                if (hash != ""){
                    addListeInAPI(hash, listeToDo);
                }
            }
            // Add in the db
            addListeInDB(listeToDo);
        });
        futures.add(future);
    }

    public void delListeToDo(Context context, ListeToDo listeToDo){
        Future future = Utils.BACKGROUND.submit(() -> {
            // Try to add to the API
            if (isConnectedToInternet && internetCheck()) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                String hash = settings.getString("hash","");
                if (hash != ""){
                    delListeInAPI(hash, listeToDo);
                }
            }
            // Add in the db
            delListeInDB(listeToDo);
        });
        futures.add(future);
    }

    public void addItemToDo(Context context, ItemToDo itemToDo){
        Future future = Utils.BACKGROUND.submit(() -> {
            // Try to add to the API
            if (isConnectedToInternet && internetCheck()) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                String hash = settings.getString("hash","");
                if (hash != ""){
                    addItemInAPI(hash, itemToDo);
                }
            }
            // Add in the db
            addItemInDB(itemToDo);
        });
        futures.add(future);
    }

    public void delItemToDo(Context context, ItemToDo itemToDo){
        Future future = Utils.BACKGROUND.submit(() -> {
            // Try to add to the API
            if (isConnectedToInternet && internetCheck()) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                String hash = settings.getString("hash","");
                if (hash != ""){
                    delItemInAPI(hash, itemToDo);
                }
            }
            // Add in the db
            delItemInDB(itemToDo);
        });
        futures.add(future);
    }

    public void updateItemToDo(Context context, ItemToDo itemToDo){
        Future future = Utils.BACKGROUND.submit(() -> {
            // Try to add to the API
            if (isConnectedToInternet && internetCheck()) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                String hash = settings.getString("hash","");
                if (hash != ""){
                    updateItemInAPI(hash, itemToDo);
                }
            }
            // Add in the db
            updateItemInDB(itemToDo);
        });
        futures.add(future);
    }

    /// PRIVATE METHODS ///

    private ProfilListeToDo getProfileFromAPI(String hash, String pseudo){
        ProfilListeToDo profil = new ProfilListeToDo();
        profil.setLogin(pseudo);

        // Get profil and lists
        Call<RetroMain> call = service.getLists(hash);
        try {
            Response<RetroMain> response = call.execute();
            if (response.body() != null) {
                RetroMain retroMain = response.body();
                if (retroMain.isSuccess()) {
                    ArrayList<ListeToDo> listeToDos = new ArrayList<>();
                    for (RetroMain r : retroMain.getLists()) {
                        listeToDos.add(new ListeToDo(r.getId(), profil.getLogin(), r.getLabel(), new ArrayList<ItemToDo>()));
                    }
                    profil.setMesListeToDo(listeToDos);
                } else {
                    Log.d(TAG, "getProfileFromAPI onResponse: http code : "+retroMain.getStatus());
                }
            } else {
                Log.d(TAG, "getProfileFromAPI onResponse: empty response. HTTP CODE : "+response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get items
        for (ListeToDo listeToDo : profil.getMesListeToDo()) {
            call = service.getItems(hash, listeToDo.getId());
            try {
                Response<RetroMain> response = call.execute();
                if (response.body() != null) {
                    RetroMain retroMain = response.body();
                    if (retroMain.isSuccess()) {
                        ArrayList<ItemToDo> itemToDos = new ArrayList<>();
                        for (RetroMain r : retroMain.getItems()) {
                            itemToDos.add(new ItemToDo(r.getId(), listeToDo.getId(), r.getLabel(), (r.getChecked() == 1)));
                        }
                        listeToDo.setLesItems(itemToDos);
                    } else {
                        Log.d(TAG, "getProfileFromAPI onResponse: http code : " + retroMain.getStatus());
                    }
                } else {
                    Log.d(TAG, "getProfileFromAPI onResponse: empty response. HTTP CODE : " + response.code());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return profil;
    }

    private ProfilListeToDo getProfileFromDB(String pseudo) {
        ProfilListeToDo profil = myDatabase.profilListeToDoDao().getProfilListeToDo(pseudo);
        if (profil == null){
            return null;
        }
        List<ListeToDo> listeToDos = myDatabase.listeToDoDao().getAllListeToDo(profil.getLogin());
        if (listeToDos == null)
            return profil;
        profil.setMesListeToDo(new ArrayList<>(listeToDos));
        for (ListeToDo listeToDo : profil.getMesListeToDo()) {
            List<ItemToDo> itemToDos = myDatabase.itemToDoDao().getAllItemToDo(listeToDo.getId());
            if (itemToDos == null)
                continue;
            listeToDo.setLesItems(new ArrayList<>(itemToDos));
        }
        return profil;
    }

    private ListeToDo getListeToDoFromDB(String pseudo, int idList){
        ListeToDo listeToDo = myDatabase.listeToDoDao().getListeToDo(pseudo, idList);
        listeToDo.setLesItems(new ArrayList<>(myDatabase.itemToDoDao().getAllItemToDo(listeToDo.getId())));
        return listeToDo;
    }

    /**
     * Add the listeToDo passed in parameter to the API. When finished, the listeToDo's id
     * is updated in the DB.
     * @param hash
     * @param listeToDo
     */
    private void addListeInAPI(String hash, ListeToDo listeToDo){
        Call<RetroMain> call = service.addList(hash, listeToDo.getTitreListeToDo());
        try {
            Response<RetroMain> response = call.execute();
            if (response.body() != null) {
                RetroMain retroMain = response.body();
                if (retroMain.isSuccess()) {
                    listeToDo.setId(retroMain.getList().getId());
                    myDatabase.listeToDoDao().updateListeToDo(listeToDo);
                    for (ItemToDo itemToDo : listeToDo.getLesItems()){
                        addItemInAPI(hash, itemToDo);
                    }
                } else {
                    Log.d(TAG, "addListeAPI onResponse: http code : "+retroMain.getStatus());
                }
            } else {
                Log.d(TAG, "addListeAPI onResponse: empty response. HTTP CODE : "+response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delListeInAPI(String hash, ListeToDo listeToDo){
        Call<RetroMain> call = service.delList(hash, Integer.toString(listeToDo.getId()));
        try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add the listeToDo passed in parameter to the API. When finished, the listeToDo's id
     * is updated in the DB.
     * @param hash
     * @param itemToDo
     */
    private void addItemInAPI(String hash, ItemToDo itemToDo){
        Log.d(TAG, "addItemInAPI: params="+hash+Integer.toString(itemToDo.getListeToDoId())+itemToDo.getDescription()+"noURL");
        Call<RetroMain> call = service.addItem(hash, Integer.toString(itemToDo.getListeToDoId()), itemToDo.getDescription(), "noURL");
        try {
            Response<RetroMain> response = call.execute();
            if (response.body() != null) {
                RetroMain retroMain = response.body();
                if (retroMain.isSuccess()) {
                    itemToDo.setId(retroMain.getItem().getId());
                    myDatabase.itemToDoDao().updateItemToDo(itemToDo);
                } else {
                    Log.d(TAG, "addItemAPI onResponse: http code : "+retroMain.getStatus());
                }
            } else {
                Log.d(TAG, "addItemAPI onResponse: empty response. HTTP CODE : "+response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delItemInAPI(String hash, ItemToDo itemToDo){
        Call<RetroMain> call = service.delItem(hash, Integer.toString(itemToDo.getListeToDoId()), Integer.toString(itemToDo.getId()));
        try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean updateItemInAPI(String hash, ItemToDo item){

        // Get profil and lists
        Call<RetroMain> call = service.checkItem(hash, Integer.toString(item.getListeToDoId()), Integer.toString(item.getId()), (item.isFait() ? "1" : "0"));
        try {
            call.execute();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private void addProfilInDB(ProfilListeToDo profilListeToDo) {
        myDatabase.profilListeToDoDao().addProfilListeToDo(profilListeToDo);
        for (ListeToDo listeToDo : profilListeToDo.getMesListeToDo()){
            myDatabase.listeToDoDao().addListeToDo(listeToDo);
            for (ItemToDo itemToDo : listeToDo.getLesItems()){
                myDatabase.itemToDoDao().addItemToDo(itemToDo);
            }
        }
    }

    private void addListeInDB(ListeToDo listeToDo){
        myDatabase.listeToDoDao().addListeToDo(listeToDo);
        for (ItemToDo itemToDo : listeToDo.getLesItems()){
            myDatabase.itemToDoDao().addItemToDo(itemToDo);
        }
    }

    private void delListeInDB(ListeToDo listeToDo){
        myDatabase.listeToDoDao().delListeToDo(listeToDo);
        // CASCADE allow to delete all items child of this list
    }

    private void addItemInDB(ItemToDo itemToDo){
        myDatabase.itemToDoDao().addItemToDo(itemToDo);
    }

    private void delItemInDB(ItemToDo itemToDo){
        myDatabase.itemToDoDao().delItemToDo(itemToDo);
    }

    private void updateItemInDB(ItemToDo itemToDo){
        myDatabase.itemToDoDao().updateItemToDo(itemToDo);
    }

    ///////////////////

    public void stop() {
        for (Future future : futures) {
            if(!future.isDone()){
                future.cancel(true);
            }
        }
        futures.clear();
    }

    /**
     * Return true if connected to internet, false otherwise.
     * BE CAREFUL, this function can only be launched in a different
     * thread than the UI thread.
     * @return
     */
    public boolean internetCheck(){
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public interface PostsListener {
        @UiThread
        void onSuccess(DataResponse dataResponse);
        @UiThread
        void onError(String error);
    }

    public class DataResponse {

        private String hash="";
        private ProfilListeToDo profilListeToDo = new ProfilListeToDo();
        private ListeToDo listeToDo = new ListeToDo();

        public DataResponse() {
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public ProfilListeToDo getProfilListeToDo() {
            return profilListeToDo;
        }

        public void setProfilListeToDo(ProfilListeToDo profilListeToDo) {
            this.profilListeToDo = profilListeToDo;
        }

        public ListeToDo getListeToDo() {
            return listeToDo;
        }

        public void setListeToDo(ListeToDo listeToDo) {
            this.listeToDo = listeToDo;
        }
    }
}

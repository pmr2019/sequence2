package com.example.todoudou.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.example.todoudou.Constant;
import com.example.todoudou.Converter;
import com.example.todoudou.Reseau;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {

    /*----------------------- INTERFACE -----------------------*/
    public interface DataListener {
        void onDataReady(int type, Object... data);
    }

    /*----------------------- ATTRIBUTS -----------------------*/
    private Context my_context;
    private DataListener listener;

    private int session_idBDD = 0;
    private String session_hash = "";


    /*----------------------- SINGLETON -----------------------*/
    private static final DataProvider INSTANCE = new DataProvider();
    public static DataProvider getInstance(Context context) {
        INSTANCE.my_context = context;
        return INSTANCE;
    }
    private DataProvider() {}

    public void setCustomObjectListener(DataListener listener) {
        INSTANCE.listener = listener;
    }


    /*----------------------- METHODES PRIVEES UTILES -----------------------*/
    private Context getMy_context(){
        return INSTANCE.my_context;
    }
    private DataListener getListener(){
        return INSTANCE.listener;
    }
    private int getCacheIdBDD(){
        return INSTANCE.session_idBDD;
    }
    private String getCacheHash(){
        return INSTANCE.session_hash;
    }
    private void setSession_idBDD(int id){
        INSTANCE.session_idBDD = id;
    }
    private void setSession_hash(String hash){
        INSTANCE.session_hash = hash;
    }

    private String getPrefPseudo(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getMy_context());
        return settings.getString("pseudo","");
    }
    private String getPrefPass(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getMy_context());
        return settings.getString("pass","");
    }

    private void deb(String s){
        Log.i("debug2", s);
    }

    private void alerter(String s) {
        Toast myToast = Toast.makeText(getMy_context(),s,Toast.LENGTH_SHORT);
        myToast.show();
    }

    private boolean onLine(){
        boolean onLine = new Reseau(getMy_context()).verifReseau();
//        if(onLine)
//            alerter("ON line");
//        else
//            alerter("OFF line");
        return onLine;
    }

    /*----------------------- METHODES POUR ACTIVITY -----------------------*/

    public void getConnexionUser(String pseudo, String pass){
        if(onLine())
            new Connexion_User_API_AsyncTask().execute(pseudo, pass);
        else
            new Connexion_User_BDD_AsyncTask().execute(pseudo, pass);
    }

    public void getListToDo(){
        if(onLine())
            new GetList_API_AsyncTask().execute();
        else
            new GetList_BDD_AsyncTask().execute();
    }

    public void postListToDo(ListeToDo list){
        if(onLine())
            new PostList_API_AsyncTask().execute(list);
        else{
            listener.onDataReady(Constant.ACTION_IMPOSSIBLE, 0);
//                        new PostList_BDD_AsyncTask().execute(list);
        }
    }

    public void deleteListToDo(ListeToDo list){
        if(onLine())
            new DeleteList_API_AsyncTask().execute(list.getId());
        else{
            listener.onDataReady(Constant.ACTION_IMPOSSIBLE, 0);
//            new DeleteList_BDD_AsyncTask().execute(list.getId());
        }
    }

    public void deconnexion(){
        // on efface le mot de passe enregistré afin d'éviter que la prochaine fois l'utilisateur se connecte directement
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getMy_context());
        SharedPreferences.Editor editor = settings.edit();
        if(settings.contains("pass")){
            editor.remove("pass");
            editor.commit();
        }
        setSession_hash("");
        setSession_idBDD(0);
    }

    /*----------------------- USER BDD API -----------------------*/

    class Connexion_User_BDD_AsyncTask  extends AsyncTask<String, Void, Integer>{
        private String pseudo = "";
        private String pass = "";
        @Override
        protected Integer doInBackground(String... strings) {
            pseudo = strings[0];
            pass = strings[1];
            ProfilListeToDo profil = new ProfilListeToDo(pseudo, pass);
            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
            return db.profilDao().getId(profil.getLogin(), profil.getPass());
        }
        protected void onPostExecute(Integer idUser) {
            Log.i("debug2", "id user : " + idUser);
            if(idUser == 0){ // pseudo et password absent dans la bdd
                if(getCacheHash().equals(""))
                    getListener().onDataReady(Constant.CONNEXION_ECHEC, pseudo, pass);
                else // sinon, si on s'est d'abord connecté de façon réussie à l'API pour ensuite créer un user dans la bdd car il n'y existait pas
                    new Create_User_BDD_AsyncTask().execute(pseudo, pass);
            }
            else{
                setSession_idBDD(idUser);
                getListener().onDataReady(Constant.CONNEXION, pseudo, pass);
            }
        }
    }

    // fonction utilisée par l'async task se connectant à l'API, et par toutes les async task
    // exécutant des requêtes à l'API lorsque le hash n'est pas définie
    private String connexionAPI(String pseudo, String pass){
        if(!pseudo.equals("") && !pass.equals("")){
            Reseau res = new Reseau(getMy_context());
            String url = "/authenticate?user=" + pseudo + "&password=" + pass;
            String result = res.executePost(url, "");
            if(result!=null){
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(result,JsonObject.class);
                String hash = resp.get("hash").getAsString();
                setSession_hash(hash); // on sauvegarde le hash dans le DataProvider pour les prochaines fois
                return hash;
            }
            else
                return "";
        }
        else
            return "";
    }

    class Connexion_User_API_AsyncTask  extends AsyncTask<String, Void, String>{
        private String pseudo = "";
        private String pass = "";
        @Override
        protected String doInBackground(String... strings) {
            pseudo = strings[0];
            pass = strings[1];
            return connexionAPI(pseudo, pass);
        }
        protected void onPostExecute(String hash) {
            if(!hash.equals("")){ // si pseudo et password correct, et connexion à API réussie
                //on se connecte automatiquement à la base de donnée de l'utilisateur
                new Connexion_User_BDD_AsyncTask().execute(pseudo, pass);
            }
            else{ // sinon on indique qu'il y a eu un problème
                getListener().onDataReady(Constant.CONNEXION_ECHEC, pseudo, pass);
            }
        }
    }

    class Create_User_BDD_AsyncTask  extends AsyncTask<String, Void, Integer>{
        private String pseudo = "";
        private String pass = "";
        @Override
        protected Integer doInBackground(String... strings) {
            pseudo = strings[0];
            pass = strings[1];
            ProfilListeToDo profil = new ProfilListeToDo(pseudo, pass);
            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
            db.profilDao().insert(profil);
            return db.profilDao().getId(profil.getLogin(), profil.getPass());
        }
        protected void onPostExecute(Integer idUser) {
            Log.i("debug2", "id cree : " + idUser);
            setSession_idBDD(idUser);
            getListener().onDataReady(Constant.CONNEXION, pseudo, pass);
        }
    }


    /*----------------------- LIST BDD -----------------------*/

    class GetList_BDD_AsyncTask extends AsyncTask<Void, Void, List<ListeToDo>> {
        @Override
        protected List<ListeToDo> doInBackground(Void... voids) {
            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
            return db.listDao().getAll(getCacheIdBDD());
        }
        protected void onPostExecute(List<ListeToDo> list) {
            getListener().onDataReady(Constant.GET_LIST, list);
        }
    }

    class PostList_BDD_AsyncTask extends AsyncTask<ListeToDo, Void, ListeToDo> {
        @Override
        protected ListeToDo doInBackground(ListeToDo... list) {
            list[0].setIdProfil(getCacheIdBDD());
            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
            db.listDao().insert(list[0]);
            return list[0];
        }
        protected void onPostExecute(ListeToDo list) {
            getListener().onDataReady(Constant.POST_LIST, list);
        }
    }

    class DeleteList_BDD_AsyncTask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... id) {
            int idList = id[0];
            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
            db.listDao().delete(idList, getCacheIdBDD());
            return idList;
        }
        protected void onPostExecute(Integer idList) {
            getListener().onDataReady(Constant.DELETE_LIST, idList);
        }
    }

    /*----------------------- LIST API -----------------------*/

    class Synchro_List_BDD_API_AsyncTask  extends AsyncTask<ArrayList<ListeToDo>, Void, ArrayList<ListeToDo>> {
        @Override
        protected ArrayList<ListeToDo> doInBackground(ArrayList<ListeToDo>... lists) {
            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
            db.listDao().deleteAllUser(getCacheIdBDD());
            ArrayList<ListeToDo> listToDo = lists[0];
            for(int k = 0 ; k < listToDo.size() ; k++){
                db.listDao().insert(listToDo.get(k));
            }
            return listToDo;
        }
        protected void onPostExecute(ArrayList<ListeToDo> listToDo) {
            getListener().onDataReady(Constant.GET_LIST, listToDo);
        }
    }

    // async task permettant d'exécuter la requete GET permettant de récupérer la liste des listes d'un utilisateur
    class GetList_API_AsyncTask  extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String hash = getCacheHash();
            if(hash.equals("")){ // si le hash n'est pas définie (possible lorsqu'on se connecte au début en mode hors ligne et qu'on récupère la connexion après
                hash = connexionAPI(getPrefPseudo(), getPrefPass());
                setSession_hash(hash);
            }
            Reseau res = new Reseau(getMy_context());
            String reponse = "";
            if( !hash.equals("") && res.verifReseau()){
                String url = "/lists?hash=" + hash;
                reponse = res.requete(url);
            }
            return reponse;
        }
        protected void onPostExecute(String result) {
            if(!result.equals("")) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonArray tab = gson.fromJson(result, JsonObject.class).get("lists").getAsJsonArray();
                // on initialise notre listToDo avec la réponse de l'API
                ArrayList<ListeToDo> listToDo = new ArrayList<>();
                Converter conv = new Converter();
                for(int k = 0 ; k < tab.size() ; k++){
                    JsonObject list = tab.get(k).getAsJsonObject();
                    ListeToDo liste = conv.ListFromJson(list);
                    liste.setIdProfil(getCacheIdBDD());
                    listToDo.add(liste);
                }

                new Synchro_List_BDD_API_AsyncTask().execute(listToDo);
//                getListener().onDataReady(Constant.GET_LIST, listToDo);
            }
            else
                getListener().onDataReady(Constant.ECHEC_LIAISON_API, 0);
        }
    }

    // async task permettant d'exécuter la requete POST permettant de créer une nouvelle liste
    class PostList_API_AsyncTask extends AsyncTask<ListeToDo, Void, String> {
        @Override
        protected String doInBackground(ListeToDo... lists) {
            String hash = getCacheHash();
            if(hash.equals("")){ // si le hash n'est pas définie (possible lorsqu'on se connecte au début en mode hors ligne et qu'on récupère la connexion après
                hash = connexionAPI(getPrefPseudo(), getPrefPass());
                setSession_hash(hash);
            }
            String description = lists[0].getTitreListeToDo();
            Reseau res = new Reseau(getMy_context());
            String reponse = "";
            if(!hash.equals("") && res.verifReseau()){
                String url = "/lists?label=" + description + "&hash=" + hash;
                reponse = res.executePost(url, "");
            }
            return reponse;
        }
        protected void onPostExecute(String result) {
            if(!result.equals("")) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(result, JsonObject.class);
                if(resp != null && resp.get("success").getAsBoolean()){
                    JsonObject list = resp.get("list").getAsJsonObject();
                    new PostList_BDD_AsyncTask().execute(new Converter().ListFromJson(list));
//                    getListener().onDataReady(Constant.POST_LIST, new Converter().ListFromJson(list));
                }
                else
                    getListener().onDataReady(Constant.ECHEC_LIAISON_API, 0);
            }
        }
    }

    // async task permettant d'exécuter la requete DELETE permettant de supprimer une liste
    class DeleteList_API_AsyncTask extends AsyncTask<Integer, Void, String> {
        private int idList = 0;
        @Override
        protected String doInBackground(Integer... ints) {
            String hash = getCacheHash();
            if(hash.equals("")){ // si le hash n'est pas définie (possible lorsqu'on se connecte au début en mode hors ligne et qu'on récupère la connexion après
                hash = connexionAPI(getPrefPseudo(), getPrefPass());
                setSession_hash(hash);
            }
            idList = ints[0];
            Reseau res = new Reseau(getMy_context());
            String reponse = "";
            if(!hash.equals("")  && res.verifReseau()){
                String url = "/lists/" + idList + "&hash=" + hash;
                reponse = res.executeDelete(url);
            }
            return reponse;
        }
        protected void onPostExecute(String result) {
            if(result!=null) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(result, JsonObject.class);
                if(resp != null && resp.get("success").getAsBoolean()){
                    new DeleteList_BDD_AsyncTask().execute(idList);
                }
                else
                    getListener().onDataReady(Constant.ECHEC_LIAISON_API, 0);
            }
        }
    }


    /*----------------------- ITEM -----------------------*/
    class Synchro_Item_BDD_API_AsyncTask  extends AsyncTask<Integer, Void, ArrayList<ItemToDo>> {
        @Override
        protected ArrayList<ItemToDo> doInBackground(Integer... ids) {
            int typeRetour = 0;
            ArrayList<ItemToDo> listItemsAPI = new ArrayList<>();
            ArrayList<ItemToDo> listItemsBDD = null;
            int idListCourante = ids[0];

            // On récupère les items depuis l'API si possible
            Reseau res = new Reseau(getMy_context());
            String reponse = "";
            if(res.verifReseau()){
                String hash = getCacheHash();
                if(hash.equals("")){ // si le hash n'est pas définie (possible lorsqu'on se connecte au début en mode hors ligne et qu'on récupère la connexion après
                    hash = connexionAPI(getPrefPseudo(), getPrefPass());
                }
                if(!hash.equals("")){
                    setSession_hash(hash);
                    String url = "/lists/" + idListCourante + "/items?hash=" + hash;
                    reponse = res.requete(url);
                }
            }
            if(!reponse.equals("")) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonArray tab = gson.fromJson(reponse, JsonObject.class).get("items").getAsJsonArray();
                for(int k = 0 ; k < tab.size() ; k++){
                    JsonObject item = tab.get(k).getAsJsonObject();
                    listItemsAPI.add(new Converter().ItemFromJson(item));
                }
                typeRetour = Constant.GET_ITEM;
            }
            else
                typeRetour = Constant.ECHEC_LIAISON_API;

            // on compare la liste d'item issue de l'API à celle de la BDD et on fait le nécessaire
            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
            listItemsBDD = (ArrayList<ItemToDo>) db.itemDao().getAll(idListCourante);

            if(typeRetour == Constant.ECHEC_LIAISON_API){
                return listItemsBDD;
            }
            else{

                deb("list bdd gggg gggggg" + listItemsAPI.toString());
                deb("list bdd avant modif" + listItemsBDD.toString());


                // vérifier qu'il n'y pas eu des items qui ont été cochés. Si c'est le cas,
                //  envoyer les requetes PUT nécessaire à l'API pour mettre à jouer l'API
                for(int k = 0 ; k < listItemsBDD.size() ; k++){
                    ItemToDo itemBDD = listItemsBDD.get(k);
                    if(itemBDD.getModifie()){ // requete PUT à l'API
                        // on fait la requete en vérifiant qu'on a du réseau avant
                        String reponsePut = "";
                        if(res.verifReseau()){
                            int checked = 0;
                            if(itemBDD.getFait()) checked = 1;
                            String url = "/lists/" + idListCourante + "/items/" + itemBDD.getId() + "?check=" + checked + "&hash=" + getCacheHash();
                            reponsePut = res.executePut(url);
                        }
                        if(!reponsePut.equals("")) {
                            final GsonBuilder builder = new GsonBuilder();
                            final Gson gson = builder.create();
                            JsonObject resp = gson.fromJson(reponsePut, JsonObject.class);
                            if(resp.get("success").getAsBoolean()){ // si l'item a été modifié dans l'API avec succès, on remet l'item de la bdd à non modifie
//                                int index = itemBDD.estDans(listItemsAPI);
//                                listItemsAPI.get(index).setFait(itemBDD.getFait());
                                listItemsAPI.get( itemBDD.estDans(listItemsAPI) ).setFait(itemBDD.getFait());
                                db.itemDao().updateModif(itemBDD.getUid(), false);

                            }
                            db.itemDao().updateModif(itemBDD.getUid(), false);
                        }

                        }
                    }

                deb("list bdd apres modif" + listItemsBDD.toString());

                // on assigne l'uid aux items
                for(int k = 0 ; k < listItemsAPI.size() ; k++){
                    ItemToDo itemAPI = listItemsAPI.get(k);
                    int index = itemAPI.estDans(listItemsBDD);
                    if(index != -1){ // si itemAPI est dans la list de la BDD, on lui affecte l'uid de la BDD
                        itemAPI.setUid(listItemsBDD.get(index).getUid());
                    }
                    else{ // sinon on l'insère dans la bdd, et on lui assigne l'uid
                        db.itemDao().insert(itemAPI);
                        itemAPI.setUid(db.itemDao().postInsert());
                    }
                }
                return listItemsAPI;

            }
        }
        protected void onPostExecute(ArrayList<ItemToDo> listItem) {
            getListener().onDataReady(Constant.GET_ITEM, listItem);
        }
    }

    public void getItem(int idList){
        new Synchro_Item_BDD_API_AsyncTask().execute(idList);
//        if(onLine())
//            new GetItem_API_AsyncTask().execute(idList);
//        else
//            new GetItem_BDD_AsyncTask().execute(idList);
    }
    public void postItem(ItemToDo item){
        if(onLine())
            new PostItem_API_AsyncTask().execute(item);
        else
            listener.onDataReady(Constant.ACTION_IMPOSSIBLE, 0);
//            new PostItem_BDD_AsyncTask().execute(item);
    }
    public void deleteItem(ItemToDo item){
        if(onLine())
            new DeleteItem_API_AsyncTask().execute(item);
        else
            listener.onDataReady(Constant.ACTION_IMPOSSIBLE, 0);
//            new DeleteItem_BDD_AsyncTask().execute(item);
    }
    public void putItem(ItemToDo item){
        if(onLine())
            new PutItem_API_AsyncTask().execute(item);
        else
//            listener.onDataReady(Constant.ACTION_IMPOSSIBLE, 0);
            new PutItem_BDD_AsyncTask().execute(item);
    }

    /*----------------------- ITEM API -----------------------*/

    // async task permettant d'exécuter la requete GET permettant de récupérer la liste des items d'une liste
//    class GetItem_API_AsyncTask extends AsyncTask<Integer, Void, String> {
//        private int idListCourante;
//        @Override
//        protected String doInBackground(Integer... ids) {
//            idListCourante = ids[0];
//            String hash = getCacheHash();
//            Reseau res = new Reseau(getMy_context());
//            String reponse = "";
//            if( !hash.equals("") && res.verifReseau()){
//                String url = "/lists/" + idListCourante + "/items?hash=" + hash;
//                reponse = res.requete(url);
//            }
//            return reponse;
//        }
//        protected void onPostExecute(String result) {
//            if(!result.equals("")) {
//                final GsonBuilder builder = new GsonBuilder();
//                final Gson gson = builder.create();
//                JsonArray tab = gson.fromJson(result, JsonObject.class).get("items").getAsJsonArray();
//                ArrayList<ItemToDo> items = new ArrayList<>();
//                for(int k = 0 ; k < tab.size() ; k++){
//                    JsonObject item = tab.get(k).getAsJsonObject();
//                    items.add(new Converter().ItemFromJson(item));
//                }
//                new GetItem_BDD_AsyncTask().execute(idListCourante);
////                getListener().onDataReady(Constant.GET_ITEM, items);
//            }
//            else
//                getListener().onDataReady(Constant.ECHEC_LIAISON_API, 0);
//        }
//    }


    // async task permettant d'exécuter la requete POST permettant de créer un nouvel item
    class PostItem_API_AsyncTask extends AsyncTask<ItemToDo, Void, String> {
        private ItemToDo item;
        @Override
        protected String doInBackground(ItemToDo... items) {
            deb("POST API item avant : " + items[0]);
            String hash = getCacheHash();
            item = items[0];
            Reseau res = new Reseau(getMy_context());
            String reponse = "";
            if(!hash.equals("") && res.verifReseau()){
                String url = "/lists/" + item.getIdliste() + "/items?label=" + item.getDescription() + "&check=0&hash=" + hash;
                reponse = res.executePost(url, "");
            }
            return reponse;
        }
        protected void onPostExecute(String result) {
            if(!result.equals("")) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(result, JsonObject.class);
                if(resp.get("success").getAsBoolean()){
                    JsonObject itemJson = resp.get("item").getAsJsonObject();
//                    deb("POST API item reponse : " + new Converter().ItemFromJson(itemJson));

                    ItemToDo newItem = new Converter().ItemFromJson(itemJson);
                    newItem.setIdliste(item.getIdliste());
                    new PostItem_BDD_AsyncTask().execute(newItem);
//                    getListener().onDataReady(Constant.POST_ITEM, new Converter().ItemFromJson(itemJson));
                }
            }
            else
                getListener().onDataReady(Constant.ECHEC_LIAISON_API, 0);
        }
    }

    // async task permettant d'exécuter la requete DELETE permettant de supprimer un item
    class DeleteItem_API_AsyncTask extends AsyncTask<ItemToDo, Void, String> {
        private ItemToDo item;
        @Override
        protected String doInBackground(ItemToDo... items) {
            String hash = getCacheHash();
            item = items[0];
            deb("DELETE ITEM API item 1: " + item);
            Reseau res = new Reseau(getMy_context());
            String reponse = "";
            if(!hash.equals("")  && res.verifReseau()){
                String url = "/lists/" + item.getIdliste() + "/items/" + item.getId() + "?hash=" + hash;
                reponse = res.executeDelete(url);
            }
            return reponse;
        }
        protected void onPostExecute(String result) {
            if(!result.equals("")) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(result, JsonObject.class);
                if(resp.get("success").getAsBoolean()){
                    deb("reponse: " + resp);
                    deb("POST ITEM API item 1: " + item);
                    new DeleteItem_BDD_AsyncTask().execute(item);
//                    getListener().onDataReady(Constant.DELETE_ITEM, item);
                }
            }
            else
                getListener().onDataReady(Constant.ECHEC_LIAISON_API, 0);
        }
    }


    // async task permettant d'exécuter la requete PUT permettant de cocher l'item
    class PutItem_API_AsyncTask extends AsyncTask<ItemToDo, Void, String> {
        private ItemToDo item;
        @Override
        protected String doInBackground(ItemToDo... items) {
            String hash = getCacheHash();

            item = items[0];

            deb("back put api" + item.toString());
            int checked = 0;
            if(!item.getFait()) checked = 1;
            Reseau res = new Reseau(getMy_context());
            String reponse = "";
            if(!hash.equals("")  && res.verifReseau()){
                String url = "/lists/" + item.getIdliste() + "/items/" + item.getId() + "?check=" + checked + "&hash=" + hash;
                reponse = res.executePut(url);
            }
            return reponse; //
        }
        protected void onPostExecute(String result) {
            if(!result.equals("")) {
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                JsonObject resp = gson.fromJson(result, JsonObject.class);
                if(resp.get("success").getAsBoolean()){
                    deb("post put api" + item.toString());

                    new PutItem_BDD_AsyncTask(true).execute(item);
//                    getListener().onDataReady(Constant.CHECK_ITEM, item);
                }
            }
            else
                getListener().onDataReady(Constant.ECHEC_LIAISON_API, 0);
        }
    }

    /*----------------------- ITEM BDD -----------------------*/

//    class GetItem_BDD_AsyncTask extends AsyncTask<Integer, Void, List<ItemToDo>> {
//        @Override
//        protected List<ItemToDo> doInBackground(Integer... ids) {
//            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
//            return db.itemDao().getAll(ids[0]);
//        }
//        protected void onPostExecute(List<ItemToDo> items) {
//            deb("list : " + items);
//            getListener().onDataReady(Constant.GET_ITEM, items);
//        }
//    }

    class PostItem_BDD_AsyncTask extends AsyncTask<ItemToDo, Void, ItemToDo> {
        @Override
        protected ItemToDo doInBackground(ItemToDo... items) {
            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
            deb("POST ITEM BDD item 1: " + items[0]);
            db.itemDao().insert(items[0]);
            deb("POST ITEM BDD item 2: " + items[0]);
            int uid = db.itemDao().postInsert();
            deb("uid : " + uid);
            items[0].setUid(uid);
            return items[0];
        }
        protected void onPostExecute(ItemToDo item) {
            deb("POST ITEM BDD item 3: " + item);
            getListener().onDataReady(Constant.POST_ITEM, item);
        }
    }

    class DeleteItem_BDD_AsyncTask extends AsyncTask<ItemToDo, Void, ItemToDo> {
        @Override
        protected ItemToDo doInBackground(ItemToDo... items) {
            deb("DELETE ITEM BDD item 1: " + items[0]);
            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
            db.itemDao().delete(items[0].getUid());
            deb("DELETE ITEM BDD item 2: " + items[0]);
            return items[0];
        }
        protected void onPostExecute(ItemToDo item) {

            getListener().onDataReady(Constant.DELETE_ITEM, item);
        }
    }


    class PutItem_BDD_AsyncTask extends AsyncTask<ItemToDo, Void, ItemToDo> {
        private boolean depuisAPI_PUT;
        public PutItem_BDD_AsyncTask(){
            this.depuisAPI_PUT = false;
        }
        public PutItem_BDD_AsyncTask(boolean depuisAPI){
            this.depuisAPI_PUT = depuisAPI;
        }
        @Override
        protected ItemToDo doInBackground(ItemToDo... items) {
            deb("back put bdd" + items[0].toString());
            AppDatabase db = Room.databaseBuilder(getMy_context(), AppDatabase.class, "database-name").build();
            db.itemDao().update(items[0].getUid(), !items[0].getFait());
            if(!depuisAPI_PUT)
                 db.itemDao().updateModif(items[0].getUid(), true);
            return items[0];
        }
        protected void onPostExecute(ItemToDo item) {
            deb("post put bdd" + item);
            getListener().onDataReady(Constant.CHECK_ITEM, item);
        }
    }

}



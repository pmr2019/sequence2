package com.example.td_wang_yang_wei;

import android.app.LauncherActivity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.UiThread;

import com.example.td_wang_yang_wei.Database.ToDoListdb;
import com.example.td_wang_yang_wei.Database.dao.ItemDao;
import com.example.td_wang_yang_wei.Database.dao.ListDao;
import com.example.td_wang_yang_wei.Database.dao.UserDao;
import com.example.td_wang_yang_wei.Database.model.Itemdb;
import com.example.td_wang_yang_wei.Database.model.Listdb;
import com.example.td_wang_yang_wei.api.Items;
import com.example.td_wang_yang_wei.api.Lists;
import com.example.td_wang_yang_wei.api.NouveauListe;
import com.example.td_wang_yang_wei.api.Users;
import com.example.td_wang_yang_wei.api.requestService;
import com.example.td_wang_yang_wei.api.requestServiceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import retrofit2.Response;

public class DataProvider {

    private List<Future> futures = new ArrayList<>();
    private static List<String> addFail = new ArrayList<>();
    private final Handler uiHandler;
    private final UserDao userDao;
    private final ListDao listDao;
    private final ItemDao itemDao;
    private String userId="";

    //TODO:bug测试 之后改成动态url
    private String url="http://tomnab.fr/todo-api/";
    //TODO:实现url替换（现在用的默认）
    private final requestService service = requestServiceFactory.createService(url,requestService.class);
    private final Converter converter = new Converter();

    private List<String> labelslist = new ArrayList<>();
    private List<String> labelsitem = new ArrayList<>();
    private List<String> fitem = new ArrayList<>();

    private static String id = null;


    public DataProvider(Context context){
        uiHandler = new Handler(context.getMainLooper());
        userDao = ToDoListdb.getDatabase(context).userDao();
        listDao = ToDoListdb.getDatabase(context).listDao();
        itemDao = ToDoListdb.getDatabase(context).itemDao();
    }

    public void syncGetLists(final String hash, final String userIdConneted,final ListsListener listener) {

        Future future = Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                try {
                    Response<Lists> response = service.getLists(hash).execute();
                    if( response.isSuccessful()) {
                        final List<Listdb> listsSave = converter.listsfrom(response.body(),userIdConneted);
                        int delete = listDao.deleteAll();
                        Log.d("test",delete+"");
                        listDao.save(listsSave);
                        getLabelslist(userIdConneted, new GetListListener() {
                            @Override
                            public void onSuccess(List<String> label) {

                            }

                            @Override
                            public void onError(List<String> label) {

                            }
                        });

                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onSuccess(labelslist);
                            }
                        });

                    }else {
                        getLabelslist(userIdConneted, new GetListListener() {
                            @Override
                            public void onSuccess(List<String> label) {

                            }

                            @Override
                            public void onError(List<String> label) {

                            }
                        });
                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onError(labelslist);
                            }
                        });

                    }

                } catch (IOException e) {
                    getLabelslist(userIdConneted, new GetListListener() {
                        @Override
                        public void onSuccess(List<String> label) {

                        }

                        @Override
                        public void onError(List<String> label) {

                        }
                    });
                    uiHandler.post(new Runnable() {
                        @Override public void run() {
                            listener.onError(labelslist);
                        }
                    });
                }
            }
        });
        futures.add(future);

    }

    public void syncGetUserId(final String hash, final String pseudo, final UserListener listener) {

        Future future = Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                try {
                    Response<Users> response = service.getUsers(hash).execute();
                    if( response.isSuccessful()) {
                        final String  userIdConneted = response.body().getUserId(pseudo);
                        Log.d("test111",userIdConneted);

                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                userId=userIdConneted;
                                listener.onSuccess(userIdConneted);
                            }
                        });

                    }else {
                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onError();
                            }
                        });

                    }

                } catch (IOException e) {
                    uiHandler.post(new Runnable() {
                        @Override public void run() {
                            listener.onError();
                        }
                    });
                }
            }
        });
        futures.add(future);

    }


    public void getLabelslist(final String userId, final GetListListener listListener){
        Future future=Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                labelslist.clear();
                for (int i = 0; i< listDao.findListByUserId(userId).size(); i++) {
                    labelslist.add(listDao.findListByUserId(userId).get(i).getLabel());
                }
                listListener.onSuccess(labelslist);

            }
        });
        futures.add(future);


    }

    public void getUserId(final GetUserIdListener getUserIdListener){
        Future future=Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                getUserIdListener.onSuccess(userId);

            }
        });
        futures.add(future);
    }




    public void syncAddList(final String hash, final String userId, final String liste, final ListAddListener listener) {

        Future future = Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                try {
                    Response<NouveauListe> response = service.addList(hash,userId,liste).execute();
                    if( response.isSuccessful()) {

                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onSuccess(liste);
                            }
                        });

                    }else {
                        addFail.add(liste);
                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onError(addFail);
                            }
                        });

                    }

                } catch (IOException e) {
                    //TODO:补全 有网时在判断网络处调用for循环和syncAdd 用sharepreference？
                    addFail.add(liste);
                    uiHandler.post(new Runnable() {
                        @Override public void run() {
                            listener.onError(addFail);
                        }
                    });
                }
            }
        });
        futures.add(future);
    }


    public void syncAddListTodb(final String liste) {

        Future future = Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                Listdb listAddTodb = null;
                listAddTodb.setLabel(liste);
                listDao.add(listAddTodb);
            }
        });
        futures.add(future);

    }

    public void syncRemoveListAtdb(final String list) {

        Future future = Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                Listdb listRemoveAtdb = null;

                listDao.deleteList(listRemoveAtdb);
            }
        });
        futures.add(future);

    }

    public void syncGetItems(final String hash, final String listIdConneted,final ItemsListener listener) {

        Future future = Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                try {
                    Response<Items> response = service.getItems(hash,listIdConneted).execute();
                    if( response.isSuccessful()) {
                        final List<Itemdb> itemsSave = converter.itemsfrom(response.body(),listIdConneted);
                        int delete = itemDao.deleteAll();
                        Log.d("testDelete",delete+"");
                        itemDao.save(itemsSave);
                        getListItems(listIdConneted);

                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onSuccess(labelsitem,fitem);
                            }
                        });

                    }else {
                        getListItems(listIdConneted);
                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onError(labelsitem,fitem);
                            }
                        });

                    }

                } catch (IOException e) {
                    getListItems(listIdConneted);
                    uiHandler.post(new Runnable() {
                        @Override public void run() {
                            listener.onError(labelsitem,fitem);
                        }
                    });
                }
            }
        });
        futures.add(future);

    }


    public String syncGetListId(final String liste) {

        Log.d("test777",id+"");
        Future future = Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                id = listDao.findListByUserLabel(liste).getId();
            }
        });
        futures.add(future);
        return id;

    }

    public void getListItems(String listId){
        labelsitem.clear();fitem.clear();
        for (int i = 0; i< itemDao.getAllItems().size(); i++) {
            ItemsState state = null;
            fitem.add(itemDao.findItemBylistId(listId).get(i).getChecked());
            labelsitem.add(itemDao.findItemBylistId(listId).get(i).getLabel());
        }
    }

    public void stop() {
        for (Future future : futures) {
            if(!future.isDone()){
                future.cancel(true);
            }
        }
        futures.clear();
    }

    public interface ListsListener {
        @UiThread
        public void onSuccess(List<String> lists);
        @UiThread
        public void onError(List<String> lists);
    }

    public interface ListAddListener {
        @UiThread
        public void onSuccess(String listAdd);
        @UiThread
        public void onError(List addFail);
    }

    public interface UserListener {
        @UiThread
        public void onSuccess(String userId);
        @UiThread
        public void onError();
    }

    public interface ItemsListener {
        @UiThread
        public void onSuccess(List<String> label,List<String> f);
        @UiThread
        public void onError(List<String> label,List<String> f);
    }

    public interface GetListListener{
        @UiThread
        public void onSuccess(List<String> label);
        @UiThread
        public void onError(List<String> label);
    }

    public interface GetUserIdListener{
        @UiThread
        public void onSuccess(String userId);
        @UiThread
        public void onError(String userId);
    }

    private class ItemsState {
        private String label;
        private String f;

        private String getLabel(){return label;}
        private String getF(){return f;}
        private void setLabel(String label){this.label = label;}
        private void setF(String f){this.f = f;}
    }

}

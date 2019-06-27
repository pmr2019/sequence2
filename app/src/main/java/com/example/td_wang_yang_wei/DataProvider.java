package com.example.td_wang_yang_wei;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.UiThread;

import com.example.td_wang_yang_wei.Database.ToDoListdb;
import com.example.td_wang_yang_wei.Database.dao.ListDao;
import com.example.td_wang_yang_wei.Database.dao.UserDao;
import com.example.td_wang_yang_wei.Database.model.Listdb;
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

    //TODO:bug测试 之后改成动态url
    private String url="http://tomnab.fr/todo-api/";
    //TODO:实现url替换（现在用的默认）
    private final requestService service = requestServiceFactory.createService(url,requestService.class);
    private final Converter converter = new Converter();

    private List<String> labelslist = new ArrayList<>();


    public DataProvider(Context context){
        uiHandler = new Handler(context.getMainLooper());
        userDao = ToDoListdb.getDatabase(context).userDao();
        listDao = ToDoListdb.getDatabase(context).listDao();
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
                        getLabelslist(userIdConneted);

                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onSuccess(labelslist);
                            }
                        });

                    }else {
                        getLabelslist(userIdConneted);
                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onError(labelslist);
                            }
                        });

                    }

                } catch (IOException e) {
                    getLabelslist(userIdConneted);
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


    public void getLabelslist(String userId){
        labelslist.clear();
        for (int i = 0; i< listDao.getAllLists().size(); i++) {
            labelslist.add(listDao.findListByUserId(userId).get(i).getLabel());
        }
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

    public void syncRemoveListAtdb(final String listLabel) {

        Future future = Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                Listdb listAddTodb = null;

                listDao.add(listAddTodb);
            }
        });
        futures.add(future);

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
}

package com.example.td_wang_yang_wei;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.UiThread;

import com.example.td_wang_yang_wei.DataClass.ListeDeUtilisateur;
import com.example.td_wang_yang_wei.Database.ToDoListdb;
import com.example.td_wang_yang_wei.Database.dao.ListDao;
import com.example.td_wang_yang_wei.Database.dao.UserDao;
import com.example.td_wang_yang_wei.Database.model.Listdb;
import com.example.td_wang_yang_wei.api.Lists;
import com.example.td_wang_yang_wei.api.requestService;
import com.example.td_wang_yang_wei.api.requestServiceFactory;
import com.example.td_wang_yang_wei.ui.ChoixListeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import retrofit2.Response;

public class DataProvider {

    private List<Future> futures = new ArrayList<>();
    private final Handler uiHandler;
    private final UserDao userDao;
    private final ListDao listDao;

    //TODO:bug测试 之后改成动态url
    private String url="http://tomnab.fr/todo-api/";
    //TODO:实现url替换（现在用的默认）
    private final requestService service = requestServiceFactory.createService(url,requestService.class);
    private final Converter converter = new Converter();

    private static List<String> labelslist = new ArrayList<>();


    public DataProvider(Context context){
        uiHandler = new Handler(context.getMainLooper());
        userDao = ToDoListdb.getDatabase(context).userDao();
        listDao = ToDoListdb.getDatabase(context).listDao();
    }

    public void syncLists(final String hash, final ListsListener listener) {

        Future future = Utils.BACKGROUND.submit(new Runnable() {
            @Override public void run() {
                try {
                    Log.d("test",listener+"");
                    Response<Lists> response = service.getLists(hash).execute();
                    if( response.isSuccessful()) {
                        final List<Listdb> listsSave = converter.listsfrom(response.body());
                        listDao.save(listsSave);
                        Log.d("test",listsSave+"");
                        readLabelslist();
                        Log.d("test",labelslist.toString());

                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onSuccess(labelslist);
                            }
                        });

                    }else {
                        readLabelslist();
                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onError(labelslist);
                            }
                        });

                    }

                } catch (IOException e) {
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

    public void readLabelslist(){
        labelslist.clear();
        for (int i = 0; i< listDao.getAllLists().size(); i++) {
            labelslist.add(listDao.getAllLists().get(i).getLabel());
        }
    }

    public interface ListsListener {
        @UiThread
        public void onSuccess(List<String> lists);
        @UiThread
        public void onError(List<String> lists);
    }
}

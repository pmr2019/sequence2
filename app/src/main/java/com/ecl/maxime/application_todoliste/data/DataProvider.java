package com.ecl.maxime.application_todoliste.data;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.UiThread;

import com.ecl.maxime.application_todoliste.adapters.Converter;
import com.ecl.maxime.application_todoliste.api_request.ItemResponseList;
import com.ecl.maxime.application_todoliste.data.dao.ItemDAO;
import com.ecl.maxime.application_todoliste.data.dao.ListeDAO;
import com.ecl.maxime.application_todoliste.api_request.ListeResponseList;
import com.ecl.maxime.application_todoliste.api_request.ServiceFactory;
import com.ecl.maxime.application_todoliste.api_request.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Response;


/**
 * Created by Max on 2019-06-30.
 */
public class DataProvider {

    private List<Future> futures = new ArrayList<>();
    private final ListeDAO mListeDAO;
    private final ItemDAO mItemDAO;
    private final Services service = ServiceFactory.createService(Services.class);
    private final Handler uiHandler;

    public DataProvider(Context context) {
        uiHandler = new Handler(context.getMainLooper());
        mListeDAO = MyRoomDB.getDatabase(context).listeDAO();
        mItemDAO = MyRoomDB.getDatabase(context).itemDAO();
    }

    public void syncLists(final ListesListener listener, final String hash) {
        Future future = Executors.newFixedThreadPool(2).submit(new Runnable() {
            @Override public void run() {
                try {
                    Response<ListeResponseList> response = service.getListes(hash).execute();
                    if(response.isSuccessful()) {
                        final List<Liste> listes = Converter.fromListe(response.body().lists);
                        mListeDAO.saveListes(listes);
                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onSuccess(listes);
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

    public void syncItems(final ItemsListener listener, final String hash, final int i) {

        Future future = Executors.newFixedThreadPool(2).submit(new Runnable() {
            @Override public void run() {
                try {
                    Response<ItemResponseList> response = service.getListeItems(hash, i).execute();
                    if(response.isSuccessful()) {
                        final List<Item> items = Converter.fromItem(response.body().items);
                        mItemDAO.saveItems(items);
                        uiHandler.post(new Runnable() {
                            @Override public void run() {
                                listener.onSuccess(items);
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

    public void stop() {
        for (Future future : futures) {
            if(!future.isDone()){
                future.cancel(true);

            }
        }
        futures.clear();
    }

    public interface ListesListener {
        @UiThread
        void onSuccess(List<Liste> lists);
        @UiThread
        void onError();
    }

    public interface ItemsListener {
        @UiThread
        void onSuccess(List<Item> items);
        @UiThread
        void onError();
    }
}

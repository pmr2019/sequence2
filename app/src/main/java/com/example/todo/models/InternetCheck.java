package com.example.todo.models;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class InternetCheck extends AsyncTask<Void,Void,Boolean> {

    private Consumer mConsumer;

    public  interface Consumer {
        void isConnectedToInternet(Boolean internet);
    }

    /**
     * Constructor
     * @param consumer : Interface with only one methode : accept(bool)
     */
    public  InternetCheck(Consumer consumer) {
        mConsumer = consumer;
        execute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean internet) {
        mConsumer.isConnectedToInternet(internet);
    }
}
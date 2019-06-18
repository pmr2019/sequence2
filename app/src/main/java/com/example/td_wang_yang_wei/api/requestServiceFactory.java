package com.example.td_wang_yang_wei.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class requestServiceFactory {
    public static <T> T createService(String url, Class<T> type) {
        //créer l'objet de retrofit
        Retrofit retrofit = new Retrofit.Builder()
                //Régler l'url de demande d'envoyer
                .baseUrl(url)
                //Régler pour utiliser la résolution Gson
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(type);
    }
}

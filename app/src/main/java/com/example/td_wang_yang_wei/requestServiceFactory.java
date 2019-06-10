package com.example.td_wang_yang_wei;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class requestServiceFactory {
    public static <T> T createService(String url, Class<T> type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(type);
    }
}

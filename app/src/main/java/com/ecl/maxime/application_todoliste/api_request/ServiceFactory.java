package com.ecl.maxime.application_todoliste.api_request;

import com.ecl.maxime.application_todoliste.activites.MainActivity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Max on 2019-06-17.
 */
public class ServiceFactory {

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MainActivity.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <T> T createService(Class<T> type) {
        return retrofit.create(type);
    }
}

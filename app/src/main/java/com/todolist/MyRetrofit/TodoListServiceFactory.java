package com.todolist.MyRetrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodoListServiceFactory {
    public static <T> T createService(String url, Class<T> type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(type);
    }
}

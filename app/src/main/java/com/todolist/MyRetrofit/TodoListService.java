package com.todolist.MyRetrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TodoListService {
    @FormUrlEncoded
    @POST("authenticate")
    Call<Hash> authenticate(@Field("user") String user, @Field("password") String password);
}

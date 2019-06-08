package com.todolist.MyRetrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TodoListService {
    @FormUrlEncoded
    @POST("authenticate")
    Call<Hash> authenticate(@Field("user") String user, @Field("password") String password);

    @GET("lists")
    Call<Lists> getLists(@Query("hash") String hash);

    @GET("users")
    Call<Users> getUsers(@Query("hash") String hash);

    @POST("users/{id}/lists")
    Call<NewListInfo> addList(@Header("hash") String hash, @Path("id") String id, @Query("label") String label);

    @GET("lists/{id}/items")
    Call<Items> getItems(@Header("hash") String hash, @Path("id") String id);

    @POST("lists/{id}/items")
    Call<NewItemInfo> addItem(@Header("hash") String hash, @Path("id") String id, @Query("label") String label);

//    @PUT("lists/{id}/items/{id}")
}

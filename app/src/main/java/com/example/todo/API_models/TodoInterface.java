package com.example.todo.API_models;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TodoInterface {

    @POST("/todo-api/authenticate")
    Call<RetroMain> authenticate(@Query("user") String pseudo,
                                 @Query("password") String password);

    @GET("/todo-api/lists")
    Call<RetroMain> getLists(@Query("hash") String hash);

    @GET("/todo-api/users")
    Call<RetroMain> searchUsers(@Header("hash") String hash);

    @POST("/todo-api/users")
    Call<RetroMain> addUser(@Header("hash") String hash,
                            @Query("pseudo") String pseudo,
                            @Query("pass") String password);

    @POST("todo-api/lists")
    Call<RetroMain> addList(@Header("hash") String hash,
                            @Query("label") String title);

    @DELETE("todo-api/lists/{idList}")
    Call<RetroMain> delList(@Header("hash") String hash,
                            @Path(value = "idList") String idList);

    @GET("todo-api/lists/{idList}/items")
    Call<RetroMain> getItems(@Header("hash") String hash,
                             @Path(value = "idList") Integer idList);

    @POST("todo-api/lists/{idList}/items")
    Call<RetroMain> addItem(@Header("hash") String hash,
                            @Path(value = "idList") String idList,
                            @Query("label") String description,
                            @Query("url") String url);

    @DELETE("todo-api/lists/{idList}/items/{idItem}")
    Call<RetroMain> delItem(@Header("hash") String hash,
                            @Path(value = "idList") String idList,
                            @Path(value = "idItem") String idItem);

    @PUT("todo-api/lists/{idList}/items/{idItem}")
    Call<RetroMain> checkItem(@Header("hash") String hash,
                              @Path(value = "idList") String idList,
                              @Path(value = "idItem") String idItem,
                              @Query("check") String isCheck);
}

package com.ecl.maxime.application_todoliste.api_request;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Max on 2019-06-17.
 */
public interface Services {

    @POST("authenticate")
    Call<Hashcode> connexion(@Query("user") String user, @Query("password") String password);

    @GET("lists")
    Call<ListeDeListes> getListes(@Header("hash") String hash);

    @POST("lists")
    Call<Void> addListe(@Header("hash") String hash, @Query("label") String label);

    @GET("lists/{id}/items")
    Call<ListeItems> getListeItems(@Header("hash") String hash, @Path("id") String id);

    @POST("lists/{id}/items")
    Call<Void> addItem(@Header("hash") String hash, @Path("id") String id, @Query("label") String label);

    @PUT("lists/{id}/items/{id_item}")
    Call<Void> modifyItem(@Header("hash") String hash, @Path("id") String id, @Path("id_item") String id_item, @Query("check") String check);
}

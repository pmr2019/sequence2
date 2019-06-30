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
    /*
    Chaque requête utile est ici répertoriée
     */

    // Connexion
    @POST("authenticate")
    Call<Hashcode> connexion(@Query("user") String user, @Query("password") String password);

    // Obtenir l'ensemble des listesToDo
    @GET("lists")
    Call<ListeResponseList> getListes(@Header("hash") String hash);

    // Ajouter une liste
    @POST("lists")
    Call<Void> addListe(@Header("hash") String hash, @Query("label") String label);

    // Obtenir l'ensemble des mItemResponses d'une liste
    @GET("lists/{id}/mItemResponses")
    Call<ItemResponseList> getListeItems(@Header("hash") String hash, @Path("id") int id);

    // Ajouter un item à une liste
    @POST("lists/{id}/mItemResponses")
    Call<Void> addItem(@Header("hash") String hash, @Path("id") long id, @Query("label") String label);

    // Modifier un item
    @PUT("lists/{id}/mItemResponses/{id_item}")
    Call<Void> modifyItem(@Header("hash") String hash, @Path("id") long id, @Path("id_item") long id_item, @Query("check") String check);
}

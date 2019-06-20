package com.example.todolist.data.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @POST("authenticate")
    Call<ResponseHash> authenticate(@Query("user") String user, @Query("password") String password);

    // récupération de : version, succès, status, et des to-do listes de l'utilisateur
    @GET("lists")
    Call<ResponseTodoLists> getTodoLists(@Query("hash") String hash);

    // récupération de : version, succès, status, et des items d'une to-do liste
    @GET("lists/{idTodoList}/items")
    Call<ResponseItems> getItems(@Path("idTodoList") int idListe, @Query("hash") String hash);

    // envoie de : nom d'une nouvelle liste
    // récupération de : version, succès, status, et la liste envoyée ainsi que son identifiant
    @POST("lists")
    Call<ResponseTodoList> postTodoList(@Query("hash") String hash, @Query("label") String label);

    // envoie de : nom d'une nouvel item
    // récupération de : version, succès, status, et l'item envoyé
    @POST("lists/{idTodoList}/items")
    Call<ResponseItem> postItem(@Path("idTodoList") int idListe, @Query("hash") String hash, @Query("label") String label);

    // mise à jour de : l'état d'un item
    // récupération de : version, succès, status, et l'item modifié
    @PUT("lists/{idTodoList}/items/{idItem}")
    Call<ResponseItem> putCheck(@Path("idTodoList") int idListe, @Path("idItem") int idItem, @Query("hash") String hash, @Query("check") int check);

    // suppression de : une to-do liste
    // récupération de : version, succès, status
    @DELETE("lists/{idTodoList}")
    Call<ResponseBasic> deleteTodoList(@Path("idTodoList") int idListe, @Query("hash") String hash);

    // suppression de : un item
    // récupération de : version, succès, status
    @DELETE("lists/{idTodoList}/items/{idItem}")
    Call<ResponseBasic> deleteItem(@Path("idTodoList") int idListe, @Path("idItem") int idItem, @Query("hash") String hash);


}

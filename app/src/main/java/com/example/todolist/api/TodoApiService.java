package com.example.todolist.api;

import com.example.todolist.api.response_class.Hash;
import com.example.todolist.api.response_class.ItemResponse;
import com.example.todolist.api.response_class.Items;
import com.example.todolist.api.response_class.ListResponse;
import com.example.todolist.api.response_class.Lists;
import com.example.todolist.api.response_class.UnItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/** Définition de l'interface TodoApiService.
 * Cette interface contient l'ensemble des requêtes possibles vers l'API
 */
public interface TodoApiService {

  /** Requête de connexion à l'API
   * @param user le pseudo de l'utilisateur qui souhaite se connecter
   * @param password son mot de passe
   * @return le hash d'identification en cas de succès de la requête
   */
  @POST("authenticate")
  Call<Hash> connexion(@Query("user") String user, @Query("password") String password);

  /** Requête de récupération de l'ensemble des listes associées à l'utilisateur courant
   * @param hash le hash de l'utilisateur courant
   * @return la liste des TodoLists en cas de succès de la requête
   */
  @GET("lists")
  Call<Lists> recupereListes(@Header("hash") String hash);

  /** Requête de récupération de l'ensemble des items associés à une ToDoList de l'utilisateur
   *          courant
   * @param hash le hash de l'utilisateur courant
   * @param idListe l'identifiant de la ToDoList
   * @return la liste des items en cas de succès de la requête
   */
  @GET("lists/{id}/items")
  Call<Items> recupereItems(@Header("hash") String hash, @Path("id") int idListe);

  /** Requête de mise à jour de l'état d'un item dans une ToDoList
   * @param hash le hash de l'utilisateur courant
   * @param idListe l'identifiant de la ToDoList
   * @param idItem l'identifiant de l'item
   * @param etat le nouvel état de l'item
   * @return l'item mis à jour en cas de succès de la requête
   */
  @PUT("lists/{idListe}/items/{idItem}")
  Call<UnItem> cocherItem(@Header("hash") String hash, @Path("idListe") int idListe,
                          @Path("idItem") int idItem, @Query("check") int etat);

  /** Requête d'ajout d'un item dans une ToDoList
   * @param hash le hash de l'utilisateur courant
   * @param idListe l'identifiant de la ToDoList
   * @param label le label (description) associé au nouvel item
   * @return le nouvel item ajouté en cas de succès de la requête
   */
  @POST("lists/{idListe}/items")
  Call<ItemResponse> ajoutItem(@Header("hash") String hash, @Path("idListe") int idListe,
                               @Query("label") String label);

  /** Requête d'ajout d'une ToDoList
   * @param hash le hash de l'utilisateur courant
   * @param label le label (description) associé à la nouvelle ToDoList
   * @return la nouvelle ToDoList ajoutée en cas de succès de la requête
   */
  @POST("lists")
  Call<ListResponse> ajoutListe(@Header("hash") String hash, @Query("label") String label);
}

package com.example.todolist.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Définition de la classe TodoApiServiceFactory.
 * Cette classe génère l'interface de connexion auprès de l'API à l'aide de Retrofit
 */
public class TodoApiServiceFactory {

    /* L'URL de base vers l'API */
    public static String BASE_URL = "http://tomnab.fr/todo-api/";

    /* Initialisation de l'objet de type Retrofit */
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    /**
     * Permet de modifier l'URL de base de l'API et de regénérer l'objet de type Retrofit
     *
     * @param newApiBaseUrl la nouvelle adresse URL de base
     */
    public static void changeApiBaseUrl(String newApiBaseUrl) {
        BASE_URL = newApiBaseUrl;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Fonction générant l'interface de connexion à l'API
     *
     * @return l'interface de connexion
     */
    public static <T> T createService(Class<T> type) {
        return retrofit.create(type);
    }

}

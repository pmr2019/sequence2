package com.example.todolist.recycler_activities;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.modele.ProfilListeToDo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/** Définition de la classe Library.
 * Cette classe contient des méthodes communes aux classes ChoixListActivity et ShowListActivity
 */
public class Library extends AppCompatActivity {

    /** Permet d'importer un profil depuis un fichier JSon
     * @param pseudo le pseudo du profil
     * @return le profil associé au pseudo
     * Crée un profil par défaut si aucun profil correspondant au pseudo n'est sauvegardé
     */
    public ProfilListeToDo importProfil(String pseudo)
    {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        String filename = pseudo + "_json";
        FileInputStream inputStream;
        String sJsonLu = "";

        ProfilListeToDo profil = new ProfilListeToDo();

        /* Import du fichier JSON de sauvegarde dans l'objet */

        try {
            inputStream = openFileInput(filename);
            int content;
            while ((content = inputStream.read()) != -1) {
                // convert to char and display it
                sJsonLu = sJsonLu + (char)content;
            }
            inputStream.close();

            profil = gson.fromJson(sJsonLu, ProfilListeToDo.class);
        }
        catch (Exception e) {

            /* Creation d'un profil par defaut */
            profil.setLogin(pseudo);
            Log.i("LIBRARY","Création du profil par défaut " + profil.getLogin());

            String fileContents = gson.toJson(profil);
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(fileContents.getBytes());
                outputStream.close();
                Log.i("LIBRARY","Création du fichier pseudo_json");
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.i("LIBRARY","Impossible de créer le fichier de sauvegarde du profil par défaut");
            }


        }

        return profil;
    }

    /** Permet de sauvegarder un profil au format JSon
     * @param profil le profil à sauvegarder
     */
    public void sauveProfilToJsonFile(ProfilListeToDo profil)
    {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        String filename = profil.getLogin() + "_json";
        String fileContents = gson.toJson(profil);
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
            Log.i("LIBRARY","Sauvegarde du fichier pseudo_json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

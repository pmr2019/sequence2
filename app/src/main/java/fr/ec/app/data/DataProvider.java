package fr.ec.app.data;

import android.util.Log;
import com.google.gson.Gson;
import fr.ec.app.data.api.PostResponse;
import fr.ec.app.data.api.PostResponseList;
import fr.ec.app.data.api.ProductHuntService;
import fr.ec.app.data.api.ProductHuntServiceFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {

  public static final String POST_API_END_POINT =
      "https://api.producthunt.com/v1/posts?access_token=46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abb";

  public static String getPostsFromWeb(String apiUrl) {

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Contiendra la réponse JSON brute sous forme de String .
    String posts = null;

    try {
      // Construire l' URL de l'API ProductHunt
      URL url = new URL(apiUrl);

      // Creer de la requête http vers  l'API ProductHunt , et ouvrir la connexion
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();

      // Lire le  input stream et le convertir String
      InputStream inputStream = urlConnection.getInputStream();
      StringBuffer buffer = new StringBuffer();
      if (inputStream == null) {
        // Nothing to do.
        return null;
      }
      reader = new BufferedReader(new InputStreamReader(inputStream));

      String line;
      while ((line = reader.readLine()) != null) {
        buffer.append(line + "\n");
      }

      if (buffer.length() == 0) {
        // Si le stream est vide, on revoie null;
        return null;
      }
      posts = buffer.toString();
    } catch (IOException e) {
      Log.e("TAG", "Error ", e);
      return null;
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (final IOException e) {
          Log.e("TAG", "Error closing stream", e);
        }
      }
    }

    return posts;
  }

  public static List<PostResponse> getPosts() {
    //String response = getPostsFromWeb(POST_API_END_POINT);
    //Gson gson = new Gson();
    //
    //PostResponseList postResponseList = gson.fromJson(response, PostResponseList.class);
    ProductHuntService service = ProductHuntServiceFactory.createService(ProductHuntService.class);
    List<PostResponse> postResponses = new ArrayList<>(0);
    try {
      postResponses = service.getPosts().execute().body().posts;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return postResponses;
  }
}

package fr.ec.app.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductHuntServiceFactory {


  public static Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("https://api.producthunt.com/")
      .addConverterFactory(GsonConverterFactory.create())
      .build();

  public static  <T> T createService(Class<T> type) {
    return retrofit.create(type);
  }
}

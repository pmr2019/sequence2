package fr.ec.app.data.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ProductHuntService {

  @GET("v1/posts?access_token=46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abbb")
  public Call<PostResponseList> getPosts();
}

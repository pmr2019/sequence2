package api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ToDoApi {

    @POST("authenticate")
    Call<ReponseApi> authenticate(@Query("user") String user, @Query("password") String mdp );


}

package fr.ec.app.data;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.UiThread;
import fr.ec.app.Utils;
import fr.ec.app.data.api.PostResponseList;
import fr.ec.app.data.api.ProductHuntService;
import fr.ec.app.data.api.ProductHuntServiceFactory;
import fr.ec.app.data.convertr.Converter;
import fr.ec.app.data.database.RoomProductHuntDb;
import fr.ec.app.data.database.dao.PostDao;
import fr.ec.app.data.model.Post;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import retrofit2.Response;

public class DataProvider {

  private List<Future> futures = new ArrayList<>();
  private final PostDao postDao;
  private final ProductHuntService service =
      ProductHuntServiceFactory.createService(ProductHuntService.class);

  private final Converter converter = new Converter();
  private final Handler uiHandler;

  public DataProvider(Context context) {
    uiHandler = new Handler(context.getMainLooper());
    postDao = RoomProductHuntDb.getDatabase(context).postDao();
  }

  //EXEMPLE AVEC UN EXECUTOR
  public void syncPosts(final PostsListener listener) {

   Future future = Utils.BACKGROUND.submit(new Runnable() {
      @Override public void run() {
        try {
          Response<PostResponseList> response = service.getPosts().execute();
          if( response.isSuccessful()) {
            final List<Post> post = converter.from(response.body().posts);
            postDao.save(post);
            uiHandler.post(new Runnable() {
              @Override public void run() {
                listener.onSuccess(post);
              }
            });

          }else {
            uiHandler.post(new Runnable() {
              @Override public void run() {
                listener.onError();
              }
            });

          }

        } catch (IOException e) {
          uiHandler.post(new Runnable() {
            @Override public void run() {
              listener.onError();
            }
          });
        }
      }
    });
    futures.add(future);

  }

  public void stop() {
    for (Future future : futures) {
      if(!future.isDone()){
        future.cancel(true);

      }
    }
    futures.clear();
  }

  public List<Post> loadPost() {
    return postDao.getPosts();
  }

  public void save(List<Post> postList) {
    postDao.save(postList);
  }

  public interface PostsListener {
    @UiThread
    public void onSuccess(List<Post> posts);
    @UiThread
    public void onError();
  }

}

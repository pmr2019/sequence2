package fr.ec.app.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.ec.app.R;
import fr.ec.app.data.DataProvider;
import fr.ec.app.data.model.Post;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.ActionListener {

  private ItemAdapter itemAdapter;
  private DataProvider dataProvider;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    dataProvider = new DataProvider(this);

    RecyclerView recyclerView = findViewById(R.id.list);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    itemAdapter = new ItemAdapter( this);
    recyclerView.setAdapter(itemAdapter);
    recyclerView.addItemDecoration(
        new DividerItemDecoration(this,LinearLayout.VERTICAL));
    new PostLoadTask().execute();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      sync();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void sync() {
    findViewById(R.id.progess).setVisibility(View.VISIBLE);

    dataProvider.syncPosts(new DataProvider.PostsListener() {
      @Override public void onSuccess(List<Post> posts) {
        itemAdapter.show(posts);
        findViewById(R.id.progess).setVisibility(View.GONE);
      }

      @Override public void onError() {
        findViewById(R.id.progess).setVisibility(View.GONE);
      }
    });
  }

  @Override protected void onStop() {
    super.onStop();
    dataProvider.stop();
  }

  public void onItemClicked(String data) {
    Toast.makeText(this,data,Toast.LENGTH_LONG).show();
  }

  //TODO remplacer l'asynctask avec un executor + un livedata
  public class PostLoadTask extends AsyncTask<Object,Void,List<Post>> {

    @Override protected void onPreExecute() {
      super.onPreExecute();
      findViewById(R.id.progess).setVisibility(View.VISIBLE);
      Log.d("TAG", "onPreExecute() called +" +Thread.currentThread().getName());
    }

    @Override protected List<Post> doInBackground(Object... objects) {
      return  dataProvider.loadPost();
    }

    @Override protected void onPostExecute(List<Post> posts) {
      super.onPostExecute(posts);
      findViewById(R.id.progess).setVisibility(View.GONE);
      itemAdapter.show(posts);
    }
  }

}

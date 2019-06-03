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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.ec.app.R;
import fr.ec.app.data.DataProvider;
import fr.ec.app.data.api.PostResponse;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.ActionListener {

  private ItemAdapter itemAdapter;
  private PostAsyncTask task;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    RecyclerView recyclerView = findViewById(R.id.list);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    itemAdapter = new ItemAdapter( this);
    recyclerView.setAdapter(itemAdapter);
    recyclerView.addItemDecoration(
        new DividerItemDecoration(this,LinearLayout.VERTICAL));
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      task = new PostAsyncTask();
      task.execute();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    task.cancel(true);
  }

  public void onItemClicked(String data) {
    Toast.makeText(this,data,Toast.LENGTH_LONG).show();
  }

  public class PostAsyncTask extends AsyncTask<Object,Void,List<PostResponse>> {

    @Override protected void onPreExecute() {
      super.onPreExecute();
      findViewById(R.id.progess).setVisibility(View.VISIBLE);
      Log.d("TAG", "onPreExecute() called +" +Thread.currentThread().getName());
    }

    @Override protected List<PostResponse> doInBackground(Object... objects) {
      return  DataProvider.getPosts();
    }


    @Override protected void onPostExecute(List<PostResponse> posts) {
      super.onPostExecute(posts);
      findViewById(R.id.progess).setVisibility(View.GONE);
      itemAdapter.show(posts);
    }
  }
}

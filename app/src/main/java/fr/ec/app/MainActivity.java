package fr.ec.app;

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
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.ActionListener {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    List<String> listItemData = newItemList();
    RecyclerView recyclerView = findViewById(R.id.list);

    //recyclerView.setLayoutManager(new LinearLayoutManager(this));
    GridLayoutManager lg = new GridLayoutManager(this, 2);
    lg.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
         if(position == 0)  {
           return  2;
        }
         else { return 1;}
      }
    });
    recyclerView.setLayoutManager(lg);

    recyclerView.setAdapter(new ItemAdapter(newItemList(), this));
    recyclerView.addItemDecoration(
        new DividerItemDecoration(this,LinearLayout.VERTICAL));
  }

  private List<String> newItemList() {

    // parser posts
    List<String> data = new ArrayList<>();
    for (int i = 0; i < 10000; i++) {
      data.add("Item n°"+i);
    }

    return data;
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
      AsyncTask task = new PostAsyncTask();
      task.execute();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public void onItemClicked(String data) {
    Toast.makeText(this,data,Toast.LENGTH_LONG).show();
  }

  public class PostAsyncTask extends AsyncTask<Object,Void,String> {

    @Override protected String doInBackground(Object... objects) {

      return  DataProvider.getPostsFromWeb(DataProvider.POST_API_END_POINT);
    }


    @Override protected void onPreExecute() {
      super.onPreExecute();
      findViewById(R.id.progess).setVisibility(View.VISIBLE);
      Log.d("TAG", "onPreExecute() called +" +Thread.currentThread().getName());
    }


    @Override protected void onPostExecute(String s) {
      super.onPostExecute(s);
      Log.d("TAG", "onPostExecute() called with: s = [" + s + "]");
      Log.d("TAG", "onPostExecute() called +" +Thread.currentThread().getName());
      findViewById(R.id.progess).setVisibility(View.GONE);
    }
  }
}

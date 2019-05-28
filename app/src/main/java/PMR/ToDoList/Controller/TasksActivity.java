package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import PMR.ToDoList.R;

public class TasksActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}

package com.anna.lozytska.achievementstimer.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.anna.lozytska.achievementstimer.R;
import com.anna.lozytska.achievementstimer.db.TasksProvider;
import com.anna.lozytska.achievementstimer.model.TaskModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

    }

    @OnClick(R.id.add_task)
    void addNewTask() {
        //FIXME test implementation, remove with normal feature
        TaskModel newTask = new TaskModel("Some title");
        TasksProvider.getInstance().addTask(newTask);
    }
}

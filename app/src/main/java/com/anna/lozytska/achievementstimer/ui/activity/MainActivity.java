package com.anna.lozytska.achievementstimer.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.anna.lozytska.achievementstimer.R;
import com.anna.lozytska.achievementstimer.db.TasksProvider;
import com.anna.lozytska.achievementstimer.model.TaskModel;
import com.anna.lozytska.achievementstimer.ui.widget.CurrentTaskAppBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.current_task_appbar)
    CurrentTaskAppBar mCurrentTaskAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCurrentTaskAppBar.setTasksProvider(TasksProvider.getInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentTaskAppBar.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCurrentTaskAppBar.onStop();
    }

    @OnClick(R.id.add_task)
    void addNewTask() {
        //FIXME test implementation, replace with normal feature

        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setView(editText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = editText.getText().toString();
                        TaskModel newTask = new TaskModel(title);
                        TasksProvider.getInstance().addTask(newTask);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
        .show();

    }
}

package com.anna.lozytska.achievementstimer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.anna.lozytska.achievementstimer.R;
import com.anna.lozytska.achievementstimer.ui.fragment.CommentsFragment;
import com.anna.lozytska.achievementstimer.ui.fragment.TaskListFragment;

/**
 * Created by Anna Lozytska on 01.09.16.
 */
public class TaskDetailsActivity extends AppCompatActivity {
    private static final String TASK_ID = "task_id";

    private long mTaskId;

    public static void startActivity(Context context, long taskId) {

        Intent intent = new Intent(context, TaskDetailsActivity.class)
                .putExtra(TASK_ID, taskId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        mTaskId = getIntent().getLongExtra(TASK_ID, -1L);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction()
                    .add(R.id.fragments_container, TaskListFragment.newInstance(mTaskId))
                    .add(R.id.fragments_container, CommentsFragment.newInstance(mTaskId));
            transaction.commit();
        }

    }
}

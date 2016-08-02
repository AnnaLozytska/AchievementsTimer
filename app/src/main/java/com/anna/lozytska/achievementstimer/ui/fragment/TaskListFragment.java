package com.anna.lozytska.achievementstimer.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anna.lozytska.achievementstimer.R;
import com.anna.lozytska.achievementstimer.db.Database;
import com.anna.lozytska.achievementstimer.db.modelspec.Task;
import com.anna.lozytska.achievementstimer.ui.adapter.TasksAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alozytska on 31.07.16.
 */
public class TaskListFragment extends Fragment {
    private static final int TASKS_PER_ROW = 2;

    @BindView(R.id.task_list)
    RecyclerView mTasksView;

    private TasksAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        ButterKnife.bind(this, root);

        //FIXME remove after testing
        Task task = new Task();
        task.setTitle("TestTitle");
        Database.getInstance().persist(task);

        mLayoutManager = new GridLayoutManager(getContext(), TASKS_PER_ROW);
        mTasksView.setLayoutManager(mLayoutManager);

        mAdapter = new TasksAdapter();
        //FIXME remove after implementation of fetching tasks from data layer
        List<Task> testTasks = new ArrayList<>();
        Task testTask = Database.getInstance().fetch(Task.class, 1);
        if (testTask != null) {
            testTasks.add(testTask);
        } else {
            Log.e("Fail", "test task is null");
        }
        mAdapter.setTasks(testTasks);
        mTasksView.setAdapter(mAdapter);

        mTasksView.addItemDecoration(new TaskDecoration());
        return root;
    }

    private class TaskDecoration extends RecyclerView.ItemDecoration {
        private static final int TASK_MARGIN = 10;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = TASK_MARGIN;
            outRect.top = TASK_MARGIN;
            outRect.left = TASK_MARGIN;
            outRect.right = TASK_MARGIN;
        }
    }
}

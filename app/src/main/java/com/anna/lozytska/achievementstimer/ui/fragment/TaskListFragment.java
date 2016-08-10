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

import com.anna.lozytska.achievementstimer.AppConfig;
import com.anna.lozytska.achievementstimer.R;
import com.anna.lozytska.achievementstimer.db.TasksProvider;
import com.anna.lozytska.achievementstimer.db.modelspec.Task;
import com.anna.lozytska.achievementstimer.ui.adapter.TasksAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alozytska on 31.07.16.
 */
public class TaskListFragment extends Fragment {
//    private static final String TAG = TaskListFragment.class.getSimpleName();
    private static final String TAG = AppConfig.TEST_LOG_TAG;

    private static final int TASKS_PER_ROW = 2;

    @BindView(R.id.task_list)
    RecyclerView mTasksView;

    private TasksAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private TasksProvider mTasksProvider;
    private Subscription mTasksSubscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksProvider = TasksProvider.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        ButterKnife.bind(this, root);

        mLayoutManager = new GridLayoutManager(getContext(), TASKS_PER_ROW);
        mTasksView.setLayoutManager(mLayoutManager);

        mAdapter = new TasksAdapter();
        mTasksView.setAdapter(mAdapter);

        mTasksView.addItemDecoration(new TaskDecoration());
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mTasksSubscription = mTasksProvider.getAchievementsStream()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Task>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Task task) {
                        Log.d(TAG, "getSubTasksStream(): onNext. Task id: " + task.getId());
                        mAdapter.addTask(task);
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!mTasksSubscription.isUnsubscribed()) {
            mTasksSubscription.unsubscribe();
        }
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

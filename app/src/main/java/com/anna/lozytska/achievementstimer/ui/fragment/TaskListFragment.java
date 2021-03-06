package com.anna.lozytska.achievementstimer.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.anna.lozytska.achievementstimer.model.TaskModel;
import com.anna.lozytska.achievementstimer.ui.adapter.TasksAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alozytska on 31.07.16.
 */
public class TaskListFragment extends Fragment {
    //    private static final String TAG = TaskListFragment.class.getSimpleName();
    private static final String TAG = AppConfig.TEST_LOG_TAG;

    private static final String PARENT_TASK_ID = "parent_task_id";
    private static final int TASKS_PER_ROW = 2;

    @BindView(R.id.task_list)
    RecyclerView mTasksView;

    private TasksAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    public static TaskListFragment newInstance(long parentTaskId) {
        Bundle args = new Bundle();
        args.putLong(PARENT_TASK_ID, parentTaskId);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        ButterKnife.bind(this, root);

        mLayoutManager = new GridLayoutManager(getContext(), TASKS_PER_ROW);
        mTasksView.setLayoutManager(mLayoutManager);
        mAdapter = new TasksAdapter(getContext());
        mTasksView.setAdapter(mAdapter);
        mTasksView.addItemDecoration(new TaskDecoration());

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        long parentTaskId = 0L;
        if (getArguments() != null) {
            parentTaskId = getArguments().getLong(PARENT_TASK_ID);
        }
        TasksProvider tasksProvider = TasksProvider.getInstance();
        mSubscriptions.add(
                tasksProvider.getSubTasksStream(parentTaskId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<TaskModel>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(List<TaskModel> tasks) {
                                mAdapter.setItems(tasks);
                            }
                        })
        );
        mSubscriptions.add(
                tasksProvider.getTasksUpdates()
                        .filter(byParentTask(parentTaskId))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<TaskModel>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                //TODO: DELETE AFTER TESTING:
                                Log.d(TAG, "onError", e);
                            }

                            @Override
                            public void onNext(TaskModel task) {
                                Log.d(TAG, "New task updated: " + task.toString());

                                if (task != null && mAdapter.canAddItem(task)) {
                                    mAdapter.addOrUpdateItem(task);
                                }
                            }
                        })
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubscriptions.clear();
    }

    @NonNull
    private Func1<TaskModel, Boolean> byParentTask(final long parentTaskId) {
        return new Func1<TaskModel, Boolean>() {
            @Override
            public Boolean call(TaskModel taskModel) {
                return taskModel.getParentTaskId() == parentTaskId;
            }
        };
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

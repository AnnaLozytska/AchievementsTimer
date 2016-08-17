package com.anna.lozytska.achievementstimer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anna.lozytska.achievementstimer.AppConfig;
import com.anna.lozytska.achievementstimer.R;
import com.anna.lozytska.achievementstimer.model.TaskModel;
import com.anna.lozytska.achievementstimer.ui.widget.TimerView;
import com.anna.lozytska.achievementstimer.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anna Lozytska on 31.07.16.
 */
public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    //    private static final String TAG = TaskListFragment.class.getSimpleName();
    private static final String TAG = AppConfig.TEST_LOG_TAG;

    private List<TaskModel> mTasks;

    public TasksAdapter() {
        mTasks = new ArrayList<>();
    }

    public void addTask(@NotNull TaskModel task) {
        int index = findTaskIndexById(task);
        if (index == -1) {
            Log.d(TAG, "task is new");
            switch (task.getState()) {
                case CREATED:
                case UPDATED:
                    mTasks.add(task);
                    notifyItemInserted(mTasks.indexOf(task));
                    break;
                default:
            }
        } else {
            Log.d(TAG, "task exists");
            switch (task.getState()) {
                case CREATED:
                case UPDATED:
                    mTasks.set(index, task);
                    notifyItemChanged(index);
                    break;
                case DELETED:
                    mTasks.remove(task);
                    notifyItemRemoved(index);
            }
        }
    }

    private int findTaskIndexById(TaskModel task) {
        for (TaskModel existingTask : mTasks) {
            if (existingTask.getId() == task.getId()) {
                return mTasks.indexOf(existingTask);
            }
        }
        return -1;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        final TaskModel task = mTasks.get(position);

        //TODO: load image

        holder.title.setText(task.getTitle());
        holder.timer.setText(task.getEstimatedTime() - task.getSpentTime());
        holder.isAchieved.setChecked(task.isAchieved());
    }

    @Override
    public int getItemCount() {
        return Utils.getSize(mTasks);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.timer)
        TimerView timer;
        @BindView(R.id.is_achieved)
        SwitchCompat isAchieved;

        public TaskViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

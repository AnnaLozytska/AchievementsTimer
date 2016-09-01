package com.anna.lozytska.achievementstimer.ui.adapter;

import android.content.res.Resources;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
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

    /**
     * Adds new item to {@link TasksAdapter} if there is no {@link TaskModel} with same id among existing items
     * and it has the same parent task as items in this adapter.
     * If item with same id is already added to this adapter, new item will replace it.
     * @param task to be added or updated in this adapter
     * @throws IllegalStateException if task has different parent task than items in this adapter.
     */
    public void addOrUpdateItem(@NotNull TaskModel task) throws IllegalStateException {
        if (!canAddItem(task)) {
            throw new IllegalStateException("Only tasks this same parent task id can be added");
        }

        int index = findTaskIndexById(task);
        if (index == -1) {
            switch (task.getState()) {
                case CREATED:
                case UPDATED:
                    mTasks.add(task);
                    notifyItemInserted(mTasks.indexOf(task));
                    break;
                default:
                    throw new IllegalStateException("Can't define state for task: " + task.toString());
            }
        } else {
            switch (task.getState()) {
                case CREATED:
                case UPDATED:
                    mTasks.set(index, task);
                    notifyItemChanged(index);
                    break;
                case DELETED:
                    mTasks.remove(index);
                    notifyItemRemoved(index);
                    break;
                default:
                    throw new IllegalStateException("Can't define state for task: " + task.toString());
            }
        }
    }

    /**
     * Checks if task is eligible for adding to this adapter.
     * @param task
     */
    public boolean canAddItem(@NotNull TaskModel task) {
        return !(getItemCount() > 0 && task.getParentTaskId() != mTasks.get(0).getParentTaskId());
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
    public void onBindViewHolder(final TaskViewHolder holder, int position) {
        final TaskModel task = mTasks.get(position);
        Resources resources = holder.itemView.getContext().getResources();

        if (task.isRunning()) {
            holder.itemView.setBackgroundColor(resources.getColor(R.color.colorRunningTaskBackground));
        } else {
            holder.itemView.setBackgroundColor(resources.getColor(android.R.color.white));
        }
        //TODO: load image

        holder.title.setText(task.getTitle());
        holder.timer.setText(task.getEstimatedTime() - task.getSpentTime());
        // recycle previous listener to avoid changing previously bound task
        holder.run.setOnClickListener(null);
        holder.run.setImageDrawable(task.isRunning() ?
                resources.getDrawable(R.drawable.ic_pause_white_24dp)
                : resources.getDrawable(R.drawable.ic_play_white_24dp));
        holder.run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.startRun(!task.isRunning());
            }
        });
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
        @BindView(R.id.start_pause)
        AppCompatImageButton run;

        public TaskViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

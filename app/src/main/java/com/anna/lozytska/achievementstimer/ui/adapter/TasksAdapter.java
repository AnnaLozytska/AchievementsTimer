package com.anna.lozytska.achievementstimer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anna.lozytska.achievementstimer.R;
import com.anna.lozytska.achievementstimer.db.modelspec.Task;
import com.anna.lozytska.achievementstimer.ui.widget.TimerView;
import com.anna.lozytska.achievementstimer.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alozytska on 31.07.16.
 */
public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Task> mTasks;

    public TasksAdapter() {
        mTasks = new ArrayList<>();
    }

    public void addTask(@NotNull Task task) {
        int index = mTasks.indexOf(task);
        if (index == -1) {
            switch (task.getTaskState()) {
                case CREATED:
                case UPDATED:
                    mTasks.add(task);
                    notifyItemInserted(mTasks.indexOf(task));
                    break;
                default:
            }
        } else {
            switch (task.getTaskState()) {
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

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        final Task task = getValue(position);

        //TODO: load image

        holder.title.setText(task.getTitle());
        holder.timer.setText(task.getEstimatedTime() - task.getTotalSpentTime());
        holder.isAchieved.setChecked(task.isAchieved());
    }

    private Task getValue(int position) {

        return null;
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

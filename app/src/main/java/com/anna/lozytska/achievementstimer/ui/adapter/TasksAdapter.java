package com.anna.lozytska.achievementstimer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anna.lozytska.achievementstimer.R;
import com.anna.lozytska.achievementstimer.model.TaskModel;
import com.anna.lozytska.achievementstimer.ui.widget.TimerView;
import com.anna.lozytska.achievementstimer.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alozytska on 31.07.16.
 */
public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<TaskModel> mTasks;

    public TasksAdapter() {
    }

    public void setTasks(List<TaskModel> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
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

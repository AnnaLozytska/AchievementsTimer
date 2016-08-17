package com.anna.lozytska.achievementstimer.model;

import com.anna.lozytska.achievementstimer.db.modelspec.TaskRow;

/**
 * Created by Anna Lozytska on 17.08.2016.
 */

/**
 * Wrapper around Database row model that hides details of its implementation
 * and provides independent and more convenient API
 */
public class TaskModel {
    private TaskRow taskRow;

    public static TaskModel fromTaskRow(TaskRow taskRow) {
        return new TaskModel(taskRow);
    }

    private TaskModel(TaskRow taskRow) {
        this.taskRow = taskRow;
    }

    public TaskModel(String title) {
        taskRow = new TaskRow();
        taskRow.setTitle(title);
    }

    public TaskRow toTaskRow() {
        return taskRow;
    }

    public long getId() {
        return taskRow.getId();
    }

    public String getTitle() {
        return taskRow.getTitle();
    }

    public void setTitle(String title) {
        taskRow.setTitle(title);
        updateState();
    }

    public String getDescription() {
        return taskRow.getDescription();
    }

    public void setDescription(String description) {
        taskRow.setDescription(description);
        updateState();
    }

    public String getImageUrl() {
        return taskRow.getImageUrl();
    }

    public void setImageUrl(String imageUrl) {
        taskRow.setImageUrl(imageUrl);
        updateState();
    }

    public long getEstimatedTime() {
        return  taskRow.getEstimatedTime();
    }

    public void setEstimatedTime(long estimatedTime) {
        taskRow.setEstimatedTime(estimatedTime);
        updateState();
    }
    /**
     * Returns sum of all time intervals ever spent on this task.
     */
    public long getSpentTime() {
        long spentSinceLastStart = taskRow.getLastStartTimestamp() == -1 ?
                0 : System.currentTimeMillis() - taskRow.getLastStartTimestamp();
        return taskRow.getSpentBeforeLastStart() + spentSinceLastStart;
    }

    public void setSpentTime(long spentTime) {
        long currentInterval = 0L;
        if (isRunning()) {
            currentInterval = System.currentTimeMillis() - taskRow.getLastStartTimestamp();
        }
        taskRow.setSpentBeforeLastStart(spentTime - currentInterval);
        updateState();
    }

    public boolean isRunning() {
        return taskRow.getLastStartTimestamp() != -1;
    }

    public void startRun() {
        if (taskRow.getLastStartTimestamp() != -1) {
            throw new IllegalStateException("Task is already running");
        }
        taskRow.setLastStartTimestamp(System.currentTimeMillis());
        updateState();
    }

    public void stopRun() {
        if (taskRow.getLastStartTimestamp() == -1) {
            throw new IllegalStateException("Task is already stopped");
        }
        long lastInterval = System.currentTimeMillis() - taskRow.getLastStartTimestamp();
        taskRow.setSpentBeforeLastStart(taskRow.getSpentBeforeLastStart() + lastInterval);
        taskRow.setLastStartTimestamp(-1L);
        updateState();
    }

    public boolean isAchieved() {
        return taskRow.getAchievedTimestamp() != -1;
    }

    public void setIsAchieved(boolean isAchieved) {
        if (isAchieved) {
            taskRow.setAchievedTimestamp(System.currentTimeMillis());
        } else {
            taskRow.setAchievedTimestamp(-1L);
        }
        updateState();
    }

    /**
     * Returns {@link TaskState} depending on what last transaction was made
     * on this task - creation, update or deletion.
     *
     * @return task's {@link TaskState}
     */
    public TaskState getState() {
        if (taskRow.getDeletedTimestamp() != -1) {
            return TaskState.DELETED;
        }
        if (taskRow.getUpdatedTimestamp() != -1) {
            return TaskState.UPDATED;
        }
        if (taskRow.getCreatedTimestamp() != -1) {
            return TaskState.CREATED;
        }
        return TaskState.UNKNOWN;
    }

    public void setState(TaskState taskState) {
        long timeStamp = System.currentTimeMillis();
        switch (taskState) {
            case CREATED:
                taskRow.setCreatedTimestamp(timeStamp);
                taskRow.setUpdatedTimestamp(-1L);
                taskRow.setDeletedTimestamp(-1L);
                break;
            case UPDATED:
                taskRow.setUpdatedTimestamp(timeStamp);
                taskRow.setDeletedTimestamp(-1L);
                break;
            case DELETED:
                taskRow.setDeletedTimestamp(timeStamp);
                break;
        }
    }

    private void updateState() {
        if (getId() == 0) {
            setState(TaskState.CREATED);
        } else {
            setState(TaskState.UPDATED);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TaskModel)) {
            return false;
        }
        //FIXME stub, need to be rewritten
        return taskRow.equals(((TaskModel) obj).toTaskRow());
    }

    @Override
    public String toString() {
        return "TaskModel: id = " + taskRow.getId()
                + ", title = " + taskRow.getTitle()
                + ", description = " + taskRow.getDescription()
                + ", imageUrl = " + taskRow.getImageUrl()
                + ", ";
    }
}

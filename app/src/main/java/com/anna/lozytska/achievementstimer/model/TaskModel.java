package com.anna.lozytska.achievementstimer.model;

/**
 * Created by alozytska on 31.07.16.
 */
public class TaskModel {
    private long id;
    private String title;
    private String description;
    private String imageUrl;
    private long estimatedTime;
    private long spentTime;
    private long parentTaskId;
    private boolean isAchieved;

    public TaskModel(String title) {
        this(0L, title, null, 0L, 0L, false);
    }

    public TaskModel(long id, String title, String description,
                     long estimatedTime, long parentTaskId, boolean isAchieved) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.parentTaskId = parentTaskId;
        this.isAchieved = isAchieved;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public long getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(long spentTime) {
        this.spentTime = spentTime;
    }

    public long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public boolean isAchieved() {
        return isAchieved;
    }

    public void setAchieved(boolean achieved) {
        isAchieved = achieved;
    }
}

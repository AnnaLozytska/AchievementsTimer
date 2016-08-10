package com.anna.lozytska.achievementstimer.db.modelspec;

import com.anna.lozytska.achievementstimer.model.TaskState;
import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.ModelMethod;
import com.yahoo.squidb.annotations.TableModelSpec;

/**
 * Created by alozytska on 01.08.16.
 */
@TableModelSpec(className="Task", tableName="tasks")
public class TaskSpec {
    // primary key _id is autogenerated
    @ColumnSpec(constraints = "NOT NULL")
    String title;
    String description;
    String imageUrl;
    @ColumnSpec(defaultValue = "0")
    long estimatedTime;
    @ColumnSpec(defaultValue = "0")
    long spentBeforeLastStart;
    @ColumnSpec(defaultValue = "0")
    long parentTaskId;
    @ColumnSpec(defaultValue = "false")
    boolean isCurrent;
    @ColumnSpec(defaultValue = "-1")
    long lastStartTimestamp;
    @ColumnSpec(defaultValue = "-1")
    long achievedTimestamp;
    @ColumnSpec(defaultValue = "-1")
    long createdTimestamp;
    @ColumnSpec(defaultValue = "-1")
    long updatedTimestamp;
    @ColumnSpec(defaultValue = "-1")
    long deletedTimestamp;

    @ModelMethod
    public static boolean isAchieved(Task task) {
        return task.getAchievedTimestamp() != -1;
    }

    @ModelMethod
    public static void setIsAchieved(Task task, boolean isAchieved) {
        if (isAchieved) {
            task.setAchievedTimestamp(System.currentTimeMillis());
        } else {
            task.setAchievedTimestamp(-1L);
        }
    }

    /**
     * Returns sum of all time intervals ever spent on this task.
     */
    @ModelMethod
    public static long getTotalSpentTime(Task task) {
        long spentSinceLastStart = task.getLastStartTimestamp() == -1 ?
                0 : System.currentTimeMillis() - task.getLastStartTimestamp();
        return task.getSpentBeforeLastStart() + spentSinceLastStart;
    }

    /**
     * Returns {@link TaskState} depending on what transaction was made
     * on this task - creation, update or deletion.
     *
     * @param task
     * @return task's {@link TaskState}
     */
    @ModelMethod
    public static TaskState getState(Task task) {
        if (task.getDeletedTimestamp() != -1) {
            return TaskState.DELETED;
        }
        if (task.getUpdatedTimestamp() != -1) {
            return TaskState.UPDATED;
        }
        if (task.getCreatedTimestamp() != -1) {
            return TaskState.CREATED;
        }
        return TaskState.UNKNOWN;
    }

    @ModelMethod
    public static void setState(Task task, TaskState taskState) {
        long timeStamp = System.currentTimeMillis();
        switch (taskState) {
            case CREATED:
                task.setCreatedTimestamp(timeStamp);
                task.setUpdatedTimestamp(-1L);
                task.setDeletedTimestamp(-1L);
                break;
            case UPDATED:
                task.setUpdatedTimestamp(timeStamp);
                task.setDeletedTimestamp(-1L);
                break;
            case DELETED:
                task.setDeletedTimestamp(timeStamp);
                break;
        }
    }
}

package com.anna.lozytska.achievementstimer.db.modelspec;

import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.TableModelSpec;

/**
 * Created by alozytska on 01.08.16.
 */
@TableModelSpec(className="TaskRow", tableName="tasks")
public class TaskSpec {
    // primary key _id is autogenerated
    @ColumnSpec(constraints = "NOT NULL")
    String title;
    @ColumnSpec(defaultValue="")
    String description;
    @ColumnSpec(defaultValue="")
    String imageUrl;
    @ColumnSpec(defaultValue = "0")
    long estimatedTime;
    /**
     * Time spent on this task before it was started to run for the last time.
     * Should increase every time this task stops running by adding the difference between
     * the moment when it was stopped and {@link #lastStartTimestamp}
     */
    @ColumnSpec(defaultValue = "0")
    long spentBeforeLastStart;
    @ColumnSpec(defaultValue = "0")
    long parentTaskId;
    @ColumnSpec(defaultValue = "false")
    boolean isCurrent;
    /**
     * Timestamp when this task was started to run for the last time.
     * Should be set to -1 when running of this task is stopped.
     */
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



}

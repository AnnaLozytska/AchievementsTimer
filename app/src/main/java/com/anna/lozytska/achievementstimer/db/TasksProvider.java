package com.anna.lozytska.achievementstimer.db;

/**
 * Created by alozytska on 09.08.16.
 */

import android.util.Log;

import com.anna.lozytska.achievementstimer.AppConfig;
import com.anna.lozytska.achievementstimer.db.modelspec.Task;
import com.anna.lozytska.achievementstimer.model.TaskState;

import rx.Observable;

/**
 * Layer class between tasks presenters and persistent storage. Provides convenient API for
 * requesting and updating tasks.
 */
public class TasksProvider {
//    private static final String TAG = TasksProvider.class.getSimpleName();
private static final String TAG = AppConfig.TEST_LOG_TAG;

    private static volatile TasksProvider sInstance;

    private Task testTask;
    private int count;

    public static TasksProvider getInstance() {
        synchronized (TasksProvider.class) {
            if (sInstance == null) {
                sInstance = new TasksProvider();
            }
        }
        return sInstance;
    }

    private TasksProvider() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test title " + count);
        testTask.setTaskState(TaskState.CREATED);
    }

    public Observable<Task> getCurrentTask() {
        //TODO: TBD
        return Observable.just(testTask);
    }

    public Observable<Task> getAchievementsStream() {
        Log.d(TAG, "Entered getAchievementsStream()");
        return getSubTasksStream(0L);
    }

    public Observable<Task> getSubTasksStream(long parentTaskId) {
        //TODO: TBD
        count++;
        testTask.setTitle("Test title " + count);
        return Observable.just(testTask);

    }

    public Observable<Task> getTask(long id) {
        //TODO: TBD
        return Observable.just(testTask);

    }

    public void addTask(Task task) {
        //TODO: TBD
        Log.d(TAG, "TBD: task added");
    }

    public void updateTask(Task task) {
        //TODO: TBD
        Log.d(TAG, "TBD: task updated");

    }

    public void archiveTask(Task task) {
        //TODO: TBD
        Log.d(TAG, "TBD: task archived");

    }
}

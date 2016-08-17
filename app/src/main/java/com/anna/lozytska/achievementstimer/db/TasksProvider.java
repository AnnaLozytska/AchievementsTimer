package com.anna.lozytska.achievementstimer.db;

/**
 * Created by alozytska on 09.08.16.
 */

import android.util.Log;

import com.anna.lozytska.achievementstimer.AppConfig;
import com.anna.lozytska.achievementstimer.model.TaskModel;

import rx.Observable;

/**
 * Layer class between tasks presenters and persistent storage. Provides convenient API for
 * requesting and updating tasks.
 */
public class TasksProvider {
    //    private static final String TAG = TasksProvider.class.getSimpleName();
    private static final String TAG = AppConfig.TEST_LOG_TAG;

    private static volatile TasksProvider sInstance;

    private final AppReactiveSquidDatabase mDatabase;

    public static TasksProvider getInstance() {
        synchronized (TasksProvider.class) {
            if (sInstance == null) {
                sInstance = new TasksProvider();
            }
        }
        return sInstance;
    }

    private TasksProvider() {
        mDatabase = AppReactiveSquidDatabase.getInstance();
    }

    public Observable<TaskModel> getCurrentTask() {
        //TODO: TBD
        return null;
    }

    public Observable<TaskModel> getAchievementsStream() {
        Log.d(TAG, "Entered getAchievementsStream()");
        return getSubTasksStream(0L);
    }

    public Observable<TaskModel> getSubTasksStream(long parentTaskId) {
        //TODO: TBD
        return null;

    }

    public Observable<TaskModel> getTask(long id) {
        //TODO: TBD
        return null;

    }

    public void addTask(TaskModel task) {
        //TODO: TBD
        Log.d(TAG, "TBD: task added");
    }

    public void updateTask(TaskModel task) {
        //TODO: TBD
        Log.d(TAG, "TBD: task updated");

    }

    public void deleteTask(TaskModel task) {
        //TODO: TBD
        Log.d(TAG, "TBD: task deleted");

    }
}

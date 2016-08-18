package com.anna.lozytska.achievementstimer.db;

/**
 * Created by alozytska on 09.08.16.
 */

import android.util.Log;

import com.anna.lozytska.achievementstimer.AppConfig;
import com.anna.lozytska.achievementstimer.model.TaskModel;
import com.anna.lozytska.achievementstimer.model.TaskState;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Layer class between tasks presenters and persistent storage. Provides convenient API for
 * requesting and updating tasks.
 */
public class TasksProvider {
    //    private static final String TAG = TasksProvider.class.getSimpleName();
    private static final String TAG = AppConfig.TEST_LOG_TAG;

    private static volatile TasksProvider sInstance;

    private final AppReactiveSquidDatabase mDatabase;
    private final PublishSubject<TaskModel> mTasksUpdates;

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
        mTasksUpdates = PublishSubject.create();
    }

    public Observable<TaskModel> getTasksUpdates() {
        return mTasksUpdates;
    }

    public Observable<TaskModel> getCurrentTask() {
        //TODO: TBD
        return null;
    }

    public Observable<TaskModel> getAchievementsStream() {
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
        mDatabase.beginTransaction();
        boolean isSuccess = mDatabase.persist(task.toTaskRow());
        if (isSuccess) {
            mDatabase.setTransactionSuccessful();
            task.setState(TaskState.CREATED);
            mTasksUpdates.onNext(task);
        } else {
            mTasksUpdates.onError(new Throwable("Failed to add: " + task.toString()));
        }
        mDatabase.endTransaction();
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

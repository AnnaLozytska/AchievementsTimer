package com.anna.lozytska.achievementstimer.businesslogic;

/**
 * Created by alozytska on 09.08.16.
 */

import com.anna.lozytska.achievementstimer.db.modelspec.Task;

import rx.Observable;

/**
 * Layer class between tasks presenters and persistent storage. Provides convenient API for
 * requesting and updating tasks.
 */
public class TasksManager {
    private static volatile TasksManager sInstance;

    public static TasksManager getInstance() {
        synchronized (TasksManager.class) {
            if (sInstance == null) {
                sInstance = new TasksManager();
            }
        }
        return sInstance;
    }

    public Observable<Task> getCurrentTask() {

    }

    public Observable<Task> getTasks(long parentTaskId) {

    }

    public Observable<Task> getTask(long id) {

    }

    public void addTask(Task task) {

    }

    public void updateTask(Task task) {

    }

    public void deleteTask(Task task) {

    }
}

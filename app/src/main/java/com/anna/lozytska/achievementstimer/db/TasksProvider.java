package com.anna.lozytska.achievementstimer.db;

/**
 * Created by alozytska on 09.08.16.
 */

import android.util.Log;

import com.anna.lozytska.achievementstimer.AppConfig;
import com.anna.lozytska.achievementstimer.db.modelspec.TaskRow;
import com.anna.lozytska.achievementstimer.model.TaskModel;
import com.anna.lozytska.achievementstimer.model.TaskState;
import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.sql.Query;

import rx.Observable;
import rx.Subscriber;
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

    public Observable<TaskModel> getSubTasksStream(final long parentTaskId) {
        return Observable.create(new Observable.OnSubscribe<TaskModel>() {
            @Override
            public void call(Subscriber<? super TaskModel> subscriber) {
                Query tasksQuery = Query.select(TaskRow.PROPERTIES)
                        .where(TaskRow.PARENT_TASK_ID.eq(parentTaskId));
                SquidCursor<TaskRow> tasks = mDatabase.query(TaskRow.class, tasksQuery);
                try {
                    TaskRow taskRow = new TaskRow();
                    for (tasks.moveToFirst(); !tasks.isAfterLast(); tasks.moveToNext()) {
                        taskRow.readPropertiesFromCursor(tasks);
                        subscriber.onNext(TaskModel.fromTaskRow(taskRow));
                    }
                } finally {
                    tasks.close();
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<TaskModel> getTask(long id) {
        //TODO: TBD
        return null;

    }

    public void addTask(TaskModel task) {
        mDatabase.beginTransaction();
        task.setState(TaskState.CREATED);
        boolean isSuccess = mDatabase.persist(task.toTaskRow());
        if (isSuccess) {
            mDatabase.setTransactionSuccessful();
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

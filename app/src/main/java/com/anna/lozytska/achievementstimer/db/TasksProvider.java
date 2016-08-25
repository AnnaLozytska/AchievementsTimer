package com.anna.lozytska.achievementstimer.db;

/**
 * Created by Anna Lozytska on 09.08.16.
 */

import android.util.Log;

import com.anna.lozytska.achievementstimer.AppConfig;
import com.anna.lozytska.achievementstimer.db.modelspec.TaskRow;
import com.anna.lozytska.achievementstimer.model.TaskModel;
import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.sql.Query;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Layer class between tasks presenters and persistent storage. Provides convenient API for
 * requesting and updating tasks.
 */
public class TasksProvider {
    //    private static final String TAG = TasksProvider.class.getSimpleName();
    private static final String TAG = AppConfig.TEST_LOG_TAG;

    private static volatile TasksProvider sInstance;

    private final AppReactiveSquidDatabase mDatabase;
    private final SerializedSubject<TaskModel, TaskModel> mTasksUpdates;

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
        mTasksUpdates = new SerializedSubject<TaskModel, TaskModel>(PublishSubject.<TaskModel>create()) {
            @Override
            public void onNext(TaskModel taskModel) {
                Log.d(TAG, "Called doOnNext()");
                boolean isUpdated = updateTask(taskModel);
                if (isUpdated) {
                    super.onNext(taskModel);
                } else {
                    Log.d(TAG, "Failed to update");
                    super.onError(new Throwable("Failed to update: " + taskModel.toString()));
                }
            }
        };
    }

    public Observable<TaskModel> getTasksUpdates() {
        return mTasksUpdates;
    }

    public Single<TaskModel> getCurrentTask() {
        return Single.create(new Single.OnSubscribe<TaskModel>() {
            @Override
            public void call(SingleSubscriber<? super TaskModel> singleSubscriber) {
                Query currentTaskQuery = Query.select(TaskRow.PROPERTIES)
                        .where(TaskRow.IS_CURRENT.eq(true));
                TaskRow currentTaskRow = mDatabase.fetchByQuery(TaskRow.class, currentTaskQuery);
                if (currentTaskRow != null) {
                    TaskModel currentTaskModel = TaskModel.fromTaskRow(currentTaskRow);
                    currentTaskModel.setUpdatesObserver(mTasksUpdates);
                    singleSubscriber.onSuccess(currentTaskModel);
                } else {
                    singleSubscriber.onSuccess(null);
                }
            }
        });
    }

    public Observable<TaskModel> getSubTasksStream(final long parentTaskId) {
        return Observable.create(new Observable.OnSubscribe<TaskModel>() {
                    @Override
                    public void call(Subscriber<? super TaskModel> subscriber) {
                        Query tasksQuery = Query.select(TaskRow.PROPERTIES)
                                .where(TaskRow.PARENT_TASK_ID.eq(parentTaskId));
                        SquidCursor<TaskRow> tasks = mDatabase.query(TaskRow.class, tasksQuery);
                        try {
                            for (tasks.moveToFirst(); !tasks.isAfterLast(); tasks.moveToNext()) {
                                TaskRow taskRow = new TaskRow();
                                taskRow.readPropertiesFromCursor(tasks);
                                TaskModel taskModel = TaskModel.fromTaskRow(taskRow);
                                taskModel.setUpdatesObserver(mTasksUpdates);
                                subscriber.onNext(taskModel);
                            }
                        } finally {
                            tasks.close();
                            subscriber.onCompleted();
                        }
                    }
                })
                .onBackpressureBuffer();
    }

    public Observable<TaskModel> getTask(long id) {
        //TODO: TBD
        return null;

    }

    public void addTask(TaskModel task) {
        task.setUpdatesObserver(mTasksUpdates);
        mTasksUpdates.onNext(task);
    }

    private boolean updateTask(TaskModel task) {
        return mDatabase.persist(task.toTaskRow());
    }
}

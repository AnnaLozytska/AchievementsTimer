package com.anna.lozytska.achievementstimer.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.anna.lozytska.achievementstimer.R;
import com.anna.lozytska.achievementstimer.db.TasksProvider;
import com.anna.lozytska.achievementstimer.model.TaskModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CurrentTaskAppBar extends AppBarLayout {
    private static final int TIMER_UPDATE_MESSAGE = 0;
    private static final long TIMER_UPDATE_INTERVAL_MS = DateUtils.SECOND_IN_MILLIS;

    @BindView(R.id.current_task)
    TextView mCurrentTask;
    @BindView(R.id.timer)
    TimerView mTimerView;

    private TasksProvider mTasksProvider;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    private Handler mTimerUpdateHandler;

    public CurrentTaskAppBar(Context context) {
        this(context, null);
    }

    public CurrentTaskAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.appbar_current_task, this, true);
        ButterKnife.bind(this, this);
    }

    public void onStart() {
        mSubscriptions.add(
                mTasksProvider.getCurrentTask()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getOnNewCurrentTaskAction())
        );
        mSubscriptions.add(
                mTasksProvider.getTasksUpdates()
                .subscribe(getOnNewCurrentTaskAction())
        );
    }

    @NonNull
    private Action1<TaskModel> getOnNewCurrentTaskAction() {
        return new Action1<TaskModel>() {
            @Override
            public void call(TaskModel taskModel) {
                if (taskModel != null) {
                    mCurrentTask.setText(taskModel.getTitle());
                    mTimerView.setVisibility(VISIBLE);
                    mTimerView.setText(taskModel.getEstimatedTime() - System.currentTimeMillis());
                    if (mTimerUpdateHandler == null) {
                        setupTimerHandler();
                    }
                    sendDelayedTimerUpdate();
                } else {
                    mCurrentTask.setText(getContext().getString(R.string.no_current_task));
                    mTimerView.setVisibility(GONE);
                    stopTimerUpdate();
                }
            }
        };
    }

    public void onStop() {
        mSubscriptions.clear();
        stopTimerUpdate();
    }

    public void setTasksProvider(TasksProvider tasksProvider) {
        mTasksProvider = tasksProvider;
    }

    private void setupTimerHandler() {
        mTimerUpdateHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == TIMER_UPDATE_MESSAGE) {
                    //displayRemainingTime();
                    sendDelayedTimerUpdate();
                }
                return false;
            }
        });
    }

    private void sendDelayedTimerUpdate() {
        Message updateTimerMessage = Message.obtain(mTimerUpdateHandler, TIMER_UPDATE_MESSAGE);
        mTimerUpdateHandler.sendMessageDelayed(updateTimerMessage, TIMER_UPDATE_INTERVAL_MS);
    }

    private void stopTimerUpdate() {
        if (mTimerUpdateHandler != null) {
            mTimerUpdateHandler.removeMessages(TIMER_UPDATE_MESSAGE);
        }
    }
}

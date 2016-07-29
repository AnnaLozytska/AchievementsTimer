package com.anna.lozytska.achievementstimer.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anna.lozytska.achievementstimer.R;
import com.anna.lozytska.achievementstimer.ui.widget.TimerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolBarFragment extends Fragment {
    private static final String TIME_BALANCE_DEADLINE = "time_balance_deadline";
    private static final int TIMER_UPDATE_MESSAGE = 0;
    private static final long TIMER_UPDATE_INTERVAL_MS = DateUtils.SECOND_IN_MILLIS;

    @BindView(R.id.timer)
    TimerView mTimerView;

    private Handler mTimerUpdateHandler;
    private long mTimeBalanceDeadline;
    private SharedPreferences mSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_toolbar, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        }
        mTimeBalanceDeadline = mSharedPreferences.getLong(TIME_BALANCE_DEADLINE, 0L);
        if (mTimeBalanceDeadline != 0) {
            if (mTimerUpdateHandler == null) {
                setupTimerHandler();
            }
            sendDelayedTimerUpdate();
//            mStartView.setClickable(false);
//            mResetView.setClickable(true);
        } else {
            stopTimerUpdate();
//            mStartView.setClickable(true);
//            mResetView.setClickable(false);
        }
        displayRemainingTime();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimerUpdate();
    }

    private void setupTimerHandler() {
        mTimerUpdateHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == TIMER_UPDATE_MESSAGE) {
                    displayRemainingTime();
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

    private void saveTimeBalanceDeadline(long timeBalanceDeadline) {
        mTimeBalanceDeadline = timeBalanceDeadline;
        mSharedPreferences.edit().putLong(TIME_BALANCE_DEADLINE, timeBalanceDeadline).apply();
    }

    private void displayRemainingTime() {
        if (mTimeBalanceDeadline == 0) {
            mTimerView.setText(getString(R.string.no_current_task));
        } else {
            long timeRemaining = mTimeBalanceDeadline - System.currentTimeMillis();
            mTimerView.setText(timeRemaining);
        }
    }
}

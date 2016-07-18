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
import android.widget.Button;
import android.widget.TextView;

import com.anna.lozytska.achievementstimer.R;

import java.util.concurrent.TimeUnit;

public class GlobalTimerFragment extends Fragment {
    private static final String TIME_BALANCE_DEADLINE = "time_balance_deadline";
    private static final int TIMER_UPDATE_MESSAGE = 0;
    private static final long TIMER_UPDATE_INTERVAL_MS = DateUtils.SECOND_IN_MILLIS;

    private TextView mTimerView;
    private Button mStartView;
    private Button mResetView;

    private Handler mTimerUpdateHandler;
    private long mTimeBalanceDeadline;
    private SharedPreferences mSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_global_timer, container, false);
        mTimerView = (TextView) root.findViewById(R.id.timer);

        mStartView = (Button) root.findViewById(R.id.start);
        mStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimeBalanceDeadline(System.currentTimeMillis() + DateUtils.HOUR_IN_MILLIS); //TODO: update
                displayRemainingTime();
                if (mTimerUpdateHandler == null) {
                    setupTimerHandler();
                }
                sendDelayedTimerUpdate();
                mStartView.setClickable(false);
                mResetView.setClickable(true);
            }
        });
        mResetView = (Button) root.findViewById(R.id.reset);
        mResetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimeBalanceDeadline(0L);
                stopTimerUpdate();
                displayRemainingTime();
                mStartView.setClickable(true);
                mResetView.setClickable(false);
            }
        });

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
            mStartView.setClickable(false);
            mResetView.setClickable(true);
        } else {
            stopTimerUpdate();
            mStartView.setClickable(true);
            mResetView.setClickable(false);
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
            mTimerView.setText(getString(R.string.start_timer_message));
            return;
        }

        long timeRemaining = mTimeBalanceDeadline - System.currentTimeMillis();
        long hours = TimeUnit.MILLISECONDS.toHours(timeRemaining);
        timeRemaining -= hours * DateUtils.HOUR_IN_MILLIS;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
        timeRemaining -= minutes * DateUtils.MINUTE_IN_MILLIS;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeRemaining);

        if (hours < 0 || minutes < 0) {
            seconds = Math.abs(seconds);
        }
        if (hours < 0) {
            minutes = Math.abs(minutes);
        }

        StringBuilder builder = new StringBuilder();
        if (hours != 0) {
            builder.append(getString(R.string.timer_hours, hours));
        }
        if (minutes != 0 || hours != 0) {
            builder.append(getString(R.string.timer_minutes, minutes));
        }
        builder.append(getString(R.string.timer_seconds, seconds));
        mTimerView.setText(builder.toString());
    }
}

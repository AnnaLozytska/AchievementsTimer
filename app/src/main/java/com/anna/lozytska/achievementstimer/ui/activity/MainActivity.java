package com.anna.lozytska.achievementstimer.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anna.lozytska.achievementstimer.R;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TIME_BALANCE_DEADLINE = "time_balance_deadline";
    private static final int TIMER_UPDATE_MESSAGE = 0;
    private static final long TIMER_UPDATE_INTERVAL_MS = DateUtils.SECOND_IN_MILLIS;

    private TextView mTimerView;
    private Button mStartView;
    private Button mResetView;

    private Handler mTimerHandler;
    private long mTimeBalanceDeadline;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mTimerView = (TextView) findViewById(R.id.timer);
        mStartView = (Button) findViewById(R.id.start);
        mStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimeBalanceDeadline(System.currentTimeMillis() + DateUtils.HOUR_IN_MILLIS); //FIXME
                displayRemainingTime();
                if (mTimerHandler == null) setupTimerHandler();
                sendUpdateTimerMessage();
                mStartView.setClickable(false);
                mResetView.setClickable(true);
            }
        });
        mResetView = (Button) findViewById(R.id.reset);
        mResetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimeBalanceDeadline(0L);
                mTimerHandler.removeMessages(TIMER_UPDATE_MESSAGE);
                displayRemainingTime();
                mStartView.setClickable(true);
                mResetView.setClickable(false);
            }
        });

        mTimeBalanceDeadline = mSharedPreferences.getLong(TIME_BALANCE_DEADLINE, 0L);
        if (mTimeBalanceDeadline != 0) {
            setupTimerHandler();
            mStartView.setClickable(false);
            mResetView.setClickable(true);
        } else {
            mStartView.setClickable(true);
            mResetView.setClickable(false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        displayRemainingTime();
        if (mTimerHandler != null) {
            sendUpdateTimerMessage();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTimerHandler != null) {
            mTimerHandler.removeMessages(TIMER_UPDATE_MESSAGE);
        }
    }

    private void setupTimerHandler() {
        mTimerHandler = new Handler(new Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == TIMER_UPDATE_MESSAGE) {
                    displayRemainingTime();
                    Message updateTimerMessage = Message.obtain(message);
                    mTimerHandler.sendMessageDelayed(updateTimerMessage, TIMER_UPDATE_INTERVAL_MS);
                }
                return false;
            }
        });
    }

    private void sendUpdateTimerMessage() {
        Message updateTimerMessage = Message.obtain(mTimerHandler, TIMER_UPDATE_MESSAGE);
        mTimerHandler.sendMessageDelayed(updateTimerMessage, TIMER_UPDATE_INTERVAL_MS);
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

        if (hours <0 || minutes < 0) {
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

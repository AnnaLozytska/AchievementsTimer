package com.anna.lozytska.achievementstimer.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.TextView;

import com.anna.lozytska.achievementstimer.R;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView mTimerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimerView = (TextView) findViewById(R.id.timer);
        CountDownTimer countDownTimer = new CountDownTimer(DateUtils.MINUTE_IN_MILLIS, DateUtils.SECOND_IN_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                mTimerView.setText(getString(R.string.timer_format, hours, minutes, seconds));
            }

            @Override
            public void onFinish() {
                mTimerView.setText("Done!");
            }
        };
        countDownTimer.start();
    }
}

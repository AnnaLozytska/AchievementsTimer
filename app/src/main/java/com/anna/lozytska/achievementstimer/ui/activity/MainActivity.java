package com.anna.lozytska.achievementstimer.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;

import com.anna.lozytska.achievementstimer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    private Toolbar mToolbar;
    @BindView(R.id.start)
    private Button mStartView;
    @BindView(R.id.reset)
    private Button mResetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimeBalanceDeadline(System.currentTimeMillis() + DateUtils.HOUR_IN_MILLIS + DateUtils.MINUTE_IN_MILLIS); //TODO: update
                displayRemainingTime();
                if (mTimerUpdateHandler == null) {
                    setupTimerHandler();
                }
                sendDelayedTimerUpdate();
                mStartView.setClickable(false);
                mResetView.setClickable(true);
            }
        });

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
    }
}

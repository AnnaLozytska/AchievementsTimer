package com.anna.lozytska.achievementstimer.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.anna.lozytska.achievementstimer.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by alozytska on 18.07.16.
 */
public class TimerView extends TextView {
    private static final long SOON_TIME_REMAINING_MS = DateUtils.DAY_IN_MILLIS;
    private static final long WARNING_TIME_REMAINING_MS = - DateUtils.DAY_IN_MILLIS;

    public TimerView(Context context) {
        this(context, null);
    }

    public TimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLargeTextStyle(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLargeTextStyle(context);
    }

    private void setLargeTextStyle(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            setTextAppearance(context, R.style.LargeTimerText);
        } else {
            setTextAppearance(R.style.LargeTimerText);
        }
    }

    private void setSmallTextStyle(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            setTextAppearance(context, R.style.SmallTimerText);
        } else {
            setTextAppearance(R.style.LargeTimerText);
        }
    }

    public void setText(long timeRemaining) {
        //create and set formatted string
        long timeToFormat = timeRemaining;
        long hours = TimeUnit.MILLISECONDS.toHours(timeToFormat);
        timeToFormat -= hours * DateUtils.HOUR_IN_MILLIS;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeToFormat);
        timeToFormat -= minutes * DateUtils.MINUTE_IN_MILLIS;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeToFormat);
        if (hours < 0 || minutes < 0) {
            seconds = Math.abs(seconds);
        }
        if (hours < 0) {
            minutes = Math.abs(minutes);
        }
        StringBuilder builder = new StringBuilder();
        if (hours != 0) {
            builder.append(getContext().getString(R.string.timer_hours, hours));
        }
        if (minutes != 0 || hours != 0) {
            builder.append(getContext().getString(R.string.timer_minutes, minutes));
        }
        builder.append(getContext().getString(R.string.timer_seconds, seconds));
        super.setText(builder.toString());

        // set proper text color depending of value or remaining time
        if (timeRemaining < WARNING_TIME_REMAINING_MS) {
            setTextColor(getResources().getColor(R.color.colorTimerAlarm));
        } else if (timeRemaining < 0) {
            setTextColor(getResources().getColor(R.color.colorTimerWarning));}
        else if (timeRemaining < SOON_TIME_REMAINING_MS) {
            setTextColor(getResources().getColor(R.color.colorTimerSoon));
        } else {
            setTextColor(getResources().getColor(R.color.colorTimerNormal));
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setTextColor(getResources().getColor(R.color.colorTimerNormal));
    }
}

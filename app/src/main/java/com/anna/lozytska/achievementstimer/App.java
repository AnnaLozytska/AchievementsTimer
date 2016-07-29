package com.anna.lozytska.achievementstimer;

import android.app.Application;

import com.anna.lozytska.achievementstimer.rxjava.CurrentAchievementTimerObservable;

/**
 * Created by alozytska on 28.07.16.
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();
    private static volatile App sInstance;
    private SettingsManager mSettingsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mSettingsManager = SettingsManager.getInstance(this);
    }

    public static App getsInstance() {
        return sInstance;
    }

    public SettingsManager getSettingsManager() {
        return mSettingsManager;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        CurrentAchievementTimerObservable.getInstance(this).unsubscribeAll();
    }
}

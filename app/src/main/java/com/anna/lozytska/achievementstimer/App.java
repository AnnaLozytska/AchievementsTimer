package com.anna.lozytska.achievementstimer;

import android.app.Application;

/**
 * Created by alozytska on 28.07.16.
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();
    private static volatile App sInstance;
    private SettingsManager mSettingsManager;

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mSettingsManager = SettingsManager.getInstance(this);
    }

    public SettingsManager getSettingsManager() {
        return mSettingsManager;
    }
}

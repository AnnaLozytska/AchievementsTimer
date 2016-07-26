package com.anna.lozytska.achievementstimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

/**
 * Created by alozytska on 26.07.16.
 */
public class SettingsManager {

    private static volatile SettingsManager sInstance;

    public static SettingsManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SettingsManager.class) {
                if (sInstance == null) {
                    sInstance = new SettingsManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private final Context mContext;
    private final Resources mResources;
    private volatile SharedPreferences mPreferences;

    private SettingsManager(Context appContext) {
        mContext = appContext;
        mResources = appContext.getResources();
    }

    private SharedPreferences getPreferences() {
        if (mPreferences == null) {
            synchronized (this) {
                if (mPreferences == null) {
                    mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                }
            }
        }
        return mPreferences;
    }

    private String getKey(@StringRes int resId) {
        return mResources.getString(resId);
    }

}

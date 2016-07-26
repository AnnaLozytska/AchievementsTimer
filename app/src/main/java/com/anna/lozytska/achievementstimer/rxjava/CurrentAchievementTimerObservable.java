package com.anna.lozytska.achievementstimer.rxjava;

import android.content.Context;

import com.anna.lozytska.achievementstimer.SettingsManager;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by alozytska on 26.07.16.
 */
public class CurrentAchievementTimerObservable {
    private static volatile CurrentAchievementTimerObservable sInstance;

    private Observable<Long> mCurrentTimerObservable;

    public static CurrentAchievementTimerObservable getInstance(Context appContext) {
        if (sInstance == null) {
            synchronized (CurrentAchievementTimerObservable.class) {
                if (sInstance == null) {
                    sInstance = new CurrentAchievementTimerObservable(Context appContext);

                }
            }
        }
        return sInstance;
    }

    private final Context mContext;
    private long mCurrentTimer;

    private CurrentAchievementTimerObservable() {
        mCurrentTimerObservable = Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {

            }
        })
    }
}

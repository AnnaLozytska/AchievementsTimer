package com.anna.lozytska.achievementstimer.rxjava;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by alozytska on 26.07.16.
 */
public class CurrentAchievementTimerObservable {
    private static volatile CurrentAchievementTimerObservable sInstance;

    private Observable<Long> mCurrentTimerObservable;

    public static CurrentAchievementTimerObservable getInstance() {
        if (sInstance == null) {
            synchronized (CurrentAchievementTimerObservable.class) {
                if (sInstance == null) {
                    sInstance = new CurrentAchievementTimerObservable();

                }
            }
        }
        return sInstance;
    }

    private long mCurrentTimer;

    private CurrentAchievementTimerObservable() {
        mCurrentTimerObservable = Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {

            }
        });
    }
}

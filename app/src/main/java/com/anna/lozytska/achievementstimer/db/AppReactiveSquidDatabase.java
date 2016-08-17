package com.anna.lozytska.achievementstimer.db;

import com.anna.lozytska.achievementstimer.App;
import com.anna.lozytska.achievementstimer.db.modelspec.TaskRow;
import com.yahoo.squidb.android.AndroidOpenHelper;
import com.yahoo.squidb.data.ISQLiteDatabase;
import com.yahoo.squidb.data.ISQLiteOpenHelper;
import com.yahoo.squidb.reactive.ReactiveSquidDatabase;
import com.yahoo.squidb.sql.Table;

/**
 * Created by alozytska on 01.08.16.
 */
class AppReactiveSquidDatabase extends ReactiveSquidDatabase {
    private static volatile AppReactiveSquidDatabase sInstance;
    private static final String DB_NAME = "achievements_timer.db";
    private static final int VERSION = 1;

    public static AppReactiveSquidDatabase getInstance() {
        synchronized (AppReactiveSquidDatabase.class) {
            if (sInstance == null) {
                sInstance = new AppReactiveSquidDatabase();
            }
        }
        return sInstance;
    }

    @Override
    public String getName() {
        return DB_NAME;
    }

    @Override
    protected int getVersion() {
        return VERSION;
    }

    @Override
    protected Table[] getTables() {
        return new Table[] {
                TaskRow.TABLE
        };
    }

    @Override
    protected boolean onUpgrade(ISQLiteDatabase db, int oldVersion, int newVersion) {
        return false;
    }

    @Override
    protected ISQLiteOpenHelper createOpenHelper(String databaseName, OpenHelperDelegate delegate, int version) {
        return new AndroidOpenHelper(App.getInstance(), databaseName, delegate, version);
    }
}

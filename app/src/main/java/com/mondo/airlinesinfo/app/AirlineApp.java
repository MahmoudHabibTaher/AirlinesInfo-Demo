package com.mondo.airlinesinfo.app;

import android.app.Application;

import com.mondo.airlinesinfo.realm.RealmHelper;

/**
 * Created by mahmoud on 11/9/16.
 */

public class AirlineApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
    }

    private void initRealm() {
        RealmHelper.initRealm(getApplicationContext());
    }
}

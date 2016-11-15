package com.mondo.airlinesinfo.utils.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */

public class SchedulersProvider implements BaseSchedulersProvider {

    private static SchedulersProvider INSTANCE = null;

    public static SchedulersProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SchedulersProvider();
        }

        return INSTANCE;
    }

    private SchedulersProvider() {

    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}

package com.mondo.airlinesinfo.utils.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by mahmoud on 11/12/16.
 */

public class TestSchedulersProvider implements BaseSchedulersProvider {
    private static TestSchedulersProvider INSTANCE = null;

    public static TestSchedulersProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestSchedulersProvider();
        }

        return INSTANCE;
    }

    private TestSchedulersProvider() {

    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.immediate();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.immediate();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return Schedulers.immediate();
    }
}

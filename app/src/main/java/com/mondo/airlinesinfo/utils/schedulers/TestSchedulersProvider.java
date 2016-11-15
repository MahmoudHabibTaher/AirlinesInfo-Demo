package com.mondo.airlinesinfo.utils.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Use this class to provide schedulers for testing which always return Scheduler.immediate() to
 * run the subscribe code on the same thread.
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

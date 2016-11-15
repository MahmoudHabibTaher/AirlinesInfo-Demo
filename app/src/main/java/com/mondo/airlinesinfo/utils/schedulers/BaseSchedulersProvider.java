package com.mondo.airlinesinfo.utils.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Implement this interface to provide Schedulers to be used with Observable.subscribeOn and
 * Observable.observeOn methods.
 */
public interface BaseSchedulersProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}

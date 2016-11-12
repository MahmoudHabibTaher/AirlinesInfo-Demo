package com.mondo.airlinesinfo.utils.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Created by mahmoud on 11/12/16.
 */

public interface BaseSchedulersProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}

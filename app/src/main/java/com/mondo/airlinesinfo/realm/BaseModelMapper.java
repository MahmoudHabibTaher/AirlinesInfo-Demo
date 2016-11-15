package com.mondo.airlinesinfo.realm;

import android.support.annotation.NonNull;

import io.realm.RealmObject;

/**
 * Created by mahmoud on 11/14/16.
 */

public interface BaseModelMapper<From extends RealmObject, To> {
    @NonNull
    To map(@NonNull From from);
}

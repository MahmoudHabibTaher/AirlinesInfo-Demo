package com.mondo.airlinesinfo.realm;

import android.support.annotation.NonNull;

import io.realm.RealmObject;

/**
 * Utility class to provide convenient method to convert RealmObjects to Non RealmObjects.
 */

public interface BaseModelMapper<From extends RealmObject, To> {
    @NonNull
    To map(@NonNull From from);
}

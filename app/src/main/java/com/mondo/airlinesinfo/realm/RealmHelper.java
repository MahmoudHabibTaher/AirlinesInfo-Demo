package com.mondo.airlinesinfo.realm;

import android.content.Context;

import io.realm.BuildConfig;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 *
 */

public final class RealmHelper {
    private static final String FILE_NAME = "Airlines.realm";
    private static final int SCHEMA_VERSION = 0;

    public static void initRealm(Context context) {
        Realm.init(context);

        initDefaultConfiguration();
    }

    private static void initDefaultConfiguration() {
        RealmConfiguration.Builder configBuilder = new RealmConfiguration.Builder().name(FILE_NAME)
                .schemaVersion(SCHEMA_VERSION).migration(new Migration());

        if (BuildConfig.DEBUG) {
            configBuilder.deleteRealmIfMigrationNeeded();
        }

        Realm.setDefaultConfiguration(configBuilder.build());
    }
}

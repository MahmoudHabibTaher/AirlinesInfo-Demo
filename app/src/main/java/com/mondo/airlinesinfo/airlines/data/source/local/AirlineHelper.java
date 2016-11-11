package com.mondo.airlinesinfo.airlines.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by mahmoud on 11/9/16.
 */

public final class AirlineHelper {
    public static RealmResults<AirlineRealm> findAll(@NonNull Realm realm) {
        return realm.where(AirlineRealm.class).findAllSorted("name", Sort.ASCENDING);
    }

    public static AirlineRealm findByCode(@NonNull String code, @NonNull Realm realm) {
        return realm.where(AirlineRealm.class).equalTo("code", code).findFirst();
    }

    public static void save(@NonNull String code, @NonNull String name, @Nullable String logo,
                            @Nullable String phone, @Nullable String website,
                            Realm realm) {
        AirlineRealm airlineRealm = findByCode(code, realm);
        if (airlineRealm == null) {
            airlineRealm = realm.createObject(AirlineRealm.class, code);
        }

        airlineRealm.setName(name);
        airlineRealm.setLogo(logo);
        airlineRealm.setPhone(phone);
        airlineRealm.setWebsite(website);
    }

    public static void update(@NonNull String code, @NonNull String name, @Nullable String logo,
                              @Nullable String phone, @Nullable String website, boolean isFavorite,
                              Realm realm) {
        AirlineRealm airlineRealm = findByCode(code, realm);
        if (airlineRealm != null) {
            airlineRealm.setName(name);
            airlineRealm.setLogo(logo);
            airlineRealm.setPhone(phone);
            airlineRealm.setWebsite(website);
            airlineRealm.setFavorite(isFavorite);
        }
    }

    public static void deleteAll(@NonNull Realm realm) {
        findAll(realm).deleteAllFromRealm();
    }
}

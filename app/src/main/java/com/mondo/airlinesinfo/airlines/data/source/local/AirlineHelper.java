package com.mondo.airlinesinfo.airlines.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * This class provides static methods to perform CRUD operations on AirlineRealm table.
 */

public final class AirlineHelper {
    public static List<AirlineRealm> findAll(@NonNull Realm realm) {
        return realm.where(AirlineRealm.class).findAllSorted("name", Sort.ASCENDING);
    }

    public static AirlineRealm findByCode(@NonNull String code, @NonNull Realm realm) {
        return realm.where(AirlineRealm.class).equalTo("code", code).findFirst();
    }

    public static AirlineRealm save(@NonNull String code, @NonNull String name,
                                    @Nullable String logo,
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
        return airlineRealm;
    }

    public static AirlineRealm update(@NonNull String code, @NonNull String name,
                                      @Nullable String logo,
                                      @Nullable String phone, @Nullable String website,
                                      boolean isFavorite,
                                      Realm realm) {
        AirlineRealm airlineRealm = findByCode(code, realm);
        if (airlineRealm != null) {
            airlineRealm.setName(name);
            airlineRealm.setLogo(logo);
            airlineRealm.setPhone(phone);
            airlineRealm.setWebsite(website);
            airlineRealm.setFavorite(isFavorite);
        }

        return airlineRealm;
    }
}

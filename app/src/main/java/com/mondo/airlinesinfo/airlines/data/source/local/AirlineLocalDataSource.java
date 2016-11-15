package com.mondo.airlinesinfo.airlines.data.source.local;

import android.support.annotation.NonNull;

import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.airlines.data.source.AirlineDataSource;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Observable;

/**
 * AirlineDataSource implementation to store and get airlines from local storage.
 */

public class AirlineLocalDataSource implements AirlineDataSource {
    private static final String TAG = AirlineLocalDataSource.class.getSimpleName();

    private static AirlineLocalDataSource INSTANCE = null;

    public static AirlineLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AirlineLocalDataSource();
        }

        return INSTANCE;
    }

    private AirlineLocalDataSource() {

    }

    @Override
    public Observable<List<Airline>> getAirlines() {
        return Observable.create(subscriber -> {
            List<Airline> airlines = new ArrayList<>();

            Realm realm = Realm.getDefaultInstance();

            List<AirlineRealm> results = AirlineHelper.findAll(realm);

            AirlineModelMapper mapper = new AirlineModelMapper();

            for (AirlineRealm airlineRealm : results) {
                airlines.add(mapper.map(airlineRealm));
            }

            realm.close();

            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(airlines);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Airline> getAirline(@NonNull String code) {
        return Observable.create(subscriber -> {
            Airline airline = null;

            Realm realm = Realm.getDefaultInstance();

            AirlineModelMapper mapper = new AirlineModelMapper();

            AirlineRealm airlineRealm = AirlineHelper.findByCode(code, realm);
            if (airlineRealm != null) {
                airline = mapper.map(airlineRealm);
            }

            realm.close();

            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(airline);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void addToFavorites(@NonNull Airline airline) {
        Observable.create(subscriber -> {
            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();

            AirlineHelper.update(airline.getCode(), airline.getName(), airline.getLogo(), airline
                    .getPhone(), airline.getWebsite(), true, realm);

            realm.commitTransaction();

            realm.close();

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }).subscribe();
    }

    @Override
    public void removeFromFavorites(@NonNull Airline airline) {
        Observable.create(subscriber -> {
            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();

            airline.setFavorite(false);
            AirlineHelper.update(airline.getCode(), airline.getName(), airline.getLogo(), airline
                    .getPhone(), airline.getWebsite(), false, realm);

            realm.commitTransaction();

            realm.close();

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }).subscribe();
    }

    @Override
    public void refreshAirlines() {
        // Not implemented handled by AirlineRepository
        throw new UnsupportedOperationException();
    }

    public void saveAirline(Airline airline) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        AirlineHelper.save(airline.getCode(), airline.getName(), airline.getLogo(), airline
                .getPhone(), airline.getWebsite(), realm);

        realm.commitTransaction();

        realm.close();
    }
}

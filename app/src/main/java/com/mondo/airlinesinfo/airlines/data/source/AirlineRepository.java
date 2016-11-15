package com.mondo.airlinesinfo.airlines.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.airlines.data.AirlineNameComparator;
import com.mondo.airlinesinfo.airlines.data.source.local.AirlineLocalDataSource;
import com.mondo.airlinesinfo.airlines.data.source.remote.AirlineRemoteDataSource;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import rx.Observable;

/**
 * AirlineDataSource implementation to get and save airlines from local and remote repositories.
 */

public class AirlineRepository implements AirlineDataSource {
    private static final String TAG = AirlineRepository.class.getSimpleName();

    private static AirlineRepository INSTANCE = null;

    public static AirlineRepository getInstance(@NonNull AirlineLocalDataSource localDataSource,
                                                @NonNull AirlineRemoteDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AirlineRepository(localDataSource, remoteDataSource);
        }

        return INSTANCE;
    }

    private AirlineLocalDataSource mLocalDataSource;
    private AirlineRemoteDataSource mRemoteDataSource;

    @VisibleForTesting
    boolean mCacheIsDirty;

    private AirlineRepository(@NonNull AirlineLocalDataSource localDataSource, @NonNull
            AirlineRemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<List<Airline>> getAirlines() {
        Observable<List<Airline>> remoteAirlines = getRemoteAirlines();

        if (mCacheIsDirty) {
            return remoteAirlines;
        }

        Observable<List<Airline>> localAirlines = getLocalAirlines();

        return Observable.concat(localAirlines, remoteAirlines).filter(airlines -> !airlines
                .isEmpty()).first();
    }

    private Observable<List<Airline>> getRemoteAirlines() {
        return mRemoteDataSource.getAirlines().doOnNext(
                airlines -> Collections.sort(airlines, new AirlineNameComparator())).flatMap
                (airlines -> Observable.from
                        (airlines)
                        .doOnNext(this::saveAirline).toList()).doOnCompleted(
                () -> mCacheIsDirty = false);
    }

    private Observable<List<Airline>> getLocalAirlines() {
        return mLocalDataSource.getAirlines();
    }

    private void saveAirline(Airline airline) {
        mLocalDataSource.saveAirline(airline);
    }

    @Override
    public Observable<Airline> getAirline(@NonNull String code) {
        return getLocalAirline(code);
    }

    private Observable<Airline> getLocalAirline(@NonNull String code) {
        return mLocalDataSource.getAirline(code).flatMap(airline -> {
            if (airline == null) {
                throw new NoSuchElementException(String.format("No airline with code %s was " +
                        "found", code));
            } else {
                return Observable.just(airline);
            }
        });
    }

    @Override
    public void addToFavorites(@NonNull Airline airline) {
        mLocalDataSource.addToFavorites(airline);
    }

    @Override
    public void removeFromFavorites(@NonNull Airline airline) {
        mLocalDataSource.removeFromFavorites(airline);
    }

    @Override
    public void refreshAirlines() {
        mCacheIsDirty = true;
    }

    public void dispose() {
        INSTANCE = null;
    }
}

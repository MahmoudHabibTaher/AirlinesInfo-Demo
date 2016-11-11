package com.mondo.airlinesinfo.airlines.data.source;

import android.support.annotation.NonNull;

import com.mondo.airlinesinfo.airlines.data.Airline;

import java.util.List;

import rx.Observable;

/**
 * Created by mahmoud on 11/8/16.
 */

public interface AirlineDataSource {
    Observable<List<Airline>> getAirlines();

    Observable<Airline> getAirline(@NonNull String code);

    void addToFavorites(@NonNull Airline airline);

    void removeFromFavorites(@NonNull Airline airline);

    void refreshAirlines();
}

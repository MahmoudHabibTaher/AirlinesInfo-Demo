package com.mondo.airlinesinfo.airlines.data.source;

import android.support.annotation.NonNull;

import com.mondo.airlinesinfo.airlines.data.Airline;

import java.util.List;

import rx.Observable;

public interface AirlineDataSource {
    /**
     * Returns an Observable which emits a List<Airline> items to subscribers.
     * @return Observable<List<Airline>>
     */
    Observable<List<Airline>> getAirlines();

    /**
     * Returns an Observable with a single emit item Airline or null if not found.
     * @param code the Airline Code.
     * @return Observable<Airline>
     */
    Observable<Airline> getAirline(@NonNull String code);

    /**
     * Add the airline to the favorites list.
     * @param airline
     */
    void addToFavorites(@NonNull Airline airline);

    /**
     * Remove the airline from the favorites list.
     * @param airline
     */
    void removeFromFavorites(@NonNull Airline airline);

    /**
     * Refresh the data source e.g Invalidate the cache.
     */
    void refreshAirlines();
}

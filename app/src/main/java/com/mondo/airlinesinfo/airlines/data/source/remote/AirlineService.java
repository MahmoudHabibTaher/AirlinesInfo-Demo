package com.mondo.airlinesinfo.airlines.data.source.remote;

import com.mondo.airlinesinfo.airlines.data.Airline;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by mahmoud on 11/9/16.
 */

public interface AirlineService {
    @GET("directory/airlines")
    Observable<List<Airline>> getAirlines();
}

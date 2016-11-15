package com.mondo.airlinesinfo.airlines.data.source.local;

import android.support.annotation.NonNull;

import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.realm.BaseModelMapper;

/**
 * Implementation of BaseModelMapper to convert AirlineRealm to Airline.
 */

public class AirlineModelMapper implements BaseModelMapper<AirlineRealm, Airline> {
    @Override
    @NonNull
    public Airline map(@NonNull AirlineRealm airlineRealm) {
        Airline airline = new Airline();
        airline.setCode(airlineRealm.getCode());
        airline.setName(airlineRealm.getName());
        airline.setLogo(airlineRealm.getLogo());
        airline.setPhone(airlineRealm.getPhone());
        airline.setWebsite(airlineRealm.getWebsite());
        airline.setFavorite(airlineRealm.isFavorite());
        return airline;
    }
}

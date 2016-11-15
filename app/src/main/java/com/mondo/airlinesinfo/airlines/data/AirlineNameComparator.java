package com.mondo.airlinesinfo.airlines.data;

import java.util.Comparator;

/**
 *
 */

public class AirlineNameComparator implements Comparator<Airline> {
    @Override
    public int compare(Airline airline1, Airline airline2) {
        return airline1.getName().compareTo(airline2.getName());
    }
}

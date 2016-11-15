package com.mondo.airlinesinfo.airlines.data.source.local;

import com.mondo.airlinesinfo.airlines.data.Airline;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 *
 */

public class AirlineModelMapperTest {
    @Test
    public void testMap() {
        AirlineRealm airlineRealm = new AirlineRealm();
        airlineRealm.setCode("Code1");
        airlineRealm.setName("Airline1");
        airlineRealm.setLogo("DummyLogo");
        airlineRealm.setWebsite("DummyWebsite");
        airlineRealm.setPhone("DummyPhone");
        airlineRealm.setFavorite(false);

        AirlineModelMapper mapper = new AirlineModelMapper();

        Airline airline = mapper.map(airlineRealm);

        assertEquals(airline.getCode(), airlineRealm.getCode());
        assertEquals(airline.getName(), airlineRealm.getName());
        assertEquals(airline.getLogo(), airlineRealm.getLogo());
        assertEquals(airline.getWebsite(), airlineRealm.getWebsite());
        assertEquals(airline.getPhone(), airlineRealm.getPhone());
        assertEquals(airline.isFavorite(), airlineRealm.isFavorite());
    }
}

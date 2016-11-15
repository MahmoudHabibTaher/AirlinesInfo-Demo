package com.mondo.airlinesinfo.airlines.data.source.local;

import com.mondo.airlinesinfo.airlines.data.Airline;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mahmoud on 11/12/16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Realm.class, AirlineHelper.class})
@SuppressStaticInitializationFor("io.realm.internal.Util")
public class AirlineLocalDataSourceTest {

    private static final String AIRLINE_CODE_1 = "Code1";
    private static final String AIRLINE_CODE_2 = "Code2";
    private static final String AIRLINE_CODE_3 = "Code3";

    private static final AirlineRealm AIRLINE1_REALM;
    private static final AirlineRealm AIRLINE2_REALM;
    private static final AirlineRealm AIRLINE3_REALM;

    private static final List<AirlineRealm> AIRLINES_REALM;

    static {
        AIRLINE1_REALM = new AirlineRealm();
        AIRLINE1_REALM.setCode(AIRLINE_CODE_1);

        AIRLINE2_REALM = new AirlineRealm();
        AIRLINE2_REALM.setCode(AIRLINE_CODE_2);

        AIRLINE3_REALM = new AirlineRealm();
        AIRLINE3_REALM.setCode(AIRLINE_CODE_3);

        AIRLINES_REALM = new ArrayList<>();
        AIRLINES_REALM.add(AIRLINE1_REALM);
        AIRLINES_REALM.add(AIRLINE2_REALM);
        AIRLINES_REALM.add(AIRLINE3_REALM);
    }

    private Realm mRealm;

    private AirlineLocalDataSource mLocalDataSource;

    @Before
    public void setUp() {
        mRealm = PowerMockito.mock(Realm.class);
        PowerMockito.mockStatic(Realm.class);
        PowerMockito.mockStatic(AirlineHelper.class);

        when(Realm.getDefaultInstance()).thenReturn(mRealm);

        mLocalDataSource = AirlineLocalDataSource.getInstance();
    }

    @Test
    public void testGetAirlines() {
        when(AirlineHelper.findAll(mRealm)).thenReturn(AIRLINES_REALM);

        TestSubscriber<List<Airline>> subscriber = new TestSubscriber<>();

        mLocalDataSource.getAirlines().subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .subscribe(subscriber);

        PowerMockito.verifyStatic();

        Realm.getDefaultInstance();

        AirlineHelper.findAll(mRealm);

        verify(mRealm, times(1)).close();

        assertEquals(subscriber.getOnNextEvents().size(), 1);

        subscriber.assertCompleted();
    }

    @Test
    public void testGetAirlinesNoAirlinesAvailable() {
        when(AirlineHelper.findAll(mRealm)).thenReturn(Collections.emptyList());

        TestSubscriber<List<Airline>> subscriber = new TestSubscriber<>();

        mLocalDataSource.getAirlines().subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .subscribe(subscriber);

        PowerMockito.verifyStatic();

        AirlineHelper.findAll(mRealm);

        verify(mRealm).close();

        assertEquals(subscriber.getOnNextEvents().size(), 1);
        subscriber.assertValue(Collections.emptyList());
        subscriber.assertCompleted();
    }

    @Test
    public void testGetAirline() {
        when(AirlineHelper.findByCode(AIRLINE_CODE_1, mRealm)).thenReturn(AIRLINE1_REALM);

        TestSubscriber<Airline> subscriber = new TestSubscriber<>();

        mLocalDataSource.getAirline(AIRLINE_CODE_1).subscribe(subscriber);

        PowerMockito.verifyStatic();

        Realm.getDefaultInstance();

        AirlineHelper.findByCode(AIRLINE_CODE_1, mRealm);

        List<Airline> onNextEvents = subscriber.getOnNextEvents();

        assertEquals(onNextEvents.size(), 1);
        assertEquals(onNextEvents.get(0).getCode(), AIRLINE_CODE_1);
        subscriber.assertCompleted();
        verify(mRealm, times(1)).close();
    }

    @Test
    public void testGetAirlineNotAvailable() {
        when(AirlineHelper.findByCode(AIRLINE_CODE_1, mRealm)).thenReturn(null);

        TestSubscriber<Airline> subscriber = new TestSubscriber<>();

        mLocalDataSource.getAirline(AIRLINE_CODE_1).subscribe(subscriber);

        PowerMockito.verifyStatic();

        Realm.getDefaultInstance();

        AirlineHelper.findByCode(AIRLINE_CODE_1, mRealm);

        List<Airline> onNextEvents = subscriber.getOnNextEvents();

        assertEquals(onNextEvents.size(), 1);
        assertNull(onNextEvents.get(0));
        subscriber.assertCompleted();
        verify(mRealm, times(1)).close();
    }

    @Test
    public void testSaveAirline() {
        Airline airline = new Airline();
        airline.setCode(AIRLINE_CODE_1);
        airline.setName("Airline1");
        airline.setLogo("DummyLogo");
        airline.setPhone("DummyPhone");
        airline.setWebsite("DummyWebsite");

        mLocalDataSource.saveAirline(airline);

        PowerMockito.verifyStatic();

        Realm.getDefaultInstance();

        AirlineHelper.save(airline.getCode(), airline.getName(), airline.getLogo(), airline.getPhone
                (), airline.getWebsite(), mRealm);

        verify(mRealm, times(1)).beginTransaction();
        verify(mRealm, times(1)).commitTransaction();
        verify(mRealm, times(1)).close();
    }

    @Test
    public void testAddToFavorites() {
        Airline airline = new Airline();
        airline.setCode(AIRLINE_CODE_1);
        airline.setName("Airline1");
        airline.setLogo("DummyLogo");
        airline.setPhone("DummyPhone");
        airline.setWebsite("DummyWebsite");
        airline.setFavorite(false);

        mLocalDataSource.addToFavorites(airline);

        PowerMockito.verifyStatic();

        Realm.getDefaultInstance();

        AirlineHelper.update(airline.getCode(), airline.getName(), airline.getLogo(), airline
                .getPhone(), airline.getWebsite(), true, mRealm);

        verify(mRealm, times(1)).beginTransaction();
        verify(mRealm, times(1)).commitTransaction();
        verify(mRealm, times(1)).close();
    }

    @Test
    public void testRemoveFromFavorites() {
        Airline airline = new Airline();
        airline.setCode(AIRLINE_CODE_1);
        airline.setName("Airline1");
        airline.setLogo("DummyLogo");
        airline.setPhone("DummyPhone");
        airline.setWebsite("DummyWebsite");
        airline.setFavorite(true);

        mLocalDataSource.addToFavorites(airline);

        PowerMockito.verifyStatic();

        Realm.getDefaultInstance();

        AirlineHelper.update(airline.getCode(), airline.getName(), airline.getLogo(), airline
                .getPhone(), airline.getWebsite(), false, mRealm);

        verify(mRealm, times(1)).beginTransaction();
        verify(mRealm, times(1)).commitTransaction();
        verify(mRealm, times(1)).close();
    }
}

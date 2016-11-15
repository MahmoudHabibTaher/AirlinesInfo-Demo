package com.mondo.airlinesinfo.airlines.data.source;

import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.airlines.data.source.local.AirlineLocalDataSource;
import com.mondo.airlinesinfo.airlines.data.source.remote.AirlineRemoteDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 */

public class AirlineRepositoryTest {

    private static final List<Airline> AIRLINES = Arrays.asList(
            new Airline("Code1", "", "", "", "", false), new Airline("Code2", "", "", "", "",
                    false), new Airline("Code3", "", "", "", "", false));

    @Mock
    private AirlineLocalDataSource mLocalDataSource;

    @Mock
    private AirlineRemoteDataSource mRemoteDataSource;

    private AirlineRepository mAirlineRepository;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        mAirlineRepository = AirlineRepository.getInstance(mLocalDataSource, mRemoteDataSource);
    }

    @After
    public void after() {
        mAirlineRepository.dispose();
    }

    @Test
    public void testGetAirlinesFromRemoteRepositoryWhenLocalIsEmpty() {
        setAirlinesNotAvailable(mLocalDataSource);
        setAirlinesAvailable(mRemoteDataSource, AIRLINES);

        TestSubscriber<List<Airline>> subscriber = new TestSubscriber<>();

        mAirlineRepository.getAirlines().subscribe(subscriber);

        verify(mRemoteDataSource).getAirlines();

        subscriber.assertValue(AIRLINES);
    }

    @Test
    public void testGetAirlinesFromLocalRepository() {
        setAirlinesAvailable(mLocalDataSource, AIRLINES);
        setAirlinesNotAvailable(mRemoteDataSource);

        TestSubscriber<List<Airline>> subscriber = new TestSubscriber<>();

        mAirlineRepository.getAirlines().subscribe(subscriber);

        verify(mLocalDataSource).getAirlines();

        subscriber.assertValue(AIRLINES);
    }

    @Test
    public void testGetAirlinesFromRemoteOnlyWhenCacheIsDirty() {
        setAirlinesAvailable(mRemoteDataSource, AIRLINES);
        setAirlinesNotAvailable(mLocalDataSource);

        TestSubscriber<List<Airline>> subscriber = new TestSubscriber<>();

        mAirlineRepository.refreshAirlines();

        mAirlineRepository.getAirlines().subscribe(subscriber);

        verify(mRemoteDataSource).getAirlines();
        verify(mLocalDataSource, never()).getAirlines();

        subscriber.assertValue(AIRLINES);
    }

    @Test
    public void testGetAirlinesSavedInLocalAfterGettingFromRemote() {
        setAirlinesNotAvailable(mLocalDataSource);
        setAirlinesAvailable(mRemoteDataSource, AIRLINES);

        TestSubscriber<List<Airline>> subscriber = new TestSubscriber<>();

        mAirlineRepository.getAirlines().subscribe(subscriber);

        verify(mRemoteDataSource).getAirlines();
        verify(mLocalDataSource, times(AIRLINES.size())).saveAirline(any(Airline.class));
        subscriber.assertValue(AIRLINES);
    }

    @Test
    public void testGetAirlinesErrorThrownWhenNoDataAvailable() {
        setAirlinesNotAvailable(mLocalDataSource);
        setAirlinesNotAvailable(mRemoteDataSource);

        TestSubscriber<List<Airline>> subscriber = new TestSubscriber<>();

        mAirlineRepository.getAirlines().subscribe(subscriber);

        verify(mRemoteDataSource).getAirlines();
        verify(mLocalDataSource).getAirlines();

        subscriber.assertError(NoSuchElementException.class);
    }

    @Test
    public void testGetAirlineFromLocalRepository() {
        String code = "Code1";
        Airline airline = new Airline();
        airline.setCode(code);

        setAirlineAvailable(mLocalDataSource, airline);

        TestSubscriber<Airline> subscriber = new TestSubscriber<>();
        mAirlineRepository.getAirline(code).subscribe(subscriber);

        verify(mLocalDataSource).getAirline(code);

        subscriber.assertValue(airline);
    }

    @Test
    public void testGetAirlineErrorThrownIfAirlineNotExist() {
        String code = "Code1";
        Airline airline = new Airline();
        airline.setCode(code);

        setAirlineNotAvailable(mLocalDataSource, airline);

        TestSubscriber<Airline> subscriber = new TestSubscriber<>();
        mAirlineRepository.getAirline(code).subscribe(subscriber);

        verify(mLocalDataSource).getAirline(code);

        subscriber.assertError(NoSuchElementException.class);
    }

    @Test
    public void testAddToFavorites() {
        String code = "Code1";
        Airline airline = new Airline();
        airline.setCode(code);

        setAirlineAvailable(mLocalDataSource, airline);

        mAirlineRepository.addToFavorites(airline);

        verify(mLocalDataSource).addToFavorites(airline);
    }

    @Test
    public void testRemoveFromFavorites() {
        String code = "Code1";
        Airline airline = new Airline();
        airline.setCode(code);

        setAirlineAvailable(mLocalDataSource, airline);

        mAirlineRepository.removeFromFavorites(airline);

        verify(mLocalDataSource).removeFromFavorites(airline);
    }

    private void setAirlinesAvailable(AirlineDataSource dataSource, List<Airline> airlines) {
        when(dataSource.getAirlines()).thenReturn(Observable.just(airlines).concatWith(Observable
                .<List<Airline>>never()));
    }

    private void setAirlinesNotAvailable(AirlineDataSource dataSource) {
        when(dataSource.getAirlines()).thenReturn(
                Observable.just(Collections.<Airline>emptyList()));
    }

    private void setAirlineAvailable(AirlineDataSource dataSource, Airline airline) {
        when(dataSource.getAirline(eq(airline.getCode()))).thenReturn(Observable.just(airline)
                .concatWith(Observable.never()));
    }

    private void setAirlineNotAvailable(AirlineDataSource dataSource, Airline airline) {
        when(dataSource.getAirline(eq(airline.getCode()))).thenReturn(Observable.<Airline>just(null)
                .concatWith(Observable.never()));
    }
}

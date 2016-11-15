package com.mondo.airlinesinfo.airlines.list;

import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.airlines.data.source.AirlineDataSource;
import com.mondo.airlinesinfo.airlines.data.source.AirlineRepository;
import com.mondo.airlinesinfo.utils.schedulers.TestSchedulersProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */

public class AirlinesListPresenterTest {

    private static final Airline AIRLINE1 = new Airline("Code1", "Airline1", "", "", "", false);
    private static final Airline AIRLINE2 = new Airline("Code2", "Airline2", "", "", "", true);
    private static final Airline AIRLINE3 = new Airline("Code3", "Airline3", "", "", "", true);

    private static final List<Airline> AIRLINES = Arrays.asList(AIRLINE1, AIRLINE2, AIRLINE3);

    private static final List<Airline> FAVORITE_AIRLINES = Arrays.asList(AIRLINE2, AIRLINE3);
    private static final List<Airline> QUERIED_AIRLINES = Arrays.asList(AIRLINE3);

    @Mock
    private AirlineRepository mAirlineRepository;

    @Mock
    private AirlinesListContract.View mView;

    private AirlinesListPresenter mPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new AirlinesListPresenter(mAirlineRepository, mView,
                TestSchedulersProvider.getInstance());
    }

    @Test
    public void testLoadAirlinesOnSubscribe() {
        setAirlinesAvailable(mAirlineRepository, AIRLINES);

        mPresenter.subscribe();

        verify(mAirlineRepository).getAirlines();
    }

    @Test
    public void testLoadAirlinesSetLoadingIndicator() {
        setAirlinesAvailable(mAirlineRepository, AIRLINES);

        mPresenter.loadAirlines(true);

        verify(mView).setLoadingIndicator(true);
    }

    @Test
    public void testLoadAirlinesRefreshRepositoryWhenForceUpdate() {
        setAirlinesAvailable(mAirlineRepository, AIRLINES);

        mPresenter.loadAirlines(true);

        verify(mAirlineRepository).refreshAirlines();

        verify(mAirlineRepository).getAirlines();
    }

    @Test
    public void testLoadAirlinesForceUpdateWhenFirstLoad() {
        setAirlinesAvailable(mAirlineRepository, AIRLINES);

        mPresenter = new AirlinesListPresenter(mAirlineRepository, mView, TestSchedulersProvider
                .getInstance());

        mPresenter.subscribe();

        verify(mAirlineRepository).refreshAirlines();

        verify(mAirlineRepository).getAirlines();

        assertFalse(mPresenter.mFirstLoad);
    }

    @Test
    public void testLoadAirlinesFromRepository() {
        setAirlinesAvailable(mAirlineRepository, AIRLINES);

        mPresenter.loadAirlines(true);

        verify(mAirlineRepository).getAirlines();
    }

    @Test
    public void testLoadAirlinesShowAirlinesOnSuccess() {
        setAirlinesAvailable(mAirlineRepository, AIRLINES);

        mPresenter.loadAirlines(true);

        verify(mAirlineRepository).getAirlines();

        verify(mView).showAirlines(AIRLINES);
    }

    @Test
    public void testLoadAirlinesShowNoAirlinesWhenNoAirlinesAvailable() {
        setAirlinesNotAvailable(mAirlineRepository);

        mPresenter.loadAirlines(true);

        verify(mAirlineRepository).getAirlines();

        verify(mView).showNoAirlinesAvailable();
    }

    @Test
    public void testLoadAirlinesShowErrorLoadingAirlinesWhenErrorThrown() {
        setAirlinesError(mAirlineRepository);

        mPresenter.loadAirlines(true);

        verify(mAirlineRepository).getAirlines();

        verify(mView).showLoadingAirlinesError();
    }

    @Test
    public void testChangeAirlineFavoriteStateToFavorite() {
        Airline airline = new Airline("Code1", "", "", "", "", false);
        mPresenter.changeAirlineFavoriteState(airline);

        verify(mAirlineRepository).addToFavorites(airline);
        verify(mView).showAirlineAddedToFavorites(airline);
    }

    @Test
    public void testChangeAirlineFavoriteStateToNotFavorite() {
        Airline airline = new Airline("Code1", "", "", "", "", true);
        mPresenter.changeAirlineFavoriteState(airline);

        verify(mAirlineRepository).removeFromFavorites(airline);
        verify(mView).showAirlineRemovedFromFavorites(airline);
    }

    @Test
    public void testOpenAirlineDetails() {
        Airline airline = new Airline("Code1", "Airline1", "", "", "", true);
        mPresenter.openAirlineDetails(airline);

        verify(mView).showAirlineDetailsUi(airline.getCode(), airline.getName());
    }

    @Test
    public void testLoadFavoriteAirlines() {
        setAirlinesAvailable(mAirlineRepository, AIRLINES);

        mPresenter.setFilter(null, Filter.FAVORITE);

        mPresenter.loadAirlines(false);

        verify(mAirlineRepository).getAirlines();

        verify(mView).showAirlines(FAVORITE_AIRLINES);
    }

    @Test
    public void testLoadAllAirlines() {
        setAirlinesAvailable(mAirlineRepository, AIRLINES);

        mPresenter.setFilter(null, Filter.ALL);
        mPresenter.loadAirlines(false);
        verify(mAirlineRepository).getAirlines();
        verify(mView).showAirlines(AIRLINES);
    }

    @Test
    public void testSearchAllAirlines() {
        setAirlinesAvailable(mAirlineRepository, AIRLINES);

        mPresenter.setFilter("airline3", Filter.ALL);

        mPresenter.loadAirlines(false);

        verify(mAirlineRepository).getAirlines();

        verify(mView).showAirlines(QUERIED_AIRLINES);
    }

    @Test
    public void testUnSubscribeCancelSubscriptions() {
        setAirlinesAvailable(mAirlineRepository, AIRLINES);

        mPresenter.subscribe();

        mPresenter.unSubscribe();

        assertFalse(mPresenter.mSubscriptions.hasSubscriptions());
    }

    private void setAirlinesAvailable(AirlineDataSource dataSource, List<Airline> airlines) {
        when(dataSource.getAirlines()).thenReturn(
                Observable.just(airlines).concatWith(Observable.never()));
    }

    private void setAirlinesNotAvailable(AirlineDataSource dataSource) {
        when(dataSource.getAirlines()).thenReturn(
                Observable.just(Collections.emptyList()));
    }

    private void setAirlinesError(AirlineDataSource dataSource) {
        when(dataSource.getAirlines()).thenReturn(Observable.error(new Throwable("Error " +
                "Occured")));
    }

//    private void setAirlineAvailable(AirlineDataSource dataSource, Airline airline) {
//        when(dataSource.getAirline(eq(airline.getCode()))).thenReturn(Observable.just(airline)
//                .concatWith(Observable.never()));
//    }
//
//    private void setAirlineNotAvailable(AirlineDataSource dataSource, Airline airline) {
//        when(dataSource.getAirline(eq(airline.getCode()))).thenReturn(Observable.<Airline>just
// (null)
//                .concatWith(Observable.never()));
//    }
}

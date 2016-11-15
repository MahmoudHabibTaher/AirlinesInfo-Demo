package com.mondo.airlinesinfo.airlines.details;

import android.support.annotation.NonNull;

import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.airlines.data.source.AirlineDataSource;
import com.mondo.airlinesinfo.airlines.data.source.AirlineRepository;
import com.mondo.airlinesinfo.utils.schedulers.TestSchedulersProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;

import rx.Observable;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */

public class AirlineDetailsPresenterTest {
    private static final String AIRLINE_CODE = "Code1";
    private static final Airline AIRLINE = new Airline(AIRLINE_CODE, "Airline1", "",
            "AIRLINE_WEBSITE",
            "AIRLINE_PHONE", false);

    @Mock
    private AirlineRepository mAirlineRepository;

    @Mock
    private AirlineDetailsContract.View mView;

    private AirlineDetailsPresenter mPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new AirlineDetailsPresenter(AIRLINE_CODE, mAirlineRepository, mView,
                TestSchedulersProvider.getInstance());
    }

    @Test
    public void testLoadAirlineOnSubscribe() {
        setAirlineAvailable(mAirlineRepository, AIRLINE);

        mPresenter.subscribe();

        verify(mAirlineRepository).getAirline(AIRLINE_CODE);
    }

    @Test
    public void testCancelAllSubscriptionsOnUnSubscribe() {
        setAirlineAvailable(mAirlineRepository, AIRLINE);

        mPresenter.subscribe();
        assertTrue(mPresenter.mSubscriptions.hasSubscriptions());
        mPresenter.unSubscribe();
        assertFalse(mPresenter.mSubscriptions.hasSubscriptions());
    }

    @Test
    public void testLoadAirline() {
        setAirlineAvailable(mAirlineRepository, AIRLINE);

        mPresenter.loadAirline(AIRLINE_CODE);

        verify(mAirlineRepository).getAirline(AIRLINE_CODE);

        verify(mView).showAirline(AIRLINE);
    }

    @Test
    public void testLoadAirlineAirlineNotAvailableShowAirlineNotFound() {
        setAirlineNotAvailable(mAirlineRepository, AIRLINE_CODE);

        mPresenter.loadAirline(AIRLINE_CODE);

        verify(mAirlineRepository).getAirline(AIRLINE_CODE);

        verify(mView).showAirlineNotFound();
    }

    @Test
    public void testLoadAirlineErrorThrownShowErrorLoadingAirline() {
        setAirlineError(mAirlineRepository);

        mPresenter.loadAirline(AIRLINE_CODE);

        verify(mAirlineRepository).getAirline(AIRLINE_CODE);

        verify(mView).showErrorLoadingAirline();
    }

    @Test
    public void testAddAirlineToFavorites() {
        Airline airline = new Airline(AIRLINE);
        airline.setFavorite(false);

        mPresenter.onAirlineFavoriteClick(airline);

        verify(mAirlineRepository).addToFavorites(airline);

        verify(mView).showAirlineAddedToFavorites(airline);
    }

    @Test
    public void testRemoveAirlineFromFavorites() {
        Airline airline = new Airline(AIRLINE);
        airline.setFavorite(true);

        mPresenter.onAirlineFavoriteClick(airline);

        verify(mAirlineRepository).removeFromFavorites(airline);

        verify(mView).showAirlineRemovedFromFavorites(airline);
    }

    @Test
    public void testOnAirlinePhoneClick() {
        mPresenter.onAirlinePhoneClick(AIRLINE);

        verify(mView).showCallAirlineUi(AIRLINE.getPhone());
    }

    @Test
    public void testOnAirlineWebsiteClick() {
        mPresenter.onAirlineWebsiteClick(AIRLINE);
        verify(mView).showAirlineWebsiteUi(AIRLINE.getWebsite());
    }

    private void setAirlineAvailable(@NonNull AirlineDataSource dataSource,
                                     @NonNull Airline airline) {
        when(dataSource.getAirline(eq(airline.getCode()))).thenReturn(Observable.just(airline)
                .concatWith(Observable.never()));
    }

    private void setAirlineNotAvailable(@NonNull AirlineDataSource dataSource,
                                        @NonNull String code) {
        when(dataSource.getAirline(eq(code))).thenReturn(Observable.error(new
                NoSuchElementException()));
    }

    private void setAirlineError(@NonNull AirlineDataSource dataSource) {
        when(dataSource.getAirline(anyString())).thenReturn(Observable.error(new Throwable()));
    }
}

package com.mondo.airlinesinfo.airlines.details;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.airlines.data.source.AirlineRepository;
import com.mondo.airlinesinfo.utils.schedulers.BaseSchedulersProvider;

import java.util.NoSuchElementException;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mahmoud on 11/10/16.
 */

public class AirlineDetailsPresenter implements AirlineDetailsContract.Presenter {

    private String mAirlineCode;
    private AirlineRepository mAirlineRepository;
    private AirlineDetailsContract.View mView;

    private BaseSchedulersProvider mSchedulersProvider;

    @VisibleForTesting
    CompositeSubscription mSubscriptions;

    public AirlineDetailsPresenter(String airlineCode, AirlineRepository repository,
                                   AirlineDetailsContract.View view,
                                   BaseSchedulersProvider schedulersProvider) {
        mAirlineCode = airlineCode;
        mAirlineRepository = repository;
        mView = view;
        mView.setPresenter(this);
        mSchedulersProvider = schedulersProvider;
    }

    @Override
    public void subscribe() {
        mSubscriptions = new CompositeSubscription();
        loadAirline(mAirlineCode);

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.unsubscribe();
    }

    @Override
    public void loadAirline(@NonNull String code) {
        mView.setLoadingIndicator(true);

        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

        mSubscriptions.clear();
        Subscription subscription = mAirlineRepository.getAirline(code).subscribeOn
                (mSchedulersProvider.io()).observeOn
                (mSchedulersProvider.ui()).subscribe(airline -> {
            mView.showAirline(airline);
        }, throwable -> {
            if (throwable instanceof NoSuchElementException) {
                mView.showAirlineNotFound();
            } else {
                mView.showErrorLoadingAirline();
            }
        }, () -> mView.setLoadingIndicator(false));
        mSubscriptions.add(subscription);
    }

    @Override
    public void onAirlineFavoriteClick(@NonNull Airline airline) {
        if (!airline.isFavorite()) {
            mAirlineRepository.addToFavorites(airline);
            airline.setFavorite(true);
            mView.showAirlineAddedToFavorites(airline);
        } else {
            mAirlineRepository.removeFromFavorites(airline);
            airline.setFavorite(false);
            mView.showAirlineRemovedFromFavorites(airline);
        }
    }

    @Override
    public void onAirlineWebsiteClick(@NonNull Airline airline) {
        mView.showAirlineWebsiteUi(airline.getWebsite());
    }

    @Override
    public void onAirlinePhoneClick(@NonNull Airline airline) {
        mView.showCallAirlineUi(airline.getPhone());
    }
}

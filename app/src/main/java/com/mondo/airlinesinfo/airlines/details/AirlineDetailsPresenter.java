package com.mondo.airlinesinfo.airlines.details;

import android.support.annotation.NonNull;

import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.airlines.data.source.AirlineRepository;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mahmoud on 11/10/16.
 */

public class AirlineDetailsPresenter implements AirlineDetailsContract.Presenter {

    private String mAirlineCode;
    private AirlineRepository mAirlineRepository;
    private AirlineDetailsContract.View mView;

    private CompositeSubscription mSubscriptions;

    public AirlineDetailsPresenter(String airlineCode, AirlineRepository repository,
                                   AirlineDetailsContract.View view) {
        mAirlineCode = airlineCode;
        mAirlineRepository = repository;
        mView = view;
        mView.setPresenter(this);
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
        mSubscriptions.clear();
        Subscription subscription = mAirlineRepository.getAirline(code).subscribeOn(Schedulers
                .newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(airline -> {
            mView.showAirline(airline);
        }, throwable -> {
        }, () -> {
            mView.setLoadingIndicator(false);
        });
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

package com.mondo.airlinesinfo.airlines.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.airlines.data.source.AirlineRepository;
import com.mondo.airlinesinfo.utils.LogUtils;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mahmoud on 11/9/16.
 */

public class AirlinesListPresenter implements AirlinesListContract.Presenter {
    private static final String TAG = AirlinesListPresenter.class.getSimpleName();

    private AirlineRepository mAirlineRepository;

    private AirlinesListContract.View mView;

    private CompositeSubscription mSubscriptions;

    private Filter mFilter = Filter.ALL;
    private String mQuery = null;

    private boolean mFirstLoad = true;

    public AirlinesListPresenter(@NonNull AirlineRepository repository,
                                 @NonNull AirlinesListContract.View view) {
        mAirlineRepository = repository;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mSubscriptions = new CompositeSubscription();
        loadAirlines(mFirstLoad);
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.unsubscribe();
    }

    @Override
    public void loadAirlines(boolean forceUpdate) {
        loadAirlines(forceUpdate || mFirstLoad, true);

        mFirstLoad = false;
    }

    private void loadAirlines(boolean forceUpdate, boolean showLoading) {
        if (forceUpdate) {
            mAirlineRepository.refreshAirlines();
        }

        if (showLoading) {
            mView.setLoadingIndicator(true);
        }

        mSubscriptions.clear();
        Subscription subscription = mAirlineRepository.getAirlines().subscribeOn(
                Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).flatMap(airlines -> Observable
                        .from(airlines)
                        .filter(airline -> {
                            switch (mFilter) {
                                case FAVORITE:
                                    return airline.isFavorite();
                                default:
                                    return true;
                            }
                        }).filter(airline -> {
                            if (mQuery != null) {
                                return airline.getName().toLowerCase().contains(
                                        mQuery.toLowerCase());
                            } else {
                                return true;
                            }
                        })
                        .toList()).subscribe(
                        airlines -> mView.showAirlines(airlines),
                        throwable -> {
                            LogUtils.e(TAG, "Error Getting Airlines", throwable);
                            mView.showLoadingAirlinesError();
                        },
                        () -> mView.setLoadingIndicator(false));
        mSubscriptions.add(subscription);
    }

    @Override
    public void setFilter(@Nullable String query, Filter filter) {
        mFilter = filter;
        mQuery = query;
    }

    @Override
    public void openAirlineDetails(@NonNull Airline airline) {
        mView.showAirlineDetailsUi(airline.getCode(), airline.getName());
    }

    @Override
    public void changeAirlineFavoriteState(@NonNull Airline airline) {
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
}

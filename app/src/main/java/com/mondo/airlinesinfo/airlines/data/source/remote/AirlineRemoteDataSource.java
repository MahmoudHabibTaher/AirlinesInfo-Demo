package com.mondo.airlinesinfo.airlines.data.source.remote;

import android.support.annotation.NonNull;

import com.mondo.airlinesinfo.BuildConfig;
import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.airlines.data.source.AirlineDataSource;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by mahmoud on 11/8/16.
 */

public class AirlineRemoteDataSource implements AirlineDataSource {
    private static final String TAG = AirlineRemoteDataSource.class.getSimpleName();
    private static final String DOMAIN_URL = "https://www.kayak.com";
    private static final String API_URL = DOMAIN_URL + "/h/mobileapis/";

    private static AirlineRemoteDataSource INSTANCE = null;

    public static AirlineRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AirlineRemoteDataSource();
        }

        return INSTANCE;
    }

    private AirlineService mAirlineService;

    private AirlineRemoteDataSource() {
        Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(
                RxJavaCallAdapterFactory
                        .create()).addConverterFactory(GsonConverterFactory.create()).baseUrl(
                API_URL);
        if (BuildConfig.DEBUG) {
            // Used to log Network Requests
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
        }
        Retrofit retrofit = builder.build();
        mAirlineService = retrofit.create(AirlineService.class);
    }

    @Override
    public Observable<List<Airline>> getAirlines() {
        return mAirlineService.getAirlines().flatMap(airlines -> Observable.from(airlines)
                .doOnNext(airline -> airline.setLogo(
                        DOMAIN_URL + airline.getLogo())).toList());
    }

    @Override
    public Observable<Airline> getAirline(@NonNull String code) {
        // Not implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToFavorites(@NonNull Airline airline) {
        // Not implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeFromFavorites(@NonNull Airline airline) {
        // Not implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public void refreshAirlines() {
        // Not implemented
        throw new UnsupportedOperationException();
    }
}

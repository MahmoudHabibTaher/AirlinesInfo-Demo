package com.mondo.airlinesinfo.airlines.details;

import android.support.annotation.NonNull;

import com.mondo.airlinesinfo.BasePresenter;
import com.mondo.airlinesinfo.BaseView;
import com.mondo.airlinesinfo.airlines.data.Airline;

/**
 *
 */

public interface AirlineDetailsContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean enabled);

        void showAirline(@NonNull Airline airline);

        void showAirlineNotFound();

        void showErrorLoadingAirline();

        void showAirlineAddedToFavorites(@NonNull Airline airline);

        void showAirlineRemovedFromFavorites(@NonNull Airline airline);

        void showAirlineWebsiteUi(@NonNull String webSite);

        void showCallAirlineUi(@NonNull String phone);
    }

    interface Presenter extends BasePresenter {
        void loadAirline(@NonNull String code);

        void onAirlineFavoriteClick(@NonNull Airline airline);

        void onAirlineWebsiteClick(@NonNull Airline airline);

        void onAirlinePhoneClick(@NonNull Airline airline);
    }
}

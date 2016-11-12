package com.mondo.airlinesinfo.airlines.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mondo.airlinesinfo.BasePresenter;
import com.mondo.airlinesinfo.BaseView;
import com.mondo.airlinesinfo.airlines.data.Airline;

import java.util.List;

/**
 * Created by mahmoud on 11/9/16.
 */

public interface AirlinesListContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean enabled);

        void showAirlines(@NonNull List<Airline> airlines);

        void showAirlineAddedToFavorites(@NonNull Airline airline);

        void showAirlineRemovedFromFavorites(@NonNull Airline airline);

        void showNoAirlinesAvailable();

        void showLoadingAirlinesError();

        void showAirlineDetailsUi(@NonNull String code, @NonNull String name);
    }

    interface Presenter extends BasePresenter {
        void loadAirlines(boolean forceUpdate);

        void setFilter(@Nullable String query, Filter filter);

        void openAirlineDetails(@NonNull Airline airline);

        void changeAirlineFavoriteState(@NonNull Airline airline);
    }
}

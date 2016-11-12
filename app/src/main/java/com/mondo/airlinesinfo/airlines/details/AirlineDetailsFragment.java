package com.mondo.airlinesinfo.airlines.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mondo.airlinesinfo.R;
import com.mondo.airlinesinfo.airlines.data.Airline;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mahmoud on 11/10/16.
 */

public class AirlineDetailsFragment extends Fragment implements AirlineDetailsContract.View {
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.airline_name_text_view)
    TextView mAirlineNameTextView;

    @BindView(R.id.airline_logo_image_view)
    ImageView mAirlineLogoImageView;

    @BindView(R.id.airline_phone_text_view)
    TextView mPhoneTextView;

    @BindView(R.id.airline_website_text_view)
    TextView mAirlineWebsiteTextView;

    @BindView(R.id.airline_favorite_fab)
    FloatingActionButton mAirlineFavoriteFab;

    private AirlineDetailsContract.Presenter mPresenter;

    private Airline mAirline;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_airline_details, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mAirlineFavoriteFab.setOnClickListener(view -> mPresenter.onAirlineFavoriteClick(mAirline));

        mPhoneTextView.setOnClickListener(view -> mPresenter.onAirlinePhoneClick(mAirline));

        mAirlineWebsiteTextView.setOnClickListener(
                view -> mPresenter.onAirlineWebsiteClick(mAirline));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
    }

    @Override
    public void setLoadingIndicator(boolean enabled) {
        int visibility = enabled ? View.VISIBLE : View.GONE;
        mProgressBar.setVisibility(visibility);
    }

    @Override
    public void showAirline(@NonNull Airline airline) {
        mAirline = airline;

        Glide.with(this).load(airline.getLogo()).into(mAirlineLogoImageView);

        mAirlineNameTextView.setText(airline.getName());

        String phone = airline.getPhone();
        if (phone != null) {
            mPhoneTextView.setText(phone);
        } else {
            mPhoneTextView.setVisibility(View.GONE);
        }

        String website = airline.getWebsite();
        if (website != null) {
            mAirlineWebsiteTextView.setText(website);
        } else {
            mAirlineWebsiteTextView.setVisibility(View.GONE);
        }

        if (airline.isFavorite()) {
            mAirlineFavoriteFab.setImageResource(R.drawable.ic_favorite_filled_white);
        } else {
            mAirlineFavoriteFab.setImageResource(R.drawable.ic_favorite_normal_white);
        }
    }

    @Override
    public void showAirlineNotFound() {

    }

    @Override
    public void showErrorLoadingAirline() {

    }

    @Override
    public void showAirlineAddedToFavorites(@NonNull Airline airline) {
        Toast.makeText(getContext(), getString(R.string.added_to_favorites_toast_text), Toast
                .LENGTH_SHORT).show();
        mAirlineFavoriteFab.setImageResource(R.drawable.ic_favorite_filled_white);
    }

    @Override
    public void showAirlineRemovedFromFavorites(@NonNull Airline airline) {
        Toast.makeText(getContext(), getString(R.string.removed_from_favorites_toast_text), Toast
                .LENGTH_SHORT).show();
        mAirlineFavoriteFab.setImageResource(R.drawable.ic_favorite_normal_white);
    }

    @Override
    public void showAirlineWebsiteUi(@NonNull String webSite) {

    }

    @Override
    public void showCallAirlineUi(@NonNull String phone) {

    }

    @Override
    public void setPresenter(AirlineDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }
}

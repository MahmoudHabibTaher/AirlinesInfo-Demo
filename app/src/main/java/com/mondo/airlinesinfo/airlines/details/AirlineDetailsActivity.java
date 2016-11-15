package com.mondo.airlinesinfo.airlines.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.mondo.airlinesinfo.ui.activities.BaseActivity;
import com.mondo.airlinesinfo.R;
import com.mondo.airlinesinfo.airlines.data.source.AirlineRepository;
import com.mondo.airlinesinfo.airlines.data.source.local.AirlineLocalDataSource;
import com.mondo.airlinesinfo.airlines.data.source.remote.AirlineRemoteDataSource;
import com.mondo.airlinesinfo.utils.schedulers.SchedulersProvider;

/**
 *
 */

public class AirlineDetailsActivity extends BaseActivity {
    public static Intent getStartIntent(@NonNull Context context, @NonNull String code, @Nullable
            String name) {
        Intent intent = new Intent(context, AirlineDetailsActivity.class);
        intent.putExtra(EXTRA_AIRLINE_CODE, code);
        intent.putExtra(EXTRA_AIRLINE_NAME, name);
        return intent;
    }

    private static final String EXTRA_AIRLINE_CODE = "com.mondo.airlinesinfo.airlines.details" +
            ".extras.AIRLINE_CODE";
    private static final String EXTRA_AIRLINE_NAME = "com.mondo.airlinesinfo.airlines.details" +
            ".extras.AIRLINE_NAME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String code = getIntent().getStringExtra(EXTRA_AIRLINE_CODE);

        if (code == null) {
            finish();
        } else {
            setContentView(R.layout.activity_airline_details);
            setBackEnabled(true);

            String title = getIntent().getStringExtra(EXTRA_AIRLINE_NAME);
            if (title != null) {
                setToolbarTitle(title);
            } else {
                setToolbarTitle(R.string.airline_details_activity_title);
            }

            AirlineDetailsFragment airlineDetailsFragment = (AirlineDetailsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.airline_details_fragment);
            if (airlineDetailsFragment == null) {
                airlineDetailsFragment = new AirlineDetailsFragment();
                getSupportFragmentManager().beginTransaction().add(R.id
                        .airline_details_fragment, airlineDetailsFragment).commit();
            }

            AirlineDetailsPresenter presenter = new AirlineDetailsPresenter(code, AirlineRepository
                    .getInstance(AirlineLocalDataSource.getInstance(),
                            AirlineRemoteDataSource.getInstance()), airlineDetailsFragment,
                    SchedulersProvider.getInstance());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

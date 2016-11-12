package com.mondo.airlinesinfo.airlines.list;

import android.os.Bundle;

import com.mondo.airlinesinfo.R;
import com.mondo.airlinesinfo.airlines.data.source.AirlineRepository;
import com.mondo.airlinesinfo.airlines.data.source.local.AirlineLocalDataSource;
import com.mondo.airlinesinfo.airlines.data.source.remote.AirlineRemoteDataSource;
import com.mondo.airlinesinfo.ui.activities.BaseActivity;
import com.mondo.airlinesinfo.utils.schedulers.SchedulersProvider;

public class AirlinesListActivity extends BaseActivity {

    private AirlinesListPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airlines_list);

        setToolbarTitle(R.string.airlines_activity_title);

        AirlinesListFragment airlinesListFragment = (AirlinesListFragment)
                getSupportFragmentManager().findFragmentById
                        (R.id.airlines_fragment);

        if (airlinesListFragment == null) {
            airlinesListFragment = new AirlinesListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.airlines_fragment,
                    airlinesListFragment).commit();
        }

        mPresenter = new AirlinesListPresenter(AirlineRepository.getInstance
                (AirlineLocalDataSource.getInstance(), AirlineRemoteDataSource.getInstance()),
                airlinesListFragment, SchedulersProvider.getInstance());
    }
}

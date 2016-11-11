package com.mondo.airlinesinfo.airlines.list;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mondo.airlinesinfo.R;
import com.mondo.airlinesinfo.airlines.data.Airline;
import com.mondo.airlinesinfo.airlines.details.AirlineDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mahmoud on 11/9/16.
 */

public class AirlinesListFragment extends Fragment
        implements AirlinesListContract.View, AirlinesAdapter.ItemListener {
    private static final String TAG = AirlinesListFragment.class.getSimpleName();

    @BindView(R.id.progress_bar)
    ProgressBar mLoadingProgressBar;

    @BindView(R.id.airlines_recycler_view)
    RecyclerView mAirlinesRecyclerView;

    @BindView(R.id.loading_airlines_failed_view)
    View mLoadingAirlinesFailedView;

    @BindView(R.id.no_favorite_airlines_view)
    View mNoFavoriteAirlinesView;

    @BindView(R.id.no_airlines_found_view)
    View mNoAirlinesFoundView;

    private AirlinesAdapter mAirlinesAdapter;
    private List<Airline> mAirlines;

    private AirlinesListContract.Presenter mPresenter;

    private String mQuery = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAirlines = new ArrayList<>();
        mAirlinesAdapter = new AirlinesAdapter(getContext(), mAirlines);
        mAirlinesAdapter.setItemListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_airlines_list, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_airlines_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
            case R.id.action_filter_all:
                onFilterAllSelected();
                break;
            case R.id.action_filter_favorites:
                onFilterFavoritesSelected();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onFilterAllSelected() {
        mPresenter.setFilter(mQuery, Filter.ALL);
        mPresenter.loadAirlines(false);
    }

    private void onFilterFavoritesSelected() {
        mPresenter.setFilter(mQuery, Filter.FAVORITE);
        mPresenter.loadAirlines(false);
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

    private void init() {
        mAirlinesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(
                new ColorDrawable(ContextCompat.getColor(getContext(), R.color
                        .divider)));
        mAirlinesRecyclerView.addItemDecoration(dividerItemDecoration);
        mAirlinesRecyclerView.setAdapter(mAirlinesAdapter);
    }

    @Override
    public void onItemSelected(@NonNull Airline airline) {
        mPresenter.openAirlineDetails(airline);
    }

    @Override
    public void onItemFavoriteClick(@NonNull Airline airline) {
        mPresenter.changeAirlineFavoriteState(airline);
    }

    @Override
    public void setLoadingIndicator(boolean enabled) {
        int visibility = (enabled ? View.VISIBLE : View.GONE);

        mLoadingProgressBar.setVisibility(visibility);
    }

    @Override
    public void showAirlines(@NonNull List<Airline> airlines) {
        mAirlines.clear();
        mAirlines.addAll(airlines);

        mAirlinesAdapter.notifyDataSetChanged();

        mAirlinesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAirlineAddedToFavorites(@NonNull Airline airline) {
        Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();
        mAirlinesAdapter.notifyItemChanged(mAirlines.indexOf(airline));
    }

    @Override
    public void showAirlineRemovedFromFavorites(@NonNull Airline airline) {
        Toast.makeText(getActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show();
        mAirlinesAdapter.notifyItemChanged(mAirlines.indexOf(airline));
    }

    @Override
    public void showLoadingAirlinesError() {

    }

    @Override
    public void showAirlineDetailsUi(@NonNull String code, @NonNull String name) {
        startActivity(AirlineDetailsActivity.getStartIntent(getContext(), code, name));
    }

    @Override
    public void setPresenter(AirlinesListContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
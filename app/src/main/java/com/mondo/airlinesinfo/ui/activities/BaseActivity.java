package com.mondo.airlinesinfo.ui.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mondo.airlinesinfo.R;

/**
 * Created by mahmoud on 11/10/16.
 */

public class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private boolean mBackEnabled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        getToolbar();
    }

    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
                updateToolbarBack();
            }
        }
        return mToolbar;
    }

    protected void setToolbarTitle(@StringRes int titleResId) {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitle(titleResId);
        }
    }

    protected void setToolbarTitle(@Nullable String title) {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    protected void setBackEnabled(boolean enabled) {
        mBackEnabled = enabled;
        updateToolbarBack();
    }

    protected boolean isBackEnabled() {
        return mBackEnabled;
    }

    protected void updateToolbarBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            boolean showBackButton = isBackEnabled();
            actionBar.setHomeButtonEnabled(showBackButton);
            actionBar.setDisplayShowHomeEnabled(showBackButton);
            actionBar.setDisplayHomeAsUpEnabled(showBackButton);
        }
    }
}

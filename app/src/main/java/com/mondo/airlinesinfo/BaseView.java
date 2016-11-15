package com.mondo.airlinesinfo;

/**
 * Created by mahmoud on 11/8/16.
 */

public interface BaseView<T> {
    /**
     * Used to set the presenter that will be used by this view.
     * @param presenter
     */
    void setPresenter(T presenter);
}

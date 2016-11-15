package com.mondo.airlinesinfo;

/**
 * Created by mahmoud on 11/8/16.
 */

public interface BasePresenter {
    /**
     * This method should be called to notify the presenter that the view is ready and visible to
     * the user.
     * You should register any listeners or subscribers here.
     */
    void subscribe();

    /**
     * This method should be called when the view is no longer visible to the user.
     * You should unregister any registered listeners or subscribers here.
     */
    void unSubscribe();
}

package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.ServiceProvider;

/**
 * Created by Chung Min on 7/20/2015.
 */
public interface GetServiceProviderCallback {
    /**
     * Invoked when background task is completed
     */

    void done(ServiceProvider returnedServiceProvider);
}

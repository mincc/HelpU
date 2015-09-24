package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.ServiceProvider;

import java.util.List;

/**
 * Created by Chung Min on 7/20/2015.
 */
public interface GetServiceProviderListCallback {
    /**
     * Invoked when background task is completed
     */

    void done(List<ServiceProvider> returnedServiceProviderList);
}

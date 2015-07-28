package com.example.chungmin.helpu;

import java.util.List;

/**
 * Created by Chung Min on 7/20/2015.
 */
interface GetServiceProviderListCallback {
    /**
     * Invoked when background task is completed
     */

    void done(List<ServiceProvider> returnedServiceProviderList);
}

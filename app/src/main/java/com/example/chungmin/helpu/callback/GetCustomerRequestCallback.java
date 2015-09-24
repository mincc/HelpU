package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.CustomerRequest;

/**
 * Created by Chung Min on 7/20/2015.
 */
public interface GetCustomerRequestCallback {
    /**
     * Invoked when background task is completed
     */

    void done(CustomerRequest returnedCustomerRequest);
}

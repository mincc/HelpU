package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/20/2015.
 */
public interface GetCustomerRequestCallback {
    /**
     * Invoked when background task is completed
     */

    void done(CustomerRequest returnedCustomerRequest);
}

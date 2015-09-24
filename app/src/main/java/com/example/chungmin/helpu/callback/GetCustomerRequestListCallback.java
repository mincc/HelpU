package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.CustomerRequest;

import java.util.List;

/**
 * Created by Chung Min on 7/23/2015.
 */
public interface GetCustomerRequestListCallback {
    void Complete(List<CustomerRequest> data);
    void Failure(String msg);

}

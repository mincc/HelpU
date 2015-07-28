package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/23/2015.
 */
public interface CustomerRequestCountListener {
    void onFetchCustomerRequestCountComplete(int data);
    void onFetchCustomerRequestCountFailure(String msg);

}

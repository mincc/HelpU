package com.example.chungmin.helpu;

import java.util.List;

/**
 * Created by Chung Min on 7/23/2015.
 */
public interface CustomerRequestDataListener {
    void onFetchComplete(List<CustomerRequest> data);
    void onFetchFailure(String msg);

}

package com.example.chungmin.helpu;

import java.util.List;

/**
 * Created by Chung Min on 7/23/2015.
 */
public interface FetchServiceProviderDataListener {
    void Complete(List<ServiceProvider> data);
    void Failure(String msg);

}

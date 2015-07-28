package com.example.chungmin.helpu;

import java.util.List;

/**
 * Created by Chung Min on 7/23/2015.
 */
public interface FetchServiceProviderCountListener {
    void onFetchComplete(int data);
    void onFetchFailure(String msg);

}

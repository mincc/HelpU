package com.example.chungmin.helpu;

import java.util.List;

/**
 * Created by Chung Min on 7/23/2015.
 */
public interface FetchDataListener {
    void onFetchComplete(List<Application> data);
    void onFetchFailure(String msg);

}

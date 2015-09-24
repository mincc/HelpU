package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.Application;

import java.util.List;

/**
 * Created by Chung Min on 7/23/2015.
 */
public interface FetchDataListener {
    void Complete(List<Application> data);
    void Failure(String msg);

}

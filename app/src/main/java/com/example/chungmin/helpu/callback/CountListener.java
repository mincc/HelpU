package com.example.chungmin.helpu.callback;

/**
 * Created by Chung Min on 7/23/2015.
 */
public interface CountListener {
    void CountComplete(int data);
    void CountFailure(String msg);

}
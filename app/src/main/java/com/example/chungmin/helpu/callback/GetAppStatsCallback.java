package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.AppStats;

/**
 * Created by Chung Min on 7/20/2015.
 */
public interface GetAppStatsCallback {
    /**
     * Invoked when background task is completed
     */

    void done(AppStats returnedAppStats);
}

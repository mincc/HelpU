package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.UserStats;

/**
 * Created by Chung Min on 7/20/2015.
 */
public interface GetUserStatsCallback {
    /**
     * Invoked when background task is completed
     */

    void done(UserStats returnedUserStats);
}

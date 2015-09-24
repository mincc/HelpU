package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.Rating;

/**
 * Created by Chung Min on 7/20/2015.
 */
public interface GetRatingCallback {
    /**
     * Invoked when background task is completed
     */

    void done(Rating returnedRating);
}

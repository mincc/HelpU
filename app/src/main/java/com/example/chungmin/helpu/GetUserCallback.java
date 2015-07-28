package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/20/2015.
 */
interface  GetUserCallback {
    /**
     * Invoked when background task is completed
     */

    void done(User returnedUser);
}

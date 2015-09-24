package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.User;

/**
 * Created by Chung Min on 7/20/2015.
 */
public interface GetUserCallback {
    /**
     * Invoked when background task is completed
     */

    void done(User returnedUser);

    void fail(String msg);
}

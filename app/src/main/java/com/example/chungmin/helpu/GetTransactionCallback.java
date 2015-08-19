package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 8/14/2015.
 */
public interface GetTransactionCallback {
    /**
     * Invoked when background task is completed
     */

    void done(Transaction returnedTransaction);
}
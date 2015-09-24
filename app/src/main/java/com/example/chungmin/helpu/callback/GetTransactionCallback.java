package com.example.chungmin.helpu.callback;

import com.example.chungmin.helpu.models.Transaction;

/**
 * Created by Chung Min on 8/14/2015.
 */
public interface GetTransactionCallback {
    /**
     * Invoked when background task is completed
     */

    void done(Transaction returnedTransaction);
}
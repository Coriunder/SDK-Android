package com.coriunder.transactions.callbacks;

import com.coriunder.transactions.models.Transaction;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedSearchCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param transactions ArrayList containing Transaction models
     * @see Transaction
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<Transaction> transactions, String message);
}
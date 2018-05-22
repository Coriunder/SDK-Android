package com.coriunder.transactions.callbacks;

import com.coriunder.transactions.models.Transaction;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedTransactionCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param transaction Transaction model containing transaction's data;
     *                null when request to the server was not successful (!isSuccess)
     * @see Transaction
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, Transaction transaction, String message);
}
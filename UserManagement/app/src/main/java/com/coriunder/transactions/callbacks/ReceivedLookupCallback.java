package com.coriunder.transactions.callbacks;

import com.coriunder.transactions.models.TransactionLookup;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedLookupCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param transactionLookup ArrayList containing TransactionLookup models
     * @see TransactionLookup
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<TransactionLookup> transactionLookup, String message);
}
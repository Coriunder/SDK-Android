package com.coriunder.balance.callbacks;

import com.coriunder.balance.models.Request;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedRequestListCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param requests ArrayList containing Request models
     * @see Request
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<Request> requests, String message);
}
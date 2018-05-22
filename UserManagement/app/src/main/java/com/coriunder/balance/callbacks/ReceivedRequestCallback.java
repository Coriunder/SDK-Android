package com.coriunder.balance.callbacks;

import com.coriunder.balance.models.Request;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedRequestCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param request Request model containing request's data;
     *                null when request to the server was not successful (!isSuccess)
     * @see Request
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, Request request, String message);
}

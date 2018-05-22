package com.coriunder.base.common.callbacks;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedBasicCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, String message);
}
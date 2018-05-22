package com.coriunder.account.callbacks;

import com.coriunder.account.models.LoginResult;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedLoginCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param loginResult LoginResult model containing login result data
     * @see LoginResult
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, LoginResult loginResult, String message);
}
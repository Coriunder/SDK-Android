package com.coriunder.appidentity.callbacks;

import com.coriunder.appidentity.models.Identity;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedIdentityCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param identity Identity model containing app identity data
     * @see Identity
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, Identity identity, String message);
}
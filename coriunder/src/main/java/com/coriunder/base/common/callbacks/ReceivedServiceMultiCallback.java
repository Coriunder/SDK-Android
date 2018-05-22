package com.coriunder.base.common.callbacks;

import com.coriunder.base.common.models.ServiceMultiResult;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedServiceMultiCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param serviceMultiResult ServiceMultiResult model containing server response data
     * @see ServiceMultiResult
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ServiceMultiResult serviceMultiResult, String message);
}
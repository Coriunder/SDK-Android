package com.coriunder.base.common.callbacks;

import com.coriunder.base.common.models.ServiceResult;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedServiceCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param serviceResult ServiceResult model containing server response data
     * @see ServiceResult
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ServiceResult serviceResult, String message);
}
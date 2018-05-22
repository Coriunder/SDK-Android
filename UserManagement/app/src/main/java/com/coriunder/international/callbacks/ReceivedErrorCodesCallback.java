package com.coriunder.international.callbacks;

import com.coriunder.base.common.models.ServiceResult;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedErrorCodesCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param serviceResults ArrayList containing all available ServiceResult models
     * @see ServiceResult
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<ServiceResult> serviceResults, String message);
}
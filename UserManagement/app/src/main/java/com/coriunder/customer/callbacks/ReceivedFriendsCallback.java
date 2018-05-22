package com.coriunder.customer.callbacks;

import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.customer.models.Friend;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedFriendsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param serviceResult ServiceResult model containing server response data
     * @see ServiceResult
     * @param friends ArrayList containing Friend models
     * @see Friend
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ServiceResult serviceResult, ArrayList<Friend> friends, String message);
}
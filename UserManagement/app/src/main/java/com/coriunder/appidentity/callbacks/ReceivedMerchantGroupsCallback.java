package com.coriunder.appidentity.callbacks;

import com.coriunder.appidentity.models.MerchantGroup;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedMerchantGroupsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param merchantGroups ArrayList containing MerchantGroup models
     * @see MerchantGroup
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<MerchantGroup> merchantGroups, String message);
}
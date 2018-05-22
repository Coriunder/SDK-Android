package com.coriunder.shop.callbacks;

import com.coriunder.base.common.models.Merchant;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedMerchantCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param merchant Merchant model containing merchant's data;
     *                null when request to the server was not successful (!isSuccess)
     * @see Merchant
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, Merchant merchant, String message);
}
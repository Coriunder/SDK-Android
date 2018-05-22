package com.coriunder.shop.callbacks;

import com.coriunder.base.common.models.Merchant;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedMerchantsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param merchants ArrayList containing Merchant models
     * @see Merchant
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<Merchant> merchants, String message);
}
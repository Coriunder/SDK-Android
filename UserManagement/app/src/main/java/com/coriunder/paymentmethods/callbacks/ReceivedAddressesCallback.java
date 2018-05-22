package com.coriunder.paymentmethods.callbacks;

import com.coriunder.base.common.models.Address;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedAddressesCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param addresses ArrayList containing Address models
     * @see Address
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<Address> addresses, String message);
}
package com.coriunder.customer.callbacks;

import com.coriunder.customer.models.ShippingAddress;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedShippingAddressCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param address ShippingAddress model containing shipping address' data;
     *                null when request to the server was not successful (!isSuccess)
     * @see ShippingAddress
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ShippingAddress address, String message);
}
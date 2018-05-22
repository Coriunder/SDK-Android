package com.coriunder.customer.callbacks;

import com.coriunder.customer.models.ShippingAddress;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedShippingAddressesCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param shippingAddresses ArrayList containing ShippingAddress models
     * @see ShippingAddress
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<ShippingAddress> shippingAddresses, String message);
}
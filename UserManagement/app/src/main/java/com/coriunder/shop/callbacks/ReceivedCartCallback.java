package com.coriunder.shop.callbacks;

import com.coriunder.shop.models.Cart;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedCartCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param cart Cart model containing cart's data;
     *             null when request to the server was not successful (!isSuccess)
     * @see Cart
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, Cart cart, String message);
}
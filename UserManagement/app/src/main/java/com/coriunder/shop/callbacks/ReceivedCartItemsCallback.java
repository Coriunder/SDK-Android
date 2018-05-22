package com.coriunder.shop.callbacks;

import com.coriunder.shop.models.CartItem;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedCartItemsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param cartItems ArrayList containing CartItem models
     * @see CartItem
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<CartItem> cartItems, String message);
}
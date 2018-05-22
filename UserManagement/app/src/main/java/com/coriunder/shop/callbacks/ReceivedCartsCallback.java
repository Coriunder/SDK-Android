package com.coriunder.shop.callbacks;

import com.coriunder.shop.models.Cart;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedCartsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param carts ArrayList containing Cart models
     * @see Cart
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<Cart> carts, String message);
}
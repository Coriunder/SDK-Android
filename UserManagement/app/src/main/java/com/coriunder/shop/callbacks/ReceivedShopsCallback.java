package com.coriunder.shop.callbacks;

import com.coriunder.shop.models.Shop;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedShopsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param shops ArrayList containing Shop models
     * @see Shop
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<Shop> shops, String message);
}
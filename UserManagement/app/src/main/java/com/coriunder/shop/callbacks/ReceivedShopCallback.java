package com.coriunder.shop.callbacks;

import com.coriunder.shop.models.Shop;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedShopCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param shop Shop model containing shop's data;
     *             null when request to the server was not successful (!isSuccess)
     * @see Shop
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, Shop shop, String message);
}
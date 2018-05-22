package com.coriunder.shop.callbacks;

import com.coriunder.shop.models.Product;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedProductCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param product Product model containing product's data;
     *                null when request to the server was not successful (!isSuccess)
     * @see Product
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, Product product, String message);
}
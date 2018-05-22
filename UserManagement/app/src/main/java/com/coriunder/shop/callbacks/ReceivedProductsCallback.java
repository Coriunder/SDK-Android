package com.coriunder.shop.callbacks;

import com.coriunder.shop.models.Product;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedProductsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param products ArrayList containing Product models
     * @see Product
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<Product> products, String message);
}
package com.coriunder.shop.callbacks;

import com.coriunder.shop.models.ProductCategory;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedCategoriesCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param productCategories ArrayList containing ProductCategory models
     * @see ProductCategory
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<ProductCategory> productCategories, String message);
}
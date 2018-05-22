package com.coriunder.shop.callbacks;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedShopIdCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param merchantNumber merchant number for the shop
     * @param shopId shop id
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, String merchantNumber, long shopId, String message);
}
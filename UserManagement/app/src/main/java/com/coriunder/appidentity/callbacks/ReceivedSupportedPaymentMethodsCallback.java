package com.coriunder.appidentity.callbacks;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedSupportedPaymentMethodsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param paymentMethodIds array containing supported payment methods' types' ids
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, int[] paymentMethodIds, String message);
}
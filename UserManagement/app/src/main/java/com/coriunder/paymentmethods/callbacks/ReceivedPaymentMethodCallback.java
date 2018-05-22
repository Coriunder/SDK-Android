package com.coriunder.paymentmethods.callbacks;

import com.coriunder.paymentmethods.models.PaymentMethod;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedPaymentMethodCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param paymentMethod PaymentMethod model containing payment method's data;
     *                null when request to the server was not successful (!isSuccess)
     * @see PaymentMethod
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, PaymentMethod paymentMethod, String message);
}
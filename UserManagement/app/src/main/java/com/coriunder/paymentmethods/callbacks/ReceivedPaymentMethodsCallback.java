package com.coriunder.paymentmethods.callbacks;

import com.coriunder.paymentmethods.models.PaymentMethod;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedPaymentMethodsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param paymentMethods ArrayList containing PaymentMethod models
     * @see PaymentMethod
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<PaymentMethod> paymentMethods, String message);
}
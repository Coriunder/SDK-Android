package com.coriunder.paymentmethods.callbacks;

import com.coriunder.paymentmethods.models.PaymentCardRootGroup;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedPaymentMethodsGroupsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param paymentCardGroups ArrayList containing PaymentCardRootGroup models
     * @see PaymentCardRootGroup
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<PaymentCardRootGroup> paymentCardGroups, String message);
}
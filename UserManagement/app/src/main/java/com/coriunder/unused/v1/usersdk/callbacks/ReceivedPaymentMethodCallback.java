package com.coriunder.unused.v1.usersdk.callbacks;

import com.coriunder.unused.v1.usersdk.models.PaymentMethod;

/**
 * Created by 1 on 15.02.2016.
 */
public interface ReceivedPaymentMethodCallback {
    void onResultReceived(boolean isSuccess, PaymentMethod paymentMethod, String message);
}
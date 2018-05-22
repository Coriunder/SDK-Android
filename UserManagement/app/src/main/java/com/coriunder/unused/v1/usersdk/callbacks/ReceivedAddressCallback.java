package com.coriunder.unused.v1.usersdk.callbacks;

import com.coriunder.unused.v1.usersdk.models.ShippingAddress;

/**
 * Created by 1 on 15.02.2016.
 */
public interface ReceivedAddressCallback {
    void onResultReceived(boolean isSuccess, ShippingAddress address, String message);
}
package com.coriunder.unused.v1.usersdk.callbacks;

import com.coriunder.unused.v1.usersdk.models.Customer;

/**
 * Created by 1 on 15.02.2016.
 */
public interface ReceivedCustomerCallback {
    void onResultReceived(boolean isSuccess, Customer customer, String message);
}
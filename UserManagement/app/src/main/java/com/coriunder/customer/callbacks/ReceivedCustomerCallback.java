package com.coriunder.customer.callbacks;

import com.coriunder.customer.models.Customer;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedCustomerCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param customer Customer model containing customer data;
     *                 null when request to the server was not successful (!isSuccess)
     * @see Customer
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, Customer customer, String message);
}
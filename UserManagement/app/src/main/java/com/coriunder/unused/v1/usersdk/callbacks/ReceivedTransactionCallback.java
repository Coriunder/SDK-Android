package com.coriunder.unused.v1.usersdk.callbacks;

import com.coriunder.unused.v1.usersdk.models.Transaction;

/**
 * Created by 1 on 15.02.2016.
 */
public interface ReceivedTransactionCallback {
    void onResultReceived(boolean isSuccess, Transaction transaction, String message);
}
package com.coriunder.balance.callbacks;

import com.coriunder.balance.models.BalanceTotal;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedBalanceTotalListCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param balanceTotals ArrayList containing BalanceTotal models
     * @see BalanceTotal
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<BalanceTotal> balanceTotals, String message);
}
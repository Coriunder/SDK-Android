package com.coriunder.balance.callbacks;

import com.coriunder.balance.models.BalanceRow;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedBalanceRowListCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param balanceRows ArrayList containing BalanceRow models
     * @see BalanceRow
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<BalanceRow> balanceRows, String message);
}
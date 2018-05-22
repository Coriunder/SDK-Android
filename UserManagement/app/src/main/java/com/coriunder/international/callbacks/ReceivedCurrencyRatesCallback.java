package com.coriunder.international.callbacks;

import com.coriunder.international.models.CurrencyRate;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedCurrencyRatesCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param currencyRates ArrayList containing CurrencyRate models
     * @see CurrencyRate
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<CurrencyRate> currencyRates, String message);
}
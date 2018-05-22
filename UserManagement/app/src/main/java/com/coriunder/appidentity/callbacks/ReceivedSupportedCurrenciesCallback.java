package com.coriunder.appidentity.callbacks;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedSupportedCurrenciesCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param currencies array containing supported currencies' ISO codes
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, String[] currencies, String message);
}
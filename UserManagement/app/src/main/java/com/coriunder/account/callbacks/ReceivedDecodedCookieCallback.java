package com.coriunder.account.callbacks;

import com.coriunder.account.models.CookieDecodeResult;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedDecodedCookieCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param cookieDecodeResult CookieDecodeResult model containing decoded cookie data
     * @see CookieDecodeResult
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, CookieDecodeResult cookieDecodeResult, String message);
}
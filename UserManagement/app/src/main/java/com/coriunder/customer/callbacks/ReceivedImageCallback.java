package com.coriunder.customer.callbacks;

import android.graphics.Bitmap;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedImageCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param image Bitmap for the user's profile picture;
     *              null when request to the server was not successful (!isSuccess)
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, Bitmap image, String message);
}
package com.coriunder.unused.v1.mobilesdk.callbacks;

import android.graphics.Bitmap;

/**
 * Created by 1 on 15.02.2016.
 */
public interface ReceivedImageCallback {
    void onResultReceived(boolean isSuccess, Bitmap image, String message);
}
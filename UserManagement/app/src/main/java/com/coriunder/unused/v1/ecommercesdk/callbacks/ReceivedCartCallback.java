package com.coriunder.unused.v1.ecommercesdk.callbacks;

import com.coriunder.unused.v1.ecommercesdk.models.Cart;

/**
 * Created by 1 on 15.02.2016.
 */
public interface ReceivedCartCallback {
    void onResultReceived(boolean isSuccess, Cart cart, String message);
}
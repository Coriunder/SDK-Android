package com.coriunder.unused.v1.ecommercesdk.callbacks;

import com.coriunder.unused.v1.ecommercesdk.models.Product;

/**
 * Created by 1 on 15.02.2016.
 */
public interface ReceivedProductCallback {
    void onResultReceived(boolean isSuccess, Product product, String message);
}
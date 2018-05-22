package com.coriunder.unused.v1.ecommercesdk.callbacks;

import com.coriunder.unused.v1.ecommercesdk.models.Merchant;

/**
 * Created by 1 on 15.02.2016.
 */
public interface ReceivedMerchantCallback {
    void onResultReceived(boolean isSuccess, Merchant merchant, String message);
}
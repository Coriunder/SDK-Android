package com.coriunder.unused.v1.balancesdk.callbacks;

import com.coriunder.unused.v1.balancesdk.models.RequestItem;

/**
 * Created by 1 on 22.02.2016.
 */
public interface ReceivedRequestItemCallback {
    void onResultReceived(boolean isSuccess, RequestItem item, String message);
}

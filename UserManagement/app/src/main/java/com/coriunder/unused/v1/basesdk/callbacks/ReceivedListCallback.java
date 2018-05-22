package com.coriunder.unused.v1.basesdk.callbacks;

import java.util.ArrayList;

/**
 * Created by 1 on 15.02.2016.
 */
public interface ReceivedListCallback {
    void onResultReceived(boolean isSuccess, ArrayList<Object> result, String message);
}
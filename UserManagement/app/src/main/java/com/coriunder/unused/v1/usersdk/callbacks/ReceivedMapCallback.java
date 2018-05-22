package com.coriunder.unused.v1.usersdk.callbacks;

import java.util.HashMap;

/**
 * Created by 1 on 15.02.2016.
 */
public interface ReceivedMapCallback {
    void onResultReceived(boolean isSuccess, HashMap<String,Object> result, String message);
}
package com.coriunder.unused.v1.usersdk;

import com.android.volley.DefaultRetryPolicy;

/**
 * Created by 1 on 15.02.2016.
 */
public class Constants {
    public static final String APP_TOKEN = "3fb5b551-6ba1-4e6a-b68f-8682072b0260";
    public static final String SERVICE_URL = "http://webservices.stage.obl.me/wallet.asmx/";
    public static final String CRYPTOR_SEED = "4aab2b7d9bb3dfed9c1617f9a7b2efa8";

    //Timeout and retries
    public static final int DEFAULT_TIMEOUT_MS = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
    public static final int DEFAULT_MAX_RETRIES = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
    public static final float DEFAULT_BACKOFF_MULT = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
}

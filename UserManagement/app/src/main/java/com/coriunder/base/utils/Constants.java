package com.coriunder.base.utils;

import com.android.volley.DefaultRetryPolicy;

/**
 * Common constants for all services
 */
public class Constants {
    public static final boolean ENABLE_LOGS = true;
    public static final String CRYPTO_SEED = "4aab2b7d9bb3dfed9c1617f9a7b2efa8";
    public static final String SERVICE_URL = "https://stagewebservices.netpay-intl.com/v2/";
    public static final String APP_TOKEN = "3fb5b551-6ba1-4e6a-b68f-8682072b0260";
    public static final String SHA256_SALT = "6Yn5gQ4V";

    // Timeout and retries
    public static final int DEFAULT_TIMEOUT_MS = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
    public static final int DEFAULT_MAX_RETRIES = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
    public static final float DEFAULT_BACKOFF_MULTIPLIER = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;

    // Keys for shared preferences
    public static final String SP_EMAIL = "coriunderEmail";
    public static final String SP_PASS = "coriunderPass";
    public static final String SP_CRED_TOKEN = "coriunderCt";
    public static final String SP_HEADER = "coriunderHeader";
    public static final String SP_APP_TOKEN = "coriunderAppToken";
    public static final String SP_URL = "coriunderUrl";

    // Test SSL pinning
    public static final boolean ENABLE_SSL_PINNING = true;
    public static final String SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100b343b045503dd16b6cc63eaa566d12c36a3646dcaefe96ab70fe69d5e74db26d3a7c746a04864a48071d2b9a2293640a8c44fbab3c21c09d9e445282e0f732b7d45a796a79190a4b3ae3e1c665a58a0addbbe3999ba4741d1e8c8ecded0c0181339a997c640705bd4559f41b2296e45cc561cff9356704d9497055edb3de64bae26dc4c65147e3ecca664dac9cc97c9cbc439d6d44eee61cc28d136a482874ad8ff0a36a0a4c883357ddc9cef3de77ac7d3a5369fd67efa2356c2c8aeb6631d54afe4dabdd044b4143e7d039424bbdbb336ecb8cf3a627d0320c62bf24bb6b4db4fe7818e97a6e22f3eaed538e622a4c62247afa68a5ea9a2dde2611cef56bbd0203010001";
}
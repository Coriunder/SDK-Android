package com.coriunder.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.coriunder.base.utils.Constants;
import com.coriunder.base.utils.PubKeyManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * This class allows to initialize Coriunder SDK and get some info about its state
 */
@SuppressWarnings("unused")
public class Coriunder {
    public static final String SESSION_EXPIRED = "coriunderSessionExpired";
    private static Context appContext;
    private static RequestQueue requestQueue;

    public Coriunder() {
    }

    /**
     * Initialize Coriunder SDK
     * @param context application context
     */
    public static void init(Context context) {
        appContext = context;
    }

    /**
     * Detect whether user is logged in
     * @return user login state
     */
    public static boolean isUserLoggedIn() {
        return !TextUtils.isEmpty(getCredentialsToken()) && !TextUtils.isEmpty(getCredentialsHeader());
    }

    /**
     * Get context passed to Coriuder SDK
     * @return context
     */
    public static Context getContext() {
        return appContext;
    }

    /**
     * Get credentials token for currently logged in user
     * @return credentials token or empty String in case user is not logged in
     */
    public static String getCredentialsToken() {
        return UserSession.getInstance().getCredentialsToken();
    }

    /**
     * Get credentials token for currently logged in user
     * @return credentials token or empty String in case user is not logged in
     */
    public static String getCredentialsHeader() {
        return UserSession.getInstance().getCredentialsHeader();
    }

    /**
     * Get service URL
     * @return service URL
     */
    public static String getServiceUrl() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String customUrl = prefs.getString(Constants.SP_URL, "");
        return TextUtils.isEmpty(customUrl) ? Constants.SERVICE_URL : customUrl;
    }

    /**
     * Set service URL
     */
    public static void setServiceUrl(String serviceUrl) {
        if (!TextUtils.isEmpty(serviceUrl) && !serviceUrl.contains("http://") && !serviceUrl.contains("https://"))
            serviceUrl = "http://"+serviceUrl;

        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        prefs.putString(Constants.SP_URL, serviceUrl == null ? "" : serviceUrl);
        prefs.apply();
    }

    /**
     * Get application token
     * @return application token
     */
    public static String getAppToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String customAppToken = prefs.getString(Constants.SP_APP_TOKEN, "");
        return TextUtils.isEmpty(customAppToken) ? Constants.APP_TOKEN : customAppToken;
    }

    /**
     * Set application token
     */
    public static void setAppToken(String appToken) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        prefs.putString(Constants.SP_APP_TOKEN, appToken == null ? "" : appToken);
        prefs.apply();
    }

    /**
     * Get RequestQueue object to perform the request
     * @return RequestQueue object
     */
    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            if (Constants.ENABLE_SSL_PINNING)
                requestQueue = Volley.newRequestQueue(appContext, new HurlStack(null, pinnedSSLSocketFactory()));
            else requestQueue = Volley.newRequestQueue(appContext);
        }
        return requestQueue;
    }

    private static SSLSocketFactory pinnedSSLSocketFactory() {
        TrustManager tm[] = {new PubKeyManager(Constants.SSL_PUBLIC_KEY)};
        SSLContext context;
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, tm, null);
            return context.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }
}
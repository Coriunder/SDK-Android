package com.coriunder.merchant;

import android.content.Context;

import com.android.volley.Request;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.merchant.models.RegistrationMerchant;

import org.json.JSONObject;

/**
 * This class contains methods to perform requests to the Merchant service
 */
@SuppressWarnings("unused")
public class MerchantSDK {
    private static MerchantSDK instance;
    public static final String SERVICE_URL_PART = "Merchant.svc";

    public MerchantSDK() {
    }

    /**
     * Get instance for MerchantSDK class.
     * In case there is no current instance, a new one will be created
     * @return MerchantSDK instance
     */
    public static MerchantSDK getInstance() {
        if (instance == null) instance = new MerchantSDK();
        return instance;
    }

    /**
     * Register as a merchant
     * @param merchant RegistrationMerchant object for the merchant which has to be registered
     * @see RegistrationMerchant
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void registerMerchant(RegistrationMerchant merchant, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildRegisterMerchantJson(merchant);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Register"), json, CommonRequests.createBasicSuccessListener(callback),
                CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get context
     * @return context
     */
    private Context getContext() {
        return Coriunder.getContext();
    }

    /**
     * Create URL for request
     * @param method service method name
     * @return service URL
     */
    private static String createUrl(String method) {
        return Coriunder.getServiceUrl() + SERVICE_URL_PART + "/" + method;
    }
}
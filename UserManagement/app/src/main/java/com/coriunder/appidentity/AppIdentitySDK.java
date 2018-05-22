package com.coriunder.appidentity;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.appidentity.callbacks.ReceivedIdentityCallback;
import com.coriunder.appidentity.callbacks.ReceivedMerchantGroupsCallback;
import com.coriunder.appidentity.callbacks.ReceivedSupportedCurrenciesCallback;
import com.coriunder.appidentity.callbacks.ReceivedSupportedPaymentMethodsCallback;
import com.coriunder.appidentity.models.Identity;
import com.coriunder.appidentity.models.MerchantGroup;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods to perform requests to the AppIdentity service
 */
@SuppressWarnings("unused")
public class AppIdentitySDK {
    private static AppIdentitySDK instance;
    public static final String SERVICE_URL_PART = "AppIdentity.svc";

    public AppIdentitySDK() {
    }

    /**
     * Get instance for AppIdentitySDK class.
     * In case there is no current instance, a new one will be created
     * @return AppIdentitySDK instance
     */
    public static AppIdentitySDK getInstance() {
        if (instance == null) instance = new AppIdentitySDK();
        return instance;
    }

    /**
     * Get content for the application
     *
     * @param contentName name of the content you need to get
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void getContent(String contentName, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetContent(contentName);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetContent"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        String jsonResult = Parser.getString(response, "d");
                        if (callback != null) callback.onResultReceived(true, jsonResult);
                    }
                }, CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }


    /**
     * Get identity data about the current app
     *
     * @param callback will be called after request completion
     * @see ReceivedIdentityCallback
     */
    public void getIdentityDetails(final ReceivedIdentityCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetIdentityDetails"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            // Parse response
                            Identity result = Parser.parseIdentity(response);
                            if (callback != null) callback.onResultReceived(true, result, "");
                        } else {
                            if (callback != null) callback.onResultReceived(false, new Identity(), getContext().getString(R.string.empty_response_error));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false, new Identity(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }


    /**
     * Get the list of merchant groups supported by the application
     *
     * @param callback will be called after request completion
     * @see ReceivedMerchantGroupsCallback
     */
    public void getMerchantGroups(final ReceivedMerchantGroupsCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetMerchantGroups"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<MerchantGroup> arrayList = Parser.parseMerchantGroups(response);
                        if (callback != null) callback.onResultReceived(true, arrayList, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<MerchantGroup>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get the list of currencies supported by the application
     *
     * @param callback will be called after request completion
     * @see ReceivedSupportedCurrenciesCallback
     */
    public void getSupportedCurrencies(final ReceivedSupportedCurrenciesCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetSupportedCurrencies"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        String[] currenciesArray = Parser.parseCurrencies(response);
                        if (callback != null) callback.onResultReceived(true, currenciesArray, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false, new String[0], Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get the list of payment methods supported by the application
     *
     * @param callback will be called after request completion
     * @see ReceivedSupportedPaymentMethodsCallback
     */
    public void getSupportedPaymentMethods(final ReceivedSupportedPaymentMethodsCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetSupportedPaymentMethods"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        int[] pmsArray = Parser.parseSupportedPaymentMethods(response);
                        if (callback != null) callback.onResultReceived(true, pmsArray, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false, new int[0], Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Log data to the server
     *
     * @param severityId severity ID
     * @param message main log message
     * @param longMessage long log message
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void log(int severityId, String message, String longMessage, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForLog(severityId, message, longMessage);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Log"), json, CommonRequests.createBasicSuccessListener(callback),
                CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Send email to the application owner
     *
     * @param from email sender
     * @param subject email subject
     * @param body main email text
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void sendEmail(String from, String subject, String body, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForEmail(from, subject, body);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SendContactEmail"), json, CommonRequests.createBasicSuccessListener(callback),
                CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Create URL for request
     * @param method service method name
     * @return service URL
     */
    private String createUrl(String method) {
        return Coriunder.getServiceUrl() + SERVICE_URL_PART + "/" + method;
    }

    /**
     * Get context
     * @return context
     */
    private Context getContext() {
        return Coriunder.getContext();
    }
}
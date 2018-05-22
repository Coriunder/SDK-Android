package com.coriunder.shop;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.shop.callbacks.ReceivedCategoriesCallback;
import com.coriunder.shop.callbacks.ReceivedMerchantCallback;
import com.coriunder.shop.callbacks.ReceivedMerchantsCallback;
import com.coriunder.base.common.models.Merchant;
import com.coriunder.shop.models.ProductCategory;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods to perform requests to the Shop service related to merchants
 */
@SuppressWarnings("unused")
public class ShopSDKMerchants {
    private static ShopSDKMerchants instance;
    public static final String SERVICE_URL_PART = "Shop.svc";
    public enum MerchantStatus { ARCHIVED, NEW, BLOCKED, CLOSED, LOGIN_ONLY, INTEGRATION, PROCESSING, ALL }

    public ShopSDKMerchants() {
    }

    /**
     * Get instance for ShopSDKMerchants class.
     * In case there is no current instance, a new one will be created
     * @return ShopSDKMerchants instance
     */
    public static ShopSDKMerchants getInstance() {
        if (instance == null) instance = new ShopSDKMerchants();
        return instance;
    }

    /**
     * Get exact merchant.
     *
     * @param merchantNumber id of the required merchant
     * @param callback will be called after request completion
     * @see ReceivedMerchantCallback
     */
    public void getMerchant(String merchantNumber, final ReceivedMerchantCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetMerchant(merchantNumber);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetMerchant"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject merchantObj = Parser.getJsonObject(response, "d");
                        if (merchantObj != null) {
                            // Parse response
                            Merchant merchant = Parser.parseMerchant(merchantObj, R.string.shop_log);
                            if (callback != null) callback.onResultReceived(true, merchant, "");
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.shop_error_no_merchant));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get categories for exact merchant.
     *
     * @param merchantNumber id of the required merchant
     * @param language set which language should be included
     * @param callback will be called after request completion
     * @see ReceivedCategoriesCallback
     */
    public void getCategories(String merchantNumber, String language, final ReceivedCategoriesCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetCategories(merchantNumber, language);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<ProductCategory>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetMerchantCategories"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<ProductCategory> categories = Parser.parseCategories(response);
                        if (callback != null) callback.onResultReceived(true, categories, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<ProductCategory>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get merchant policies content.
     *
     * @param merchantNumber id of the required merchant
     * @param language language of content to return
     * @param contentName name of content you want to get. Applicable values are About.html,
     *                    Policy.html and Terms.html
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void getContent(String merchantNumber, String language, String contentName, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetContent(merchantNumber, language, contentName);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetMerchantContent"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        String content = Parser.getString(response, "d");
                        if (callback != null) callback.onResultReceived(true, content);
                    }
                }, CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get list of merchants with exact parameters.
     *
     * @param groupId id of the group which merchants should belong to
     * @param status merchant status
     * @see MerchantStatus
     * @param text search text
     * @param callback will be called after request completion
     * @see ReceivedMerchantsCallback
     */
    public void getMerchants(long groupId, MerchantStatus status, String text, final ReceivedMerchantsCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetMerchants(groupId, status, text);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<Merchant>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetMerchants"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<Merchant> merchants = Parser.parseMerchants(response);
                        if (callback != null) callback.onResultReceived(true, merchants, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<Merchant>(), Parser.getErrorText(error));
                    }
                }
        );
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
    private String createUrl(String method) {
        return Coriunder.getServiceUrl() + SERVICE_URL_PART + "/" + method;
    }
}
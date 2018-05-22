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
import com.coriunder.shop.callbacks.ReceivedCartItemsCallback;
import com.coriunder.shop.models.CartItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods to perform requests to the Shop service related to downloads
 */
@SuppressWarnings("unused")
public class ShopSDKDownloads {
    private static ShopSDKDownloads instance;
    public static final String SERVICE_URL_PART = "Shop.svc";

    public ShopSDKDownloads() {
    }

    /**
     * Get instance for ShopSDKDownloads class.
     * In case there is no current instance, a new one will be created
     * @return ShopSDKDownloads instance
     */
    public static ShopSDKDownloads getInstance() {
        if (instance == null) instance = new ShopSDKDownloads();
        return instance;
    }

    /*
    ToDoV2:
    no success parser
     */
    /**
     * Download an item by id (requires authorization)
     *
     * @param itemId id of the item to download
     * @param asPlainData form of downloaded data
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void download(long itemId, boolean asPlainData, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForDownload(itemId, asPlainData);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Download"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response

                    }
                }, CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /*
    ToDoV2:
    no success parser
     */
    /**
     * Download an item by file key (doesn't require authorization)
     *
     * @param fileKey file key of the item to download
     * @param asPlainData form of downloaded data
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void downloadUnauthorized(String fileKey, boolean asPlainData, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForDownloadUnauthorized(fileKey, asPlainData);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("DownloadUnauthorized"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response

                    }
                }, CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get list of downloads.
     *
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results amount per page
     * @param callback will be called after request completion
     * @see ReceivedCartItemsCallback
     */
    public void getDownloads(int page, int pageSize, final ReceivedCartItemsCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForDownloads(page, pageSize);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<CartItem>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetDownloads"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        JSONArray result = Parser.getJsonArray(response, "d");
                        ArrayList<CartItem> cartItems = Parser.parseCartItems(result);
                        if (callback != null) callback.onResultReceived(true, cartItems, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<CartItem>(), Parser.getErrorText(error));
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
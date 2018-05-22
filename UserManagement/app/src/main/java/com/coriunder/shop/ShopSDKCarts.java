package com.coriunder.shop;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.shop.callbacks.ReceivedCartCallback;
import com.coriunder.shop.callbacks.ReceivedCartsCallback;
import com.coriunder.shop.models.Cart;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods to perform requests to the Shop service related to carts
 */
@SuppressWarnings("unused")
public class ShopSDKCarts {
    private static ShopSDKCarts instance;
    public static final String SERVICE_URL_PART = "Shop.svc";

    public ShopSDKCarts() {
    }

    /**
     * Get instance for ShopSDKCarts class.
     * In case there is no current instance, a new one will be created
     * @return ShopSDKCarts instance
     */
    public static ShopSDKCarts getInstance() {
        if (instance == null) instance = new ShopSDKCarts();
        return instance;
    }

    /**
     * Get active carts.
     *
     * @param callback will be called after request completion
     * @see ReceivedCartsCallback
     */
    public void getActiveCarts(final ReceivedCartsCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetActiveCarts"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<Cart> carts = Parser.parseActiveCarts(response);
                        if (callback != null) callback.onResultReceived(true, carts, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<Cart>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get exact cart by its cookie.
     *
     * @param cookie cookie of the required cart
     * @param callback will be called after request completion
     * @see ReceivedCartCallback
     */
    public void getCart(String cookie, final ReceivedCartCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetCart(cookie);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetCart"), json, createCartSuccessListener(callback), createCartErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get cart for exact transaction id.
     *
     * @param transactionId transaction id of the required cart
     * @param callback will be called after request completion
     * @see ReceivedCartCallback
     */
    public void getCartOfTransaction(long transactionId, final ReceivedCartCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetCartOfTransaction(transactionId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetCartOfTransaction"), json, createCartSuccessListener(callback), createCartErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Update cart data.
     *
     * @param cart cart object for the cart you want to update
     * @see Cart
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void updateCart(Cart cart, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForUpdateCart(cart);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SetCart"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        String obj = Parser.getString(response, "d");
                        if (!TextUtils.isEmpty(obj)) {
                            if (callback != null) callback.onResultReceived(true, obj);
                        } else {
                            if (callback != null)
                                callback.onResultReceived(false, getContext().getString(R.string.shop_error_no_cart));
                        }
                    }
                }, CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * @param callback ReceivedCartCallback to be called on request result
     * @return success listener which parses result to Cart object and calls ReceivedCartCallback
     * @see Cart
     * @see ReceivedCartCallback
     */
    public Response.Listener<JSONObject> createCartSuccessListener(final ReceivedCartCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject cartObj = Parser.getJsonObject(response, "d");
                if (cartObj != null) {
                    // Parse response
                    Cart cart = Parser.parseCart(cartObj);
                    if (callback != null) callback.onResultReceived(true, cart, "");
                } else {
                    if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.shop_error_no_cart));
                }
            }
        };
    }

    /**
     * @param callback ReceivedCartCallback to be called on request result
     * @return error listener which parses error and calls ReceivedCartCallback
     * @see ReceivedCartCallback
     */
    public Response.ErrorListener createCartErrorListener(final ReceivedCartCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
            }
        };
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
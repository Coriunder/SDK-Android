package com.coriunder.customer;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.callbacks.ReceivedServiceMultiCallback;
import com.coriunder.base.common.models.ServiceMultiResult;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.customer.models.ShippingAddress;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.customer.callbacks.ReceivedShippingAddressCallback;
import com.coriunder.customer.callbacks.ReceivedShippingAddressesCallback;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods to perform requests to the Customer service related to shipping addresses
 */
@SuppressWarnings("unused")
public class CustomerSDKShippingAddresses {
    private static CustomerSDKShippingAddresses instance;
    public static final String SERVICE_URL_PART = "Customer.svc";

    public CustomerSDKShippingAddresses() {
    }

    /**
     * Get instance for CustomerSDKShippingAddresses class.
     * In case there is no current instance, a new one will be created
     * @return CustomerSDKShippingAddresses instance
     */
    public static CustomerSDKShippingAddresses getInstance() {
        if (instance == null) instance = new CustomerSDKShippingAddresses();
        return instance;
    }

    /**
     * Delete exact shipping address
     *
     * @param addressId id of address which has to be deleted
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void deleteShippingAddress(long addressId, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForDeleteAddress(addressId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("DeleteShippingAddress"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        boolean success = Parser.getBoolean(response, "d");
                        if (callback != null)
                            callback.onResultReceived(success, success ? "" : getContext().getString(R.string.customer_error_no_item));
                    }
                }, CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get exact shipping address by id
     *
     * @param addressId id of the required shipping address
     * @param callback will be called after request completion
     * @see ReceivedShippingAddressCallback
     */
    public void getShippingAddress(long addressId, final ReceivedShippingAddressCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetAddress(addressId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShippingAddress"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonResult = Parser.getJsonObject(response, "d");
                        if (jsonResult != null) {
                            // Parse response
                            ShippingAddress address = Parser.parseShippingAddress(jsonResult);
                            if (callback != null) callback.onResultReceived(true, address, "");
                        } else {
                            if (callback != null)
                                callback.onResultReceived(false, null, getContext().getString(R.string.customer_error_no_item));
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
     * Get all shipping addresses added by current user
     *
     * @param callback will be called after request completion
     * @see ReceivedShippingAddressesCallback
     */
    public void getShippingAddresses(final ReceivedShippingAddressesCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShippingAddresses"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<ShippingAddress> addressesArray = Parser.parseShippingAddresses(response);
                        if (callback != null) callback.onResultReceived(true, addressesArray, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<ShippingAddress>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Add new shipping address for current user
     *
     * @param address new shipping address data
     * @see ShippingAddress
     * @param callback will be called after request completion
     * @see ReceivedServiceMultiCallback
     */
    public void addNewShippingAddress(ShippingAddress address, final ReceivedServiceMultiCallback callback) {
        // Parse ShippingAddress object to JSONObject
        JSONObject addressJson = JsonBuilder.buildAddressJson(address, true);
        // Prepare and perform request
        saveShippingAddress(addressJson, callback);
    }

    /**
     * Update existing shipping address for current user
     *
     * @param address updated shipping address data
     * @see ShippingAddress
     * @param callback will be called after request completion
     * @see ReceivedServiceMultiCallback
     */
    public void updateShippingAddress(ShippingAddress address, final ReceivedServiceMultiCallback callback) {
        // Parse ShippingAddress object to JSONObject
        JSONObject addressJson = JsonBuilder.buildAddressJson(address, false);
        // Prepare and perform request
        saveShippingAddress(addressJson, callback);
    }

    /**
     * Common method to add or update shipping address
     *
     * @param addressJson JSONObject containing shipping address data
     * @param callback will be called after request completion
     * @see ReceivedServiceMultiCallback
     */
    private void saveShippingAddress(JSONObject addressJson, final ReceivedServiceMultiCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForSaveAddress(addressJson);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceMultiResult(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SaveShippingAddress"), json, CommonRequests.createServiceMultiSuccessListener(callback),
                CommonRequests.createServiceMultiErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Add or update several shipping address for current user at once
     *
     * @param shippingAddresses ArrayList containing ShippingAddress objects for shipping addresses
     *                          which have to be updated.
     * @see ShippingAddress
     * @param callback will be called after request completion
     * @see ReceivedServiceMultiCallback
     */
    public void saveShippingAddresses(ArrayList<ShippingAddress> shippingAddresses, final ReceivedServiceMultiCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForSaveAddresses(shippingAddresses);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceMultiResult(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SaveShippingAddresses"), json, CommonRequests.createServiceMultiSuccessListener(callback),
                CommonRequests.createServiceMultiErrorListener(callback));
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
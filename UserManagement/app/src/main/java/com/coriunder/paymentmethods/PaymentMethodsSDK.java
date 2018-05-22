package com.coriunder.paymentmethods;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.callbacks.ReceivedServiceCallback;
import com.coriunder.base.common.models.Address;
import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.paymentmethods.callbacks.ReceivedAddressesCallback;
import com.coriunder.paymentmethods.callbacks.ReceivedPaymentMethodCallback;
import com.coriunder.paymentmethods.callbacks.ReceivedPaymentMethodsCallback;
import com.coriunder.paymentmethods.callbacks.ReceivedPaymentMethodsGroupsCallback;
import com.coriunder.base.common.callbacks.ReceivedServiceMultiCallback;
import com.coriunder.paymentmethods.models.PaymentCardRootGroup;
import com.coriunder.paymentmethods.models.PaymentMethod;
import com.coriunder.base.common.models.ServiceMultiResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class contains methods to perform requests to the PaymentMethods service
 */
@SuppressWarnings("unused")
public class PaymentMethodsSDK {
    private static PaymentMethodsSDK instance;
    public static final String SERVICE_URL_PART = "PaymentMethods.svc";

    public PaymentMethodsSDK() {
    }

    /**
     * Get instance for PaymentMethodsSDK class.
     * In case there is no current instance, a new one will be created
     * @return PaymentMethodsSDK instance
     */
    public static PaymentMethodsSDK getInstance() {
        if (instance == null) instance = new PaymentMethodsSDK();
        return instance;
    }

    /**
     * Delete exact payment method
     *
     * @param cardId id of the payment method which has to be deleted
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void deletePaymentMethod(long cardId, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForDeletePm(cardId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("DeleteStoredPaymentMethod"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        boolean success = Parser.getBoolean(response, "d");
                        if (callback != null)
                            callback.onResultReceived(success, success ? "" : getContext().getString(R.string.pms_error_no_item));
                    }
                }, CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get all billing addresses added by current user
     *
     * @param callback will be called after request completion
     * @see ReceivedAddressesCallback
     */
    public void getBillingAddresses(final ReceivedAddressesCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetBillingAddresses"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<Address> addressesArray = Parser.parseAddresses(response);
                        if (callback != null) callback.onResultReceived(true, addressesArray, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<Address>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get available payment methods' types
     *
     * @param callback will be called after request completion
     * @see ReceivedPaymentMethodsGroupsCallback
     */
    public void getPaymentMethodsTypes(final ReceivedPaymentMethodsGroupsCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetStaticData"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject result = Parser.getJsonObject(response, "d");
                        if (result != null) {
                            // Parse response
                            ArrayList<PaymentCardRootGroup> paymentGroups = Parser.parsePaymentMethodsTypes(result);
                            if (callback != null) callback.onResultReceived(true, paymentGroups, "");
                        } else {
                            if (callback != null) callback.onResultReceived(false,
                                    new ArrayList<PaymentCardRootGroup>(), getContext().getString(R.string.empty_response_error));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<PaymentCardRootGroup>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get exact payment method by id
     *
     * @param cardId id of the required payment method
     * @param callback will be called after request completion
     * @see ReceivedPaymentMethodCallback
     */
    public void getPaymentMethod(long cardId, final ReceivedPaymentMethodCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetPm(cardId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetStoredPaymentMethod"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonResult = Parser.getJsonObject(response, "d");
                        if (jsonResult != null) {
                            // Parse response
                            PaymentMethod paymentMethod = Parser.parsePaymentMethod(jsonResult);
                            if (callback != null) callback.onResultReceived(true, paymentMethod, "");
                        } else {
                            if (callback != null)
                                callback.onResultReceived(false, null, getContext().getString(R.string.pms_error_no_item));
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
     * Get all payment methods added by current user
     *
     * @param callback will be called after request completion
     * @see ReceivedPaymentMethodsCallback
     */
    public void getPaymentMethods(final ReceivedPaymentMethodsCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetStoredPaymentMethods"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<PaymentMethod> paymentArray = Parser.parsePaymentMethods(response);
                        if (callback != null) callback.onResultReceived(true, paymentArray, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<PaymentMethod>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Link payment method
     *
     * @param accountValue1 account value
     * @param dateOfBirth owner's date of birth
     * @param personalNumber owner's personal number
     * @param phoneNumber owner's phone number
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void linkPaymentMethod(String accountValue1, Date dateOfBirth, String personalNumber,
                                  String phoneNumber, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForLinkPm(accountValue1, dateOfBirth, personalNumber, phoneNumber);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("LinkPaymentMethod"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Load payment method
     *
     * @param amount required amount
     * @param currencyIso ISO of the currency which should be loaded
     * @param paymentMethodID id of the payment method
     * @param pinCode current user's pin code
     * @param referenceCode reference code
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void loadPaymentMethod(double amount, String currencyIso, long paymentMethodID, String pinCode,
                                  String referenceCode, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForLoadPm(amount, currencyIso, paymentMethodID, pinCode, referenceCode);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("LoadPaymentMethod"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Request physical payment method
     *
     * @param address1 physical payment method's main address
     * @param address2 physical payment method's secondary address
     * @param city physical payment method's city
     * @param countryIso physical payment method's country ISO
     * @param postalCode physical payment method's postal code
     * @param stateIso physical payment method's state ISO
     * @param providerID physical payment method's provider ID
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void requestPhysicalPaymentMethod(String address1, String address2, String city, String countryIso,
                                             String postalCode, String stateIso, String providerID,
                                             final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForRequestPhysicalPm(address1, address2,
                city, countryIso, postalCode, stateIso, providerID);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("RequestPhysicalPaymentMethod"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Add new payment method for current user
     *
     * @param paymentMethod new payment method data
     * @see PaymentMethod
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void addNewPaymentMethod(PaymentMethod paymentMethod, final ReceivedServiceCallback callback) {
        // Parse PaymentMethod object to JSONObject
        JSONObject methodData = JsonBuilder.buildCardJson(paymentMethod, true);
        // Prepare and perform request
        savePaymentMethod(methodData, callback);
    }

    /**
     * Update existing payment method for current user
     *
     * @param paymentMethod updated payment method data
     * @see PaymentMethod
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void updatePaymentMethod(PaymentMethod paymentMethod, final ReceivedServiceCallback callback) {
        // Parse PaymentMethod object to JSONObject
        JSONObject methodData = JsonBuilder.buildCardJson(paymentMethod, false);
        // Prepare and perform request
        savePaymentMethod(methodData, callback);
    }

    /**
     * Common method to add or update payment method
     *
     * @param methodData JSONObject containing payment method data
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    private void savePaymentMethod(JSONObject methodData, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForSavePm(methodData);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("StorePaymentMethod"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Add or update several payment methods for current user at once
     *
     * @param paymentMethods ArrayList containing PaymentMethod objects for payment methods which
     *                       have to be updated.
     * @see PaymentMethod
     * @param callback will be called after request completion
     * @see ReceivedServiceMultiCallback
     */
    public void savePaymentMethods(ArrayList<PaymentMethod> paymentMethods, final ReceivedServiceMultiCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForSavePms(paymentMethods);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceMultiResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("StorePaymentMethods"), json, CommonRequests.createServiceMultiSuccessListener(callback),
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
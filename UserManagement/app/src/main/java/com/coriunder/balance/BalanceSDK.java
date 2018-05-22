package com.coriunder.balance;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.balance.callbacks.ReceivedBalanceRowListCallback;
import com.coriunder.balance.callbacks.ReceivedBalanceTotalListCallback;
import com.coriunder.balance.callbacks.ReceivedRequestListCallback;
import com.coriunder.balance.models.BalanceRow;
import com.coriunder.balance.models.BalanceTotal;
import com.coriunder.balance.models.Request;
import com.coriunder.base.Coriunder;
import com.coriunder.balance.callbacks.ReceivedRequestCallback;
import com.coriunder.base.common.callbacks.ReceivedServiceCallback;
import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods to perform requests to the Balance service
 */
@SuppressWarnings("unused")
public class BalanceSDK {
    private static BalanceSDK instance;
    public static final String SERVICE_URL_PART = "Balance.svc";

    public BalanceSDK() {
    }

    /**
     * Get instance for BalanceSDK class.
     * In case there is no current instance, a new one will be created
     * @return BalanceSDK instance
     */
    public static BalanceSDK getInstance() {
        if (instance == null) instance = new BalanceSDK();
        return instance;
    }

    /**
     * Get info about exact request
     *
     * @param requestId id of the request which info should be loaded
     * @param callback will be called after request completion
     * @see ReceivedRequestCallback
     */
    public void getRequest(long requestId, final ReceivedRequestCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetRequest(requestId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(com.android.volley.Request.Method.POST,
                createUrl("GetRequest"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject result = Parser.getJsonObject(response, "d");
                        if (result != null) {
                            // Parse response
                            Request item = Parser.parseRequest(result);
                            if (callback != null) callback.onResultReceived(true, item, "");
                        } else  {
                            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.balance_no_item));
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
     * Get requests' history
     *
     * @param currencyIso ISO code for currency which requests should be returned for
     * @param paymentMethodId id of the payment method
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results amount per page
     * @param callback will be called after request completion
     * @see ReceivedRequestListCallback
     */
    public void getRequests(String currencyIso, long paymentMethodId, int page, int pageSize,
                                       final ReceivedRequestListCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetRequests(currencyIso, paymentMethodId, page, pageSize);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<Request>(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(com.android.volley.Request.Method.POST,
                createUrl("GetRequests"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<Request> arrayList = Parser.parseRequests(response);
                        if (callback != null) callback.onResultReceived(true, arrayList, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false, new ArrayList<Request>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get transactions' history
     *
     * @param currencyIso ISO code for currency which transactions should be returned for
     * @param paymentMethodId id of the payment method
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results amount per page
     * @param callback will be called after request completion
     * @see ReceivedBalanceRowListCallback
     */
    public void getTransactionsHistory(String currencyIso, long paymentMethodId, int page, int pageSize,
                                                  final ReceivedBalanceRowListCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetRows(currencyIso, paymentMethodId, page, pageSize);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<BalanceRow>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(com.android.volley.Request.Method.POST,
                createUrl("GetRows"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<BalanceRow> arrayList = Parser.parseRows(response);
                        if (callback != null) callback.onResultReceived(true, arrayList, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<BalanceRow>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get list of user balances
     *
     * @param currencyIso ISO code for currency which balances should be returned for
     * @param callback will be called after request completion
     * @see ReceivedBalanceTotalListCallback
     */
    public void getTotal(String currencyIso, final ReceivedBalanceTotalListCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetTotal(currencyIso);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<BalanceTotal>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(com.android.volley.Request.Method.POST,
                createUrl("GetTotal"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<BalanceTotal> arrayList = Parser.parseTotal(response);
                        if (callback != null) callback.onResultReceived(true, arrayList, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<BalanceTotal>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Reply transaction request
     *
     * @param requestId id of the request which has to approved or declined
     * @param approve defines whether request has to approved or declined
     * @param pinCode current user pin code
     * @param text comment for transaction
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void replyRequest(long requestId, boolean approve, String pinCode, String text,
                                    final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForReplyRequest(requestId, approve, pinCode, text);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(com.android.volley.Request.Method.POST,
                createUrl("ReplyRequest"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Send transaction request
     *
     * @param userId id of the user who should receive the request
     * @param amount amount which has to be requested
     * @param currencyIso ISO code of the currency for the request
     * @param text comment for request
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void requestAmount(String userId, double amount, String currencyIso, String text,
                                     final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForRequestAmount(userId, amount, currencyIso, text);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(com.android.volley.Request.Method.POST,
                createUrl("RequestAmount"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Transfer amount to another user
     *
     * @param userId id of the user who should receive the amount
     * @param amount amount which has to be sent
     * @param currencyIso ISO code of the currency for the transfer
     * @param pinCode current user pin code
     * @param text comment for transfer
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void transferAmount(String userId, double amount, String currencyIso, String pinCode,
                                      String text, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForTransferAmount(userId, amount, currencyIso, pinCode, text);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(com.android.volley.Request.Method.POST,
                createUrl("TransferAmount"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
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
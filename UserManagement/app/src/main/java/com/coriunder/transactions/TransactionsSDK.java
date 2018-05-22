package com.coriunder.transactions;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedServiceCallback;
import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.transactions.callbacks.ReceivedLookupCallback;
import com.coriunder.transactions.callbacks.ReceivedSearchCallback;
import com.coriunder.transactions.models.Transaction;
import com.coriunder.transactions.callbacks.ReceivedTransactionCallback;
import com.coriunder.transactions.models.TransactionLookup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class contains methods to perform requests to the Transactions service
 */
@SuppressWarnings("unused")
public class TransactionsSDK {
    private static TransactionsSDK instance;
    public static final String SERVICE_URL_PART = "Transactions.svc";
    public enum TransactionStatus { TYPE_1, TYPE_2, TYPE_3, ALL }

    public TransactionsSDK() {
    }

    /**
     * Get instance for TransactionsSDK class.
     * In case there is no current instance, a new one will be created
     * @return TransactionsSDK instance
     */
    public static TransactionsSDK getInstance() {
        if (instance == null) instance = new TransactionsSDK();
        return instance;
    }

    /**
     * Get full info about exact transaction
     *
     * @param transactionId id of the transaction which info has to be loaded
     * @param callback will be called after request completion
     * @see ReceivedTransactionCallback
     */
    public void getTransaction(long transactionId, final ReceivedTransactionCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGet(transactionId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Get"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject object = Parser.getJsonObject(response, "d");
                        if (object != null) {
                            // Parse response
                            Transaction transaction = Parser.parseTransaction(object);
                            if (callback != null) callback.onResultReceived(true, transaction, "");
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.transactions_no_item));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                });
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Lookup transaction. Gives short info about transactions which match search terms
     *
     * @param transDate date of transaction
     * @param amount amount of transaction
     * @param last4cc last 4 symbols for the payment method used to pay for transaction
     * @param callback will be called after request completion
     * @see ReceivedLookupCallback
     */
    public void lookupTransaction(Date transDate, double amount, String last4cc, final ReceivedLookupCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForLookup(transDate, amount, last4cc);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<TransactionLookup>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Lookup"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<TransactionLookup> arrayList = Parser.parseTransactionLookup(response);
                        if (callback != null) callback.onResultReceived(true, arrayList, "");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<TransactionLookup>(), Parser.getErrorText(error));
                    }
                });
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Process exact cart. Should be called separately for each cart.
     *
     * @param pin current user's pin code
     * @param addressId id of the shipping address, which has to be used to process the cart
     * @param cartCookie cookie of the cart which has to be processed
     * @param paymentMethodId id of the payment method, which has to be used to process the cart
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void processCart(String pin, long addressId, String cartCookie, long paymentMethodId,
                            final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForProcess(pin, addressId, cartCookie, paymentMethodId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("ProcessTransaction"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Search for transactions. Gives full info about transactions which match search terms
     *
     * @param amountFrom min transaction amount
     * @param amountTo max transaction amount
     * @param currencyIso currency of transaction
     * @param dateFrom min transaction date
     * @param dateTo max transaction date
     * @param idFrom min transaction id
     * @param idTo max transaction id
     * @param loadMerchant sets whether merchant info needs to be included in the response
     * @param loadPayer sets whether payer info needs to be included in the response
     * @param loadPayment sets whether payment info needs to be included in the response
     * @param transType transaction type
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results' amount per page
     * @param callback will be called after request completion
     * @see ReceivedSearchCallback
     */
    public void searchTransactions(double amountFrom, double amountTo, String currencyIso, Date dateFrom, Date dateTo,
                                   long idFrom, long idTo, boolean loadMerchant, boolean loadPayer, boolean loadPayment,
                                   TransactionStatus transType, int page, int pageSize, final ReceivedSearchCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForSearch(amountFrom, amountTo, currencyIso, dateFrom,
                dateTo, idFrom, idTo, loadMerchant, loadPayer, loadPayment, transType, page, pageSize);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<Transaction>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Search"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<Transaction> array = Parser.parseSearch(response);
                        if (callback != null) callback.onResultReceived(true, array, "");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<Transaction>(), Parser.getErrorText(error));
                    }
                });
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
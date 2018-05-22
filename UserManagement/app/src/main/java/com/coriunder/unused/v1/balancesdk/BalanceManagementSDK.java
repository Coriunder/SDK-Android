package com.coriunder.unused.v1.balancesdk;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coriunder.unused.v1.balancesdk.callbacks.ReceivedRequestItemCallback;
import com.coriunder.unused.v1.balancesdk.callbacks.ReceivedStringCallback;
import com.coriunder.unused.v1.balancesdk.models.BalanceItem;
import com.coriunder.unused.v1.balancesdk.models.RequestItem;
import com.coriunder.unused.v1.basesdk.BaseParser;
import com.coriunder.unused.v1.basesdk.CustomHeadersJsonObjectRequest;
import com.coriunder.unused.v1.basesdk.UserSession;
import com.coriunder.unused.v1.basesdk.callbacks.ReceivedBasicCallback;
import com.coriunder.unused.v1.basesdk.callbacks.ReceivedListCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 1 on 22.02.2016.
 */
public class BalanceManagementSDK {

/************************************ REQUESTS SECTION START ************************************/

    /**
     * Get total user balance for currency.
     *
     * @param currencyIso - ISO code for currency which the balance should be returned for, can't be empty or null
     * @param includePending - defines whether any pending amounts should be included (true) or only real balance
     *   should be returned (false)
     * @param callback - callback.onResultReceived(boolean isSuccess, String result, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); result - user
     *   balance for currency (0 in case request was NOT successful); message - error description if any (null in
     *   case request was successful)
     */
    public static void getUserBalanceForCurrency(Context context, String currencyIso, boolean includePending,
                                                 final ReceivedStringCallback callback) {
        if (TextUtils.isEmpty(currencyIso)) {
            if (callback != null) callback.onResultReceived(false, "0", "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("currencyIsoCode", currencyIso);
            json.put("includePending", includePending);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "0", "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetTotal"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray) Parser.getValue(response, "d", BaseParser.ValueType.JSONARRAY);
                        String balance = "0";
                        try {
                            if (result.length() != 0)
                                balance = (String) Parser.getValue((JSONObject)result.get(0), "Value", BaseParser.ValueType.STRING);
                            if (callback != null) callback.onResultReceived(true, balance, null);
                        } catch (Exception e) {
                            if (callback != null) callback.onResultReceived(true, balance, null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, "0", Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Get transactions history for currency.
     *
     * @param currencyIso - ISO code for currency which transactions should be returned for, can't be empty or null
     * @param page - number of the page with results, minimum value is 0
     * @param pageSize - results amount per page
     * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); result - arrayList
     *   containing BalanceRow objects (null in case request was NOT successful); message - error description if any (null in
     *   case request was successful)
     */
    public static void getTransactionsHistoryForCurrency(Context context, String currencyIso, int page, int pageSize,
                                                         final ReceivedListCallback callback) {

        if (TextUtils.isEmpty(currencyIso)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            JSONObject params = new JSONObject();
            params.put("Page", page);
            params.put("PageSize", pageSize);
            params.put("CurrencyIso", currencyIso);
            json.put("filters", params);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetRows"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray) Parser.getValue(response, "d", BaseParser.ValueType.JSONARRAY);
                        ArrayList<Object> arrayList = new ArrayList<>();
                        if (result != null) {
                            for (int i=0; i<result.length(); i++) {
                                try {
                                    JSONObject requestObj = result.getJSONObject(i);
                                    BalanceItem item = new BalanceItem();
                                    item.setCurrencyIso((String) Parser.getValue(requestObj, "CurrencyIso", Parser.ValueType.STRING));
                                    item.setAmount((String) Parser.getValue(requestObj, "Amount", Parser.ValueType.STRING));
                                    item.setTotal((String) Parser.getValue(requestObj, "Total", Parser.ValueType.STRING));
                                    item.setText((String) Parser.getValue(requestObj, "Text", Parser.ValueType.STRING));
                                    item.setSourceId((String) Parser.getValue(requestObj, "SourceID", Parser.ValueType.STRING));
                                    item.setDate(Parser.getReadableDate(requestObj, "InsertDate"));
                                    item.setSourceType((String) Parser.getValue(requestObj, "SourceType", Parser.ValueType.STRING));
                                    item.setBalanceRowId((String) Parser.getValue(requestObj, "ID", Parser.ValueType.STRING));
                                    item.setIsPending((boolean) Parser.getValue(requestObj, "IsPending", Parser.ValueType.BOOLEAN));
                                    arrayList.add(item);
                                } catch (Exception e) {
                                    Log.d("BalanceSDK", "Error when parsing balance item with index " + i);
                                }
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, arrayList, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Get exact request info.
     *
     * @param requestId - id of request which info should be loaded
     * @param callback - callback.onResultReceived(boolean isSuccess, Request item, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); item - Request
     *   object (null in case request was NOT successful); message - error description if any (null in case request was
     *   successful)
     */
    public static void getRequest(Context context, long requestId, final ReceivedRequestItemCallback callback) {

        JSONObject json = new JSONObject();
        try {
            json.put("requestId", requestId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetRequest"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject result = (JSONObject) Parser.getValue(response, "d", BaseParser.ValueType.JSONOBJECT);
                        if (result != null) {
                            RequestItem item = Parser.parseRequest(result);
                            if (callback != null) callback.onResultReceived(true, item, null);
                        } else  {
                            if (callback != null) callback.onResultReceived(false, null, "No such item");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Get requests history for currency.
     *
     * @param currencyIso - ISO code for currency which requests should be returned for, can't be empty or null
     * @param page - number of the page with results, minimum value is 0
     * @param pageSize - results amount per page
     * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); result - arrayList
     *   containing Request objects (null in case request was NOT successful); message - error description if any (null in
     *   case request was successful)
     */
    public static void getRequestsForCurrency(Context context, String currencyIso, int page, int pageSize,
                                              final ReceivedListCallback callback) {

        if (TextUtils.isEmpty(currencyIso)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            JSONObject params = new JSONObject();
            params.put("Page", page);
            params.put("PageSize", pageSize);
            params.put("CurrencyIso", currencyIso);
            json.put("filters", params);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetRequests"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray) Parser.getValue(response, "d", BaseParser.ValueType.JSONARRAY);
                        ArrayList<Object> arrayList = new ArrayList<>();
                        if (result != null) {
                            for (int i=0; i<result.length(); i++) {
                                try {
                                    JSONObject requestObj = result.getJSONObject(i);
                                    RequestItem item = Parser.parseRequest(requestObj);
                                    arrayList.add(item);
                                } catch (Exception e) {
                                    Log.d("BalanceSDK", "Error when parsing request with index " + i);
                                }
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, arrayList, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Reply transaction request
     *
     * @param requestId - id of the request which has to approved or declined
     * @param approve - defines whether request has to approved (value true) or declined (value false)
     * @param pinCode - current user pincode, can't be empty or null
     * @param text - comment for transaction
     * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); message -
     *   error description if any (null in case request was successful)
     */
    public static void replyRequest(Context context, long requestId, boolean approve, String pinCode, String text,
                                    final ReceivedBasicCallback callback) {

        if (TextUtils.isEmpty(pinCode)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("requestId", requestId);
            json.put("approve", approve);
            json.put("text", text);
            json.put("pinCode", pinCode);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("ReplyRequest"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Send transaction request
     *
     * @param userId - id of the user who should receive the request, can't be empty or null
     * @param amount - amount which has to be requested
     * @param currencyIso - ISO code for currency in which request should be made, can't be empty or null
     * @param text - comment for request
     * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); message -
     *   error description if any (null in case request was successful)
     */
    public static void requestAmount(Context context, String userId, float amount, String currencyIso, String text,
                                     final ReceivedBasicCallback callback) {

        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(currencyIso)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("destAcocuntId", userId);
            json.put("amount", amount);
            json.put("currencyIso", currencyIso);
            json.put("text", text);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("RequestAmount"), json, createBasicSuccessListener(callback),createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Transfer amount to another user
     *
     * @param userId - id of the user who should receive the amount, can't be empty or null
     * @param amount - amount which has to be sent
     * @param currencyIso - ISO code for currency in which transfer should be made, can't be empty or null
     * @param text - comment for transfer
     * @param pinCode - current user pincode, can't be empty or null
     * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); message -
     *   error description if any (null in case request was successful)
     */
    public static void transferAmount(Context context, String userId, float amount, String currencyIso, String text,
                                      String pinCode, final ReceivedBasicCallback callback) {

        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(currencyIso) || TextUtils.isEmpty(pinCode)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("destAcocuntId", userId);
            json.put("amount", amount);
            json.put("currencyIso", currencyIso);
            json.put("pinCode", pinCode);
            json.put("text", text);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("TransferAmount"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void loadBalanceFromCard(Context context, String cardNumber, int expMonth, int expYear, String cvv,
                                           String idNumber, long storedPaymentMethodId, int installments, String currencyIso,
                                           float amount, final ReceivedBasicCallback callback) {

        if (TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(cvv) ||
                TextUtils.isEmpty(idNumber) || TextUtils.isEmpty(currencyIso)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, expYear);
        cal.set(Calendar.MONTH, expMonth);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String expDate = formatter.format(cal.getTime());

        JSONObject json = new JSONObject();
        try {
            JSONObject params = new JSONObject();
            params.put("Amount", amount);
            params.put("CurrencyIso", currencyIso);
            params.put("Installments", installments);
            params.put("StoredPaymentMethodID", storedPaymentMethodId);
            params.put("CardNumber", cardNumber);
            params.put("CardExpDate", expDate);
            params.put("Cvv", cvv);
            params.put("IdNumber", idNumber);
            json.put("data", params);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("LoadBalance"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
        /**
         * explanations
         * isSuccess/Message
         * parse response
         */
    }

/************************************* REQUESTS SECTION END *************************************/

    private static String createUrl(String method) {
        return Constants.SERVICE_URL+method;
    }

    private static Response.Listener<JSONObject> createBasicSuccessListener(final ReceivedBasicCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (Parser.isRequestSuccessful(response)) {
                    if (callback != null) callback.onResultReceived(true, null);
                } else {
                    if (callback != null) callback.onResultReceived(false, Parser.getMessage(response));
                }
            }
        };
    }

    private static Response.ErrorListener createBasicErrorListener(final ReceivedBasicCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (callback != null) callback.onResultReceived(false, Parser.getErrorText(error));
            }
        };
    }
}
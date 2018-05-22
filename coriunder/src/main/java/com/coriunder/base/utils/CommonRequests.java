package com.coriunder.base.utils;

import android.util.Base64;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.callbacks.ReceivedServiceCallback;
import com.coriunder.base.common.callbacks.ReceivedServiceMultiCallback;
import com.coriunder.base.common.models.ServiceMultiResult;
import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * This class allows to create basic service success and error listeners
 */
public class CommonRequests {

    /**
     * Configuring common request headers and retry policy before performing request
     *
     * @param postRequest request to perform
     */
    public static void performRequest(CustomHeadersJsonObjectRequest postRequest) {

        // Add signature header. Don't forget to check signature!
        String base64Hash = createSignature(postRequest.getBody());
        postRequest.addHeader("Signature", "bytes-SHA256, "+base64Hash);

        // Add content type header
        if (postRequest.getBody() == null)
            postRequest.addHeader("Content-Type", "application/json");

        // Add credentials token header
        if (Coriunder.isUserLoggedIn())
            postRequest.addHeader(Coriunder.getCredentialsHeader(), Coriunder.getCredentialsToken());

        // Add app token header
        postRequest.addHeader("applicationToken", Coriunder.getAppToken());

        // Set timeouts
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULTIPLIER));
        // Run request
        Coriunder.getRequestQueue().add(postRequest);
    }

    public static String createSignature(byte[] data) {
        String base64Result = null;
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA256");
            if (data != null) digest.update(data);              // add data to hash if any
            digest.update(Constants.SHA256_SALT.getBytes());    // add salt to hash
            byte[] hashResult = digest.digest();
            base64Result = Base64.encodeToString(hashResult, Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e("exception while hashing", "Cannot calculate signature");
        }
        return base64Result;
    }

    /**
     * @param callback ReceivedServiceCallback to be called on request result
     * @return success listener which parses result to a ServiceResult model and calls ReceivedServiceCallback
     * @see ReceivedServiceCallback
     * @see ServiceResult
     */
    public static Response.Listener<JSONObject> createServiceSuccessListener(final ReceivedServiceCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    // Parse response to ServiceResult object
                    JSONObject jsonResult = BaseParser.getJsonObject(response, "d");
                    ServiceResult result = BaseParser.parseServiceResult(jsonResult);
                    if (callback != null)
                        callback.onResultReceived(result.isSuccess(), result, result.getMessage());
                } else {
                    if (callback != null) callback.onResultReceived(false, new ServiceResult(),
                            Coriunder.getContext().getString(R.string.empty_response_error));
                }
            }
        };
    }

    /**
     * @param callback ReceivedServiceCallback to be called on request result
     * @return error listener which parses error and calls ReceivedServiceCallback
     * @see ReceivedServiceCallback
     */
    public static Response.ErrorListener createServiceErrorListener(final ReceivedServiceCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.onResultReceived(false, new ServiceResult(), BaseParser.getErrorText(error));
            }
        };
    }

    /**
     * @param callback ReceivedServiceMultiCallback to be called on request result
     * @return success listener which parses result to a ServiceMultiResult model and calls ReceivedServiceMultiCallback
     * @see ReceivedServiceMultiCallback
     * @see ServiceMultiResult
     */
    public static Response.Listener<JSONObject> createServiceMultiSuccessListener(final ReceivedServiceMultiCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ServiceMultiResult result = new ServiceMultiResult();
                if (response != null) {
                    // Parse response to ServiceMultiResult object
                    JSONObject jsonResult = BaseParser.getJsonObject(response, "d");
                    result.setCode(BaseParser.getInt(jsonResult, "Code"));
                    result.setSuccess(BaseParser.getBoolean(jsonResult, "IsSuccess"));
                    result.setKey(BaseParser.getString(jsonResult, "Key"));
                    result.setMessage(BaseParser.getString(jsonResult, "Message"));
                    result.setNumber(BaseParser.getString(jsonResult, "Number"));
                    result.setRecordNumber(BaseParser.getLong(jsonResult, "RecordNumber"));

                    ArrayList<String> refNumbers = new ArrayList<>();
                    JSONArray refNumbersArray = BaseParser.getJsonArray(jsonResult, "RefNumbers");
                    if (refNumbersArray != null) {
                        for (int i = 0; i < refNumbersArray.length(); i++){
                            try {
                                refNumbers.add(refNumbersArray.get(i).toString());
                            } catch (Exception e) {
                                Log.e(Coriunder.getContext().getString(R.string.sdk_log),
                                        Coriunder.getContext().getString(R.string.sdk_error_refnumbers)+i);
                            }
                        }
                    }
                    result.setRefNumbers(refNumbers);

                    if (callback != null)
                        callback.onResultReceived(result.isSuccess(), result, result.getMessage());
                } else {
                    if (callback != null) callback.onResultReceived(false, result,
                            Coriunder.getContext().getString(R.string.empty_response_error));
                }
            }
        };
    }

    /**
     * @param callback ReceivedServiceMultiCallback to be called on request result
     * @return error listener which parses error and calls ReceivedServiceMultiCallback
     * @see ReceivedServiceMultiCallback
     */
    public static Response.ErrorListener createServiceMultiErrorListener(final ReceivedServiceMultiCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.onResultReceived(false, new ServiceMultiResult(), BaseParser.getErrorText(error));
            }
        };
    }

    /**
     * @param callback ReceivedBasicCallback to be called on request result
     * @return success listener which calls ReceivedBasicCallback
     * @see ReceivedBasicCallback
     */
    public static Response.Listener<JSONObject> createBasicSuccessListener(final ReceivedBasicCallback callback)  {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (callback != null) callback.onResultReceived(true, "");
            }
        };
    }

    /**
     * @param callback ReceivedBasicCallback to be called on request result
     * @return error listener which calls ReceivedBasicCallback
     * @see ReceivedBasicCallback
     */
    public static Response.ErrorListener createBasicErrorListener(final ReceivedBasicCallback callback)  {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null) callback.onResultReceived(false, BaseParser.getErrorText(error));
            }
        };
    }
}
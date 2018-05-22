package com.coriunder.base.common.requests;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.coriunder.base.Coriunder;
import com.coriunder.base.UserSession;
import com.coriunder.base.utils.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Json request which allows to add custom headers
 */
public class CustomHeadersJsonObjectRequest extends JsonObjectRequest {

    /**
     * Constructor
     */
    public CustomHeadersJsonObjectRequest(int method, String url, JSONObject jsonRequest,
                                          Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        if (Constants.ENABLE_LOGS && jsonRequest != null) Log.d("RequestLog", jsonRequest.toString());
    }

    private Map<String, String> headers = new HashMap<>();

    /**
     * Method to add custom headers
     *
     * @param headerField header title
     * @param headerValue header value
     */
    public void addHeader(String headerField, String headerValue) {
        headers.put(headerField, headerValue);
    }

    /**
     * Override default getHeaders() method to return our custom headers
     *
     * @return custom headers' list
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    /**
     * Overriding this method to Log all successful responses
     */
    @Override
    public Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Response<JSONObject> parsedResponse = super.parseNetworkResponse(response);
        // log response in case logging is enabled
        if (Constants.ENABLE_LOGS && parsedResponse.isSuccess() && parsedResponse.result != null)
            Log.d("ResponseLogSuccess",parsedResponse.result.toString());
/*
        // check signature if needed
        String signature = response.headers.get("Signature");
        if (!TextUtils.isEmpty(signature)) {
            //String value = BaseParser.getJsonObject(parsedResponse.result, "d").toString();
            String localSignature = CommonRequests.createSignature(response.data);
            boolean a = localSignature.equals(signature);
        }
*/
        return parsedResponse;
    }

    /**
     * Overriding this method to catch 403 errors
     */
    @Override
    public void deliverError(VolleyError error) {
        if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 403) {
            // Session expired, stop it
            UserSession.getInstance().resetSession();
            // Clear auto-login data
            SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(Coriunder.getContext()).edit();
            prefs.putString(Constants.SP_EMAIL, "");
            prefs.putString(Constants.SP_PASS, "");
            prefs.putString(Constants.SP_CRED_TOKEN, "");
            prefs.putString(Constants.SP_HEADER, "");
            prefs.apply();
        }
        super.deliverError(error);
    }
}
package com.coriunder.unused.v1.basesdk;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1 on 15.02.2016.
 */
public class CustomHeadersJsonObjectRequest extends JsonObjectRequest {

    public CustomHeadersJsonObjectRequest(int method, String url, JSONObject jsonRequest,
                                          Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    private Map<String, String> headers = new HashMap<>();

    public void addHeader(String headerField, String headerValue) {
        headers.put(headerField, headerValue);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }
}
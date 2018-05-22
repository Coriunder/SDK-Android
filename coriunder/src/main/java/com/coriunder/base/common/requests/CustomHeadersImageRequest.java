package com.coriunder.base.common.requests;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Image request which allows to add custom headers
 */
public class CustomHeadersImageRequest extends ImageRequest {

    /**
     * Constructor
     */
    public CustomHeadersImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight,
                                     ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener)  {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
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
}
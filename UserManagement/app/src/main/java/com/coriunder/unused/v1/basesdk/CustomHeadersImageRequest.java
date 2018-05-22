package com.coriunder.unused.v1.basesdk;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1 on 15.02.2016.
 */
public class CustomHeadersImageRequest extends ImageRequest {

    public CustomHeadersImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight,
                                     ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener)  {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
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
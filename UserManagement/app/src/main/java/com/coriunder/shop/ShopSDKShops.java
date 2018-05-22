package com.coriunder.shop;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.shop.callbacks.ReceivedShopCallback;
import com.coriunder.shop.callbacks.ReceivedShopIdCallback;
import com.coriunder.shop.callbacks.ReceivedShopsCallback;
import com.coriunder.shop.models.Shop;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods to perform requests to the Shop service related to shops
 */
@SuppressWarnings("unused")
public class ShopSDKShops {
    private static ShopSDKShops instance;
    public static final String SERVICE_URL_PART = "Shop.svc";

    public ShopSDKShops() {
    }

    /**
     * Get instance for ShopSDKShops class.
     * In case there is no current instance, a new one will be created
     * @return ShopSDKShops instance
     */
    public static ShopSDKShops getInstance() {
        if (instance == null) instance = new ShopSDKShops();
        return instance;
    }

    /**
     * Get exact shop
     *
     * @param merchantNumber id of the merchant which shop has to be received
     * @param shopId id of the required shop
     * @param callback will be called after request completion
     * @see ReceivedShopCallback
     */
    public void getShop(String merchantNumber, long shopId, final ReceivedShopCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetShop(merchantNumber, shopId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShop"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject result = Parser.getJsonObject(response, "d");
                        if (result != null) {
                            // Parse response
                            Shop shop = Parser.parseShop(result);
                            if (callback != null) callback.onResultReceived(true, shop, "");
                        } else {
                            if (callback != null) callback.onResultReceived(false, null,
                                    getContext().getString(R.string.shop_error_no_shop));
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
     * Get shop for a sub-domain
     *
     * @param subDomainName sub-domain which the shop belongs to
     * @param callback will be called after request completion
     * @see ReceivedShopIdCallback
     */
    public void getShopIds(String subDomainName, final ReceivedShopIdCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetShopIds(subDomainName);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, "", 0, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShopIds"), json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject result = Parser.getJsonObject(response, "d");
                        if (result != null) {
                            // Parse response
                            String merchant = Parser.getString(result, "MerchantNumber");
                            long shopId = Parser.getLong(result, "ShopId");
                            if (callback != null) callback.onResultReceived(true, merchant, shopId, "");
                        } else {
                            if (callback != null) callback.onResultReceived(false, "", 0, getContext().getString(R.string.shop_error_no_shop));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false, "", 0, Parser.getErrorText(error));
                    }
                });
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get list of shops matching specified params
     *
     * @param merchantNumber id of the merchant which shops have to be received
     * @param culture culture of required shops
     * @param callback will be called after request completion
     * @see ReceivedShopsCallback
     */
    public void getShops(String merchantNumber, String culture, final ReceivedShopsCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetShops(merchantNumber, culture);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<Shop>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShops"), json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<Shop> shops = Parser.parseShops(response);
                        if (callback != null) callback.onResultReceived(true, shops, "");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<Shop>(), Parser.getErrorText(error));
                    }
                });
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get list of shops at the specified location
     *
     * @param regions array of regions to search in
     * @param countries array of countries to search in
     * @param includeGlobalShops set whether you need global region shops to be included or not
     * @param culture culture of required shops
     * @param callback will be called after request completion
     * @see ReceivedShopsCallback
     */
    public void getShopsByLocation(String[] regions, String[] countries, boolean includeGlobalShops,
                                   String culture, final ReceivedShopsCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetShopsByLocation(regions, countries, includeGlobalShops, culture);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<Shop>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShopsByLocation"), json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<Shop> shops = Parser.parseShops(response);
                        if (callback != null) callback.onResultReceived(true, shops, "");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<Shop>(), Parser.getErrorText(error));
                    }
                });
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Set urls and image sizes for the session
     *
     * @param declineUrl set decline URL
     * @param imageHeight desired image height
     * @param imageWidth desired image width
     * @param pendingUrl set pending URL
     * @param successUrl set success URL
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void setSession(String declineUrl, long imageHeight, long imageWidth, String pendingUrl,
                           String successUrl, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForSetSession(declineUrl, imageHeight, imageWidth, pendingUrl, successUrl);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SetSession"), json, CommonRequests.createBasicSuccessListener(callback),
                CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get context
     * @return context
     */
    private Context getContext() {
        return Coriunder.getContext();
    }

    /**
     * Create URL for request
     * @param method service method name
     * @return service URL
     */
    private String createUrl(String method) {
        return Coriunder.getServiceUrl() + SERVICE_URL_PART + "/" + method;
    }
}
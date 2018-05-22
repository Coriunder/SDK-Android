package com.coriunder.shop;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.shop.callbacks.ReceivedProductCallback;
import com.coriunder.shop.callbacks.ReceivedProductsCallback;
import com.coriunder.shop.models.Product;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods to perform requests to the Shop service related to products
 */
@SuppressWarnings("unused")
public class ShopSDKProducts {
    private static ShopSDKProducts instance;
    public static final String SERVICE_URL_PART = "Shop.svc";

    public ShopSDKProducts() {
    }

    /**
     * Get instance for ShopSDKProducts class.
     * In case there is no current instance, a new one will be created
     * @return ShopSDKProducts instance
     */
    public static ShopSDKProducts getInstance() {
        if (instance == null) instance = new ShopSDKProducts();
        return instance;
    }

    /**
     * Get list of products of exact category.
     *
     * @param category id of the category to load products from
     * @param shopId id of the shop to load products from
     * @param language set language to load products with
     * @param callback will be called after request completion
     * @see ReceivedProductsCallback
     */
    public void getProductsForCategory(long category, long shopId, String language, final ReceivedProductsCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForCategorisedProducts(category, shopId, language);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<Product>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetCategorisedProducts"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<Product> products = Parser.parseProducts(response);
                        if (callback != null) callback.onResultReceived(true, products, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<Product>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get exact product.
     *
     * @param merchantNumber id of the merchant which product belongs to
     * @param productId id of the required product
     * @param language language which product data should be returned with
     * @param callback will be called after request completion
     * @see ReceivedProductCallback
     */
    public void getProduct(String merchantNumber, long productId, String language, final ReceivedProductCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetProduct(merchantNumber, productId, language);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetProduct"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject result = Parser.getJsonObject(response, "d");
                        if (result != null) {
                            // Parse response
                            Product product = Parser.parseProduct(result);
                            if (callback != null) callback.onResultReceived(true, product, "");
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.shop_error_no_product));
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
     * Get list of products with exact parameters.
     *
     * @param categories array of product categories to search in
     * @param countries array of countries to search in
     * @param includeGlobalRegion set whether you need global region products to be included or not
     * @param language set language to get products with
     * @param merchantGroups array of merchant groups to search in
     * @param merchantId specify merchant id to get exact merchant's products
     * @param name name of the product
     * @param promoOnly get only promo products
     * @param regions array of regions to search in
     * @param shopId id of the shop to search products in
     * @param tags tags to search product with
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results amount per page
     * @param callback will be called after request completion
     * @see ReceivedProductsCallback
     */
    public void getProducts(int[] categories, String[] countries, boolean includeGlobalRegion, String language,
                            int[] merchantGroups, String merchantId, String name, boolean promoOnly, String[] regions,
                            long shopId, String tags, int page, int pageSize, final ReceivedProductsCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetProducts(categories, countries, includeGlobalRegion, language,
                merchantGroups, merchantId, name, promoOnly, regions, shopId, tags, page, pageSize);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<Product>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetProducts"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<Product> products = Parser.parseProducts(response);
                        if (callback != null) callback.onResultReceived(true, products, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<Product>(), Parser.getErrorText(error));
                    }
                }
        );
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
package com.coriunder.unused.v1.ecommercesdk;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coriunder.unused.v1.basesdk.BaseParser;
import com.coriunder.unused.v1.basesdk.CustomHeadersJsonObjectRequest;
import com.coriunder.unused.v1.basesdk.UserSession;
import com.coriunder.unused.v1.basesdk.callbacks.ReceivedBasicCallback;
import com.coriunder.unused.v1.basesdk.callbacks.ReceivedListCallback;
import com.coriunder.unused.v1.ecommercesdk.callbacks.ReceivedProductCallback;
import com.coriunder.unused.v1.ecommercesdk.callbacks.ReceivedCartCallback;
import com.coriunder.unused.v1.ecommercesdk.callbacks.ReceivedLongCallback;
import com.coriunder.unused.v1.ecommercesdk.callbacks.ReceivedMerchantCallback;
import com.coriunder.unused.v1.ecommercesdk.models.Cart;
import com.coriunder.unused.v1.ecommercesdk.models.CartItem;
import com.coriunder.unused.v1.ecommercesdk.models.Merchant;
import com.coriunder.unused.v1.ecommercesdk.models.Product;
import com.coriunder.unused.v1.ecommercesdk.models.ProductCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1 on 22.02.2016.
 */
public class EcommerceManagementSDK {
    public enum MerchantStatus { ARCHIVED, NEW, BLOCKED, CLOSED, LOGINONLY, INTEGRATION, PROCESSING, ALL }

/************************************ PRODUCTS SECTION START ************************************/

    /**
     * Get product by id.
     *
     * @param productId - id of required product
     * @param callback - callback.onResultReceived(boolean isSuccess, Product product, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); product -
     *   Product object (null in case request was NOT successful); message - error description if any (null in case
     *   request was successful)
     */
    public static void getProduct(Context context, long productId, final ReceivedProductCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", "");
            json.put("itemId", productId);
            json.put("language", "Unknown");
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetProduct"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject result = (JSONObject) Parser.getValue(response, "d", BaseParser.ValueType.JSONOBJECT);
                        if (result != null) {
                            Product product = Parser.parseProduct(result);
                            if (callback != null) callback.onResultReceived(true, product, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, "Something went wrong or product with this id doesn't exist");
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
     * Get list of products. Returns results by pages.
     *
     * @param page - number of the page with results, minimum value is 1
     * @param pageSize - results amount per page
     * @param categories - array of product categories to search in. Set to null to get products from all categories
     * @param merchantGroups - array of merchant groups to search in. Set to null to get products from all merchant groups
     * @param merchantId - specify merchant id to get exact merchant products or set to null to search through all merchants
     * @param text - text to search or null
     * @param promoOnly - get only promo products
     * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); result - arrayList
     *   containing Product objects (null in case request was NOT successful); message - error description if any (null in case
     *   request was successful)
     */
    public static void getProducts(Context context, int page, int pageSize, int[] categories, int[] merchantGroups,
                                   String merchantId, String text, boolean promoOnly, final ReceivedListCallback callback) {
        JSONObject json = new JSONObject();
        try {
            JSONObject filters = new JSONObject();
            filters.put("PromoOnly", promoOnly);
            filters.put("Language", "Unknown");
            if (!TextUtils.isEmpty(text)) filters.put("Text", text);
            if (!TextUtils.isEmpty(merchantId)) filters.put("MerchantNumber", merchantId);
            if (categories!=null && categories.length>0) {
                JSONArray array = new JSONArray();
                for (int i=0; i<categories.length; i++) {
                    array.put(categories[i]);
                }
                filters.put("Categories", array);
            }
            if (merchantGroups!=null && merchantGroups.length>0) {
                JSONArray array = new JSONArray();
                for (int i=0; i<merchantGroups.length; i++) {
                    array.put(merchantGroups[i]);
                }
                filters.put("MerchantGroups", array);
            }
            json.put("page", page);
            json.put("pageSize", pageSize);
            json.put("filters", filters);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetProducts"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray) Parser.getValue(response, "d", BaseParser.ValueType.JSONARRAY);
                        ArrayList<Object> products = new ArrayList<>();
                        if (result != null) {
                            try {
                                for (int i=0; i<result.length(); i++) {
                                    JSONObject obj = (JSONObject) result.get(i);
                                    Product product = Parser.parseProduct(obj);
                                    products.add(product);
                                }
                            } catch (Exception e) {
                                Log.d("EcommerceSDK", "Error when parsing products");
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, products, null);
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
        postRequest.addHeader("Cookie", "credentialsToken=" + UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Get next product for merchant.
     *
     * @param merchantNumber - current merchant id
     * @param productId - current product id
     * @param callback - callback.onResultReceived(boolean isSuccess, long value, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); value -
     *   next product id (returns 0 in case there is no next product or not existing product id in case of any
     *   other issue); message - error description if any (null in case request was successful)
     */
    public static void getNextProductForMerchant(Context context, String merchantNumber, long productId, ReceivedLongCallback callback) {
        getProductWithMethod(context, "GetNextProductId", merchantNumber, productId, callback);
    }

    /**
     * Get previous product for merchant.
     *
     * @param merchantNumber - current merchant id
     * @param productId - current product id
     * @param callback - callback.onResultReceived(boolean isSuccess, long value, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); value -
     *   previous product id (returns 0 in case there is no previous product or not existing product id in case
     *   of any other issue); message - error description if any (null in case request was successful)
     */
    public static void getPrevProductForMerchant(Context context, String merchantNumber, long productId, ReceivedLongCallback callback) {
        getProductWithMethod(context, "GetPrevProductId", merchantNumber, productId, callback);
    }

    private static void getProductWithMethod(Context context, String method, String merchantNumber, long productId, final ReceivedLongCallback callback) {
        if (TextUtils.isEmpty(merchantNumber)) {
            if (callback != null) callback.onResultReceived(false, 0, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", merchantNumber);
            json.put("productId", productId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, 0, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl(method), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        long productId = (long) Parser.getValue(response, "d", BaseParser.ValueType.LONG);
                        if (callback != null) callback.onResultReceived(true, productId, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, 0, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/************************************* PRODUCTS SECTION END *************************************/


/************************************ MERCHANT SECTION START ************************************/

    /**
     * Get merchant by id.
     *
     * @param merchantNumber - id of required merchant, can't be null
     * @param callback - callback.onResultReceived(boolean isSuccess, Merchant merchant, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); merchant -
     *   Merchant object (null in case request was NOT successful); message - error description if any (null in case
     *   request was successful)
     */
    public static void getMerchant(Context context, String merchantNumber, final ReceivedMerchantCallback callback) {
        if (merchantNumber == null) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", merchantNumber);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetMerchant"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject merchantObj = (JSONObject) Parser.getValue(response, "d", BaseParser.ValueType.JSONOBJECT);
                        if (merchantObj != null) {
                            Merchant merchant = Parser.parseMerchant(merchantObj);
                            if (callback != null) callback.onResultReceived(true, merchant, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, "Something went wrong or there is no such merchant");
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
     * Send email to the merchant.
     *
     * @param merchantNumber - id of the merchant you want to send email to, can't be empty or null
     * @param from - sender email, can't be empty or null
     * @param subject - email subject, can't be empty or null
     * @param body - email text, can't be empty or null
     * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not);
     *   message - error description if any (null in case request was successful)
     */
    public static void sendEmailToMerchant(Context context, String merchantNumber, String from, String subject, String body, final ReceivedBasicCallback callback) {

        if (TextUtils.isEmpty(merchantNumber) || TextUtils.isEmpty(from) ||
                TextUtils.isEmpty(subject) || TextUtils.isEmpty(body)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("applicationToken", Constants.APP_TOKEN);
            json.put("merchantNumber", merchantNumber);
            json.put("from", from);
            json.put("subject", subject);
            json.put("body", body);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SendMerchantContactEmail"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Get categories for exact merchant.
     *
     * @param merchantNumber - id of required merchant, can't be null
     * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); result - arrayList
     *   containing ProductCategory objects (null in case request was NOT successful); message - error description if any (null 
     *   in case request was successful)
     */
    public static void getCategoriesForMerchant(Context context, String merchantNumber, final ReceivedListCallback callback) {
        if (merchantNumber == null) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", merchantNumber);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetMerchantCategories"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray) Parser.getValue(response, "d", BaseParser.ValueType.JSONARRAY);
                        ArrayList<Object> categories = new ArrayList<>();
                        if (result != null) {
                            try {
                                for (int i=0; i<result.length(); i++) {
                                    JSONObject obj = (JSONObject) result.get(i);
                                    ProductCategory category = Parser.parseCategory(obj);
                                    categories.add(category);
                                }
                            } catch (Exception e) {
                                Log.d("EcommerceSDK", "Error when parsing categories");
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, categories, null);
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
     * Get merchant policies content.
     *
     * @param merchantNumber - id of required merchant, can't be empty or null
     * @param contentName - name of content you want to get, applicable values are About.html, Policy.html 
     *   and Terms.html, can't be empty or null
     * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not);
     *   message - error description if any (null in case request was successful)
     */
    public static void getContentForMerchant(Context context, String merchantNumber, String contentName, final ReceivedBasicCallback callback) {
        if (TextUtils.isEmpty(merchantNumber) || TextUtils.isEmpty(contentName)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("applicationToken", Constants.APP_TOKEN);
            json.put("merchantNumber", merchantNumber);
            json.put("language", "Unknown");
            json.put("contentName", contentName);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        /** TO DO:
         add issuccess/message check
         parse and modify callback **/
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetMerchantContent"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Get merchants with exact parameters.
     *
     * @param groupId - id of the group which the merchant should belong to, send 0 to search through all groups
     * @param text - search text, send null to ignore this parameter
     * @param status - merchant status, send MerchantStatus.ALL to search merchants with any status.
     * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); result - arrayList
     *   containing Merchant objects (null in case request was NOT successful); message - error description if any (null in case
     *   request was successful)
     */
    public static void getMerchants(Context context, long groupId, String text, MerchantStatus status, final ReceivedListCallback callback) {
        JSONObject json = new JSONObject();
        try {
            JSONObject filters = new JSONObject();
            filters.put("ApplicationToken", Constants.APP_TOKEN);
            if (groupId != 0) filters.put("GroupId", groupId);
            if (TextUtils.isEmpty(text)) filters.put("Text", text);
            if (status != MerchantStatus.ALL) {
                long statusLong = 0;
                switch (status) {
                    case ARCHIVED: statusLong = 0; break;
                    case NEW: statusLong = 1; break;
                    case BLOCKED: statusLong = 2; break;
                    case CLOSED: statusLong = 3; break;
                    case LOGINONLY: statusLong = 10; break;
                    case INTEGRATION: statusLong = 20; break;
                    case PROCESSING: statusLong = 30; break;
                    default: break;
                }
                filters.put("MerchantStatus", statusLong);
            }

            json.put("filters", filters);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetMerchants"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray) Parser.getValue(response, "d", BaseParser.ValueType.JSONARRAY);
                        ArrayList<Object> merchants = new ArrayList<>();
                        if (result != null) {
                            try {
                                for (int i=0; i<result.length(); i++) {
                                    JSONObject obj = (JSONObject) result.get(i);
                                    Merchant merchant = Parser.parseMerchant(obj);
                                    merchants.add(merchant);
                                }
                            } catch (Exception e) {
                                Log.d("EcommerceSDK", "Error when parsing merchants");
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, merchants, null);
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

/************************************* MERCHANT SECTION END *************************************/


/************************************ DOWNLOAD SECTION START ************************************/

    /**
     * Get list of downloads. Returns results by pages.
     *
     * @param page - number of the page with results, minimum value is 1
     * @param pageSize - results amount per page
     * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); result - arrayList
     *   containing CartItem objects (null in case request was NOT successful); message - error description if any (null in case
     *   request was successful)
     */
    public static void getDownloads(Context context, int page, int pageSize, final ReceivedListCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("applicationToken", Constants.APP_TOKEN);
            json.put("walletCredentials", UserSession.getInstance().getCredentialsToken(context));
            json.put("page", page);
            json.put("pageSize", pageSize);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetDownloads"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /** TO DO:
                         add issuccess/message check
                         test with non-null list **/
                        JSONArray result = (JSONArray) Parser.getValue(response, "d", BaseParser.ValueType.JSONARRAY);
                        ArrayList<Object> cartItems = new ArrayList<>();
                        if (result != null) {
                            try {
                                for (int i=0; i<result.length(); i++) {
                                    JSONObject obj = (JSONObject) result.get(i);
                                    CartItem item = Parser.parseCartItem(obj);
                                    cartItems.add(item);
                                }
                            } catch (Exception e) {
                                Log.d("EcommerceSDK", "Error when parsing cart items");
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, cartItems, null);
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
     * Download an item by id (requires authorization)
     *
     * @param itemId - id of the item to download
     * @param asPlainData - form of downloaded data
     * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not);
     *   message - error description if any (null in case request was successful)
     */
    public static void downloadItem(Context context, int itemId, boolean asPlainData, final ReceivedBasicCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("applicationToken", Constants.APP_TOKEN);
            json.put("walletCredentials", UserSession.getInstance().getCredentialsToken(context));
            json.put("itemId", itemId);
            json.put("asPlainData", asPlainData);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        /** TO DO:
         explanations
         allow different asPlainData?
         add issuccess/message check
         parse and modify callback **/
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Download"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/************************************* DOWNLOAD SECTION END *************************************/


/************************************** CART SECTION START **************************************/

    /**
     * Get active carts.
     *
     * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); result - arrayList
     *   containing Cart objects (null in case request was NOT successful); message - error description if any (null in case
     *   request was successful)
     */
    public static void getActiveCarts(Context context, final ReceivedListCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("applicationToken", Constants.APP_TOKEN);
            json.put("walletCredentials", UserSession.getInstance().getCredentialsToken(context));
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetActiveCarts"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray) Parser.getValue(response, "d", BaseParser.ValueType.JSONARRAY);
                        ArrayList<Object> carts = new ArrayList<>();
                        if (result != null) {
                            try {
                                for (int i=0; i<result.length(); i++) {
                                    JSONObject obj = (JSONObject) result.get(i);
                                    Cart cart = Parser.parseCart(obj);
                                    carts.add(cart);
                                }
                            } catch (Exception e) {
                                Log.d("EcommerceSDK", "Error when parsing carts");
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, carts, null);
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
     * Update cart data.
     *
     * @param cart - Cart object of the cart you want to update, can be received using getActiveCarts method, can't be null
     * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not);
     *   message - error description if any (null in case request was successful)
     */
    public static void updateCart(Context context, Cart cart, final ReceivedBasicCallback callback) {
        if (cart == null) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }
        if (cart.getMerchant() == null) {
            if (callback != null) callback.onResultReceived(false, "Wrong merchant");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            if (cart.getShopCartItems() == null) cart.setShopCartItems(new ArrayList<CartItem>());
            ArrayList<JSONObject> itemsArray = new ArrayList<>();
            for (CartItem item : cart.getShopCartItems()) {
                JSONObject shopCartItem = new JSONObject();
                shopCartItem.put("ProductId", Integer.valueOf(item.getItemProductId()));
                if (item.getItemProductStockId() > 0) shopCartItem.put("ProductStockId", item.getItemProductStockId());
                else shopCartItem.put("ProductStockId", JSONObject.NULL);
                shopCartItem.put("Quantity", Integer.valueOf(item.getItemQuantity()));
                shopCartItem.put("Price", Double.valueOf(item.getItemPrice()));
                itemsArray.add(shopCartItem);
            }

            JSONObject cartPart = new JSONObject();
            if (cart.getCartCookie() != null) cartPart.put("Cookie", cart.getCartCookie());
            cartPart.put("Items", new JSONArray(itemsArray));
            cartPart.put("CurrencyIso", cart.getCartCurrencyIso());
            cartPart.put("MerchantNumber", cart.getMerchant().getNumber());

            json.put("applicationToken", Constants.APP_TOKEN);
            json.put("cart", cartPart);
            json.put("walletCredentials", UserSession.getInstance().getCredentialsToken(context));

        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SetCart"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String obj = (String) Parser.getValue(response, "d", BaseParser.ValueType.STRING);
                        if (obj != null && obj.length() > 0) {
                            if (callback != null) callback.onResultReceived(true, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, "Something went wrong or cart wasn't found");
                        }
                    }
                }, createBasicErrorListener(callback)
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    /**
     * Get cart by cookie.
     *
     * @param cartCookie - cookie of the required cart, can't be empty or null
     * @param callback - callback.onResultReceived(boolean isSuccess, Cart cart, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); cart -
     *   Cart object (null in case request was NOT successful); message - error description if any (null in case
     *   request was successful)
     */
    public static void getCart(Context context, String cartCookie, final ReceivedCartCallback callback) {
        if (TextUtils.isEmpty(cartCookie)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("applicationToken", Constants.APP_TOKEN);
            json.put("cookie", cartCookie);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetCart"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject cartObj = (JSONObject) Parser.getValue(response, "d", BaseParser.ValueType.JSONOBJECT);
                        if (cartObj != null) {
                            Cart cart = Parser.parseCart(cartObj);
                            if (callback != null) callback.onResultReceived(true, cart, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, "Something went wrong or there is no such cart");
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
     * Get cart by transaction id.
     *
     * @param transactionId - transaction id of the required cart
     * @param callback - callback.onResultReceived(boolean isSuccess, Cart cart, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not); cart -
     *   Cart object (null in case request was NOT successful); message - error description if any (null in case
     *   request was successful)
     */
    public static void getCartForTransaction(Context context, long transactionId, final ReceivedCartCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("applicationToken", Constants.APP_TOKEN);
            json.put("walletCredentials", UserSession.getInstance().getCredentialsToken(context));
            json.put("transactionId", transactionId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetCartOfTransaction"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /** TO DO:
                         test with real cart transaction **/
                        JSONObject cartObj = (JSONObject) Parser.getValue(response, "d", BaseParser.ValueType.JSONOBJECT);
                        if (cartObj != null) {
                            Cart cart = Parser.parseCart(cartObj);
                            if (callback != null) callback.onResultReceived(true, cart, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, "Something went wrong or there is no such cart");
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

/*************************************** CART SECTION END ***************************************/


/************************************** SHOPS SECTION START *************************************/

    public static void getShop(Context context, long shopId, String merchantNumber, final ReceivedBasicCallback callback) {
        JSONObject json = new JSONObject();
        try {
            if (shopId != 0) json.put("shopId", shopId);
            json.put("merchantNumber", merchantNumber);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        /** TO DO:
         explanations
         parse and modify callback **/
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShop"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void getShopIds(Context context, String subDomainName, long imageHeight, final ReceivedBasicCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("subDomainName", subDomainName);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        /** TO DO:
         explanations
         parse and modify callback **/
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShopIds"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void getShops(Context context, String merchantNumber, String culture, final ReceivedBasicCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", merchantNumber);
            json.put("culture", culture);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        /** TO DO:
         explanations
         parse and modify callback **/
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShops"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/*************************************** SHOPS SECTION END **************************************/


/************************************ UNSORTED SECTION START ************************************/

    /**
     * Allows to set custom product image size in case it is called before getting product
     *
     * @param imageWidth - desired image width
     * @param imageHeight - desired image height
     * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
     *   the request completes. isSuccess - completion status (whether request was successful or not);
     *   message - error description if any (null in case request was successful)
     */
    public static void setSessionWithImageWidth(Context context, long imageWidth, long imageHeight, final ReceivedBasicCallback callback) {
        JSONObject json = new JSONObject();
        try {
            JSONObject options = new JSONObject();
            options.put("ImageWidth", imageWidth);
            options.put("ImageHeight", imageHeight);
            json.put("applicationToken", Constants.APP_TOKEN);
            json.put("options", options);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        /** TO DO:
         explanations
         parse and modify callback **/
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SetSession"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+ UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/************************************* UNSORTED SECTION END ************************************/

    private static String createUrl(String method) {
        return Constants.SERVICE_URL+method;
    }

    private static Response.Listener<JSONObject> createBasicSuccessListener(final ReceivedBasicCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (callback != null) callback.onResultReceived(true, null);
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
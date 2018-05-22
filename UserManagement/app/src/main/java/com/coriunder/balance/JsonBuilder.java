package com.coriunder.balance;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class contains methods designed to prepare
 * JSONObjects to send with requests to Balance service
 */
public class JsonBuilder {
    /**
     * Prepare JSONObject for GetRequest request
     * @param requestId id of the request which info should be loaded
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetRequest(long requestId) {
        JSONObject json = new JSONObject();
        try {
            json.put("requestId", requestId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetRequests request
     * @param currencyIso ISO code for currency which requests should be returned for
     * @param paymentMethodId id of the payment method
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results amount per page
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetRequests(String currencyIso, long paymentMethodId,
                                                        int page, int pageSize) {
        JSONObject json = new JSONObject();
        try {
            // Create filters JSONObject
            JSONObject filters = new JSONObject();
            filters.put("CurrencyIso", TextUtils.isEmpty(currencyIso) ? "" : currencyIso);
            filters.put("StoredPaymentMethodID", paymentMethodId);
            json.put("filters", filters);
            // Create sortAndPage JSONObject
            JSONObject sortAndPage = new JSONObject();
            sortAndPage.put("PageNumber", page);
            sortAndPage.put("PageSize", pageSize);
            json.put("sortAndPage", sortAndPage);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetRows request
     * @param currencyIso ISO code for currency which transactions should be returned for
     * @param paymentMethodId id of the payment method
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results amount per page
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetRows(String currencyIso, long paymentMethodId,
                                                        int page, int pageSize) {
        JSONObject json = new JSONObject();
        try {
            // Create filters JSONObject
            JSONObject filters = new JSONObject();
            filters.put("CurrencyIso", TextUtils.isEmpty(currencyIso) ? "" : currencyIso);
            filters.put("StoredPaymentMethodID", paymentMethodId == 0 ? JSONObject.NULL : paymentMethodId);
            json.put("filters", filters);
            // Create sortAndPage JSONObject
            JSONObject sortAndPage = new JSONObject();
            sortAndPage.put("PageNumber", page);
            sortAndPage.put("PageSize", pageSize);
            json.put("sortAndPage", sortAndPage);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetTotal request
     * @param currencyIso ISO code for currency which balances should be returned for
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetTotal(String currencyIso) {
        JSONObject json = new JSONObject();
        try {
            json.put("currencyIsoCode", TextUtils.isEmpty(currencyIso) ? "" : currencyIso);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for ReplyRequest request
     * @param requestId id of the request which has to approved or declined
     * @param approve defines whether request has to approved or declined
     * @param pinCode current user pin code
     * @param text comment for transaction
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForReplyRequest(long requestId, boolean approve,
                                                         String pinCode, String text) {
        JSONObject json = new JSONObject();
        try {
            json.put("requestId", requestId);
            json.put("approve", approve);
            json.put("pinCode", TextUtils.isEmpty(pinCode) ? "" : pinCode);
            json.put("text", TextUtils.isEmpty(text) ? "" : text);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for RequestAmount request
     * @param userId id of the user who should receive the request
     * @param amount amount which has to be requested
     * @param currencyIso ISO code of the currency for the request
     * @param text comment for request
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForRequestAmount(String userId, double amount,
                                                          String currencyIso, String text) {
        JSONObject json = new JSONObject();
        try {
            json.put("destAcocuntId", TextUtils.isEmpty(userId) ? "" : userId);
            json.put("amount", amount);
            json.put("currencyIso", TextUtils.isEmpty(currencyIso) ? "" : currencyIso);
            json.put("text", TextUtils.isEmpty(text) ? "" : text);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for TransferAmount request
     * @param userId id of the user who should receive the amount
     * @param amount amount which has to be sent
     * @param currencyIso ISO code of the currency for the transfer
     * @param pinCode current user pin code
     * @param text comment for transfer
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForTransferAmount(String userId, double amount,
                                        String currencyIso, String pinCode, String text) {
        JSONObject json = new JSONObject();
        try {
            json.put("destAcocuntId", TextUtils.isEmpty(userId) ? "" : userId);
            json.put("amount", amount);
            json.put("currencyIso", TextUtils.isEmpty(currencyIso) ? "" : currencyIso);
            json.put("pinCode", TextUtils.isEmpty(pinCode) ? "" : pinCode);
            json.put("text", TextUtils.isEmpty(text) ? "" : text);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }
}
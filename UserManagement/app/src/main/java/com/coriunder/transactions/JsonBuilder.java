package com.coriunder.transactions;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * This class contains methods designed to prepare
 * JSONObjects to send with requests to Transactions service
 */
public class JsonBuilder {
    /**
     * Prepare JSONObject for Get request
     * @param transactionId id of the transaction which info has to be loaded
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGet(long transactionId) {
        JSONObject json = new JSONObject();
        try {
            json.put("transactionId", transactionId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for Lookup request
     * @param transDate date of transaction
     * @param amount amount of transaction
     * @param last4cc last 4 symbols for the payment method used to pay for transaction
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForLookup(Date transDate, double amount, String last4cc) {
        // Parse date to server applicable format
        String transDateFormatted = transDate == null ? null : "/Date("+transDate.getTime()+")/";

        JSONObject json = new JSONObject();
        try {
            json.put("transDate", TextUtils.isEmpty(transDateFormatted) ? JSONObject.NULL : transDateFormatted);
            json.put("amount", amount);
            json.put("last4cc", TextUtils.isEmpty(last4cc) ? "" : last4cc);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /*
    ToDoV2:
    Not all params used
    */
    /**
     * Prepare JSONObject for ProcessTransaction request
     * @param pin current user's pin code
     * @param addressId id of the shipping address, which has to be used to process the cart
     * @param cartCookie cookie of the cart which has to be processed
     * @param paymentMethodId id of the payment method, which has to be used to process the cart
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForProcess(String pin, long addressId, String cartCookie, long paymentMethodId) {
        JSONObject json = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            data.put("PinCode", TextUtils.isEmpty(pin) ? "" : pin);
            data.put("ShippingAddressId", addressId);
            data.put("ShopCartCookie", TextUtils.isEmpty(cartCookie) ? "" : cartCookie);
            data.put("StoredPaymentMethodId", paymentMethodId);
            json.put("data", data);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /*
    ToDoV2:
    What types should TransactionStatus have
    */
    /**
     * Prepare JSONObject for Search request
     * @param amountFrom min transaction amount
     * @param amountTo max transaction amount
     * @param currencyIso currency of transaction
     * @param dateFrom min transaction date
     * @param dateTo max transaction date
     * @param idFrom min transaction id
     * @param idTo max transaction id
     * @param loadMerchant sets whether merchant info needs to be included in the response
     * @param loadPayer sets whether payer info needs to be included in the response
     * @param loadPayment sets whether payment info needs to be included in the response
     * @param transType transaction type
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results' amount per page
     * @return prepared JSONObject
     * @see TransactionsSDK.TransactionStatus
     */
    protected static JSONObject buildJsonForSearch(double amountFrom, double amountTo, String currencyIso, Date dateFrom, Date dateTo,
                                                   long idFrom, long idTo, boolean loadMerchant, boolean loadPayer, boolean loadPayment,
                                                   TransactionsSDK.TransactionStatus transType, int page, int pageSize) {
        // Parse dates to server applicable format
        String dateFromFormatted = dateFrom == null ? null : "/Date("+dateFrom.getTime()+")/";
        String dateToFormatted = dateTo == null ? null : "/Date("+dateTo.getTime()+")/";

        JSONObject json = new JSONObject();
        try {
            // Create filters JSONObject
            JSONObject filters = new JSONObject();
            filters.put("AmountFrom", amountFrom);
            filters.put("AmountTo", amountTo);
            filters.put("CurrencyIso", TextUtils.isEmpty(currencyIso) ? "" : currencyIso);
            filters.put("DateFrom", TextUtils.isEmpty(dateFromFormatted) ? JSONObject.NULL : dateFromFormatted);
            filters.put("DateTo", TextUtils.isEmpty(dateToFormatted) ? JSONObject.NULL : dateToFormatted);
            filters.put("IDFrom", idFrom);
            filters.put("IDTo", idTo);
            json.put("filters", filters);

            // Create loadOptions JSONObject
            JSONObject loadOptions = new JSONObject();
            loadOptions.put("LoadMerchant", loadMerchant);
            loadOptions.put("LoadPayer", loadPayer);
            loadOptions.put("LoadPayment", loadPayment);

            // Put int value depending on the transaction status
            if (transType != TransactionsSDK.TransactionStatus.ALL) {
                int statusInt = 0;
                switch (transType) {
                    case TYPE_1: statusInt = 0; break;
                    case TYPE_2: statusInt = 1; break;
                    case TYPE_3: statusInt = 2; break;
                    default: break;
                }
                loadOptions.put("TransType", statusInt);
            }
            json.put("loadOptions", loadOptions);

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
}
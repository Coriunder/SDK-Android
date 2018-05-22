package com.coriunder.base.utils;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.models.Address;
import com.coriunder.base.common.models.Merchant;
import com.coriunder.base.common.models.ServiceResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Commonly used parser methods
 */
public class BaseParser {
    /**
     * Get boolean value from JSONObject
     * @param resultObject JSONObject to get value from
     * @param key key for the required value
     * @return required value or false in case of error
     */
    public static boolean getBoolean(JSONObject resultObject, String key) {
        try {
            return resultObject.getBoolean(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get double value from JSONObject
     * @param resultObject JSONObject to get value from
     * @param key key for the required value
     * @return required value or 0 in case of error
     */
    public static double getDouble(JSONObject resultObject, String key) {
        try {
            return resultObject.getDouble(key);
        } catch (Exception e) {
            return 0d;
        }
    }

    /**
     * Get int value from JSONObject
     * @param resultObject JSONObject to get value from
     * @param key key for the required value
     * @return required value or 0 in case of error
     */
    public static int getInt(JSONObject resultObject, String key) {
        try {
            return resultObject.getInt(key);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get JSONArray value from JSONObject
     * @param resultObject JSONObject to get value from
     * @param key key for the required value
     * @return required value or null in case of error
     */
    public static JSONArray getJsonArray(JSONObject resultObject, String key) {
        try {
            return resultObject.getJSONArray(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get JSONObject value from JSONObject
     * @param resultObject JSONObject to get value from
     * @param key key for the required value
     * @return required value or null in case of error
     */
    public static JSONObject getJsonObject(JSONObject resultObject, String key) {
        try {
            return resultObject.getJSONObject(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get long value from JSONObject
     * @param resultObject JSONObject to get value from
     * @param key key for the required value
     * @return required value or 0 in case of error
     */
    public static long getLong(JSONObject resultObject, String key) {
        try {
            return resultObject.getLong(key);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Get String value from JSONObject
     * @param resultObject JSONObject to get value from
     * @param key key for the required value
     * @return required value or empty String in case of error
     */
    public static String getString(JSONObject resultObject, String key) {
        try {
            String value = resultObject.getString(key);
            return (value == null || value.equals("null")) ? "" : value;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get Date value from JSONObject
     * @param jsonResult JSONObject to get value from
     * @param key key for the required value
     * @return required value or new Date() in case of error
     */
    public static Date getReadableDate(JSONObject jsonResult, String key) {
        String dateString = getString(jsonResult, key);
        try {
            String dateCode = dateString.substring(dateString.indexOf("(")+1, dateString.indexOf(")"));
            if (dateCode.contains("+")) dateCode = dateCode.substring(0, dateCode.indexOf("+"));
            return new Date(Long.parseLong(dateCode));
        } catch (Exception e) { return new Date(); }
    }

    /**
     * Get error text for unsuccessful request
     * @param error error response
     * @return error text
     */
    public static String getErrorText(VolleyError error) {
        if (error == null) return Coriunder.getContext().getString(R.string.unknown_error);

        NetworkResponse response = error.networkResponse;
        if(response != null && response.data != null) {
            String errorString = new String(response.data);
            if (Constants.ENABLE_LOGS) Log.d("ResponseLogError", errorString);

            // Trying to get message (assuming we received a json object as an error)
            String message;
            try {
                message = getString(new JSONObject(errorString), "Message");
            } catch (Exception e) { message = ""; }
            if (!TextUtils.isEmpty(message)) return message;

            // Getting json message failed, trying to get an html message
            try {
                message = errorString.substring(errorString.toLowerCase().indexOf("<title>")+7,
                        errorString.toLowerCase().indexOf("</title>"));
                return message;
            } catch (Exception e) {
                return errorString;
            }
        } else {
            String message = error.toString();
            if (Constants.ENABLE_LOGS) Log.d("ResponseLogError", message);
            if (!TextUtils.isEmpty(message)) {
                if (message.contains("com.android.volley.NoConnectionError"))
                    return Coriunder.getContext().getString(R.string.connection_error);
                else return message;
            } else return Coriunder.getContext().getString(R.string.unknown_error);
        }
    }

    /**
     * Method to parse JSONObject to ServiceResult object
     * @param jsonResult JSONObject to parse
     * @return ServiceResult object
     */
    public static ServiceResult parseServiceResult(JSONObject jsonResult) {
        ServiceResult result = new ServiceResult();
        result.setCode(getInt(jsonResult, "Code"));
        result.setSuccess(getBoolean(jsonResult, "IsSuccess"));
        result.setKey(getString(jsonResult, "Key"));
        result.setMessage(getString(jsonResult, "Message"));
        result.setNumber(getString(jsonResult, "Number"));
        return result;
    }

    /**
     * Method to parse JSONObject to Address object
     * @param addressObj JSONObject to parse
     * @return Address object
     */
    protected static Address parseAddress(JSONObject addressObj) {
        Address address = new Address();
        if (addressObj == null) return address;
        address.setAddress1(getString(addressObj, "AddressLine1"));
        address.setAddress2(getString(addressObj, "AddressLine2"));
        address.setCity(getString(addressObj, "City"));
        address.setCountryIso(getString(addressObj, "CountryIso"));
        address.setPostalCode(getString(addressObj, "PostalCode"));
        address.setStateIso(getString(addressObj, "StateIso"));
        return address;
    }

    /**
     * Method to parse JSONObject to Merchant object
     * @param merchantObj JSONObject to parse
     * @param logTagId id for String to be used as Log tag
     * @return Merchant object
     */
    public static Merchant parseMerchant(JSONObject merchantObj, int logTagId) {
        Merchant merchant = new Merchant();
        if (merchantObj == null) return merchant;
        merchant.setAddress(parseAddress(getJsonObject(merchantObj, "Address")));

        // Parse merchant currencies
        ArrayList<String> currencies = new ArrayList<>();
        JSONArray currenciesArray = getJsonArray(merchantObj, "Currencies");
        if (currenciesArray != null) {
            for (int i = 0; i < currenciesArray.length(); i++){
                try {
                    currencies.add(currenciesArray.get(i).toString());
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(logTagId),
                            Coriunder.getContext().getString(R.string.error_merchant_currency)+i);
                }
            }
        }
        merchant.setCurrencies(currencies);

        merchant.setEmail(getString(merchantObj, "Email"));
        merchant.setFaxNumber(getString(merchantObj, "FaxNumber"));
        merchant.setGroup(getString(merchantObj, "Group"));

        // Parse merchant languages
        ArrayList<String> languages = new ArrayList<>();
        JSONArray languagesArray = getJsonArray(merchantObj, "Languages");
        if (languagesArray != null) {
            for (int i = 0; i < languagesArray.length(); i++){
                try {
                    languages.add(languagesArray.get(i).toString());
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(logTagId),
                            Coriunder.getContext().getString(R.string.error_merchant_language)+i);
                }
            }
        }
        merchant.setLanguages(languages);

        merchant.setName(getString(merchantObj, "Name"));
        merchant.setNumber(getString(merchantObj, "Number"));
        merchant.setPhone(getString(merchantObj, "PhoneNumber"));
        merchant.setWebsite(getString(merchantObj, "WebsiteUrl"));

        return merchant;
    }
}
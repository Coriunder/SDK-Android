package com.coriunder.unused.v1.balancesdk;

import com.android.volley.VolleyError;
import com.coriunder.unused.v1.balancesdk.models.RequestItem;
import com.coriunder.unused.v1.basesdk.BaseParser;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by 1 on 22.02.2016.
 */
public class Parser extends BaseParser {

    protected static RequestItem parseRequest(JSONObject jsonResult) {
        RequestItem item = new RequestItem();
        item.setRequestId((String) getValue(jsonResult, "ID", ValueType.STRING));
        item.setRequestDate(getReadableDate(jsonResult, "RequestDate"));
        item.setSourceAccountNumber((String) getValue(jsonResult, "SourceAccountNumber", ValueType.STRING));
        item.setTargetAccountNumber((String) getValue(jsonResult, "TargetAccountNumber", ValueType.STRING));
        item.setSourceText((String) getValue(jsonResult, "SourceText", ValueType.STRING));
        item.setTargetText((String) getValue(jsonResult, "TargetText", ValueType.STRING));
        item.setSourceAccountName((String) getValue(jsonResult, "SourceAccountName", ValueType.STRING));
        item.setTargetAccountName((String) getValue(jsonResult, "TargetAccountName", ValueType.STRING));
        item.setIsPush((boolean) getValue(jsonResult, "IsPush", ValueType.BOOLEAN));
        item.setAmount((String) getValue(jsonResult, "Amount", ValueType.STRING));
        item.setCurrencyISOCode((String) getValue(jsonResult, "CurrencyISOCode", ValueType.STRING));
        item.setIsApproved((boolean) getValue(jsonResult, "IsApproved", ValueType.BOOLEAN));
        item.setConfirmDate(getReadableDate(jsonResult, "ConfirmDate"));
        return item;
    }

    protected static Object getValue(JSONObject resultObject, String key, ValueType type) {
        return getValueBase(resultObject, key, type);
    }

    protected static String getErrorText(VolleyError error) {
        return getErrorTextBase(error);
    }

    protected static Date getReadableDate(JSONObject jsonResult, String key) {
        return getReadableDateBase(jsonResult, key);
    }

    protected static boolean isEmptyOrNull(String value) {
        return isEmptyOrNullBase(value);
    }

    protected static boolean isRequestSuccessful(JSONObject resultObject) {
        return isRequestSuccessfulBase(resultObject);
    }

    protected static String getMessage(JSONObject resultObject) {
        return getMessageBase(resultObject);
    }
}

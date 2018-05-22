package com.coriunder.unused.v1.mobilesdk;

import com.android.volley.VolleyError;
import com.coriunder.unused.v1.basesdk.BaseParser;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by 1 on 22.02.2016.
 */
public class Parser extends BaseParser {

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

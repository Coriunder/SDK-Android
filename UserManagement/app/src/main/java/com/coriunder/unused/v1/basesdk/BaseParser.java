package com.coriunder.unused.v1.basesdk;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by 1 on 22.02.2016.
 */
public class BaseParser {
    public enum ValueType { BOOLEAN, DOUBLE, INT, JSONARRAY, JSONOBJECT, LONG, STRING }

    protected static Object getValueBase(JSONObject resultObject, String key, ValueType type) {
        try {
            Object object;
            switch (type) {
                case BOOLEAN: object = resultObject.getBoolean(key);break;
                case DOUBLE: object = resultObject.getDouble(key);break;
                case INT: object = resultObject.getInt(key);break;
                case JSONARRAY: object = resultObject.getJSONArray(key);break;
                case JSONOBJECT: object = resultObject.getJSONObject(key);break;
                case LONG: object = resultObject.getLong(key);break;
                case STRING:
                    object = resultObject.getString(key);
                    if (object.equals("null")) return "";
                    break;
                default: object = resultObject.get(key);break;
            }
            return object;
        } catch (Exception e) {
            switch (type) {
                case BOOLEAN: return false;
                case DOUBLE: return 0d;
                case INT: return 0;
                case JSONARRAY: return null;
                case JSONOBJECT: return null;
                case LONG: return 0l;
                case STRING: return "";
                default: return null;
            }
        }
    }

    protected static String getErrorTextBase(VolleyError error) {
        NetworkResponse response = error.networkResponse;
        if(response != null && response.data != null) {
            String errorString = new String(response.data);
            Log.d("SDK", errorString);

            //trying to get message (assuming we have received a json object as an error)
            String message = null;
            try {
                JSONObject jsonObj = new JSONObject(errorString);
                if (jsonObj != null)
                    message = (String) getValueBase(jsonObj, "Message", ValueType.STRING);
            } catch (Exception e) { message = null; }
            if (message != null) return message;

            //geting json message failed, trying to get an html message
            try {
                message = errorString.substring(errorString.toLowerCase().indexOf("<title>")+7,
                        errorString.toLowerCase().indexOf("</title>"));
                return message;
            } catch (Exception e) {
                return errorString;
            }
        }
        return "Something went wrong";
    }

    protected static Date getReadableDateBase(JSONObject jsonResult, String key) {
        String dateString = (String)getValueBase(jsonResult, key, ValueType.STRING);
        try {
            String dateCode = dateString.substring(dateString.indexOf("(")+1, dateString.indexOf(")"));
            return new Date(Long.parseLong(dateCode));
        } catch (Exception e) { return new Date(); }
    }

    protected static boolean isEmptyOrNullBase(String value) {
        return (value == null || (value != null && value.length() == 0));
    }

    protected static boolean isRequestSuccessfulBase(JSONObject resultObject) {
        JSONObject jsonResult = (JSONObject)getValueBase(resultObject, "d", ValueType.JSONOBJECT);
        return (boolean)getValueBase(jsonResult, "IsSuccess", ValueType.BOOLEAN);
    }

    protected static String getMessageBase(JSONObject resultObject) {
        JSONObject jsonResult = (JSONObject)getValueBase(resultObject, "d", ValueType.JSONOBJECT);
        return (String)getValueBase(jsonResult, "Message", ValueType.STRING);
    }
}

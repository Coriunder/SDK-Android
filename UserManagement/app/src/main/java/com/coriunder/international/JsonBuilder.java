package com.coriunder.international;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class contains methods designed to prepare
 * JSONObjects to send with requests to International service
 */
public class JsonBuilder {
    /**
     * Prepare JSONObject for GetErrorCodes request
     * @param language language which you want to receive errors with
     * @param groups groups of errors you need to get info about
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForErrorCodes(String language, String[] groups) {
        JSONObject json = new JSONObject();
        try {
            json.put("language", TextUtils.isEmpty(language) ? "" : language);
            // Create JSONArray from String[]
            if (groups != null && groups.length > 0) {
                JSONArray groupsArray = new JSONArray();
                for (String group : groups) {
                    groupsArray.put(group);
                }
                json.put("groups", groupsArray);
            }
        } catch (JSONException e) {
            return null;
        }
        return json;
    }
}
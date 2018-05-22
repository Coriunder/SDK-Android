package com.coriunder.appidentity;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class contains methods designed to prepare
 * JSONObjects to send with requests to AppIdentity service
 */
public class JsonBuilder {
    /**
     * Prepare JSONObject for GetContent request
     * @param contentName name of the content you need to get
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetContent(String contentName) {
        JSONObject json = new JSONObject();
        try {
            json.put("contentName", TextUtils.isEmpty(contentName) ? "" : contentName);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for Log request
     * @param severityId severity ID
     * @param message main log message
     * @param longMessage long log message
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForLog(int severityId, String message, String longMessage) {
        JSONObject json = new JSONObject();
        try {
            json.put("severityId", severityId);
            json.put("message", TextUtils.isEmpty(message) ? "" : message);
            json.put("longMessage", TextUtils.isEmpty(longMessage) ? "" : longMessage);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for SendContactEmail request
     * @param from email sender
     * @param subject email subject
     * @param body main email text
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForEmail(String from, String subject, String body) {
        JSONObject json = new JSONObject();
        try {
            json.put("from", TextUtils.isEmpty(from) ? "" : from);
            json.put("subject", TextUtils.isEmpty(subject) ? "" : subject);
            json.put("body", TextUtils.isEmpty(body) ? "" : body);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }
}
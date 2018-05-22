package com.coriunder.account;

import android.provider.Settings;
import android.text.TextUtils;

import com.coriunder.base.Coriunder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class contains methods designed to prepare
 * JSONObjects to send with requests to Account service
 */
public class JsonBuilder {
    /**
     * Prepare JSONObject for DecodeLoginCookie request
     * @param cookie the cookie you need to decode
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForDecodeCookie(String cookie) {
        JSONObject json = new JSONObject();
        try {
            json.put("applicationToken", Coriunder.getAppToken());
            json.put("cookie", TextUtils.isEmpty(cookie) ? "" : cookie);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for DeviceActivate request
     * @param activationCode activation code for the device
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForActivateDevice(String activationCode) {
        String deviceId = Settings.Secure.getString(Coriunder.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        JSONObject json = new JSONObject();
        try {
            json.put("deviceId", TextUtils.isEmpty(deviceId) ? "" : deviceId);
            json.put("activationCode", TextUtils.isEmpty(activationCode) ? "" : activationCode);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for DeviceSendActivationCode request
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForSendCode() {
        String deviceId = Settings.Secure.getString(Coriunder.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        JSONObject json = new JSONObject();
        try {
            json.put("deviceId", TextUtils.isEmpty(deviceId) ? "" : deviceId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for Login request
     * @param email user's email
     * @param userName username for the user which is going to log in
     * @param password user's password
     * @param appName name of the application
     * @param pushToken send your push token here if you want the app to receive pushes
     * @param setCookie set whether app needs to set cookie
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForLogin(final String email, String userName, final String password,
                                                  String appName, String pushToken, boolean setCookie) {
        String deviceId = Settings.Secure.getString(Coriunder.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        JSONObject json = new JSONObject();
        try {
            // Create options JSONObject
            JSONObject options = new JSONObject();
            options.put("appName", TextUtils.isEmpty(appName) ? "" : appName);
            options.put("applicationToken", Coriunder.getAppToken());
            options.putOpt("deviceId", deviceId);
            options.putOpt("pushToken", pushToken);
            options.put("setCookie", setCookie);

            json.put("email", TextUtils.isEmpty(email) ? "" : email);
            json.put("userName", TextUtils.isEmpty(userName) ? JSONObject.NULL : userName);
            json.put("password", TextUtils.isEmpty(password) ? "" : password);
            json.put("options", options);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for RegisterDevice request
     * @param phoneNumber phone number to receive SMS with the activation code
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForRegisterDevice(String phoneNumber) {
        String deviceId = Settings.Secure.getString(Coriunder.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        JSONObject json = new JSONObject();
        try {
            json.put("deviceId", TextUtils.isEmpty(deviceId) ? "" : deviceId);
            json.put("phoneNumber", TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for ResetPassword request
     * @param email email of the user which needs to reset his password
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForResetPass(String email) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", TextUtils.isEmpty(email) ? "" : email);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for UpdatePassword request
     * @param newPassword new password for the account
     * @param oldPassword old password for the account
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForSetPass(String newPassword, String oldPassword) {
        JSONObject json = new JSONObject();
        try {
            json.put("oldPassword", TextUtils.isEmpty(oldPassword) ? "" : oldPassword);
            json.put("newPassword", TextUtils.isEmpty(newPassword) ? "" : newPassword);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for UpdatePincode request
     * @param newPinCode new pin code for the account
     * @param password password for the account
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForSetPin(String newPinCode, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("password", TextUtils.isEmpty(password) ? "" : password);
            json.put("newPincode", TextUtils.isEmpty(newPinCode) ? "" : newPinCode);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }
}
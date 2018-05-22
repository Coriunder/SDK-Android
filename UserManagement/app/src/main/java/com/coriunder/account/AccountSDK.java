package com.coriunder.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.account.callbacks.ReceivedDecodedCookieCallback;
import com.coriunder.account.callbacks.ReceivedLoginCallback;
import com.coriunder.account.models.CookieDecodeResult;
import com.coriunder.account.models.LoginResult;
import com.coriunder.base.UserSession;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.callbacks.ReceivedServiceCallback;
import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.Coriunder;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.base.utils.Constants;
import com.coriunder.base.utils.SimpleCrypto;

import org.json.JSONObject;

/**
 * This class contains methods to perform requests to the Account service
 */
@SuppressWarnings("unused")
public class AccountSDK {
    private static AccountSDK instance;
    public static final String SERVICE_URL_PART = "Account.svc";

    public AccountSDK() {
    }

    /**
     * Get instance for AccountSDK class.
     * In case there is no current instance, a new one will be created
     * @return AccountSDK instance
     */
    public static AccountSDK getInstance() {
        if (instance == null) instance = new AccountSDK();
        return instance;
    }

    /**
     * Decode login cookie
     *
     * @param cookie the cookie you need to decode
     * @param callback will be called after request completion
     * @see ReceivedDecodedCookieCallback
     */
    public void decodeLoginCookie(String cookie, final ReceivedDecodedCookieCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForDecodeCookie(cookie);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new CookieDecodeResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("DecodeLoginCookie"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            // Parse response
                            CookieDecodeResult result = Parser.parseCookieDecoder(response);
                            if (callback != null) callback.onResultReceived(result.isSuccess(), result, result.getMessage());
                        } else {
                            if (callback != null) callback.onResultReceived(false, new CookieDecodeResult(), getContext().getString(R.string.empty_response_error));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false, new CookieDecodeResult(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Device activation
     *
     * @param activationCode activation code for the device. Call
     * {@link #sendActivationCode(ReceivedBasicCallback) sendActivationCode} to get it.
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void activateDevice(String activationCode, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForActivateDevice(activationCode);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("DeviceActivate"), json, createBasicSuccessListener(callback),
                CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Request SMS with an activation code for current device
     *
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void sendActivationCode(final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForSendCode();
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("DeviceSendActivationCode"), json, createBasicSuccessListener(callback),
                CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Log current user out. Successful request also cleans data used for auto login.
     *
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void logOff(final ReceivedBasicCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("LogOff"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Stop session
                        UserSession.getInstance().resetSession();
                        // Clear auto-login data
                        updateSessionData("", "", "", "");
                        if (callback != null) callback.onResultReceived(true, "");
                    }
                }, CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Login for existing users. Successful request also stores login data to log user in automatically
     * in future. This data is being cleared when {@link #logOff(ReceivedBasicCallback) logOff} is called.
     *
     * @param email user's email
     * @param userName username for the user which is going to log in
     * @param password user's password
     * @param appName name of the application
     * @param pushToken send your push token here if you want the app to receive pushes
     * @param setCookie set whether app needs to set cookie
     * @param callback will be called after request completion
     * @see ReceivedLoginCallback
     */
    public void login(final String email, String userName, final String password, String appName,
                      String pushToken, boolean setCookie, final ReceivedLoginCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForLogin(email, userName, password, appName, pushToken, setCookie);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new LoginResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Login"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            // Parse response
                            LoginResult result = Parser.parseLoginResult(response);

                            if (result.isSuccess()) {
                                // Start session
                                UserSession.getInstance().setCredentialsToken(result.getCredentialsToken());
                                UserSession.getInstance().setCredentialsHeader(result.getCredentialsHeaderName());

                                try {
                                    // Store data for auto-login
                                    updateSessionData(email, SimpleCrypto.encrypt(password),
                                            SimpleCrypto.encrypt(result.getCredentialsToken()), result.getCredentialsHeaderName());
                                } catch (Exception e) {
                                    Log.d(getContext().getString(R.string.account_log), getContext().getString(R.string.account_error_updating_credentials));
                                }
                            }
                            if (callback != null) callback.onResultReceived(result.isSuccess(), result, result.getMessage());
                        } else {
                            if (callback != null) callback.onResultReceived(false, new LoginResult(), getContext().getString(R.string.empty_response_error));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Clear auto-login data if any
                        updateSessionData("", "", "", "");
                        if (callback != null) callback.onResultReceived(false, new LoginResult(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Register device in the system
     *
     * @param phoneNumber phone number to receive SMS with the activation code
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void registerDevice(String phoneNumber, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForRegisterDevice(phoneNumber);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("RegisterDevice"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Reset password for user.
     *
     * @param email email of the user which needs to reset his password
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void resetPassword(String email, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForResetPass(email);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("ResetPassword"), json, createBasicSuccessListener(callback),
                CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Update user's password
     *
     * @param newPassword new password for the account
     * @param oldPassword old password for the account
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void setNewPassword(final String newPassword, String oldPassword, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForSetPass(newPassword, oldPassword);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("UpdatePassword"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        JSONObject jsonResult = Parser.getJsonObject(response, "d");
                        ServiceResult result = Parser.parseServiceResult(jsonResult);

                        if (result.isSuccess()) {
                            try {
                                // Update password used for auto-login
                                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                                prefs.putString(Constants.SP_PASS, SimpleCrypto.encrypt(newPassword));
                                prefs.apply();
                            } catch (Exception e) {
                                Log.e(getContext().getString(R.string.account_log), getContext().getString(R.string.account_error_updating_credentials));
                            }
                        }

                        if (callback != null) callback.onResultReceived(result.isSuccess(), result, result.getMessage());
                    }
                }, CommonRequests.createServiceErrorListener(callback)
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Update user's pin code
     *
     * @param newPinCode new pin code for the account
     * @param password password for the account
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void setNewPinCode(String newPinCode, String password, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForSetPin(newPinCode, password);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("UpdatePincode"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Auto-login with email and pass stored on previous login.
     *
     * @param pushToken send your push token here if you want the app to receive pushes
     * @param callback will be called after request completion
     */
    public void tryAutoLogin(String pushToken, final ReceivedLoginCallback callback) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String email = prefs.getString(Constants.SP_EMAIL, "");
        String password;
        try { password = SimpleCrypto.decrypt(prefs.getString(Constants.SP_PASS, "")); }
        catch (Exception e) { password = ""; }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            login(email, null, password, null, pushToken, false, callback);
        } else {
            if (callback != null) callback.onResultReceived(false, null, "Saved login data not found");
        }
    }

    /**
     * @param callback ReceivedBasicCallback to be called on request result
     * @return success listener which parses result to boolean and calls ReceivedBasicCallback
     * @see ReceivedBasicCallback
     */
    private Response.Listener<JSONObject> createBasicSuccessListener(final ReceivedBasicCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (callback != null) callback.onResultReceived(Parser.getBoolean(response, "d"), "");
            }
        };
    }

    /**
     * Update session data stored in SharedPreferences
     * @param email email to store
     * @param pass pass to store
     * @param credToken credToken to store
     * @param credTokenHeader credTokenHeader to store
     */
    private void updateSessionData(String email, String pass, String credToken, String credTokenHeader) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        prefs.putString(Constants.SP_EMAIL, email);
        prefs.putString(Constants.SP_PASS, pass);
        prefs.putString(Constants.SP_CRED_TOKEN, credToken);
        prefs.putString(Constants.SP_HEADER, credTokenHeader);
        prefs.apply();
    }

    /**
     * Create URL for request
     * @param method service method name
     * @return service URL
     */
    private String createUrl(String method) {
        return Coriunder.getServiceUrl() + SERVICE_URL_PART + "/" + method;
    }

    /**
     * Get context
     * @return context
     */
    private Context getContext() {
        return Coriunder.getContext();
    }
}
package com.coriunder.unused.v1.usersdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coriunder.unused.v1.basesdk.CustomHeadersImageRequest;
import com.coriunder.unused.v1.basesdk.CustomHeadersJsonObjectRequest;
import com.coriunder.unused.v1.basesdk.UserSession;
import com.coriunder.unused.v1.usersdk.callbacks.ReceivedAddressCallback;
import com.coriunder.unused.v1.basesdk.callbacks.ReceivedBasicCallback;
import com.coriunder.unused.v1.usersdk.callbacks.ReceivedCustomerCallback;
import com.coriunder.unused.v1.usersdk.callbacks.ReceivedImageCallback;
import com.coriunder.unused.v1.basesdk.callbacks.ReceivedListCallback;
import com.coriunder.unused.v1.usersdk.callbacks.ReceivedMapCallback;
import com.coriunder.unused.v1.usersdk.callbacks.ReceivedPaymentMethodCallback;
import com.coriunder.unused.v1.usersdk.callbacks.ReceivedTransactionCallback;
import com.coriunder.unused.v1.usersdk.models.BillingAddress;
import com.coriunder.unused.v1.usersdk.models.Country;
import com.coriunder.unused.v1.usersdk.models.Customer;
import com.coriunder.unused.v1.usersdk.models.PaymentCardRootGroup;
import com.coriunder.unused.v1.usersdk.models.PaymentCardSubrootGroup;
import com.coriunder.unused.v1.usersdk.models.PaymentMethod;
import com.coriunder.unused.v1.usersdk.models.Relation;
import com.coriunder.unused.v1.usersdk.models.ShippingAddress;
import com.coriunder.unused.v1.usersdk.models.State;
import com.coriunder.unused.v1.usersdk.models.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class UserManagementSDK {

/************************************ PRE-LOGIN SECTION START ************************************/

/**
 * Register new user and automatically login this user after successful registration.
 * Login data is automatically stored for future logins.
 *
 * @param firstName - user name, can't be empty or null
 * @param lastName - user surname, can't be empty or null
 * @param password - password to set, should contain at least 9 symbols including 2 chars and 2 numbers, can't be empty or null
 * @param pinCode - pincode to set, can't be empty or null
 * @param email - user email, can't be empty or null
 * @param pushToken - send app push token here in case you want your app to receive push notifications from server
 * @param callback - callback.onResultReceived(boolean isSuccess, HashMap<String,Object> result, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); result - HashMap
 *   containing useful data like UserId, LastLogin, IsFirstLogin, VersionUpdateRequired (null in case login after
 *   registration was NOT successful); message - error description if any (null in case request was successful)
 */
    public static void registerUser(final Context context, final String firstName, final String lastName,
                                    final String password, final String pinCode, final String email,
                                    final String pushToken, final ReceivedMapCallback callback) {

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(pinCode) || TextUtils.isEmpty(email)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        if (!validatePassword(password)) {
            if (callback != null) callback.onResultReceived(false, null, "Password should contain at least 9 symbols including 2 chars and 2 numbers");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            JSONObject walletCustomer = new JSONObject();
            walletCustomer.put("FirstName", firstName);
            walletCustomer.put("LastName", lastName);
            walletCustomer.put("EmailAddress", email);

            JSONObject walletRegisterData = new JSONObject();
            walletRegisterData.put("Password", password);
            walletRegisterData.put("PinCode", pinCode);
            walletRegisterData.put("ApplicationToken", Constants.APP_TOKEN);
            walletRegisterData.put("info", walletCustomer);

            json.put("data", walletRegisterData);

        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("RegisterCustomer"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.isRequestSuccessful(response)) {
                            loginAfterSignUp(context, email, password, pushToken, callback);
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, Parser.getMessage(response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    private static void loginAfterSignUp(Context context, String email, String password, String pushToken, final ReceivedMapCallback callback) {
        login(context, email, password, pushToken, new ReceivedMapCallback() {
            @Override
            public void onResultReceived(boolean isSuccess, HashMap<String, Object> result, String message) {
                if (isSuccess) {
                    if (callback != null) callback.onResultReceived(true, result, null);
                } else {
                    if (callback != null)
                        callback.onResultReceived(false, null, "Signed up successfully. An error occurred while logging in: " + message);
                }
            }
        });
    }

/**
 * Check whether user can be signed up with the specified email
 *
 * @param email - user email, can't be empty or null
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void checkUserEmail(Context context, String email, final ReceivedBasicCallback callback) {
        if (TextUtils.isEmpty(email)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("applicationToken", Constants.APP_TOKEN);
            json.put("email", email);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("IsFreeEmailAddress"), json,  createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

 /**
 * Login for existing users. Login data is automatically stored for future logins.
 *
 * @param applicationContext - getApplicationContext().
 *   DON'T send Activity Context here, otherwise keeping session alive might fail
 * @param email - user email, can't be empty or null
 * @param password - user password, can't be empty or null
 * @param pushToken - send app push token here in case you want your app to receive push notifications from server
 * @param callback - callback.onResultReceived(boolean isSuccess, HashMap<String,Object> result, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); result - HashMap
 *   containing useful data like UserId, LastLogin, IsFirstLogin, VersionUpdateRequired (null in case login after
 *   registration was NOT successful); message - error description if any (null in case request was successful)
 */
    public static void login(final Context applicationContext, final String email, final String password, String pushToken,
                             final ReceivedMapCallback callback) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        String deviceId = Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        JSONObject json = new JSONObject();
        try {
            JSONObject options = new JSONObject();
            options.put("setCookie", false);
            options.put("applicationToken", Constants.APP_TOKEN);
            options.put("appName", "Android Wallet");
            options.putOpt("deviceId", deviceId);
            options.putOpt("pushToken", pushToken);
            options.put("excludeDataImages", true);

            json.put("email", email);
            json.put("password", password);
            json.put("options", options);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Login"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.isRequestSuccessful(response)) {
                            JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                            if (jsonResult == null) {
                                if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                                return;
                            }

                            String credentialsToken = (String)Parser.getValue(jsonResult, "CredentialsToken", Parser.ValueType.STRING);
                            UserSession.getInstance().start(applicationContext, credentialsToken);

                            try {
                                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
                                prefs.putString("email", email);
                                prefs.putString("pass", Cryptor.encrypt(password));
                                prefs.putString("ct", Cryptor.encrypt(credentialsToken));
                                prefs.apply();
                            } catch (Exception e) {
                                Log.d("UserSDK", "Error. Customer credentials for relogin not saved");
                            }

                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("UserId", Parser.getValue(jsonResult, "Number", Parser.ValueType.STRING));
                            hashMap.put("LastLogin", Parser.getReadableDate(jsonResult, "LastLogin"));
                            hashMap.put("IsFirstLogin", Parser.getValue(jsonResult, "IsFirstLogin", Parser.ValueType.BOOLEAN));
                            hashMap.put("VersionUpdateRequired", Parser.getValue(jsonResult, "VersionUpdateRequired", Parser.ValueType.BOOLEAN));
                            if (callback != null) callback.onResultReceived(true, hashMap, null);

                        } else {
                            if (callback != null) callback.onResultReceived(false, null, Parser.getMessage(response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        try {
                            SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
                            prefs.putString("email", "");
                            prefs.putString("pass", "");
                            prefs.putString("ct", "");
                            prefs.apply();
                        } catch (Exception e) {
                            Log.d("UserSDK", "Error. Customer credentials for relogin not cleared");
                        }

                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(applicationContext)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(applicationContext).add(postRequest);
    }

/**
 * Autologin with email and pass stored on previous login.
 *
 * @param pushToken - send app push token here in case you want your app to receive push notifications from server
 * @param callback - callback.onResultReceived(boolean isSuccess, HashMap<String,Object> result, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); result - HashMap
 *   containing useful data like UserId, LastLogin, IsFirstLogin, VersionUpdateRequired (null in case login after
 *   registration was NOT successful); message - error description if any (null in case request was successful)
 */
    public static void tryAutoLogin(Context context, String pushToken, final ReceivedMapCallback callback) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String email = prefs.getString("email", "");
        String password;
        try { password = Cryptor.decrypt(prefs.getString("pass", "")); }
        catch (Exception e) { password = ""; }

        if (password!=null && email.length()>0 && password.length()>0) {
            login(context, email, password, pushToken, callback);
        } else {
            if (callback != null) callback.onResultReceived(false, null, "Saved login data not found");
        }
    }

/**
 * Reset password for user.
 *
 * @param email - user email, can't be empty or null
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void resetPassword(Context context, String email, final ReceivedBasicCallback callback) {
        if (TextUtils.isEmpty(email)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("ResetPassword"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean success = (boolean)Parser.getValue(response, "d", Parser.ValueType.BOOLEAN);
                        if (success) {
                            if (callback != null) callback.onResultReceived(true, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, "Something went wrong");
                        }
                    }
                }, createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Log off current user. Also cleans saved login data (email/pass) after successful logoff.
 *
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void logOff(final Context context, final ReceivedBasicCallback callback) {
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("LogOff"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserSession.getInstance().stop();
                        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        prefs.putString("email", "");
                        prefs.putString("pass", "");
                        prefs.putString("ct", "");
                        prefs.apply();
                        if (callback != null) callback.onResultReceived(true, null);
                    }
                }, createBasicErrorListener(callback)
        );
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/************************************* PRE-LOGIN SECTION END *************************************/


/********************************** CUSTOMER DATA SECTION START **********************************/

/**
 * Get text info about current user (profile image is not included; call getImageForUserWithId to get it).
 *
 * @param callback - callback.onResultReceived(boolean isSuccess, Customer customer, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); customer -
 *   object which contains all info about current user (null in case request was NOT successful); message -
 *   error description if any (null in case request was successful)
 */
    public static void getCustomer(Context context, final ReceivedCustomerCallback callback) {
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetCustomer"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            Customer customer = Parser.parseCustomer(response);
                            if (callback != null) callback.onResultReceived(true, customer, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Save/Update profile info for current user
 *
 * @param firstName - user name, can't be empty or null
 * @param lastName - user surname, can't be empty or null
 * @param email - user email, can't be empty or null
 * @param address1 - user main address
 * @param address2 - user secondary address
 * @param city - user city
 * @param zipCode - user address zip code
 * @param stateIso - user state ISO if any. If not null, then it should match any of state ISO codes
 *   from the list which can be received using getCountriesAndStatesWithCallback method
 * @param countryIso - user country ISO. If not null, then it should match any of country ISO codes
 *   from the list which can be received using getCountriesAndStatesWithCallback method
 * @param phone - user phone
 * @param profileImage - image to be set as user profile image; send null if you don't want to change image
 * @param birthDay - day of birth
 * @param birthMonth - month of birth
 * @param birthYear - year of birth (requires 4 digits)
 * @param personalNumber - user social security number
 * @param cellNumber - cell phone number
 * @param callback - callback.onResultReceived(boolean isSuccess, Customer customer, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); customer -
 *   object which contains all updated info about current user (null in case request was NOT successful); message -
 *   error description if any (null in case request was successful)
 */
    public static void saveCustomer(final Context context, String firstName, String lastName, String email, String address1,
                                     String address2, String city, String zipCode, String stateIso, String countryIso,
                                     String phone, Uri profileImageUri, int birthDay, int birthMonth, int birthYear,
                                     String personalNumber, String cellNumber, final ReceivedCustomerCallback callback) {

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email)) {
            if (callback != null)
                callback.onResultReceived(false, null,
                        "Attempt to send an empty value to the service. First name, last name and email can't be empty");
            return;
        }

        if (birthYear < 1900 || birthYear > Calendar.getInstance().get(Calendar.YEAR)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send wrong year");
            return;
        }

        //Volley uses main thread to encode JSON and image encoding takes too long
        //Calling SaveCustomer service in AsyncTask to move all JSON encoding to a separate thread
        //Calling SaveCustomer AsyncTask in Service so that it wouldn't depend on Activity
        Intent intent = new Intent(context, LongTaskAsyncService.class);
        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);
        intent.putExtra("EmailAddress", email);
        intent.putExtra("AddressLine1", address1);
        intent.putExtra("AddressLine2", address2);
        intent.putExtra("City", city);
        intent.putExtra("PostalCode", zipCode);
        intent.putExtra("StateIso", stateIso);
        intent.putExtra("CountryIso", countryIso);
        intent.putExtra("PhoneNumber", phone);
        intent.putExtra("ProfileImageUri", profileImageUri.toString());
        intent.putExtra("DayOfBirth", birthDay);
        intent.putExtra("MonthOfBirth", birthMonth-1);
        intent.putExtra("YearOfBirth", birthYear);
        intent.putExtra("PersonalNumber", personalNumber);
        intent.putExtra("CellNumber", cellNumber);
        intent.putExtra("ResultReceiver", new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                boolean isSuccess = resultData.getBoolean("IsSucccess");
                if (isSuccess) {
                    getCustomer(context, new ReceivedCustomerCallback() {
                        @Override
                        public void onResultReceived(boolean isSuccess, Customer customer, String message) {
                            if (isSuccess) {
                                if (callback != null)
                                    callback.onResultReceived(true, customer, null);
                            } else {
                                if (callback != null)
                                    callback.onResultReceived(true, new Customer(),
                                            "Profile was saved, but local info wasn't updated. Call getCustomer to force updating local info");
                            }
                        }
                    });
                } else {
                    String message = resultData.getString("Message");
                    if (callback != null)
                        callback.onResultReceived(isSuccess, null, message);
                }
            }
        });
        context.startService(intent);
    }

/**
 * Update password for user
 *
 * @param newPassword - new password, can't be empty or null, should contain at least 9 symbols including
 *   2 chars and 2 numbers
 * @param oldPassword - old password, can't be empty or null
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void setNewPassword(final Context context, final String newPassword, String oldPassword, final ReceivedBasicCallback callback) {
        if (TextUtils.isEmpty(oldPassword)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        if (!validatePassword(newPassword)) {
            if (callback != null) callback.onResultReceived(false, "New password should contain at least 9 symbols including 2 chars and 2 numbers");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("oldPassword", oldPassword);
            json.put("newPassword", newPassword);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("UpdatePassword"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.isRequestSuccessful(response)) {
                            try {
                                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
                                prefs.putString("pass", Cryptor.encrypt(newPassword));
                                prefs.apply();
                            } catch (Exception e) {
                                Log.d("UserSDK", "Error. Customer credentials for relogin not updated");
                            }

                            if (callback != null) callback.onResultReceived(true, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, Parser.getMessage(response));
                        }
                    }
                }, createBasicErrorListener(callback)
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Update pincode for user
 *
 * @param newPincode - new pincode, can't be empty or null
 * @param password - current password, can't be empty or null
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void setNewPincode(Context context, String newPincode, String password, final ReceivedBasicCallback callback) {
        if (TextUtils.isEmpty(newPincode) || TextUtils.isEmpty(password)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("password", password);
            json.put("newPincode", newPincode);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("UpdatePincode"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Get all billing addresses added by current user
 *
 * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); result - arrayList
 *   containing Address objects (null in case request was NOT successful); message -
 *   error description if any (null in case request was successful)
 */
    public static void getBillingAddresses(Context context, final ReceivedListCallback callback) {
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetBillingAddresses"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray)Parser.getValue(response, "d", Parser.ValueType.JSONARRAY);
                        ArrayList<Object> addressesArray = new ArrayList<>();
                        if (result != null) {
                            for (int i=0; i<result.length(); i++) {
                                try {
                                    JSONObject addressObj = result.getJSONObject(i);
                                    BillingAddress address = Parser.parseBillingAddress(addressObj);
                                    addressesArray.add(address);
                                } catch (Exception e) {
                                    Log.d("UserSDK", "Error when parsing address with index "+i);
                                }
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, addressesArray, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/*********************************** CUSTOMER DATA SECTION END ***********************************/


/********************************* SHIPPING ADDRESS SECTION START ********************************/

/**
 * Get exact shipping address by id
 *
 * @param addressId - id of the required shipping address
 * @param callback - callback.onResultReceived(boolean isSuccess, ShippingAddress address, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); address - 
 *   ShippingAddress object (null in case request was NOT successful); message - error description if any (null in case 
 *   request was successful)
 */
    public static void getShippingAddress(Context context, long addressId, final ReceivedAddressCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("addressId", addressId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShippingAddress"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                        if (jsonResult != null) {
                            ShippingAddress address = Parser.parseShippingAddress(jsonResult);
                            if (callback != null) callback.onResultReceived(true, address, null);
                        } else {
                            if (callback != null)
                                callback.onResultReceived(false, null, "Something went wrong or such address doesn't exist");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Get all shipping addresses added by current user
 *
 * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); result - arrayList
 *   containing ShippingAddress objects (null in case request was NOT successful); message -  error description if
 *   any (null in case request was successful)
 */
    public static void getShippingAddresses(Context context, final ReceivedListCallback callback) {
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetShippingAddresses"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray)Parser.getValue(response, "d", Parser.ValueType.JSONARRAY);
                        ArrayList<Object> addressesArray = new ArrayList<>();
                        if (result != null) {
                            for (int i=0; i<result.length(); i++) {
                                try {
                                    JSONObject addressObj = result.getJSONObject(i);
                                    ShippingAddress address = Parser.parseShippingAddress(addressObj);
                                    addressesArray.add(address);
                                } catch (Exception e) {
                                    Log.d("UserSDK", "Error when parsing address with index "+i);
                                }
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, addressesArray, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Add/Update shipping address for user
 *
 * @param title - title for address
 * @param address1 - main address for this shipping address
 * @param address2 - secondary address for this shipping address
 * @param city - city for this shipping address
 * @param countryIso - country ISO. If not null, then it should match any of country ISO codes
 *   from the list which can be received using getCountriesAndStatesWithCallback method
 * @param stateIso - state ISO if any. If not null, then it should match any of state ISO codes
 *   from the list which can be received using getCountriesAndStatesWithCallback method
 * @param zipCode - zip code for this shipping address
 * @param comment - description for this address
 * @param addressId - id of address which has to be updated or null in case it is a new address
 * @param isDefault - defines whether this address should be set as default address or not
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void saveShippingAddress(Context context, String title, String address1, String address2, String city,
                                           String countryIso, String stateIso, String zipCode, String comment,
                                           String addressId, boolean isDefault, final ReceivedBasicCallback callback) {

        JSONObject params = createAddressJson(title, address1, address2, city,
                countryIso, stateIso, zipCode, comment, addressId, isDefault);
        if (params == null) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("address", params);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SaveShippingAddress"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Update several shipping address for user at once
 *
 * @param shippingAddresses - arrayList containing ShippingAddress object for each address which has
 *   to be updated, can't be empty or null. Each ShippingAddress object should already contain
 *   all changes which have to be done for exact address
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void saveShippingAddresses(Context context, ArrayList<ShippingAddress> shippingAddresses,
                                             final ReceivedBasicCallback callback) {

        if (shippingAddresses == null || (shippingAddresses != null && shippingAddresses.size() == 0)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONArray addressesArray = new JSONArray();
        for (ShippingAddress address : shippingAddresses) {
            JSONObject params = createAddressJson(address.getTitle(), address.getAddress1(), address.getAddress2(),
                    address.getCity(),   address.getCountryIso(), address.getStateIso(), address.getZipcode(),
                    address.getComment(), address.getAddressId(), address.isDefault());
            if (params == null) {
                if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
                return;
            }
            addressesArray.put(params);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("data", addressesArray);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SaveShippingAddresses"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Delete exact shipping address
 *
 * @param addressId - id of address which has to be deleted, can't be empty or null
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void deleteShippingAddress(Context context, String addressId, final ReceivedBasicCallback callback) {
        if (TextUtils.isEmpty(addressId)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("addressId", addressId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("DeleteShippingAddress"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean success = (boolean)Parser.getValue(response, "d", Parser.ValueType.BOOLEAN);
                        if (success) {
                            if (callback != null) callback.onResultReceived(true, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, "Something went wrong or there is no such address");
                        }
                    }
                }, createBasicErrorListener(callback)
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/********************************* SHIPPING ADDRESS SECTION END **********************************/


/********************************* PAYMENT METHODS SECTION START *********************************/

/**
 * Get exact payment method by id
 *
 * @param cardId - id of the required payment method
 * @param callback - callback.onResultReceived(boolean isSuccess, PaymentMethod paymentMethod, String message)
 *   is called when the request completes. isSuccess - completion status (whether request was successful or not);
 *   paymentMethod - PaymentMethod object (null in case request was NOT successful); message - error description
 *   if any (null in case request was successful)
 */
    public static void getPaymentMethod(Context context, long cardId, final ReceivedPaymentMethodCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("pmid", cardId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetStoredPaymentMethod"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                        if (jsonResult != null) {
                            PaymentMethod paymentMethod = Parser.parsePaymentMethod(jsonResult);
                            if (callback != null) callback.onResultReceived(true, paymentMethod, null);
                        } else {
                            if (callback != null)
                                callback.onResultReceived(false, null, "Something went wrong or such payment method doesn't exist");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Get all payment methods added by current user
 *
 * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); result - arrayList
 *   containing PaymentMethod objects (null in case request was NOT successful); message -  error description if
 *   any (null in case request was successful)
 */
    public static void getPaymentMethods(Context context, final ReceivedListCallback callback) {
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetStoredPaymentMethods"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray)Parser.getValue(response, "d", Parser.ValueType.JSONARRAY);
                        ArrayList<Object> paymentArray = new ArrayList<>();
                        if (result != null) {
                            for (int i=0; i<result.length(); i++) {
                                try {
                                    JSONObject cardObj = result.getJSONObject(i);
                                    PaymentMethod paymentMethod = Parser.parsePaymentMethod(cardObj);
                                    paymentArray.add(paymentMethod);
                                } catch (Exception e) {
                                    Log.d("UserSDK", "Error when parsing payment method with index "+i);
                                }
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, paymentArray, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Add new payment method for user
 *
 * @param cardNumber - card number, can't be empty or null
 * @param accountValue2 - secondary card value if any
 * @param title - card title
 * @param expMonth - card expiry month
 * @param expYear - card expiry year (requires 4 digits)
 * @param paymentMethodKey - defines card type, should be taken from the default list which could be received using
 *   getPaymentAndRelationListsWithCallback method, can't be empty or null
 * @param paymentMethodGroupKey - defines card type, should be taken from the default list which could be received
 *   using getPaymentAndRelationListsWithCallback method, can't be empty or null
 * @param iconUrl - card icon url
 * @param display - card display name
 * @param ownerName - card owner name
 * @param isDefault - defines whether this payment method should be set as default payment method or not
 * @param usesDefaultAddress - defines whether this payment method will have a custom billing address (value set to
 *   false) or it will use address from the customer info (value set to true). In case it will use customer info
 *   address fields address1, address2, city, countryIso, stateIso and zipCode are not required and will be ignored
 * @param address1 - main address for this payment method, not required in case usesDefaultAddress is set to true
 * @param address2 - secondary address for this payment method, not required in case usesDefaultAddress is set to true
 * @param city - city for this payment method, not required in case usesDefaultAddress is set to true
 * @param countryIso - country ISO. If not null, then it should match any of country ISO codes from the list which can
 *   be received using getCountriesAndStatesWithCallback method, not required in case usesDefaultAddress is set to true
 * @param stateIso - state ISO if any. If not null, then it should match any of state ISO codes from the list which can
 *   be received using getCountriesAndStatesWithCallback method, not required in case usesDefaultAddress is set to true
 * @param zipCode - zip code for this payment method not required in case usesDefaultAddress is set to true
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when the request completes.
 *   isSuccess - completion status (whether request was successful or not); message - error description if any (null in
 *   case request was successful)
 */
    public static void addNewPaymentMethod(Context context, String cardNumber, String accountValue2, String title, int expMonth,
                                           int expYear, String paymentMethodKey, String paymentMethodGroupKey, String iconUrl,
                                           String display, String ownerName, boolean isDefault, boolean usesDefaultAddress,
                                           String address1, String address2, String city, String countryIso, String stateIso,
                                           String zipCode, final ReceivedBasicCallback callback) {

        if (TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(paymentMethodKey) || TextUtils.isEmpty(paymentMethodGroupKey)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        Calendar cal = Calendar.getInstance();
        if (expYear < cal.get(Calendar.YEAR)) {
            if (callback != null)
                callback.onResultReceived(false, "Card has already expired. Check the expiry year");
            return;
        }

        JSONObject params = createCardJson(cardNumber, accountValue2, null, title, expMonth-1, expYear,
                paymentMethodKey,  paymentMethodGroupKey, iconUrl, display, ownerName, isDefault,
                usesDefaultAddress, address1, address2, city, countryIso, stateIso, zipCode);
        if (params == null) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("methodData", params);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("StorePaymentMethod"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Update existing payment method for user
 *
 * @param cardId - id of payment method which has to be updated, can't be empty or null
 * @param accountValue2 - secondary card value if any
 * @param title - card title
 * @param expMonth - card expiry month
 * @param expYear - card expiry year (requires 4 digits)
 * @param paymentMethodKey - defines card type, should be taken from the default list which could be received using
 *   getPaymentAndRelationListsWithCallback method, can't be empty or null
 * @param paymentMethodGroupKey - defines card type, should be taken from the default list which could be received
 *   using getPaymentAndRelationListsWithCallback method, can't be empty or null
 * @param iconUrl - card icon url
 * @param display - card display name
 * @param ownerName - card owner name
 * @param isDefault - defines whether this payment method should be set as default payment method or not
 * @param usesDefaultAddress - defines whether this payment method will have a custom billing address (value set to
 *   false) or it will use address from the customer info (value set to true). In case it will use customer info
 *   address fields address1, address2, city, countryIso, stateIso and zipCode are not required and will be ignored
 * @param address1 - main address for this payment method, not required in case usesDefaultAddress is set to true
 * @param address2 - secondary address for this payment method, not required in case usesDefaultAddress is set to true
 * @param city - city for this payment method, not required in case usesDefaultAddress is set to true
 * @param countryIso - country ISO. If not null, then it should match any of country ISO codes from the list which can
 *   be received using getCountriesAndStatesWithCallback method, not required in case usesDefaultAddress is set to true
 * @param stateIso - state ISO if any. If not null, then it should match any of state ISO codes from the list which can
 *   be received using getCountriesAndStatesWithCallback method, not required in case usesDefaultAddress is set to true
 * @param zipCode - zip code for this payment method not required in case usesDefaultAddress is set to true
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void updatePaymentMethod(Context context, String cardId, String accountValue2, String title,
                                           int expMonth, int expYear, String paymentMethodKey, String paymentMethodGroupKey,
                                           String iconUrl, String display, String ownerName, boolean isDefault,
                                           boolean usesDefaultAddress, String address1, String address2, String city,
                                           String countryIso, String stateIso, String zipCode, final ReceivedBasicCallback callback) {

        if (TextUtils.isEmpty(cardId) || TextUtils.isEmpty(paymentMethodKey) || TextUtils.isEmpty(paymentMethodGroupKey)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        Calendar cal = Calendar.getInstance();
        if (expYear < cal.get(Calendar.YEAR)) {
            if (callback != null)
                callback.onResultReceived(false, "Card has already expired. Check the expiry year");
            return;
        }

        JSONObject params = createCardJson(null, accountValue2, cardId, title, expMonth-1, expYear,
                paymentMethodKey,  paymentMethodGroupKey, iconUrl, display, ownerName, isDefault,
                usesDefaultAddress, address1, address2, city, countryIso, stateIso, zipCode);
        if (params == null) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("methodData", params);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("StorePaymentMethod"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Update several shipping address for user at once
 *
 * @param paymentMethods - array containing PaymentMethod object for each payment method which has to be updated,
 *   can't be empty or null. Each PaymentMethod object should already contain all changes which have to be done
 *   for exact payment method
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void savePaymentMethods(Context context, ArrayList<PaymentMethod> paymentMethods,
                                             final ReceivedBasicCallback callback) {

        if (paymentMethods == null || (paymentMethods != null && paymentMethods.size() == 0)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONArray cardsArray = new JSONArray();
        for (PaymentMethod card : paymentMethods) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(card.getExpirationDate());
            int expYear = calendar.get(Calendar.YEAR);
            int expMonth = calendar.get(Calendar.MONTH);

            JSONObject params = createCardJson(card.getCardNumber(), card.getAccountValue2(), card.getPaymentMethodId(),
                    card.getTitle(), expMonth, expYear, card.getPaymentMethodKey(), card.getPaymentMethodGroupKey(),
                    card.getIconURL(), card.getDisplayName(), card.getOwnerName(), card.isDefault(), card.isUsesBillingAddress(),
                    card.getAddress().getAddress1(), card.getAddress().getAddress2(), card.getAddress().getCity(),
                    card.getAddress().getCountryIso(), card.getAddress().getStateIso(), card.getAddress().getZipcode());
            if (params == null) {
                if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
                return;
            }
            cardsArray.put(params);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("data", cardsArray);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("StorePaymentMethods"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Delete exact payment method
 *
 * @param cardId - id of the payment method which has to be deleted
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void deletePaymentMethod(Context context, long cardId, final ReceivedBasicCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("pmid", cardId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("DeleteStoredPaymentMethod"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean success = (boolean)Parser.getValue(response, "d", Parser.ValueType.BOOLEAN);
                        if (success) {
                            if (callback != null) callback.onResultReceived(true, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, "Something went wrong or there is no such payment method");
                        }
                    }
                }, createBasicErrorListener(callback)
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/********************************** PAYMENT METHODS SECTION END **********************************/


/************************************* FRIENDS SECTION START ************************************/

/**
 * Get list of friends of current user and text info about each friend (profile images are not
 * included; call getImageForUserWithId for each user to get corresponding image).
 *
 * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is
 *   called when the request completes. isSuccess - completion status (whether request was successful or not);
 *   result - arrayList containing Friend objects (null in case request was NOT successful); message - error
 *   description if any (null in case request was successful)
 */
    public static void getFriendList(Context context, final ReceivedListCallback callback) {
        getFriend(context, null, callback);
    }

/**
 * Get text info about exact friend of  the current user (profile image is not included; call
 * getImageForUserWithId to get it).
 *
 * @param friendId - id of the user which info has to be loaded, can't be null
 * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is
 *   called when the request completes. isSuccess - completion status (whether request was successful or not);
 *   result - arrayList containing one Friend object (null in case request was NOT successful); message -
 *   error description if any (null in case request was successful)
 */
    public static void getFriend(Context context, String friendId, final ReceivedListCallback callback) {
        JSONObject json = new JSONObject();
        try {
            if (friendId == null) json.put("destWalletId", JSONObject.NULL);
            else json.put("destWalletId", friendId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetFriends"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.isRequestSuccessful(response)) {
                            ArrayList<Object> friendsArray = Parser.parseFriendsResponse(response);
                            if (callback != null) callback.onResultReceived(true, friendsArray, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, Parser.getMessage(response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Find user by name or id. Returns results by pages.
 *
 * @param nameOrId - user name or id to perform search, can't be null
 * @param page - number of the page with results, minimum value is 1
 * @param pageSize - results amount per page
 * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is
 *   called when the request completes. isSuccess - completion status (whether request was successful or not);
 *   result - arrayList containing Friend objects (null in case request was NOT successful); message - error
 *   description if any (null in case request was successful)
 */
    public static void findFriendByIdOrName(Context context, String nameOrId, int page, int pageSize,
                                            final ReceivedListCallback callback) {

        if (nameOrId == null) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("searchTerm", nameOrId);
            json.put("page", page);
            json.put("pageSize", pageSize);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("FindFriend"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.isRequestSuccessful(response)) {
                            ArrayList<Object> friendsArray = Parser.parseFriendsResponse(response);
                            if (callback != null) callback.onResultReceived(true, friendsArray, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, Parser.getMessage(response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Remove exact user from the firend list of current user.
 *
 * @param friendId - id of the user which has to be removed from the friend list, can't be null
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void removeFriend(Context context, String friendId, final ReceivedBasicCallback callback) {
        if (friendId == null) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", friendId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("RemoveFriend"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Set relation with exact user from the firend list of current user.
 *
 * @param relation - relation type (key) to set, available relation types and their names
 *   can be received using getPaymentAndRelationListsWithCallback method
 * @param friendId - id of the user you are going to set relation with, can't be null
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void setFriendRelation(Context context, int relation, String friendId, final ReceivedBasicCallback callback) {
        if (friendId == null) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", friendId);
            json.put("relationTypeKey", relation);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SetFriendRelation"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/************************************** FRIENDS SECTION END **************************************/


/********************************* FRIEND REQUESTS SECTION START *********************************/

/**
 * Send friend request to exact user.
 *
 * @param friendId - id of the user you want to send friend request to, can't be null
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void sendFriendRequest(Context context, String friendId, final ReceivedBasicCallback callback) {
        if (friendId == null) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", friendId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("FriendRequest"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Get friend requests for current user.
 *
 * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called
 *   when the request completes. isSuccess - completion status (whether request was successful or not); result -
 *   arrayList containing Friend objects (null in case request was NOT successful); message - error description if any
 *   (null in case request was successful)
 */
    public static void getFriendRequests(Context context, final ReceivedListCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", JSONObject.NULL);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetFriendRequests"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.isRequestSuccessful(response)) {
                            ArrayList<Object> requestsArray = Parser.parseFriendsResponse(response);
                            if (callback != null) callback.onResultReceived(true, requestsArray, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, Parser.getMessage(response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Aprrove or decline exact friend request.
 *
 * @param friendId - id of the user whose friend request has to approved or declined, can't be null
 * @param approve - defines whether friend request has to approved (value true) or declined (value false)
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void replyFriendRequest(Context context, String friendId, boolean approve, final ReceivedBasicCallback callback) {
        if (friendId == null) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", friendId);
            json.put("approve", approve);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("ReplyFriendRequest"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/********************************** FRIEND REQUESTS SECTION END **********************************/


/*********************************** TRANSACTIONS SECTION START **********************************/

/**
 * Process exact cart. Should be called separately for each cart.
 *
 * @param cartCookie - cookie of the cart which has to be processed, can't be empty or null
 * @param merchantNumber - number of the merchant, can't be empty or null
 * @param totalPrice - total cart price, can't be empty or null
 * @param cartCurrencyIso - currency ISO for the cart, can't be empty or null
 * @param pin - current user pincode, can't be empty or null
 * @param paymentMethodId - id of the current user payment method, which has to be used to
 *   process the cart, can't be empty or null
 * @param addressId - id of the current user shipping address, which has to be used to
 *   process the cart, can't be empty or null
 * @param callback - callback.onResultReceived(boolean isSuccess, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); message -
 *   error description if any (null in case request was successful)
 */
    public static void processCart(Context context, String cartCookie, String merchantNumber, float totalPrice,
                                   String cartCurrencyIso, String pin, String paymentMethodId, String addressId,
                                   final ReceivedBasicCallback callback) {

        if (TextUtils.isEmpty(pin) || TextUtils.isEmpty(paymentMethodId) || TextUtils.isEmpty(addressId) ||
            TextUtils.isEmpty(merchantNumber) || TextUtils.isEmpty(cartCurrencyIso) || TextUtils.isEmpty(cartCookie)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            JSONObject params = new JSONObject();
            params.put("PinCode", pin);
            params.put("StoredPaymentMethodId", Long.valueOf(paymentMethodId));
            params.put("ShippingAddressId", Long.valueOf(addressId));
            params.put("MerchantNumber", merchantNumber);
            params.put("Amount", totalPrice);
            params.put("CurrencyIso", cartCurrencyIso);
            params.put("ShopCartCookie", cartCookie);
            json.put("data", params);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("ProcessTransaction"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Get info about exact transaction.
 *
 * @param transactionId - id of the transaction which info has to be loaded, can't be empty or null
 * @param callback - callback.onResultReceived(boolean isSuccess, Transaction transaction, String message) is called
 *   when the request completes. isSuccess - completion status (whether request was successful or not); transaction -
 *   Transaction object for the requested transaction (null in case request was NOT successful); message - error
 *   description if any (null in case request was successful)
 */
    public static void getTransaction(Context context, String transactionId, final ReceivedTransactionCallback callback) {

        if (TextUtils.isEmpty(transactionId)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("transactionId", Long.valueOf(transactionId));
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetTransaction"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject object = (JSONObject) Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                        if (object != null) {
                            Transaction transaction = Parser.parseTransaction(object);
                            if (callback != null) callback.onResultReceived(true, transaction, null);
                        } else {
                            if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Get all transactions for the current user. Returns results by pages.
 *
 * @param page - number of the page with results, minimum value is 1
 * @param pageSize - results amount per page
 * @param callback - callback.onResultReceived(boolean isSuccess, ArrayList<Object> result, String message) is called
 *   when the request completes. isSuccess - completion status (whether request was successful or not); result -
 *   arrayList containing Transaction object (null in case request was NOT successful); message - error description
 *   if any (null in case request was successful)
 */
    public static void getTransactions(Context context, int page, int pageSize, final ReceivedListCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("page", page);
            json.put("pageSize", pageSize);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetTransactions"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray result = (JSONArray)Parser.getValue(response, "d", Parser.ValueType.JSONARRAY);
                        ArrayList<Object> array = new ArrayList<>();
                        if (result != null) {
                            for (int i=0; i<result.length(); i++) {
                                try {
                                    JSONObject transObj = result.getJSONObject(i);
                                    Transaction transaction = Parser.parseTransaction(transObj);
                                    array.add(transaction);
                                } catch (Exception e) {
                                    Log.d("UserSDK", "Error when parsing transaction with index "+i);
                                }
                            }
                        }
                        if (callback != null) callback.onResultReceived(true, array, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/************************************ TRANSACTIONS SECTION END ***********************************/


/********************************** COMMON REQUESTS SECTION START ********************************/

/**
 * Get available types of payment methods and relations.
 *
 * @param callback - callback.onResultReceived(boolean isSuccess, HashMap<String,Object> result, String message) is
 *   called when the request completes. isSuccess - completion status (whether request was successful or not);
 *   result - map containing arrayLists with PaymentCardRootGroup and Relation objects
 *   (null in case request was NOT successful); message - error description if any (null in case request was successful)
 */
    public static void getPaymentAndRelationLists(Context context, final ReceivedMapCallback callback) {
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetCustomerLists"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject result = (JSONObject) Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                        if (result != null) {
                            JSONArray rootGroupResult = (JSONArray) Parser.getValue(result, "PaymentMethodGroups", Parser.ValueType.JSONARRAY);
                            JSONArray subrootGroupResult = (JSONArray) Parser.getValue(result, "PaymentMethods", Parser.ValueType.JSONARRAY);
                            if (rootGroupResult == null) rootGroupResult = new JSONArray();
                            if (subrootGroupResult == null) subrootGroupResult = new JSONArray();

                            ArrayList<PaymentCardRootGroup> paymentGroups = new ArrayList<>();

                            //Storing all subroots by roots
                            HashMap<String, PaymentCardRootGroup> roots = new HashMap<>();
                            for (int i=0; i<subrootGroupResult.length(); i++) {
                                try {
                                    JSONObject subgroup = subrootGroupResult.getJSONObject(i);
                                    String key = (String) Parser.getValue(subgroup, "GroupKey", Parser.ValueType.STRING);

                                    PaymentCardRootGroup rootModel = roots.get(key);
                                    if (rootModel == null) {
                                        rootModel = new PaymentCardRootGroup();
                                        rootModel.setName("Group "+key);
                                        roots.put(key, rootModel);
                                    }

                                    PaymentCardSubrootGroup subrootModel =  new PaymentCardSubrootGroup();
                                    subrootModel.setKey((String) Parser.getValue(subgroup, "Key", Parser.ValueType.STRING));
                                    subrootModel.setName((String) Parser.getValue(subgroup, "Name", Parser.ValueType.STRING));
                                    subrootModel.setIcon((String) Parser.getValue(subgroup, "Icon", Parser.ValueType.STRING));
                                    subrootModel.setGroupKey((String) Parser.getValue(subgroup, "GroupKey", Parser.ValueType.STRING));
                                    subrootModel.setValue1Caption((String) Parser.getValue(subgroup, "Value1Caption", Parser.ValueType.STRING));
                                    subrootModel.setValue1ValidationRegex((String) Parser.getValue(subgroup, "Value1ValidationRegex", Parser.ValueType.STRING));
                                    subrootModel.setValue2Caption((String) Parser.getValue(subgroup, "Value2Caption", Parser.ValueType.STRING));
                                    subrootModel.setValue2ValidationRegex((String) Parser.getValue(subgroup, "Value2ValidationRegex", Parser.ValueType.STRING));
                                    subrootModel.setHasExpirationDate((boolean) Parser.getValue(subgroup, "HasExpirationDate", Parser.ValueType.BOOLEAN));
                                    rootModel.getSubGroups().add(subrootModel);
                                } catch (Exception e) {
                                    Log.d("UserSDK", "Error when parsing card group with index "+i);
                                }
                            }

                            //Storing root data
                            HashMap<String, PaymentCardRootGroup> rootsData = new HashMap<>();
                            for (int i=0; i<rootGroupResult.length(); i++) {
                                try {
                                    JSONObject group = rootGroupResult.getJSONObject(i);
                                    String key = (String) Parser.getValue(group, "Key", Parser.ValueType.STRING);

                                    PaymentCardRootGroup rootModel = new PaymentCardRootGroup();
                                    rootModel.setIcon((String) Parser.getValue(group, "Icon", Parser.ValueType.STRING));
                                    rootModel.setKey((String) Parser.getValue(group, "Key", Parser.ValueType.STRING));
                                    rootModel.setName((String) Parser.getValue(group, "Name", Parser.ValueType.STRING));
                                    rootsData.put(key, rootModel);
                                } catch (Exception e) {
                                    Log.d("UserSDK", "Error when parsing card group with index "+i);
                                }
                            }

                            //Setting real root data to roots which hold subroots
                            Set<String> keySet = roots.keySet();
                            String[] allKeys = keySet.toArray(new String[keySet.size()]);
                            for (String key : allKeys) {
                                PaymentCardRootGroup rootModel = rootsData.get(key);
                                PaymentCardRootGroup savedRoot = roots.get(key);
                                if (rootModel != null) {
                                    savedRoot.setIcon(rootModel.getIcon());
                                    savedRoot.setKey(rootModel.getKey());
                                    savedRoot.setName(rootModel.getName());
                                }
                                paymentGroups.add(savedRoot);
                            }

                            JSONArray relationsResult = (JSONArray) Parser.getValue(result, "RelationTypes", Parser.ValueType.JSONARRAY);
                            if (relationsResult == null) relationsResult = new JSONArray();
                            ArrayList<Object> relations = new ArrayList<>();
                            for (int i=0; i<relationsResult.length(); i++) {
                                try {
                                    JSONObject relationObj = relationsResult.getJSONObject(i);
                                    Relation relation = new Relation();
                                    relation.setKey((String) Parser.getValue(relationObj, "Key", Parser.ValueType.STRING));
                                    relation.setName((String) Parser.getValue(relationObj, "Name", Parser.ValueType.STRING));
                                    relation.setIcon((String) Parser.getValue(relationObj, "Icon", Parser.ValueType.STRING));
                                    relations.add(relation);
                                } catch (Exception e) {
                                    Log.d("UserSDK", "Error when parsing relation with index "+i);
                                }
                            }

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("paymentGroups", paymentGroups);
                            map.put("relations", relations);
                            if (callback != null) callback.onResultReceived(true, map, null);

                        } else {
                            if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/**
 * Get profile image of any user
 *
 * @param userId - id of the user whose profile image has to be loaded, can't be empty or null
 * @param callback - callback.onResultReceived(boolean isSuccess, Bitmap image, String message) is called when
 *   the request completes. isSuccess - completion status (whether request was successful or not); image -
 *   Bitmap for requested user image (null in case request was NOT successful); message - error description if
 *   any (null in case request was successful)
 */
    public static void getImageForUser(Context context, String userId, final ReceivedImageCallback callback) {
        if (TextUtils.isEmpty(userId)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        CustomHeadersImageRequest imageRequest = new CustomHeadersImageRequest(getImageUrl(userId), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (callback != null) callback.onResultReceived(true, response, null);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
            }
        });
        imageRequest.addHeader("Cookie", "credentialsToken=" + UserSession.getInstance().getCredentialsToken(context));
        Volley.newRequestQueue(context).add(imageRequest);
    }

/**
 * Get URL for profile image of any user
 *
 * @param userId - id of the user whose profile image URL has to be created
 * @return image URL
 */
    public static String getImageUrl(String userId) {
        return createUrl("GetImage")+"?walletId="+userId+"&asRaw=true";
    }

/**
 * Get list of countries, states and their ISO codes
 *
 * @param callback - callback.onResultReceived(boolean isSuccess, HashMap<String,Object> result, String message) is
 *   called when the request completes. isSuccess - completion status (whether request was successful or not);
 *   result - map containing countries and states (null in case request was NOT successful); message - error
 *   description if any (null in case request was successful)
 */
    public static void getCountriesAndStates(Context context, final ReceivedMapCallback callback) {
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetLists"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject root = (JSONObject) Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                        JSONArray countriesJson = (JSONArray) Parser.getValue(root, "Countries", Parser.ValueType.JSONARRAY);
                        JSONArray usaStatesJson = (JSONArray) Parser.getValue(root, "UsaStates", Parser.ValueType.JSONARRAY);
                        JSONArray canadaStatesJson = (JSONArray) Parser.getValue(root, "CanadaStates", Parser.ValueType.JSONARRAY);
                        //Languages

                        ArrayList<Country> countries = new ArrayList<>();
                        ArrayList<State> usaStates = new ArrayList<>();
                        ArrayList<State> canadaStates = new ArrayList<>();

                        if (countriesJson != null) {
                            for (int i=0; i<countriesJson.length(); i++) {
                                try {
                                    JSONObject obj = countriesJson.getJSONObject(i);
                                    Country country = new Country();
                                    country.setKey((String) Parser.getValue(obj, "Key", Parser.ValueType.STRING));
                                    country.setName((String) Parser.getValue(obj, "Name", Parser.ValueType.STRING));
                                    country.setIcon((String) Parser.getValue(obj, "Icon", Parser.ValueType.STRING));
                                    countries.add(country);
                                } catch (Exception e) {
                                    Log.d("UserSDK", "Error when parsing country with index "+i);
                                }
                            }
                        }

                        if (usaStatesJson != null) {
                            for (int i=0; i<usaStatesJson.length(); i++) {
                                try {
                                    JSONObject obj = usaStatesJson.getJSONObject(i);
                                    State state = new State();
                                    state.setKey((String) Parser.getValue(obj, "Key", Parser.ValueType.STRING));
                                    state.setName((String) Parser.getValue(obj, "Name", Parser.ValueType.STRING));
                                    state.setIcon((String) Parser.getValue(obj, "Icon", Parser.ValueType.STRING));
                                    usaStates.add(state);
                                } catch (Exception e) {
                                    Log.d("UserSDK", "Error when parsing state with index "+i);
                                }
                            }
                        }

                        if (canadaStatesJson != null) {
                            for (int i=0; i<canadaStatesJson.length(); i++) {
                                try {
                                    JSONObject obj = canadaStatesJson.getJSONObject(i);
                                    State state = new State();
                                    state.setKey((String) Parser.getValue(obj, "Key", Parser.ValueType.STRING));
                                    state.setName((String) Parser.getValue(obj, "Name", Parser.ValueType.STRING));
                                    state.setIcon((String) Parser.getValue(obj, "Icon", Parser.ValueType.STRING));
                                    canadaStates.add(state);
                                } catch (Exception e) {
                                    Log.d("UserSDK", "Error when parsing state with index "+i);
                                }
                            }
                        }

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("Countries", countries);
                        map.put("UsaStates", usaStates);
                        map.put("CanadaStates", canadaStates);
                        if (callback != null) callback.onResultReceived(true, map, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/*********************************** COMMON REQUESTS SECTION END *********************************/


/************************************* KEEP ALIVE SECTION START **********************************/

    public static void keepAliveSession(Context context, final ReceivedBasicCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", UserSession.getInstance().getCredentialsToken(context));
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("KeepAlive"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean jsonResult = (boolean)Parser.getValue(response, "d", Parser.ValueType.BOOLEAN);
                        if (callback != null) callback.onResultReceived(jsonResult, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback != null) callback.onResultReceived(false, Parser.getErrorText(error));
                    }
                }
        );
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken(context)+"; HttpOnly");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/************************************** KEEP ALIVE SECTION END ***********************************/


    private static JSONObject createAddressJson(String title, String address1, String address2, String city, String countryIso,
                                                String stateIso, String zipCode, String comment, String addressId, boolean isDefault) {

        JSONObject json = new JSONObject();
        try {
            json.put("Title", title);
            json.put("AddressLine1", address1);
            json.put("AddressLine2", address2);
            json.put("City", city);
            json.put("CountryIso", countryIso);
            json.put("StateIso", stateIso);
            json.put("PostalCode", zipCode);
            json.put("IsDefault", isDefault);
            json.put("Comment", comment);
            json.put("ID", addressId);
            return json;
        } catch (JSONException e) {
            return null;
        }
    }

    private static JSONObject createCardJson(String cardNumber, String accountValue2, String cardId, String title, int expMonth,
                                             int expYear, String paymentMethodKey, String paymentMethodGroupKey, String iconUrl,
                                             String display, String ownerName, boolean isDefault, boolean usesDefaultAddress,
                                             String address1, String address2, String city, String countryIso, String stateIso, String zipCode) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, expYear);
        cal.set(Calendar.MONTH, expMonth);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String expDate = formatter.format(cal.getTime());

        if (cardId == null) cardId = "0";
        JSONObject json = new JSONObject();
        try {
            json.put("ID", cardId);
            json.put("Title", title);
            json.put("PaymentMethodKey", paymentMethodKey);
            json.put("PaymentMethodGroupKey", paymentMethodGroupKey);
            json.put("OwnerName", ownerName);
            json.put("ExpirationDate", expDate);
            json.put("IsDefault", isDefault);
            json.put("Icon", iconUrl);
            json.put("Display", display);
            json.put("AccountValue1", cardNumber);
            json.put("AccountValue2", accountValue2);
            //Last4Digits
            JSONObject billingAddress = new JSONObject();
            if (!usesDefaultAddress) {
                billingAddress.put("AddressLine1", address1);
                billingAddress.put("AddressLine2", address2);
                billingAddress.put("City", city);
                billingAddress.put("PostalCode", zipCode);
                billingAddress.put("StateIso", stateIso);
                billingAddress.put("CountryIso", countryIso);
            }
            json.put("Address", billingAddress);
            return json;
        } catch (JSONException e) {
            return null;
        }
    }

    protected static String createUrl(String method) {
        return Constants.SERVICE_URL+method;
    }

    private static boolean validatePassword(String password) {
        int numOfDigits = 0, numOfChars = 0;
        if (password.length() < 8) return false;
        for (int i = 0; i < password.length(); i++) {
            char chrValue = password.charAt(i);
            if(chrValue >= '0' && chrValue <= '9') numOfDigits ++;
            else numOfChars++;
        }
        return (numOfDigits >= 2) && (numOfChars >= 2);
    }

    private static String saveBitmap(Context context, Bitmap bitmap, boolean recycle) {
        String filename = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String cachePath = context.getCacheDir().getPath();
        FileOutputStream out=null;
        String filePath = null;
        try {
            File f = new File(cachePath,filename);
            if(!f.exists()) f.createNewFile();
            out = new FileOutputStream(f);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) filePath = f.getPath();
        } catch (Exception e) {
            filePath = null;
            Log.e("UserSDK", "Could not cache bitmap. Bitmap will not be saved", e);
        } finally {
            try { out.close(); } catch(Throwable ignore) {}
            if(recycle) bitmap.recycle();
            return filePath;
        }
    }

    private static Response.Listener<JSONObject> createBasicSuccessListener(final ReceivedBasicCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (Parser.isRequestSuccessful(response)) {
                    if (callback != null) callback.onResultReceived(true, null);
                } else {
                    if (callback != null) callback.onResultReceived(false, Parser.getMessage(response));
                }
            }
        };
    }

    private static Response.ErrorListener createBasicErrorListener(final ReceivedBasicCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (callback != null) callback.onResultReceived(false, Parser.getErrorText(error));
            }
        };
    }
}
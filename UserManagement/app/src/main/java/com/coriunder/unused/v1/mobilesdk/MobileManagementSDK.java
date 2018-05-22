package com.coriunder.unused.v1.mobilesdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coriunder.unused.v1.basesdk.BaseParser;
import com.coriunder.unused.v1.basesdk.CustomHeadersImageRequest;
import com.coriunder.unused.v1.basesdk.CustomHeadersJsonObjectRequest;
import com.coriunder.unused.v1.basesdk.callbacks.ReceivedBasicCallback;
import com.coriunder.unused.v1.basesdk.callbacks.ReceivedListCallback;
import com.coriunder.unused.v1.mobilesdk.callbacks.ReceivedImageCallback;
import com.coriunder.unused.v1.mobilesdk.callbacks.ReceivedMapCallback;
import com.coriunder.unused.v1.mobilesdk.callbacks.ReceivedTransactionCallback;
import com.coriunder.unused.v1.mobilesdk.models.Product;
import com.coriunder.unused.v1.mobilesdk.models.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by 1 on 25.02.2016.
 */
public class MobileManagementSDK {
    private static String signCode;

    /*httpPost.setHeader("Content-type", "application/json; charset=" + HTTP.UTF_8);
    httpPost.setHeader("Accept-Charset", HTTP.UTF_8);*/

    public static void login(final Context applicationContext, final String email, final String userName,
                                      String password, final ReceivedMapCallback callback) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(userName)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("userName", userName);
            json.put("password", password);
            json.put("deviceId", getDeviceId(applicationContext));
            json.put("appVersion", getVersion(applicationContext));
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Login"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                if (jsonResult == null) {
                    if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                    return;
                }

                if ((boolean)Parser.getValue(jsonResult, "IsLoginSuccessful", BaseParser.ValueType.BOOLEAN)) {
                    String merchant = (String)Parser.getValue(jsonResult, "MerchantNumber", Parser.ValueType.STRING);
                    boolean isDeviceActivated = (boolean)Parser.getValue(jsonResult, "IsDeviceActivated", Parser.ValueType.BOOLEAN);
                    String credentialsToken = (String)Parser.getValue(jsonResult, "CredentialsToken", Parser.ValueType.STRING);

                    //Start session
                    MerchantSession.getInstance().start(applicationContext, credentialsToken);

                    //Save login data
                    SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
                    preferences.putString("email", email);
                    preferences.putString("username", userName);
                    preferences.putString("merchant", merchant);
                    preferences.apply();

                    //Parse result
                    final HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("LastLogin", Parser.getReadableDate(jsonResult, "LastLogin"));
                    hashMap.put("MerchantNumber", merchant);
                    hashMap.put("IsDeviceBlocked", Parser.getValue(jsonResult, "IsDeviceBlocked", Parser.ValueType.BOOLEAN));
                    hashMap.put("VersionUpdateRequired", Parser.getValue(jsonResult, "VersionUpdateRequired", Parser.ValueType.BOOLEAN));
                    hashMap.put("IsDeviceRegistered", Parser.getValue(jsonResult, "IsDeviceRegistered", Parser.ValueType.BOOLEAN));
                    hashMap.put("IsDeviceActivated", isDeviceActivated);

                    //Try to get activation code
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
                    String activationCode;
                    try { activationCode = Cryptor.decrypt(prefs.getString("activationCode", "")); }
                    catch (Exception e) { activationCode = ""; }

                    //if device is activated - get settings
                    if (isDeviceActivated && activationCode.length() > 0) {
                        getSettings(applicationContext, hashMap, new ReceivedMapCallback() {
                            @Override
                            public void onResultReceived(boolean isSuccess, HashMap<String, Object> result, String message) {
                                if (callback != null) {
                                    if (isSuccess) callback.onResultReceived(true, result, null);
                                    else callback.onResultReceived(true, hashMap, "Logged in successfully, " +
                                            "but an error occurred while getting settings. Use getSettings " +
                                            "to force loading settings manually");
                                }
                            }
                        });
                    } else {
                        if (callback != null) callback.onResultReceived(true, hashMap, null);
                    }

                } else {
                    String message = (String)Parser.getValue(jsonResult, "LoginMessage", Parser.ValueType.STRING);
                    if (callback != null) callback.onResultReceived(false, null, message);
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
            }
        });
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(applicationContext).add(postRequest);
    }

    public static void relogin(final Context applicationContext, String password, final ReceivedMapCallback callback) {
        if (TextUtils.isEmpty(password)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String email = prefs.getString("email", "");
        String userName = prefs.getString("username", "");

        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", getMerchantNumber(applicationContext, false));
            json.put("email", email);
            json.put("userName", userName);
            json.put("password", password);
            json.put("deviceId", getDeviceId(applicationContext));
            json.put("appVersion", getVersion(applicationContext));
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Relogin"), json,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                if (jsonResult == null) {
                    if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                    return;
                }

                if ((boolean)Parser.getValue(jsonResult, "IsLoginSuccessful", BaseParser.ValueType.BOOLEAN)) {
                    String merchant = (String)Parser.getValue(jsonResult, "MerchantNumber", Parser.ValueType.STRING);
                    boolean isDeviceActivated = (boolean)Parser.getValue(jsonResult, "IsDeviceActivated", Parser.ValueType.BOOLEAN);
                    String credentialsToken = (String)Parser.getValue(jsonResult, "CredentialsToken", Parser.ValueType.STRING);

                    //Start session
                    MerchantSession.getInstance().start(applicationContext, credentialsToken);

                    //Save login data
                    SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
                    preferences.putString("merchant", merchant);
                    preferences.apply();

                    //Parse result
                    final HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("LastLogin", Parser.getReadableDate(jsonResult, "LastLogin"));
                    hashMap.put("MerchantNumber", merchant);
                    hashMap.put("IsDeviceBlocked", Parser.getValue(jsonResult, "IsDeviceBlocked", Parser.ValueType.BOOLEAN));
                    hashMap.put("VersionUpdateRequired", Parser.getValue(jsonResult, "VersionUpdateRequired", Parser.ValueType.BOOLEAN));
                    hashMap.put("IsDeviceRegistered", Parser.getValue(jsonResult, "IsDeviceRegistered", Parser.ValueType.BOOLEAN));
                    hashMap.put("IsDeviceActivated", isDeviceActivated);

                    //Try to get activation code
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
                    String activationCode;
                    try { activationCode = Cryptor.decrypt(prefs.getString("activationCode", "")); }
                    catch (Exception e) { activationCode = ""; }

                    if (isDeviceActivated && activationCode.length() > 0) {
                        getSettings(applicationContext, hashMap, new ReceivedMapCallback() {
                            @Override
                            public void onResultReceived(boolean isSuccess, HashMap<String, Object> result, String message) {
                                if (callback != null) {
                                    if (isSuccess) callback.onResultReceived(true, result, null);
                                    else callback.onResultReceived(true, hashMap, "Logged in successfully, " +
                                            "but an error occurred while getting settings. Use getSettings " +
                                            "to force loading settings manually");
                                }
                            }
                        });
                    } else {
                        if (callback != null) callback.onResultReceived(true, hashMap, null);
                    }

                } else {
                    String message = (String)Parser.getValue(jsonResult, "LoginMessage", Parser.ValueType.STRING);
                    if (callback != null) callback.onResultReceived(false, null, message);
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
            }
        });
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(applicationContext).add(postRequest);
    }

    public static void registerDevice(Context context, String phoneNumber, final ReceivedBasicCallback callback) {
        if (TextUtils.isEmpty(phoneNumber)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("deviceId", getDeviceId(context));
            json.put("phoneNumber", phoneNumber);
            json.put("signature", createCallSignature(json, ""));
            /*ORDER OF DATA!!! JSON SENDS WRONG ORDER? ENCODES WRONG ORDER - YES, but what does server receive?
              d8111edf-ef20-45e2-9eca-bc527cb1ab93fa966faf18026080972549200123
              zLQtgv5WCLdRWW21doPTa0p28RnCpBSAtTikKc8HuE0=*/
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("RegisterDevice"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                if (jsonResult == null) {
                    if (callback != null) callback.onResultReceived(false, "Something went wrong");
                    return;
                }

                boolean isDeviceRegistered = (boolean)Parser.getValue(jsonResult, "IsSuccessful", BaseParser.ValueType.BOOLEAN);
                if (isDeviceRegistered) {
                    String signature = (String)Parser.getValue(jsonResult, "Signature", BaseParser.ValueType.STRING);
                    //tb6kG2xiP3wJ8b8k3K5Y66s8DN2QrZZrxDpFtEhn4Ss=
                    if (callback != null) callback.onResultReceived(true, null);
                } else {
                    String message = (String)Parser.getValue(jsonResult, "Reason", BaseParser.ValueType.STRING);
                    if (callback != null) callback.onResultReceived(false, message);
                }
            }
        }, createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void activateDevice(final Context context, final String activationCode, final ReceivedMapCallback callback) {
        if (TextUtils.isEmpty(activationCode)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("deviceId", getDeviceId(context));
            json.put("signature", createCallSignature(json, activationCode));
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("ActivateDevice"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                if (jsonResult == null) {
                    if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                    return;
                }

                boolean isDeviceActivated = (boolean)Parser.getValue(jsonResult, "Value", BaseParser.ValueType.BOOLEAN);
                if (isDeviceActivated) {
                    try {
                        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        prefs.putString("activationCode", Cryptor.encrypt(activationCode));
                        prefs.apply();
                    } catch (Exception e) {
                        Log.d("ModileSDK", "An error occurred while saving activation code");
                    }
                    getSettings(context, new ReceivedMapCallback() {
                        @Override
                        public void onResultReceived(boolean isSuccess, HashMap<String, Object> result, String message) {
                            if (callback != null) {
                                if (isSuccess) callback.onResultReceived(true, result, null);
                                else callback.onResultReceived(true, null, "Activated successfully, " +
                                        "but an error occurred while getting settings. Use getSettings " +
                                        "to force loading settings manually");
                            }
                        }
                    });
                } else {
                    if (callback != null) callback.onResultReceived(false, null, "Something went wrong or wrong activation code entered");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
            }
        });
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/************************************** LOGIN SECTION END **************************************/


/********************************** TRANSACTION SECTION START **********************************/

    public static void getTransactionById(Context context, long transactionId, final ReceivedTransactionCallback callback) {
        //TO DO: signed with activationCode, isDemo
        boolean isDemo = false;

        if (isDemo) {
            Transaction trans = new Transaction();
            trans.setBaseAmount(new BigDecimal("12.00"));
            trans.setCurrency("USD");
            if (callback != null) callback.onResultReceived(false, trans, null);

        } else {
            JSONObject json = new JSONObject();
            try {
                json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
                json.put("transactionId", transactionId);
                json.put("signature", signCode);
            } catch (JSONException e) {
                if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
                return;
            }

            //TO DO: parse
            CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                    createUrl("GetTransaction"), json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                    if (jsonResult == null) {
                        if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                        return;
                    }
                    //TO DO: validate signature after request completes

                    JSONObject data = (JSONObject)Parser.getValue(jsonResult, "Data", BaseParser.ValueType.JSONOBJECT);
                    Transaction trans = new Transaction();
                    trans.setTransactionID((String) Parser.getValue(data, "ID", BaseParser.ValueType.STRING));
                    trans.setCurrency((String)Parser.getValue(data, "Currency", BaseParser.ValueType.STRING));
                    trans.setTransactionDate(Parser.getReadableDate(data, "InsertDate"));
                    trans.setEmail((String)Parser.getValue(data, "Email", BaseParser.ValueType.STRING));
                    trans.setPhone((String)Parser.getValue(data, "Phone", BaseParser.ValueType.STRING));
                    trans.setCreditcardDisplay((String)Parser.getValue(data, "Card", BaseParser.ValueType.STRING));
                    trans.setComment((String)Parser.getValue(data, "Comment", BaseParser.ValueType.STRING));
                    trans.setIsRefunded((boolean)Parser.getValue(data, "IsRefunded", BaseParser.ValueType.BOOLEAN));
                    trans.setIsManual((boolean)Parser.getValue(data, "IsManual", BaseParser.ValueType.BOOLEAN));
                    trans.setPaymentDetails((String)Parser.getValue(data, "PaymentDetails", BaseParser.ValueType.STRING));
                    trans.setCardholderName((String)Parser.getValue(data, "CardholderName", BaseParser.ValueType.STRING));

                    String amount = (String)Parser.getValue(data, "Amount", BaseParser.ValueType.STRING);
                    if (TextUtils.isEmpty(amount)) amount = "0";
                    trans.setBaseAmount(new BigDecimal(amount));

                    if (callback != null) callback.onResultReceived(true, trans, null);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                }
            });
            //postRequest.addHeader("Content-Type", "application/json");
            postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                    Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(context).add(postRequest);
        }
    }

    public static void getTransactionByQrId(Context context, String transactionQrId, final ReceivedTransactionCallback callback) {
        if (TextUtils.isEmpty(transactionQrId)) {
            if (callback != null) callback.onResultReceived(false, null, "Attempt to send an empty value to the service");
            return;
        }

        //TO DO: signed with activationCode, isDemo
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("transQrId", transactionQrId);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        //TO DO: parse
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetTransWithQrId"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                if (jsonResult == null) {
                    if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                    return;
                }
                //TO DO: validate signature after request completes

                Transaction trans = new Transaction();
                trans.setTransactionID((String) Parser.getValue(jsonResult, "TransactionNumber", BaseParser.ValueType.STRING));
                trans.setReplyCode((String) Parser.getValue(jsonResult, "ReplyCode", BaseParser.ValueType.STRING));
                trans.setReplyText((String) Parser.getValue(jsonResult, "ReplyText", BaseParser.ValueType.STRING));
                trans.setIsAccepted((boolean) Parser.getValue(jsonResult, "IsSuccess", BaseParser.ValueType.BOOLEAN));
                trans.setIsProccessed(true);

                if (callback != null) callback.onResultReceived(true, trans, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
            }
        });
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void getTransactionHistory(Context context, int page, int pageSize, final ReceivedListCallback callback) {
        //TO DO: signed with activationCode, isDemo
        boolean isDemo = false;

        if (isDemo) {
            ArrayList<Object> transactions = new ArrayList<>();
            Transaction trans = new Transaction();
            trans.setBaseAmount(new BigDecimal("12.00"));
            trans.setCurrency("USD");
            transactions.add(trans);
            if (callback != null) callback.onResultReceived(true, transactions, null);

        } else {
            JSONObject json = new JSONObject();
            try {
                json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
                json.put("page", page);
                json.put("pageSize", pageSize);
            } catch (JSONException e) {
                if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
                return;
            }

            //TO DO: parse
            CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                    createUrl("GetTransactionHistory"), json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray jsonResult = (JSONArray)Parser.getValue(response, "d", Parser.ValueType.JSONARRAY);
                    if (jsonResult == null) {
                        if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                        return;
                    }
                    //TO DO: validate signature after request completes

                    ArrayList<Object> transactions = new ArrayList<>();
                    for (int i=0; i<jsonResult.length(); i++) {
                        try {
                            JSONObject item = jsonResult.getJSONObject(i);
                            Transaction trans = new Transaction();
                            trans.setTransactionID((String) Parser.getValue(item, "ID", BaseParser.ValueType.STRING));
                            trans.setCurrency((String) Parser.getValue(item, "Currency", BaseParser.ValueType.STRING));
                            trans.setTransactionDate(Parser.getReadableDate(item, "InsertDate"));
                            String amount = (String)Parser.getValue(item, "Amount", BaseParser.ValueType.STRING);
                            if (TextUtils.isEmpty(amount)) amount = "0";
                            trans.setBaseAmount(new BigDecimal(amount));
                            transactions.add(trans);
                        } catch (Exception e) {
                            Log.d("BalanceSDK", "Error when parsing transaction with index " + i);
                        }
                    }
                    if (callback != null) callback.onResultReceived(true, transactions, null);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                }
            });
            //postRequest.addHeader("Content-Type", "application/json");
            postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                    Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(context).add(postRequest);
        }
    }

/*********************************** TRANSACTION SECTION END ***********************************/


/************************************* OTHER SECTION START *************************************/

    public static void getQRCodeImage(Context context, Transaction transaction, final ReceivedImageCallback callback) {
        //TO DO: signature; replace getBaseAmount with getTotalAmount (with VAT)
        transaction.setQrTransactionCode(UUID.randomUUID().toString());
        String merchant = PreferenceManager.getDefaultSharedPreferences(context).getString("merchant", "");

        StringBuilder urlParams = new StringBuilder();
        urlParams.append("merchantID=" + merchant);
        urlParams.append("&trans_amount=" + transaction.getBaseAmount());
        urlParams.append("&trans_currency=" + transaction.getCurrency());
        urlParams.append("&trans_comment=" + transaction.getPaymentDetails());
        urlParams.append("&disp_payFor=" + transaction.getPaymentDetails());
        urlParams.append("&trans_installments=" + transaction.getInstallments());
        urlParams.append("&QrCodeID=" + transaction.getQrTransactionCode());

        CustomHeadersImageRequest imageRequest = new CustomHeadersImageRequest(
                getImageUrl(MerchantSession.getInstance().getCredentialsToken(), signCode, urlParams.toString()),
                new Response.Listener<Bitmap>() {
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
        Volley.newRequestQueue(context).add(imageRequest);
    }

    public static void getProductList(Context context, final ReceivedListCallback callback) {
        boolean isDemo = false;

        if (isDemo) {
            ArrayList<Object> products = new ArrayList<>();
            Product demoProd = new Product();
            demoProd.setProductName("Demo Product");
            demoProd.setProductPrice(new BigDecimal("12.30"));
            products.add(demoProd);
            if (callback != null) callback.onResultReceived(true, products, null);
        } else {
            JSONObject json = new JSONObject();
            try {
                json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            } catch (JSONException e) {
                if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
                return;
            }

            //TO DO: parse
            CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                    createUrl("GetProductList"), json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray jsonResult = (JSONArray)Parser.getValue(response, "d", Parser.ValueType.JSONARRAY);
                    if (jsonResult == null) {
                        if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                        return;
                    }
                    //TO DO: validate signature after request completes

                    ArrayList<Object> products = new ArrayList<>();
                    for (int i=0; i<jsonResult.length(); i++) {
                        try {
                            JSONObject item = jsonResult.getJSONObject(i);
                            Product prod = new Product();
                            prod.setProductDescription((String) Parser.getValue(item, "Description", BaseParser.ValueType.STRING));
                            prod.setProductName((String) Parser.getValue(item, "Name", BaseParser.ValueType.STRING));
                            String amount = (String)Parser.getValue(item, "Price", BaseParser.ValueType.STRING);
                            if (TextUtils.isEmpty(amount)) amount = "0";
                            prod.setProductPrice(new BigDecimal(amount));
                            products.add(prod);
                        } catch (Exception e) {
                            Log.d("BalanceSDK", "Error when parsing transaction with index " + i);
                        }
                    }
                    if (callback != null) callback.onResultReceived(true, products, null);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                }
            });
            //postRequest.addHeader("Content-Type", "application/json");
            postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                    Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(context).add(postRequest);
        }
    }

    public static void getSettings(Context context, final ReceivedMapCallback callback) {
        getSettings(context, null, callback);
    }

    private static void getSettings(Context context, final HashMap<String, Object> loginMap, final ReceivedMapCallback callback) {
        //TO DO: signed with activationCode, isDemo
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("signature", signCode);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        //TO DO: isSuccess/message;
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetSettings"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                if (jsonResult == null) {
                    if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                    return;
                }
                //TO DO: validate signature after request completes

                JSONObject settingsObj = (JSONObject)Parser.getValue(jsonResult, "Settings", BaseParser.ValueType.JSONOBJECT);
                HashMap<String,Object> hashMap = new HashMap<>();
                if (loginMap != null) hashMap.putAll(loginMap);
                hashMap.put("IsMobileAppEnabled", Parser.getValue(settingsObj, "IsMobileAppEnabled", Parser.ValueType.BOOLEAN));
                hashMap.put("IsCardNotPresent", Parser.getValue(settingsObj, "IsCardNotPresent", Parser.ValueType.BOOLEAN));
                hashMap.put("IsInstallments", Parser.getValue(settingsObj, "IsInstallments", Parser.ValueType.BOOLEAN));
                hashMap.put("IsRefund", Parser.getValue(settingsObj, "IsRefund", Parser.ValueType.BOOLEAN));
                hashMap.put("IsAuthorization", Parser.getValue(settingsObj, "IsAuthorization", Parser.ValueType.BOOLEAN));
                hashMap.put("IsFullNameRequired", Parser.getValue(settingsObj, "IsFullNameRequired", Parser.ValueType.BOOLEAN));
                hashMap.put("IsOwnerSignRequired", Parser.getValue(settingsObj, "IsOwnerSignRequired", Parser.ValueType.BOOLEAN));
                hashMap.put("IsPhoneRequired", Parser.getValue(settingsObj, "IsPhoneRequired", Parser.ValueType.BOOLEAN));
                hashMap.put("IsEmailRequired", Parser.getValue(settingsObj, "IsEmailRequired", Parser.ValueType.BOOLEAN));
                hashMap.put("IsPersonalNumberRequired", Parser.getValue(settingsObj, "IsPersonalNumberRequired", Parser.ValueType.BOOLEAN));
                hashMap.put("MerchantName", Parser.getValue(settingsObj, "MerchantName", Parser.ValueType.STRING));
                hashMap.put("MerchantAddress", Parser.getValue(settingsObj, "MerchantAddress", Parser.ValueType.STRING));
                hashMap.put("SupportedCurrencies", ((String)Parser.getValue(settingsObj, "SupportedCurrencies", Parser.ValueType.STRING)).split(","));

                String valueAddedTax = (String)Parser.getValue(settingsObj, "ValueAddedTax", Parser.ValueType.STRING);
                if (TextUtils.isEmpty(valueAddedTax)) valueAddedTax = "0";
                hashMap.put("ValueAddedTax", valueAddedTax);

                if (callback != null) callback.onResultReceived(true, hashMap, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
            }
        });
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void keepAliveSession(Context context, final ReceivedBasicCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
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
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void serverLog(Context context, int severityId, String message,
                                               String longMessage, final ReceivedBasicCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("severityId", severityId);
            json.put("message", message);
            json.put("longMessage", longMessage);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Log"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    private static boolean signImgDone;
    private static boolean cardImgDone;
    private static String resultMessage;
    public static void sendPayment(final Context context, final Transaction transaction, final ReceivedTransactionCallback callback) {

        Integer transType = 0;
        Integer typeCredit = 1;
        if (transaction.getInstallments() > 1) {
            transType = 0;
            typeCredit = 8;
        } else {
            switch (transaction.getTransactionType()) {
                case AUTHORIZE_AND_CAPTURE:
                    transType = 0;
                    typeCredit = 1;
                    break;
                case AUTHORIZE_ONLY:
                    transType = 1;
                    typeCredit = 1;
                    break;
                case REFUND:
                    transType = 0;
                    typeCredit = 0;
                    break;
                default:
                    break;
            }
        }

        //Signature; totalAmount with VAT instead of baseAmount
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("cardholderName", transaction.getCardholderName());
            json.put("transType", transType);
            json.put("typeCredit", typeCredit);
            json.put("creditcard", transaction.getCreditcardInternal());
            json.put("cvv", transaction.getCvv());
            json.put("expirationMonth", new Integer(transaction.getExpirationMonth()));
            json.put("expirationYear", new Integer(transaction.getExpirationYear()));
            json.put("currency", transaction.getCurrency());
            json.put("amount", transaction.getBaseAmount());
            json.put("payments", transaction.getInstallments());
            json.put("email", transaction.getEmail());
            json.put("personalNumber", transaction.getPersonalNumber());
            json.put("phone", transaction.getPhone());
            json.put("track2", transaction.getTrack2());
            json.put("deviceId", getDeviceId(context));
            json.put("payFor", transaction.getPaymentDetails());
            json.put("productId", 0);
            json.put("signature", signCode);
        } catch (Exception e1) {
            if (callback != null) callback.onResultReceived(false, null, "An error occurred while creating request");
            return;
        }

        //TO DO: parse
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Process3"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                transaction.clearSensitiveData();
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                if (jsonResult == null) {
                    if (callback != null) callback.onResultReceived(false, null, "Something went wrong");
                    return;
                }
                //TO DO: validate signature after request completes

                final Transaction trans = new Transaction();
                trans.setTransactionID((String) Parser.getValue(jsonResult, "TransactionNumber", BaseParser.ValueType.STRING));
                trans.setReplyCode((String) Parser.getValue(jsonResult, "ReplyCode", BaseParser.ValueType.STRING));
                trans.setReplyText((String) Parser.getValue(jsonResult, "ReplyText", BaseParser.ValueType.STRING));
                trans.setIsAccepted((boolean) Parser.getValue(jsonResult, "IsSuccess", BaseParser.ValueType.BOOLEAN));
                trans.setIsProccessed(true);

                if (trans.isAccepted()) {

                    signImgDone = false;
                    cardImgDone = false;
                    resultMessage = null;
                    if (trans.getSignatureImage() != null && trans.getSignatureImage().length > 0) {
                        saveFileForTransaction(context, trans.getTransactionID(), 1, trans.getSignatureImage(), new ReceivedBasicCallback() {
                            @Override
                            public void onResultReceived(boolean isSuccess, String message) {
                                if (!isSuccess)
                                    resultMessage = message+"Transaction processed successfully, but signature image wasn't saved. ";

                                if (cardImgDone) {
                                    if (callback != null) callback.onResultReceived(true, trans, resultMessage);
                                } else signImgDone = true;
                            }
                        });
                    } else signImgDone = true;

                    if (trans.getManualCardImage() != null && trans.getManualCardImage().length > 0) {
                        saveFileForTransaction(context, trans.getTransactionID(), 2, trans.getManualCardImage(), new ReceivedBasicCallback() {
                            @Override
                            public void onResultReceived(boolean isSuccess, String message) {
                                if (!isSuccess)
                                    resultMessage = message+"Transaction processed successfully, but manual card image wasn't saved. ";

                                if (signImgDone) {
                                    if (callback != null) callback.onResultReceived(true, trans, resultMessage);
                                } else cardImgDone = true;
                            }
                        });
                    } else cardImgDone = true;

                } else {
                    if (callback != null) callback.onResultReceived(true, trans, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                transaction.clearSensitiveData();
                error.printStackTrace();
                if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
            }
        });
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void sendRefund(Context context, long transactionId, final ReceivedBasicCallback callback) {
        //TO DO: signed with activationCode, isDemo
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("transactionID", transactionId);
            json.put("deviceID", getDeviceId(context));
            json.put("signature", signCode);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        //TO DO: parse
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Refund"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                if (jsonResult == null) {
                    if (callback != null) callback.onResultReceived(false, "Something went wrong");
                    return;
                }
                //TO DO: validate signature after request completes

                if (Parser.isRequestSuccessful(response)) {
                    String transaId = (String)Parser.getValue(jsonResult, "TransactionNumber", Parser.ValueType.STRING);
                    if (callback != null) callback.onResultReceived(true, null);
                } else {
                    if (callback != null) callback.onResultReceived(false, Parser.getMessage(response));
                }
            }
        }, createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void sendRefundRequest(Context context, long transactionId, float amount, String comment, String errorDesc, final ReceivedBasicCallback callback) {
        //TO DO: signed with activationCode
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("transactionID", transactionId);
            json.put("amount", amount);
            json.put("comment", comment);
            json.put("signature", signCode);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        //TO DO: parse
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("RefundRequest"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                //TO DO: validate signature after request completes

                String value = (String)Parser.getValue(jsonResult, "Value", Parser.ValueType.STRING);
                if (value.equals("Success")) {
                    if (callback != null) callback.onResultReceived(true, null);
                } else {
                    if (callback != null) callback.onResultReceived(false, Parser.getMessage(response));
                }
            }
        }, createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void saveFileForTransaction(Context context, String transactionId, int fileType, byte[] fileData, final ReceivedBasicCallback callback) {
        if (fileData == null) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        //TO DO: signed with activationCode, isDemo
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("transId", transactionId);
            json.put("fileType", fileType);
            json.put("fileData", Base64.encodeToString(fileData, Base64.NO_WRAP));
            json.put("signature", signCode);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        //TO DO: parse
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SaveTransImage"), json, createBasicSuccessListener(callback), createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void sendInvoiceViaEmail(Context context, String email, long transactionId, final ReceivedBasicCallback callback) {
        if (TextUtils.isEmpty(email)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        //TO DO: signed with activationCode, isDemo
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("transactionID", transactionId);
            json.put("email", email);
            json.put("signature", signCode);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        //TO DO: parse
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SendEmail"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                if (jsonResult == null) {
                    if (callback != null) callback.onResultReceived(false, "Something went wrong");
                    return;
                }
                //TO DO: validate signature after request completes

                boolean value = (boolean)Parser.getValue(jsonResult, "Value", Parser.ValueType.BOOLEAN);
                if (value) {
                    if (callback != null) callback.onResultReceived(true, null);
                } else {
                    if (callback != null) callback.onResultReceived(false, Parser.getMessage(response));
                }
            }
        }, createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void sendInvoiceViaPhone(Context context, String phone, long transactionId, String merchantText, final ReceivedBasicCallback callback) {
        if (TextUtils.isEmpty(phone)) {
            if (callback != null) callback.onResultReceived(false, "Attempt to send an empty value to the service");
            return;
        }

        //TO DO: signed with activationCode, isDemo
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("transactionID", transactionId);
            json.put("phone", phone);
            json.put("merchantText", merchantText);
            json.put("signature", signCode);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        //TO DO: parse
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SendSms"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonResult = (JSONObject)Parser.getValue(response, "d", Parser.ValueType.JSONOBJECT);
                //TO DO: validate signature after request completes

                boolean value = (boolean)Parser.getValue(jsonResult, "Value", Parser.ValueType.BOOLEAN);
                if (value) {
                    if (callback != null) callback.onResultReceived(true, null);
                } else {
                    if (callback != null) callback.onResultReceived(false, Parser.getMessage(response));
                }
            }
        }, createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static void sendPassCode(Context context, final ReceivedBasicCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("credentialsToken", MerchantSession.getInstance().getCredentialsToken());
            json.put("deviceId", getDeviceId(context));
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        //TO DO: parse
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SendPassCode"), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean jsonResult = (boolean)Parser.getValue(response, "d", Parser.ValueType.BOOLEAN);
                if (jsonResult) {
                    if (callback != null) callback.onResultReceived(true, null);
                } else {
                    if (callback != null) callback.onResultReceived(false, "Something went wrong");
                }
            }
        }, createBasicErrorListener(callback));
        //postRequest.addHeader("Content-Type", "application/json");
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/************************************** OTHER SECTION END **************************************/


    private static String createUrl(String method) {
        return Constants.SERVICE_URL+method;
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

    private static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static String getVersion(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "error";
        }
        return version;
    }

    private static String getMerchantNumber(Context context, boolean isDemo) {
        if (isDemo) return "0000000";
        else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String merchant = prefs.getString("merchant", "");
            return merchant;
        }
    }

    public static String getImageUrl(String token, String signature, String hppParams) {
        return createUrl("GetQrCodeImage")+"?credentialsToken="+token+"&signature="+signature+"&hppParams="+hppParams;
    }





//SIGNATURE

    private static String createCallSignature(JSONObject obj, String hashKey) {
        return createCallSignature(getJsonFlatObjValues(obj), hashKey);
    }

    private static String createCallSignature(String source, String hashKey) {
        String preHash = source + hashKey;
        return hash(preHash);
    }

    private static String getJsonFlatObjValues(JSONObject obj) {
        //{"credentialsToken":"b5853383-b55d-4deb-a537-d214d4a0507f","deviceId":"352372058289544"}
        //b5853383-b55d-4deb-a537-d214d4a0507f352372058289544
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = obj.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            if (!key.toLowerCase().equals("signature")) {
                Object value = Parser.getValue(obj, key, BaseParser.ValueType.STRING);
                if (value != null) sb.append(value);
            }
        }
        return sb.toString();
    }

    static String hash(String source) {
        String base64Result = null;
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA256");
            digest.update(source.getBytes());
            byte[] hashResult = digest.digest();
            base64Result = Base64.encodeToString(hashResult, Base64.NO_WRAP);
        } catch (Exception e) {
            Log.d("MobileSDK", "exception while hashing");
        }
        return base64Result;
    }
}
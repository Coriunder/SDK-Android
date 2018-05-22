package com.coriunder.customer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedServiceCallback;
import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.base.common.requests.CustomHeadersImageRequest;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.customer.callbacks.ReceivedCustomerCallback;
import com.coriunder.customer.callbacks.ReceivedImageCallback;
import com.coriunder.customer.models.Customer;

import org.json.JSONObject;

import java.util.Date;

/**
 * This class contains methods to perform requests to the Customer service related to customer data
 */
@SuppressWarnings("unused")
public class CustomerSDKCustomers {
    private static CustomerSDKCustomers instance;
    public static final String SERVICE_URL_PART = "Customer.svc";

    public CustomerSDKCustomers() {
    }

    /**
     * Get instance for CustomerSDKCustomers class.
     * In case there is no current instance, a new one will be created
     * @return CustomerSDKCustomers instance
     */
    public static CustomerSDKCustomers getInstance() {
        if (instance == null) instance = new CustomerSDKCustomers();
        return instance;
    }

    /**
     * Get info about current user (profile image is not included, call
     * {@link #getImageForUser(String, ReceivedImageCallback) getImageForUser} to get it).
     *
     * @param callback will be called after request completion
     * @see ReceivedCustomerCallback
     */
    public void getCustomer(final ReceivedCustomerCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetCustomer"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            // Parse response
                            Customer customer = Parser.parseCustomer(response);
                            if (callback != null) callback.onResultReceived(true, customer, "");
                        } else {
                            if (callback != null) callback.onResultReceived(false, null,
                                    getContext().getString(R.string.empty_response_error));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /*
     ToDoV2:
     asRaw (boolean) not included
     */
    /**
     * Get profile image of any user
     *
     * @param userId id of the user whose profile image has to be loaded
     * @param callback will be called after request completion
     * @see ReceivedImageCallback
     */
    public void getImageForUser(String userId, final ReceivedImageCallback callback) {
        // Check input data
        if (TextUtils.isEmpty(userId)) {
            if (callback != null) callback.onResultReceived(false, null, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersImageRequest imageRequest = new CustomHeadersImageRequest(getImageUrl(userId), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (callback != null) callback.onResultReceived(true, response, "");
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (callback != null) callback.onResultReceived(false, null, Parser.getErrorText(error));
            }
        });
        imageRequest.addHeader("Cookie", "credentialsToken=" + Coriunder.getCredentialsToken());
        Volley.newRequestQueue(getContext()).add(imageRequest);
    }

    /*
    ToDoV2:
    No login after signUp
    */
    /**
     * Register a new user.
     *
     * @param password password to set
     * @param pinCode pin code to set
     * @param email user's email
     * @param firstName user's name
     * @param lastName user's surname
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void registerCustomer(final String password, final String pinCode, final String email, final String firstName,
                             final String lastName, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForRegisterCustomer(password, pinCode, email, firstName, lastName);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("RegisterCustomer"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }
/*
    private void loginAfterSignUp(String email, String password, String pushToken, final ReceivedMapCallback callback) {
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
*/

    /**
     * Save/Update profile info for current user
     *
     * @param address1 user's main address
     * @param address2 user's secondary address
     * @param city user's city
     * @param countryIso user's country ISO. List of country ISO codes can be received using method
     *                   getCountriesStatesAndLanguages from InternationalSDK SDK
     * @param postalCode user's address postal code
     * @param stateIso user's state ISO if any. List of state ISO codes can be received using method
     *                 getCountriesStatesAndLanguages from InternationalSDK SDK
     * @param cellNumber cell phone number
     * @param birthDate date of birth
     * @param email user's email
     * @param firstName user's name
     * @param lastName user's surname
     * @param personalNumber user's social security number
     * @param phone user's phone
     * @param profileImageUri URI of an image which should be set as user profile image
     * @param callback will be called after request completion
     * @see ReceivedCustomerCallback
     */
    public void saveCustomerInfo(String address1, String address2, String city, String countryIso, String postalCode,
                             String stateIso, String cellNumber, Date birthDate, String email, String firstName,
                             String lastName, String personalNumber, String phone, Uri profileImageUri,
                             final ReceivedCustomerCallback callback) {
        // Parse dates to server applicable format
        String birthDateFormatted = birthDate == null ? null : "/Date("+birthDate.getTime()+")/";

        // This call includes image encoding, which takes some time. Volley uses main thread to encode JSON,
        // that's why to move JSON encoding to a separate thread SaveCustomer service is called in AsyncTask.
        // To avoid depending such long process on activity this AsyncTask is called in a Service.

        // Send all required data to the service
        Intent intent = new Intent(getContext(), SaveCustomerAsyncService.class);
        intent.putExtra("AddressLine1", TextUtils.isEmpty(address1) ? "" : address1);
        intent.putExtra("AddressLine2", TextUtils.isEmpty(address2) ? "" : address2);
        intent.putExtra("City", TextUtils.isEmpty(city) ? "" : city);
        intent.putExtra("CountryIso", TextUtils.isEmpty(countryIso) ? "" : countryIso);
        intent.putExtra("PostalCode", TextUtils.isEmpty(postalCode) ? "" : postalCode);
        intent.putExtra("StateIso", TextUtils.isEmpty(stateIso) ? "" : stateIso);
        intent.putExtra("CellNumber", TextUtils.isEmpty(cellNumber) ? "" : cellNumber);
        intent.putExtra("DateOfBirth", TextUtils.isEmpty(birthDateFormatted) ? "" : birthDateFormatted);
        intent.putExtra("EmailAddress", TextUtils.isEmpty(email) ? "" : email);
        intent.putExtra("FirstName", TextUtils.isEmpty(firstName) ? "" : firstName);
        intent.putExtra("LastName", TextUtils.isEmpty(lastName) ? "" : lastName);
        intent.putExtra("PersonalNumber", TextUtils.isEmpty(personalNumber) ? "" : personalNumber);
        intent.putExtra("PhoneNumber", TextUtils.isEmpty(phone) ? "" : phone);
        intent.putExtra("ProfileImageUri", (profileImageUri == null || TextUtils.isEmpty(profileImageUri.toString())) ? "" : profileImageUri.toString());
        intent.putExtra("ResultReceiver", new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultData.getBoolean("IsSuccess")) {
                    // Get customer data
                    getCustomer(new ReceivedCustomerCallback() {
                        @Override
                        public void onResultReceived(boolean isSuccess, Customer customer, String message) {
                            if (isSuccess) {
                                if (callback != null) callback.onResultReceived(true, customer, "");
                            } else {
                                if (callback != null)
                                    callback.onResultReceived(true, new Customer(), getContext().getString(R.string.customer_error_register));
                            }
                        }
                    });
                } else {
                    if (callback != null) callback.onResultReceived(false, null, resultData.getString("Message"));
                }
            }
        });
        getContext().startService(intent);
    }

    /**
     * Get URL for profile image of any user
     *
     * @param userId id of the user whose profile image URL has to be created
     * @return image URL
     */
    public String getImageUrl(String userId) {
        return createUrl("GetImage")+"?walletId="+userId+"&asRaw=true";
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
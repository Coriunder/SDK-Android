package com.coriunder.customer;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;

import org.json.JSONObject;

/**
 * Service to save customer data asynchronously and not depending on activity
 */
public class SaveCustomerAsyncService extends Service {
    public static final String SERVICE_URL_PART = "Customer.svc";
    String address1;
    String address2;
    String city;
    String countryIso;
    String postalCode;
    String stateIso;
    String cellNumber;
    String birthDate;
    String email;
    String firstName;
    String lastName;
    String personalNumber;
    String phone;
    Uri profileImageUri;
    ResultReceiver receiver;    // receiver to be called after the task completes

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get all input data
        address1 = intent.getStringExtra("AddressLine1");
        address2 = intent.getStringExtra("AddressLine2");
        city = intent.getStringExtra("City");
        countryIso = intent.getStringExtra("CountryIso");
        postalCode = intent.getStringExtra("PostalCode");
        stateIso = intent.getStringExtra("StateIso");
        cellNumber = intent.getStringExtra("CellNumber");
        birthDate = intent.getStringExtra("DateOfBirth");
        email = intent.getStringExtra("EmailAddress");
        firstName = intent.getStringExtra("FirstName");
        lastName = intent.getStringExtra("LastName");
        personalNumber = intent.getStringExtra("PersonalNumber");
        phone = intent.getStringExtra("PhoneNumber");
        profileImageUri = Uri.parse(intent.getStringExtra("ProfileImageUri"));
        receiver = intent.getParcelableExtra("ResultReceiver");
        // Start save customer task
        new SaveCustomerTask().execute();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * AsyncTask to save customer data in a separate thread
     */
    private class SaveCustomerTask extends AsyncTask<Void, Void, CustomHeadersJsonObjectRequest> {
        @Override
        protected CustomHeadersJsonObjectRequest doInBackground(Void... params) {
            // Create json object to send with request
            JSONObject json = JsonBuilder.buildJsonForSaveCustomer(address1, address2, city, countryIso, postalCode,
                    stateIso, cellNumber, birthDate, email, firstName, lastName, personalNumber, phone, profileImageUri);
            if (json == null) {
                fireReceiver(false, Coriunder.getContext().getString(R.string.create_request_error));
                return null;
            }

            // Create request
            CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                    createUrl("SaveCustomer"), json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonResult = Parser.getJsonObject(response, "d");
                            fireReceiver(Parser.getBoolean(jsonResult, "IsSuccess"), Parser.getString(jsonResult, "Message"));
                            stopSelf();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorString = Parser.getErrorText(error);
                            fireReceiver(false, TextUtils.isEmpty(errorString) ? "" : errorString);
                            stopSelf();
                        }
                    }
            );

            if (!TextUtils.isEmpty(Coriunder.getCredentialsToken()) && !TextUtils.isEmpty(Coriunder.getCredentialsHeader()))
                postRequest.addHeader(Coriunder.getCredentialsHeader(), Coriunder.getCredentialsToken());

            postRequest.setRetryPolicy(new DefaultRetryPolicy(com.coriunder.base.utils.Constants.DEFAULT_TIMEOUT_MS,
                    com.coriunder.base.utils.Constants.DEFAULT_MAX_RETRIES,
                    com.coriunder.base.utils.Constants.DEFAULT_BACKOFF_MULTIPLIER));
            return postRequest;
        }

        @Override
        protected void onPostExecute(CustomHeadersJsonObjectRequest postRequest) {
            // Run request if not null
            if (postRequest != null)
                Volley.newRequestQueue(getApplicationContext()).add(postRequest);
            else stopSelf();
        }
    }

    /**
     * Method to be called after SaveCustomer request completes or fails
     * @param success set whether request was successful
     * @param message set response message
     */
    private void fireReceiver(boolean success, String message) {
        if (receiver != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsSuccess", success);
            bundle.putString("Message", message);
            receiver.send(0, bundle); // send(result_code, bundle)
        }
    }

    protected String createUrl(String method) {
        return Coriunder.getServiceUrl() + SERVICE_URL_PART + "/" + method;
    }
}
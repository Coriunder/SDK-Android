package com.coriunder.unused.v1.usersdk;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coriunder.unused.v1.basesdk.CustomHeadersJsonObjectRequest;
import com.coriunder.unused.v1.basesdk.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 1 on 29.02.2016.
 */
public class LongTaskAsyncService extends Service {
    String firstName;
    String lastName;
    String email;
    String address1;
    String address2;
    String city;
    String zipCode;
    String stateIso;
    String countryIso;
    String phone;
    Uri profileImageUri;
    int birthDay;
    int birthMonth;
    int birthYear;
    String personalNumber;
    String cellNumber;
    ResultReceiver receiver;

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        firstName = intent.getStringExtra("FirstName");
        lastName = intent.getStringExtra("LastName");
        email = intent.getStringExtra("EmailAddress");
        address1 = intent.getStringExtra("AddressLine1");
        address2 = intent.getStringExtra("AddressLine2");
        city = intent.getStringExtra("City");
        zipCode = intent.getStringExtra("PostalCode");
        stateIso = intent.getStringExtra("StateIso");
        countryIso = intent.getStringExtra("CountryIso");
        phone = intent.getStringExtra("PhoneNumber");
        profileImageUri = Uri.parse(intent.getStringExtra("ProfileImageUri"));
        birthDay = intent.getIntExtra("DayOfBirth", 0);
        birthMonth = intent.getIntExtra("MonthOfBirth", 0);
        birthYear = intent.getIntExtra("YearOfBirth", 0);
        personalNumber = intent.getStringExtra("PersonalNumber");
        cellNumber = intent.getStringExtra("CellNumber");
        receiver = intent.getParcelableExtra("ResultReceiver");
        new SaveCustomerTask().execute();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private class SaveCustomerTask extends AsyncTask<Void, Void, CustomHeadersJsonObjectRequest> {
        @Override
        protected CustomHeadersJsonObjectRequest doInBackground(Void... params) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, birthYear);
            cal.set(Calendar.MONTH, birthMonth);
            cal.set(Calendar.DAY_OF_MONTH, birthDay);
            cal.set(Calendar.HOUR_OF_DAY, 12);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss", Locale.getDefault());
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String bDate = formatter.format(cal.getTime());

            JSONObject info = new JSONObject();
            try {
                info.put("FirstName", firstName);
                info.put("LastName", lastName);
                info.put("EmailAddress", email);
                info.put("AddressLine1", address1);
                info.put("AddressLine2", address2);
                info.put("City", city);
                info.put("PostalCode", zipCode);
                info.put("CountryIso", countryIso);
                info.put("StateIso", stateIso);
                info.put("PhoneNumber", phone);
                info.put("PersonalNumber", personalNumber);
                info.put("CellNumber", cellNumber);
                info.put("DateOfBirth", bDate);
            } catch (JSONException e) {
                fireReceiver(0, false, "An error occurred while creating request");
                return null;
            }

            Bitmap scaledBitmap = new BitmapUtils(getApplicationContext()).getBitmap(profileImageUri, 300, 300);
            if (scaledBitmap != null) {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    byte[] profileImageArray = stream.toByteArray();
                    stream.close();
                    scaledBitmap.recycle();

                    JSONArray byteArray = new JSONArray();
                    for (byte imgByte : profileImageArray) {
                        byteArray.put(0xFF & imgByte);
                    }

                    info.put("ProfileImage", byteArray);
                } catch (Exception e) {
                    Log.d("UserSDK", "Error when adding profile image. Profile image not added");
                }
            }

            JSONObject json = new JSONObject();
            try {
                json.put("info", info);
            } catch (JSONException e) {
                fireReceiver(0, false, "An error occurred while creating request");
                return null;
            }

            CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                    UserManagementSDK.createUrl("SaveCustomer"), json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (Parser.isRequestSuccessful(response)) fireReceiver(0, true, "");
                            else fireReceiver(0, false, Parser.getMessage(response));
                            stopSelf();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            fireReceiver(0, false, Parser.getErrorText(error));
                            stopSelf();
                        }
                    }
            );
            //postRequest.addHeader("Content-Type", "application/json");
            postRequest.addHeader("Cookie", "credentialsToken=" + UserSession.getInstance().getCredentialsToken(getBaseContext()) + "; HttpOnly");
            postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                    Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
            return postRequest;
        }

        @Override
        protected void onPostExecute(CustomHeadersJsonObjectRequest postRequest) {
            if (postRequest != null)
                Volley.newRequestQueue(getApplicationContext()).add(postRequest);
            else stopSelf();
        }
    }

    private void fireReceiver(int resultCode, boolean success, String message) {
        if (receiver != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsSucccess", success);
            bundle.putString("Message", message);
            receiver.send(resultCode, bundle);
        }
    }
}
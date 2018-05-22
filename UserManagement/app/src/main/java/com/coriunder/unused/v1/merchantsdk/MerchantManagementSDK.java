package com.coriunder.unused.v1.merchantsdk;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coriunder.unused.v1.basesdk.CustomHeadersJsonObjectRequest;
import com.coriunder.unused.v1.basesdk.callbacks.ReceivedBasicCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * Created by 1 on 23.02.2016.
 */
public class MerchantManagementSDK {

/************************************ REGISTER SECTION START ************************************/

    public static void registerMerchant(Context context, String firstName, String lastName, String address, String city, String state,
                                        String stateOfIncorporation, String zipcode, String phisicalAddress, String phisicalCity,
                                        String phisicalState, String phisicalZip, String email, String phone, String fax, String url,
                                        String bankAccountNumber, String bankRoutingNumber, String businessDescription, int industry,
                                        int businessStartMonth, int businessStartYear, String legalBusinessName, String legalBusinessNumber,
                                        int typeOfBusiness, float anticipatedAverageTransactionAmount, float anticipatedLargestTransactionAmount,
                                        float anticipatedMonthlyVolume, String canceledCheckImage, String dbaName, int ownerBirthDay,
                                        int ownerBirthMonth, int ownerBirthYear, String ownerSsn, int percentDelivery0to7,
                                        int percentDelivery15to30, int percentDelivery8to14, int percentDeliveryOver30,
                                        final ReceivedBasicCallback callback) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, ownerBirthYear);
        cal.set(Calendar.MONTH, ownerBirthMonth);
        cal.set(Calendar.DAY_OF_MONTH, ownerBirthDay);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long millisOwner = cal.getTimeInMillis();
        String businessStartDate = "/Date("+millisOwner+")/";

        cal.set(Calendar.YEAR, businessStartYear);
        cal.set(Calendar.MONTH, businessStartMonth);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        long millisCompany = cal.getTimeInMillis();
        String birthDate = "/Date("+millisCompany+")/";

        JSONObject json = new JSONObject();
        try {
            JSONObject params = new JSONObject();
            params.put("Address", address);
            params.put("AnticipatedAverageTransactionAmount", anticipatedAverageTransactionAmount);
            params.put("AnticipatedLargestTransactionAmount", anticipatedLargestTransactionAmount);
            params.put("AnticipatedMonthlyVolume", anticipatedMonthlyVolume);
            params.put("BankAccountNumber", bankAccountNumber);
            params.put("BankRoutingNumber", bankRoutingNumber);
            params.put("BusinessDescription", businessDescription);
            params.put("BusinessStartDate", businessStartDate);
            params.put("CanceledCheckImage", canceledCheckImage);
            params.put("City", city);
            params.put("DbaName", dbaName);
            params.put("Email", email);
            params.put("Fax", fax);
            params.put("FirstName", firstName);
            params.put("Industry", industry);
            params.put("LastName", lastName);
            params.put("LegalBusinessName", legalBusinessName);
            params.put("LegalBusinessNumber", legalBusinessNumber);
            params.put("OwnerDob", birthDate);
            params.put("OwnerSsn", ownerSsn);
            params.put("PercentDelivery0to7", percentDelivery0to7);
            params.put("PercentDelivery15to30", percentDelivery15to30);
            params.put("PercentDelivery8to14", percentDelivery8to14);
            params.put("PercentDeliveryOver30", percentDeliveryOver30);
            params.put("PhisicalAddress", phisicalAddress);
            params.put("PhisicalCity", phisicalCity);
            params.put("PhisicalState", phisicalState);
            params.put("PhisicalZip", phisicalZip);
            params.put("Phone", phone);
            params.put("State", state);
            params.put("StateOfIncorporation", stateOfIncorporation);
            params.put("TypeOfBusiness", typeOfBusiness);
            params.put("Url", url);
            params.put("Zipcode", zipcode);
            json.put("RegistrationData", params);
        } catch (JSONException e) {
            if (callback != null) callback.onResultReceived(false, "An error occurred while creating request");
            return;
        }

        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("Register"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /** TO DO:
                         explanations
                         parse and modify callback **/
                        if (callback != null) callback.onResultReceived(true, null);
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

        /*
        String jsonString = "{\"RegistrationData\":{}}";
        String jsonString = "{\"RegistrationData\":{\"Address\":\"\",\"AnticipatedAverageTransactionAmount\":0,\"AnticipatedLargestTransactionAmount\":0," +
                "\"AnticipatedMonthlyVolume\":0,\"BankAccountNumber\":\"\",\"BankRoutingNumber\":\"\",\"BusinessDescription\":\"\",\"BusinessStartDate\":" +
                "\"\\/Date(1229288400000)\\/\",\"CanceledCheckImage\":\"\",\"City\":\"\",\"DbaName\":\"\",\"Email\":\"\",\"Fax\":\"\",\"FirstName\":" +
                "\"Merchant\",\"Industry\":0,\"LastName\":\"Test\",\"LegalBusinessName\":\"NoName\",\"LegalBusinessNumber\":\"\",\"OwnerDob\":" +
                "\"\\/Date(485856000000)\\/\",\"OwnerSsn\":\"\",\"PercentDelivery0to7\":0,\"PercentDelivery15to30\":0,\"PercentDelivery8to14\":0," +
                "\"PercentDeliveryOver30\":0,\"PhisicalAddress\":\"\",\"PhisicalCity\":\"\",\"PhisicalState\":\"IL\",\"PhisicalZip\":\"\",\"Phone\":\"\"," +
                "\"State\":\"IL\",\"StateOfIncorporation\":\"\",\"TypeOfBusiness\":0,\"Url\":\"\",\"Zipcode\":\"\"}}";
         */
        String base64Hash = calculateSHA256("abcdefg");

        //postRequest.addHeader("Content-Type", "application/json");
        //postRequest.addHeader("Cookie", "credentialsToken="+UserSession.getInstance().getCredentialsToken()+"; HttpOnly");
        postRequest.addHeader("applicationToken", Constants.APP_TOKEN);
        postRequest.addHeader("Signature", "values-SHA256, "+base64Hash);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

/************************************* REGISTER SECTION END *************************************/

    private static String createUrl(String method) {
        return Constants.SERVICE_URL+method;
    }

    public static String calculateSHA256(String secret){
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = secret.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);

            String base64 = android.util.Base64.encodeToString(digest.digest(), android.util.Base64.DEFAULT);
            return base64;
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
            throw new RuntimeException("Cannot calculate signature");
        }
    }
}

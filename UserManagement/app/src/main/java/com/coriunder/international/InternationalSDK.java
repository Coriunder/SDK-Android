package com.coriunder.international;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.international.callbacks.ReceivedCurrencyRatesCallback;
import com.coriunder.international.callbacks.ReceivedErrorCodesCallback;
import com.coriunder.international.callbacks.ReceivedInternationalListsCallback;
import com.coriunder.international.models.Country;
import com.coriunder.international.models.CurrencyRate;
import com.coriunder.international.models.Language;
import com.coriunder.international.models.State;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods to perform requests to the International service
 */
@SuppressWarnings("unused")
public class InternationalSDK {
    private static InternationalSDK instance;
    public static final String SERVICE_URL_PART = "International.svc";

    public InternationalSDK() {
    }

    /**
     * Get instance for InternationalSDK class.
     * In case there is no current instance, a new one will be created
     * @return InternationalSDK instance
     */
    public static InternationalSDK getInstance() {
        if (instance == null) instance = new InternationalSDK();
        return instance;
    }

    /**
     * Get currency rates
     *
     * @param callback will be called after request completion
     * @see ReceivedCurrencyRatesCallback
     */
    public void getCurrencyRates(final ReceivedCurrencyRatesCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetCurrencyRates"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<CurrencyRate> arrayList = Parser.parseCurrencyRates(response);
                        if (callback != null) callback.onResultReceived(true, arrayList, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<CurrencyRate>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get list of possible errors
     *
     * @param language language which you want to receive errors with
     * @param groups groups of errors you need to get info about
     * @param callback will be called after request completion
     * @see ReceivedErrorCodesCallback
     */
    public void getErrorCodes(String language, String[] groups, final ReceivedErrorCodesCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForErrorCodes(language, groups);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ArrayList<ServiceResult>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetErrorCodes"), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        ArrayList<ServiceResult> arrayList = Parser.parseErrorCodes(response);
                        if (callback != null) callback.onResultReceived(true, arrayList, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false,
                                new ArrayList<ServiceResult>(), Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get list of countries, Canada states, USA states and languages
     *
     * @param callback will be called after request completion
     * @see ReceivedInternationalListsCallback
     */
    public void getCountriesStatesAndLanguages(final ReceivedInternationalListsCallback callback) {
        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetStaticData"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response
                        JSONObject root = Parser.getJsonObject(response, "d");
                        ArrayList<State> canadaStates = Parser.parseStates(root, true);
                        ArrayList<Country> countries = Parser.parseCountries(root);
                        ArrayList<Language> languages = Parser.parseLanguages(root);
                        ArrayList<State> usaStates = Parser.parseStates(root, false);
                        if (callback != null) callback.onResultReceived(true, languages, countries, canadaStates, usaStates, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) callback.onResultReceived(false, new ArrayList<Language>(),
                                new ArrayList<Country>(), new ArrayList<State>(), new ArrayList<State>(),
                                Parser.getErrorText(error));
                    }
                }
        );
        CommonRequests.performRequest(postRequest);
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
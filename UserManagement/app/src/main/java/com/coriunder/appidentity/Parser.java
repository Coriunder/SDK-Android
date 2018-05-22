package com.coriunder.appidentity;

import android.util.Log;

import com.coriunder.R;
import com.coriunder.appidentity.models.Identity;
import com.coriunder.appidentity.models.MerchantGroup;
import com.coriunder.base.Coriunder;
import com.coriunder.base.utils.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parser methods for AppIdentity services
 */
public class Parser extends BaseParser {
    /**
     * Method to parse JSONObject to Identity object
     * @param response JSONObject to parse
     * @return Identity object
     */
    protected static Identity parseIdentity(JSONObject response) {
        Identity result = new Identity();
        JSONObject jsonResult = Parser.getJsonObject(response, "d");
        result.setBrandName(Parser.getString(jsonResult, "BrandName"));
        result.setCompanyName(Parser.getString(jsonResult, "CompanyName"));
        result.setCopyRightText(Parser.getString(jsonResult, "CopyRightText"));
        result.setDomainName(Parser.getString(jsonResult, "DomainName"));
        result.setActive(Parser.getBoolean(jsonResult, "IsActive"));
        result.setName(Parser.getString(jsonResult, "Name"));
        result.setTheme(Parser.getString(jsonResult, "Theme"));
        result.setUrlDevCenter(Parser.getString(jsonResult, "URLDevCenter"));
        result.setUrlMerchantCP(Parser.getString(jsonResult, "URLMerchantCP"));
        result.setUrlProcess(Parser.getString(jsonResult, "URLProcess"));
        result.setUrlWallet(Parser.getString(jsonResult, "URLWallet"));
        result.setUrlWebsite(Parser.getString(jsonResult, "URLWebsite"));
        return result;
    }

    /**
     * Method to parse JSONObject to ArrayList containing MerchantGroup objects
     * @param response JSONObject to parse
     * @return ArrayList containing MerchantGroup objects
     */
    protected static ArrayList<MerchantGroup> parseMerchantGroups(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<MerchantGroup> arrayList = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject requestObj = result.getJSONObject(i);
                    MerchantGroup item = new MerchantGroup();
                    item.setKey(Parser.getInt(requestObj, "Key"));
                    item.setValue(Parser.getString(requestObj, "Value"));
                    arrayList.add(item);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.appidentity_log),
                            Coriunder.getContext().getString(R.string.appidentity_error_group) + i);
                }
            }
        }
        return arrayList;
    }

    /**
     * Method to parse JSONObject to array of String
     * @param response JSONObject to parse
     * @return array of String with currencies
     */
    protected static String[] parseCurrencies(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        if (result != null) {
            int length = result.length();
            String[] currenciesArray = new String[length];
            for (int i = 0; i < length; i++) {
                try {
                    currenciesArray[i] = result.getString(i);
                } catch (JSONException e) {
                    Log.e(Coriunder.getContext().getString(R.string.appidentity_log),
                            Coriunder.getContext().getString(R.string.appidentity_error_group) + i);
                }
            }
            return currenciesArray;
        }
        return new String[0];
    }

    /**
     * Method to parse JSONObject to array of int
     * @param response JSONObject to parse
     * @return array of int with payment methods' types' ids
     */
    protected static int[] parseSupportedPaymentMethods(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        if (result != null) {
            int length = result.length();
            int[] pmsArray = new int[length];
            for(int i=0;i<length;i++){
                try {
                    pmsArray[i]= result.getInt(i);
                } catch (JSONException e) {
                    Log.e(Coriunder.getContext().getString(R.string.appidentity_log),
                            Coriunder.getContext().getString(R.string.appidentity_error_group) + i);
                }
            }
            return pmsArray;
        }
        return new int[0];
    }
}
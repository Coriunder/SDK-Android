package com.coriunder.paymentmethods;

import android.util.Log;

import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.models.Address;
import com.coriunder.base.utils.BaseParser;
import com.coriunder.paymentmethods.models.PaymentCardRootGroup;
import com.coriunder.paymentmethods.models.PaymentCardSubRootGroup;
import com.coriunder.paymentmethods.models.PaymentMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Parser methods for PaymentMethods services
 */
public class Parser extends BaseParser {
    /**
     * Method to parse JSONObject to PaymentMethod object
     * @param jsonResult JSONObject to parse
     * @return PaymentMethod object
     */
    protected static PaymentMethod parsePaymentMethod(JSONObject jsonResult) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setAccountValue1(getString(jsonResult, "AccountValue1"));
        paymentMethod.setAccountValue2(getString(jsonResult, "AccountValue2"));
        paymentMethod.setAddress(parseAddress(getJsonObject(jsonResult, "BillingAddress")));
        paymentMethod.setDisplay(getString(jsonResult, "Display"));
        paymentMethod.setExpirationDate(getReadableDate(jsonResult, "ExpirationDate"));
        paymentMethod.setPaymentMethodId(getInt(jsonResult, "ID"));
        paymentMethod.setIconURL(getString(jsonResult, "Icon"));
        paymentMethod.setDefault(getBoolean(jsonResult, "IsDefault"));
        paymentMethod.setIssuerCountryIsoCode(getString(jsonResult, "IssuerCountryIsoCode"));
        paymentMethod.setLast4Digits(getString(jsonResult, "Last4Digits"));
        paymentMethod.setOwnerName(getString(jsonResult, "OwnerName"));
        paymentMethod.setPaymentMethodGroupKey(getString(jsonResult, "PaymentMethodGroupKey"));
        paymentMethod.setPaymentMethodKey(getString(jsonResult, "PaymentMethodKey"));
        paymentMethod.setTitle(getString(jsonResult, "Title"));
        return paymentMethod;
    }

    /**
     * Method to parse JSONObject to ArrayList containing Address objects
     * @param response JSONObject to parse
     * @return ArrayList containing Address objects
     */
    protected static ArrayList<Address> parseAddresses(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<Address> addressesArray = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject addressObj = result.getJSONObject(i);
                    Address address = Parser.parseAddress(addressObj);
                    addressesArray.add(address);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.pms_log),
                            Coriunder.getContext().getString(R.string.pms_error_address)+i);
                }
            }
        }
        return addressesArray;
    }

    /**
     * Method to parse JSONObject to ArrayList containing PaymentCardRootGroup objects
     * @param result JSONObject to parse
     * @return ArrayList containing PaymentCardRootGroup objects
     */
    protected static ArrayList<PaymentCardRootGroup> parsePaymentMethodsTypes(JSONObject result) {
        JSONArray rootGroupResult = Parser.getJsonArray(result, "PaymentMethodGroups");
        JSONArray subRootGroupResult = Parser.getJsonArray(result, "PaymentMethods");
        if (rootGroupResult == null) rootGroupResult = new JSONArray();
        if (subRootGroupResult == null) subRootGroupResult = new JSONArray();
        ArrayList<PaymentCardRootGroup> paymentGroups = new ArrayList<>();

        // Storing all subRoots by roots
        HashMap<String, PaymentCardRootGroup> roots = new HashMap<>();
        for (int i=0; i<subRootGroupResult.length(); i++) {
            try {
                JSONObject subgroup = subRootGroupResult.getJSONObject(i);
                String key = Parser.getString(subgroup, "GroupKey");

                // Creating/getting temporary root
                PaymentCardRootGroup rootModel = roots.get(key);
                if (rootModel == null) {
                    rootModel = new PaymentCardRootGroup();
                    rootModel.setName("Group "+key);
                    roots.put(key, rootModel);
                }

                PaymentCardSubRootGroup subRootModel =  new PaymentCardSubRootGroup();
                subRootModel.setIcon(Parser.getString(subgroup, "Icon"));
                subRootModel.setKey(Parser.getString(subgroup, "Key"));
                subRootModel.setName(Parser.getString(subgroup, "Name"));
                subRootModel.setGroupKey(Parser.getString(subgroup, "GroupKey"));
                subRootModel.setHasExpirationDate(Parser.getBoolean(subgroup, "HasExpirationDate"));
                subRootModel.setValue1Caption(Parser.getString(subgroup, "Value1Caption"));
                subRootModel.setValue1ValidationRegex(Parser.getString(subgroup, "Value1ValidationRegex"));
                subRootModel.setValue2Caption(Parser.getString(subgroup, "Value2Caption"));
                subRootModel.setValue2ValidationRegex(Parser.getString(subgroup, "Value2ValidationRegex"));
                rootModel.getSubGroups().add(subRootModel);
            } catch (Exception e) {
                Log.e(Coriunder.getContext().getString(R.string.pms_log),
                        Coriunder.getContext().getString(R.string.pms_error_card_group)+i);
            }
        }

        //Storing root data
        HashMap<String, PaymentCardRootGroup> rootsData = new HashMap<>();
        for (int i=0; i<rootGroupResult.length(); i++) {
            try {
                JSONObject group = rootGroupResult.getJSONObject(i);
                String key = Parser.getString(group, "Key");

                PaymentCardRootGroup rootModel = new PaymentCardRootGroup();
                rootModel.setIcon(Parser.getString(group, "Icon"));
                rootModel.setKey(Parser.getString(group, "Key"));
                rootModel.setName(Parser.getString(group, "Name"));
                rootsData.put(key, rootModel);
            } catch (Exception e) {
                Log.e(Coriunder.getContext().getString(R.string.pms_log),
                        Coriunder.getContext().getString(R.string.pms_error_card_group)+i);
            }
        }

        //Setting real root data to roots which hold subRoots
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
        return paymentGroups;
    }

    /**
     * Method to parse JSONObject to ArrayList containing PaymentMethod objects
     * @param response JSONObject to parse
     * @return ArrayList containing PaymentMethod objects
     */
    protected static ArrayList<PaymentMethod> parsePaymentMethods(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<PaymentMethod> paymentArray = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject cardObj = result.getJSONObject(i);
                    PaymentMethod paymentMethod = Parser.parsePaymentMethod(cardObj);
                    paymentArray.add(paymentMethod);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.pms_log),
                            Coriunder.getContext().getString(R.string.pms_error_pm)+i);
                }
            }
        }
        return paymentArray;
    }
}
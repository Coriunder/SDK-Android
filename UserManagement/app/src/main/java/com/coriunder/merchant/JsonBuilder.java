package com.coriunder.merchant;

import android.text.TextUtils;

import com.coriunder.merchant.models.RegistrationMerchant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class contains methods designed to prepare
 * JSONObjects to send with requests to Merchant service
 */
public class JsonBuilder {
    /**
     * Prepare JSONObject for Register request
     * @param merchant RegistrationMerchant object to be parsed to JSONObject
     * @return prepared JSONObject
     * @see RegistrationMerchant
     */
    protected static JSONObject buildRegisterMerchantJson(RegistrationMerchant merchant) {
        // Parse dates to server applicable format
        String birthDateString = merchant.getOwnerDob() == null ? null : "/Date("+merchant.getOwnerDob().getTime()+")/";
        String companyDateString = merchant.getBusinessStartDate() == null ? null : "/Date("+merchant.getBusinessStartDate().getTime()+")/";

        JSONObject json = new JSONObject();
        try {
            JSONObject params = new JSONObject();
            params.put("Address", TextUtils.isEmpty(merchant.getAddress()) ? "" : merchant.getAddress());
            params.put("AnticipatedAverageTransactionAmount", merchant.getAnticipatedAverageTransactionAmount());
            params.put("AnticipatedLargestTransactionAmount", merchant.getAnticipatedLargestTransactionAmount());
            params.put("AnticipatedMonthlyVolume", merchant.getAnticipatedMonthlyVolume());
            params.put("BankAccountNumber", TextUtils.isEmpty(merchant.getBankAccountNumber()) ? "" : merchant.getBankAccountNumber());
            params.put("BankRoutingNumber", TextUtils.isEmpty(merchant.getBankRoutingNumber()) ? "" : merchant.getBankRoutingNumber());
            params.put("BusinessDescription", TextUtils.isEmpty(merchant.getBusinessDescription()) ? "" : merchant.getBusinessDescription());
            params.put("BusinessStartDate", TextUtils.isEmpty(companyDateString) ? JSONObject.NULL : companyDateString);
            params.put("CanceledCheckImage", TextUtils.isEmpty(merchant.getCanceledCheckImage()) ? "" : merchant.getCanceledCheckImage());
            params.put("City", TextUtils.isEmpty(merchant.getCity()) ? "" : merchant.getCity());
            params.put("DbaName", TextUtils.isEmpty(merchant.getDbaName()) ? "" : merchant.getDbaName());
            params.put("Email", TextUtils.isEmpty(merchant.getEmail()) ? "" : merchant.getEmail());
            params.put("Fax", TextUtils.isEmpty(merchant.getFax()) ? "" : merchant.getFax());
            params.put("FirstName", TextUtils.isEmpty(merchant.getFirstName()) ? "" : merchant.getFirstName());
            params.put("Industry", merchant.getIndustry());
            params.put("LastName", TextUtils.isEmpty(merchant.getLastName()) ? "" : merchant.getLastName());
            params.put("LegalBusinessName", TextUtils.isEmpty(merchant.getLegalBusinessName()) ? "" : merchant.getLegalBusinessName());
            params.put("LegalBusinessNumber", TextUtils.isEmpty(merchant.getLegalBusinessNumber()) ? "" : merchant.getLegalBusinessNumber());
            params.put("OwnerDob", TextUtils.isEmpty(birthDateString) ? JSONObject.NULL : birthDateString);
            params.put("OwnerSsn", TextUtils.isEmpty(merchant.getOwnerSsn()) ? "" : merchant.getOwnerSsn());
            params.put("PercentDelivery0to7", merchant.getPercentDelivery0to7());
            params.put("PercentDelivery15to30", merchant.getPercentDelivery15to30());
            params.put("PercentDelivery8to14", merchant.getPercentDelivery8to14());
            params.put("PercentDeliveryOver30", merchant.getPercentDeliveryOver30());
            params.put("PhisicalAddress", TextUtils.isEmpty(merchant.getPhysicalAddress()) ? "" : merchant.getPhysicalAddress());
            params.put("PhisicalCity", TextUtils.isEmpty(merchant.getPhysicalCity()) ? "" : merchant.getPhysicalCity());
            params.put("PhisicalState", TextUtils.isEmpty(merchant.getPhysicalState()) ? "" : merchant.getPhysicalState());
            params.put("PhisicalZip", TextUtils.isEmpty(merchant.getPhysicalZip()) ? "" : merchant.getPhysicalZip());
            params.put("Phone", TextUtils.isEmpty(merchant.getPhone()) ? "" : merchant.getPhone());
            params.put("State", TextUtils.isEmpty(merchant.getState()) ? "" : merchant.getState());
            params.put("StateOfIncorporation", TextUtils.isEmpty(merchant.getStateOfIncorporation()) ? "" : merchant.getStateOfIncorporation());
            params.put("TypeOfBusiness", merchant.getTypeOfBusiness());
            params.put("Url", TextUtils.isEmpty(merchant.getUrl()) ? "" : merchant.getUrl());
            params.put("Zipcode", TextUtils.isEmpty(merchant.getZipcode()) ? "" : merchant.getZipcode());
            json.put("RegistrationData", params);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }
}

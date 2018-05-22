package com.coriunder.paymentmethods;

import android.text.TextUtils;

import com.coriunder.paymentmethods.models.PaymentMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class contains methods designed to prepare
 * JSONObjects to send with requests to PaymentMethods service
 */
public class JsonBuilder {
    /**
     * Prepare JSONObject for DeleteStoredPaymentMethod request
     * @param cardId id of the payment method which has to be deleted
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForDeletePm(long cardId) {
        JSONObject json = new JSONObject();
        try {
            json.put("pmid", cardId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetStoredPaymentMethod request
     * @param cardId id of the required payment method
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetPm(long cardId) {
        JSONObject json = new JSONObject();
        try {
            json.put("pmid", cardId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for LinkPaymentMethod request
     * @param accountValue1 account value
     * @param dateOfBirth owner's date of birth
     * @param personalNumber owner's personal number
     * @param phoneNumber owner's phone number
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForLinkPm(String accountValue1, Date dateOfBirth,
                                                   String personalNumber, String phoneNumber) {
        // Parse date to server applicable format
        String dateOfBirthFormatted = dateOfBirth == null ? null : "/Date("+dateOfBirth.getTime()+")/";

        JSONObject json = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            data.put("AccountValue1", TextUtils.isEmpty(accountValue1) ? "" : accountValue1);
            data.put("DateOfBirth", TextUtils.isEmpty(dateOfBirthFormatted) ? JSONObject.NULL : dateOfBirthFormatted);
            data.put("PersonalNumber", TextUtils.isEmpty(personalNumber) ? "" : personalNumber);
            data.put("PhoneNumber", TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber);
            json.put("data", data);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for LoadPaymentMethod request
     * @param amount required amount
     * @param currencyIso ISO of the currency which should be loaded
     * @param paymentMethodID id of the payment method
     * @param pinCode current user's pin code
     * @param referenceCode reference code
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForLoadPm(double amount, String currencyIso, long paymentMethodID,
                                                   String pinCode, String referenceCode) {
        JSONObject json = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            data.put("Amount", amount);
            data.put("CurrencyIso", TextUtils.isEmpty(currencyIso) ? "" : currencyIso);
            data.put("PaymentMethodID", paymentMethodID);
            data.put("PinCode", TextUtils.isEmpty(pinCode) ? "" : pinCode);
            data.put("ReferenceCode", TextUtils.isEmpty(referenceCode) ? "" : referenceCode);
            json.put("data", data);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for RequestPhysicalPaymentMethod request
     * @param address1 physical payment method's main address
     * @param address2 physical payment method's secondary address
     * @param city physical payment method's city
     * @param countryIso physical payment method's country ISO
     * @param postalCode physical payment method's postal code
     * @param stateIso physical payment method's state ISO
     * @param providerID physical payment method's provider ID
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForRequestPhysicalPm(String address1, String address2, String city, String countryIso,
                                                              String postalCode, String stateIso, String providerID) {
        JSONObject json = new JSONObject();
        try {
            JSONObject data = new JSONObject();

            // Create address JSONObject
            JSONObject address = new JSONObject();
            address.put("AddressLine1", TextUtils.isEmpty(address1) ? "" : address1);
            address.put("AddressLine2", TextUtils.isEmpty(address2) ? "" : address2);
            address.put("City", TextUtils.isEmpty(city) ? "" : city);
            address.put("CountryIso", TextUtils.isEmpty(countryIso) ? "" : countryIso);
            address.put("PostalCode", TextUtils.isEmpty(postalCode) ? "" : postalCode);
            address.put("StateIso", TextUtils.isEmpty(stateIso) ? "" : stateIso);
            data.put("Address", address);

            data.put("ProviderID", TextUtils.isEmpty(providerID) ? "" : providerID);
            json.put("data", data);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for StorePaymentMethod request
     * @param methodData JSONObject containing payment method data
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForSavePm(JSONObject methodData) {
        if (methodData == null) return null;
        JSONObject json = new JSONObject();
        try {
            json.put("methodData", methodData);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for StorePaymentMethods request
     * @param paymentMethods ArrayList containing PaymentMethod objects for payment methods which
     *                       have to be updated.
     * @return prepared JSONObject
     * @see PaymentMethod
     */
    protected static JSONObject buildJsonForSavePms(ArrayList<PaymentMethod> paymentMethods) {
        if (paymentMethods == null) return null;

        // Create JSONArray from ArrayList<PaymentMethod>
        JSONArray data = new JSONArray();
        for (PaymentMethod paymentMethod : paymentMethods) {
            JSONObject params = buildCardJson(paymentMethod, false);
            if (params == null) return null;
            data.put(params);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("data", data);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Create JSONObject from PaymentMethod object
     * @param paymentMethod PaymentMethod object to be parsed to JSONObject
     * @param isNew detects whether it is a new payment method
     * @return prepared JSONObject
     * @see PaymentMethod
     */
    protected static JSONObject buildCardJson(PaymentMethod paymentMethod, boolean isNew) {
        if (paymentMethod == null) return null;

        // Parse date to server applicable format
        String expDateFormatted = paymentMethod.getExpirationDate() == null ? null : "/Date("+paymentMethod.getExpirationDate().getTime()+")/";

        long pmId = isNew ? 0 : paymentMethod.getPaymentMethodId();
        String accountValue1 = isNew ? paymentMethod.getAccountValue1() : null;
        JSONObject json = new JSONObject();
        try {
            json.putOpt("AccountValue1",accountValue1);
            json.put("AccountValue2", TextUtils.isEmpty(paymentMethod.getAccountValue2()) ? "" : paymentMethod.getAccountValue2());

            // Create billingAddress JSONObject
            JSONObject billingAddress = new JSONObject();
            billingAddress.put("AddressLine1", TextUtils.isEmpty(paymentMethod.getAddress().getAddress1()) ? "" : paymentMethod.getAddress().getAddress1());
            billingAddress.put("AddressLine2", TextUtils.isEmpty(paymentMethod.getAddress().getAddress2()) ? "" : paymentMethod.getAddress().getAddress2());
            billingAddress.put("City", TextUtils.isEmpty(paymentMethod.getAddress().getCity()) ? "" : paymentMethod.getAddress().getCity());
            billingAddress.put("CountryIso", TextUtils.isEmpty(paymentMethod.getAddress().getCountryIso()) ? "" : paymentMethod.getAddress().getCountryIso());
            billingAddress.put("PostalCode", TextUtils.isEmpty(paymentMethod.getAddress().getPostalCode()) ? "" : paymentMethod.getAddress().getPostalCode());
            billingAddress.put("StateIso", TextUtils.isEmpty(paymentMethod.getAddress().getStateIso()) ? "" : paymentMethod.getAddress().getStateIso());

            json.put("Address", billingAddress);
            json.put("Display", TextUtils.isEmpty(paymentMethod.getDisplay()) ? "" : paymentMethod.getDisplay());
            json.put("ExpirationDate", TextUtils.isEmpty(expDateFormatted) ? JSONObject.NULL : expDateFormatted);
            json.put("ID", pmId);
            json.put("Icon", TextUtils.isEmpty(paymentMethod.getIconURL()) ? "" : paymentMethod.getIconURL());
            json.put("IsDefault", paymentMethod.isDefault());
            json.put("IssuerCountryIsoCode", TextUtils.isEmpty(paymentMethod.getIssuerCountryIsoCode()) ? "" : paymentMethod.getIssuerCountryIsoCode());
            json.put("Last4Digits", TextUtils.isEmpty(paymentMethod.getLast4Digits()) ? "" : paymentMethod.getLast4Digits());
            json.put("OwnerName", TextUtils.isEmpty(paymentMethod.getOwnerName()) ? "" : paymentMethod.getOwnerName());
            json.put("PaymentMethodGroupKey", TextUtils.isEmpty(paymentMethod.getPaymentMethodGroupKey()) ? "" : paymentMethod.getPaymentMethodGroupKey());
            json.put("PaymentMethodKey", TextUtils.isEmpty(paymentMethod.getPaymentMethodKey()) ? "" : paymentMethod.getPaymentMethodKey());
            json.put("Title", TextUtils.isEmpty(paymentMethod.getTitle()) ? "" : paymentMethod.getTitle());
            return json;
        } catch (JSONException e) {
            return null;
        }
    }
}
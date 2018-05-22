package com.coriunder.unused.v1.usersdk;

import android.util.Log;

import com.android.volley.VolleyError;
import com.coriunder.unused.v1.basesdk.BaseParser;
import com.coriunder.unused.v1.usersdk.models.BillingAddress;
import com.coriunder.unused.v1.usersdk.models.Customer;
import com.coriunder.unused.v1.usersdk.models.Friend;
import com.coriunder.unused.v1.usersdk.models.PaymentMethod;
import com.coriunder.unused.v1.usersdk.models.ShippingAddress;
import com.coriunder.unused.v1.usersdk.models.Transaction;
import com.coriunder.unused.v1.usersdk.models.TransactionMerchant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 1 on 19.02.2016.
 */
public class Parser extends BaseParser {

    protected static Customer parseCustomer(JSONObject response) {
        Customer customer = new Customer();
        JSONObject jsonResult = (JSONObject)getValue(response, "d", ValueType.JSONOBJECT);
        customer.setFirstName((String)getValue(jsonResult, "FirstName", ValueType.STRING));
        customer.setLastName((String) getValue(jsonResult, "LastName", ValueType.STRING));
        customer.setPhone((String) getValue(jsonResult, "PhoneNumber", ValueType.STRING));
        customer.setEmail((String) getValue(jsonResult, "EmailAddress", ValueType.STRING));
        customer.setCustomerNumber((String)getValue(jsonResult, "CustomerNumber", ValueType.STRING));
        customer.setAddress1((String) getValue(jsonResult, "AddressLine1", ValueType.STRING));
        customer.setAddress2((String) getValue(jsonResult, "AddressLine2", ValueType.STRING));
        customer.setCity((String) getValue(jsonResult, "City", ValueType.STRING));
        customer.setZipCode((String) getValue(jsonResult, "PostalCode", ValueType.STRING));
        customer.setStateIso((String) getValue(jsonResult, "StateIso", ValueType.STRING));
        customer.setCountryIso((String) getValue(jsonResult, "CountryIso", ValueType.STRING));
        customer.setCellNumber((String) getValue(jsonResult, "CellNumber", ValueType.STRING));
        customer.setDateOfBirth(getReadableDate(jsonResult, "DateOfBirth"));
        customer.setPersonalNumber((String) getValue(jsonResult, "PersonalNumber", ValueType.STRING));
        customer.setProfileImageSize((String)getValue(jsonResult, "ProfileImageSize", ValueType.STRING));
        customer.setRegistrationDate(getReadableDate(jsonResult, "RegistrationDate"));
        return customer;
    }

    protected static BillingAddress parseBillingAddress(JSONObject addressObj) {
        BillingAddress address = new BillingAddress();
        address.setAddress1((String) getValue(addressObj, "AddressLine1", ValueType.STRING));
        address.setAddress2((String) getValue(addressObj, "AddressLine2", ValueType.STRING));
        address.setCity((String) getValue(addressObj, "City", ValueType.STRING));
        address.setZipcode((String) getValue(addressObj, "PostalCode", ValueType.STRING));
        address.setStateIso((String) getValue(addressObj, "StateIso", ValueType.STRING));
        address.setCountryIso((String) getValue(addressObj, "CountryIso", ValueType.STRING));
        return address;
    }

    protected static ShippingAddress parseShippingAddress(JSONObject jsonResult) {
        ShippingAddress address = new ShippingAddress();
        address.setAddressId((String) getValue(jsonResult, "ID", ValueType.STRING));
        address.setTitle((String) getValue(jsonResult, "Title", ValueType.STRING));
        address.setAddress1((String) getValue(jsonResult, "AddressLine1", ValueType.STRING));
        address.setAddress2((String) getValue(jsonResult, "AddressLine2", ValueType.STRING));
        address.setCity((String) getValue(jsonResult, "City", ValueType.STRING));
        address.setZipcode((String) getValue(jsonResult, "PostalCode", ValueType.STRING));
        address.setStateIso((String) getValue(jsonResult, "StateIso", ValueType.STRING));
        address.setCountryIso((String) getValue(jsonResult, "CountryIso", ValueType.STRING));
        address.setComment((String) getValue(jsonResult, "Comment", ValueType.STRING));
        address.setIsDefault((boolean) getValue(jsonResult, "IsDefault", ValueType.BOOLEAN));
        return address;
    }

    protected static PaymentMethod parsePaymentMethod(JSONObject jsonResult) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentMethodId((String) getValue(jsonResult, "ID", ValueType.STRING));
        paymentMethod.setTitle((String) getValue(jsonResult, "Title", ValueType.STRING));
        paymentMethod.setDisplayName((String) getValue(jsonResult, "Display", ValueType.STRING));
        paymentMethod.setExpirationDate(getReadableDate(jsonResult, "ExpirationDate"));
        paymentMethod.setIconURL((String) getValue(jsonResult, "Icon", ValueType.STRING));
        paymentMethod.setIsDefault((boolean) getValue(jsonResult, "IsDefault", ValueType.BOOLEAN));
        paymentMethod.setOwnerName((String) getValue(jsonResult, "OwnerName", ValueType.STRING));
        paymentMethod.setPaymentMethodGroupKey((String) getValue(jsonResult, "PaymentMethodGroupKey", ValueType.STRING));
        paymentMethod.setPaymentMethodKey((String) getValue(jsonResult, "PaymentMethodKey", ValueType.STRING));
        paymentMethod.setLast4Digits((String) getValue(jsonResult, "Last4Digits", ValueType.STRING));
        paymentMethod.setIssuerCountryIsoCode((String) getValue(jsonResult, "IssuerCountryIsoCode", ValueType.STRING));
        paymentMethod.setCardNumber((String) getValue(jsonResult, "AccountValue1", ValueType.STRING));
        paymentMethod.setAccountValue2((String) getValue(jsonResult, "AccountValue2", ValueType.STRING));

        JSONObject addressJson = (JSONObject) getValue(jsonResult, "Address", ValueType.JSONOBJECT);
        if (addressJson != null) {
            paymentMethod.setUsesBillingAddress(false);
            paymentMethod.setAddress(parseBillingAddress(addressJson));
        } else {
            paymentMethod.setUsesBillingAddress(true);
        }
        return paymentMethod;
    }

    protected static ArrayList<Object> parseFriendsResponse(JSONObject jsonResult) {
        ArrayList<Object> array = new ArrayList<>();
        JSONObject root = (JSONObject) getValue(jsonResult, "d", ValueType.JSONOBJECT);
        JSONArray items = (JSONArray) getValue(root, "Items", ValueType.JSONARRAY);
        if (items == null) return array;

        for (int i=0; i<items.length(); i++) {
            try {
                JSONObject friendObj = items.getJSONObject(i);
                Friend friend = new Friend();
                friend.setFullName((String) getValue(friendObj, "FullName", ValueType.STRING));
                friend.setUserId((String) getValue(friendObj, "DestWalletId", ValueType.STRING));
                friend.setRelationType((int) getValue(friendObj, "RelationType", ValueType.INT));
                array.add(friend);
            } catch (Exception e) {
                Log.d("UserSDK", "Error when parsing friend with index " + i);
            }
        }
        return array;
    }

    protected static Transaction parseTransaction(JSONObject transObj) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId((String) getValue(transObj, "ID", ValueType.STRING));
        transaction.setPaymentMethodKey((String) getValue(transObj, "PaymentMethodKey", ValueType.STRING));
        transaction.setPaymentMethodGroupKey((String) getValue(transObj, "PaymentMethodGroupKey", ValueType.STRING));
        transaction.setAutoCode((String) getValue(transObj, "AutoCode", ValueType.STRING));
        transaction.setAmount((String) getValue(transObj, "Amount", ValueType.STRING));
        transaction.setDisplayName((String) getValue(transObj, "PaymentDisplay", ValueType.STRING));
        transaction.setInsertDate(getReadableDate(transObj, "InsertDate"));
        transaction.setCurrencyIso((String) getValue(transObj, "CurrencyIso", ValueType.STRING));
        transaction.setComment((String) getValue(transObj, "Comment", ValueType.STRING));
        transaction.setPhone((String) getValue(transObj, "Phone", ValueType.STRING));
        transaction.setEmail((String) getValue(transObj, "Email", ValueType.STRING));
        transaction.setText((String) getValue(transObj, "Text", ValueType.STRING));
        transaction.setFullName((String) getValue(transObj, "FullName", ValueType.STRING));
        transaction.setReceiptText((String) getValue(transObj, "ReceiptText", ValueType.STRING));
        transaction.setReceiptLink((String) getValue(transObj, "ReceiptLink", ValueType.STRING));

        JSONObject merchantObj = (JSONObject) getValue(transObj, "Merchant", ValueType.JSONOBJECT);
        if (merchantObj != null) transaction.setMerchant(parseMerchant(merchantObj));

        JSONObject billingObj = (JSONObject) getValue(transObj, "Address", ValueType.JSONOBJECT);
        if (billingObj != null) transaction.setBillingAddress(parseBillingAddress(billingObj));

        JSONObject shippingObj = (JSONObject) getValue(transObj, "ShippingAddress", ValueType.JSONOBJECT);
        if (shippingObj != null) transaction.setShippingAddress(parseShippingAddress(shippingObj));

        return transaction;
    }

    protected static TransactionMerchant parseMerchant(JSONObject merchantObj) {
        TransactionMerchant merchant = new TransactionMerchant();
        merchant.setNumber((String) getValue(merchantObj, "Number", ValueType.STRING));
        merchant.setName((String) getValue(merchantObj, "Name", ValueType.STRING));
        merchant.setWebsite((String) getValue(merchantObj, "WebsiteUrl", ValueType.STRING));
        merchant.setCountryIso((String) getValue(merchantObj, "CountryIso", ValueType.STRING));
        merchant.setAddress1((String) getValue(merchantObj, "AddressLine1", ValueType.STRING));
        merchant.setCity((String) getValue(merchantObj, "City", ValueType.STRING));
        merchant.setPhone((String) getValue(merchantObj, "PhoneNumber", ValueType.STRING));
        merchant.setEmail((String) getValue(merchantObj, "Email", ValueType.STRING));
        merchant.setZipCode((String) getValue(merchantObj, "PostalCode", ValueType.STRING));
        merchant.setAddress2((String) getValue(merchantObj, "AddressLine2", ValueType.STRING));
        merchant.setStateIso((String) getValue(merchantObj, "StateIso", ValueType.STRING));
        merchant.setLogoUrl((String) getValue(merchantObj, "LogoUrl", ValueType.STRING));
        return merchant;
    }

    protected static Object getValue(JSONObject resultObject, String key, ValueType type) {
        return getValueBase(resultObject, key, type);
    }

    protected static String getErrorText(VolleyError error) {
        return getErrorTextBase(error);
    }

    protected static Date getReadableDate(JSONObject jsonResult, String key) {
        return getReadableDateBase(jsonResult, key);
    }

    protected static boolean isEmptyOrNull(String value) {
        return isEmptyOrNullBase(value);
    }

    protected static boolean isRequestSuccessful(JSONObject resultObject) {
        return isRequestSuccessfulBase(resultObject);
    }

    protected static String getMessage(JSONObject resultObject) {
        return getMessageBase(resultObject);
    }
}
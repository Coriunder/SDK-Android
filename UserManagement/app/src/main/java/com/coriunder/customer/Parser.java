package com.coriunder.customer;

import android.util.Log;

import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.models.Address;
import com.coriunder.base.utils.BaseParser;
import com.coriunder.customer.models.Customer;
import com.coriunder.customer.models.Friend;
import com.coriunder.customer.models.ShippingAddress;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parser methods for Customer services
 */
public class Parser extends BaseParser {
    /**
     * Method to parse JSONObject to ShippingAddress object
     * @param addressObj JSONObject to parse
     * @return ShippingAddress object
     */
    protected static ShippingAddress parseShippingAddress(JSONObject addressObj) {
        ShippingAddress address = new ShippingAddress();
        address.setAddress1(getString(addressObj, "AddressLine1"));
        address.setAddress2(getString(addressObj, "AddressLine2"));
        address.setCity(getString(addressObj, "City"));
        address.setCountryIso(getString(addressObj, "CountryIso"));
        address.setPostalCode(getString(addressObj, "PostalCode"));
        address.setStateIso(getString(addressObj, "StateIso"));
        address.setComment(getString(addressObj, "Comment"));
        address.setAddressId(getLong(addressObj, "ID"));
        address.setDefault(getBoolean(addressObj, "IsDefault"));
        address.setTitle(getString(addressObj, "Title"));
        return address;
    }

    /*
    ToDoV2:
    ignored ProfileImage & ProfileImageSize
     */
    /**
     * Method to parse JSONObject to ArrayList containing Friend objects
     * @param jsonResult JSONObject to parse
     * @return ArrayList containing Friend objects
     */
    protected static ArrayList<Friend> parseFriendsResponse(JSONObject jsonResult) {
        ArrayList<Friend> array = new ArrayList<>();
        JSONArray items = getJsonArray(jsonResult, "Items");
        if (items == null) return array;

        for (int i=0; i<items.length(); i++) {
            try {
                JSONObject friendObj = items.getJSONObject(i);
                Friend friend = new Friend();
                friend.setUserId(getString(friendObj, "DestWalletId"));
                friend.setFullName(getString(friendObj, "FullName"));
                friend.setRelationType(getInt(friendObj, "RelationType"));
                array.add(friend);
            } catch (Exception e) {
                Log.e(Coriunder.getContext().getString(R.string.customer_log),
                        Coriunder.getContext().getString(R.string.customer_error_friend) + i);
            }
        }
        return array;
    }

    /*
    ToDoV2:
    ignored ProfileImage & ProfileImageSize
     */
    /**
     * Method to parse JSONObject to Customer object
     * @param response JSONObject to parse
     * @return Customer object
     */
    protected static Customer parseCustomer(JSONObject response) {
        JSONObject jsonResult = getJsonObject(response, "d");
        Address address = parseAddress(jsonResult);
        Customer customer = new Customer();
        customer.setAddress(address);
        customer.setCellNumber(getString(jsonResult, "CellNumber"));
        customer.setCustomerNumber(getString(jsonResult, "CustomerNumber"));
        customer.setDateOfBirth(getReadableDate(jsonResult, "DateOfBirth"));
        customer.setEmail(getString(jsonResult, "EmailAddress"));
        customer.setFirstName(getString(jsonResult, "FirstName"));
        customer.setLastName(getString(jsonResult, "LastName"));
        customer.setPersonalNumber(getString(jsonResult, "PersonalNumber"));
        customer.setPhone(getString(jsonResult, "PhoneNumber"));
        customer.setRegistrationDate(getReadableDate(jsonResult, "RegistrationDate"));
        return customer;
    }

    /**
     * Method to parse JSONObject to ArrayList containing ShippingAddress objects
     * @param response JSONObject to parse
     * @return ArrayList containing ShippingAddress objects
     */
    protected static ArrayList<ShippingAddress> parseShippingAddresses(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<ShippingAddress> addressesArray = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject addressObj = result.getJSONObject(i);
                    ShippingAddress address = Parser.parseShippingAddress(addressObj);
                    addressesArray.add(address);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.customer_log),
                            Coriunder.getContext().getString(R.string.customer_error_address) + i);
                }
            }
        }
        return addressesArray;
    }
}
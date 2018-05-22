package com.coriunder.customer;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.customer.models.ShippingAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * This class contains methods designed to prepare
 * JSONObjects to send with requests to Customer service
 */
public class JsonBuilder {
    /*
    ToDoV2:
    ShippingAddresses part ignored
    StoredPaymentMethods part ignored
    Many params of the info part ignored
    */
    /**
     * Prepare JSONObject for RegisterCustomer request
     * @param password password to set
     * @param pinCode pin code to set
     * @param email user's email
     * @param firstName user's name
     * @param lastName user's surname
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForRegisterCustomer(String password, String pinCode, String email,
                                                             String firstName, String lastName) {
        JSONObject json = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            data.put("ApplicationToken", Coriunder.getAppToken());
            data.put("Password", TextUtils.isEmpty(password) ? "" : password);
            data.put("PinCode", TextUtils.isEmpty(pinCode) ? "" : pinCode);

            // Create info JSONObject
            JSONObject info = new JSONObject();
            info.put("EmailAddress", TextUtils.isEmpty(email) ? "" : email);
            info.put("FirstName", TextUtils.isEmpty(firstName) ? "" : firstName);
            info.put("LastName", TextUtils.isEmpty(lastName) ? "" : lastName);
            data.put("info", info);

            json.put("data", data);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for FindFriend request
     * @param nameOrId user's name or id to perform search
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results amount per page
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForFindFriend(String nameOrId, int page, int pageSize) {
        JSONObject json = new JSONObject();
        try {
            json.put("searchTerm", TextUtils.isEmpty(nameOrId) ? "" : nameOrId);

            // Create sortAndPage JSONObject
            JSONObject sortAndPage = new JSONObject();
            sortAndPage.put("PageNumber", page);
            sortAndPage.put("PageSize", pageSize);
            json.put("sortAndPage", sortAndPage);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for FriendRequest request
     * @param friendId id of the user you want to send friend request to
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForFriendRequest(String friendId) {
        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", TextUtils.isEmpty(friendId) ? "" : friendId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetFriendRequests request
     * @param friendId id of the user whose request you want to get info about
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetFriendRequests(String friendId) {
        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", TextUtils.isEmpty(friendId) ? JSONObject.NULL : friendId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetFriends request
     * @param friendId id of the user which info has to be loaded
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetFriends(String friendId) {
        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", TextUtils.isEmpty(friendId) ? JSONObject.NULL : friendId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for ImportFriendsFromFacebook request
     * @param accessToken user's Facebook access token
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForImportFb(String accessToken) {
        JSONObject json = new JSONObject();
        try {
            json.put("accessToken", TextUtils.isEmpty(accessToken) ? "" : accessToken);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for RemoveFriend request
     * @param friendId id of the user which has to be removed from the friends' list
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForRemoveFriend(String friendId) {
        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", TextUtils.isEmpty(friendId) ? "" : friendId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for ReplyFriendRequest request
     * @param friendId id of the user whose friend request has to approved or declined
     * @param approve defines whether friend request has to approved or declined
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForReplyRequest(String friendId, boolean approve) {
        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", TextUtils.isEmpty(friendId) ? "" : friendId);
            json.put("approve", approve);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for SetFriendRelation request
     * @param friendId id of the user you are going to set relation with
     * @param relation relation type to set
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForRelation(String friendId, int relation) {
        JSONObject json = new JSONObject();
        try {
            json.put("destWalletId", TextUtils.isEmpty(friendId) ? "" : friendId);
            json.put("relationTypeKey", relation);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for DeleteShippingAddress request
     * @param addressId id of address which has to be deleted
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForDeleteAddress(long addressId) {
        JSONObject json = new JSONObject();
        try {
            json.put("addressId", addressId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetShippingAddress request
     * @param addressId id of the required shipping address
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetAddress(long addressId) {
        JSONObject json = new JSONObject();
        try {
            json.put("addressId", addressId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for SaveShippingAddress request
     * @param addressJson JSONObject containing shipping address data
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForSaveAddress(JSONObject addressJson) {
        if (addressJson == null) return null;
        JSONObject json = new JSONObject();
        try {
            json.put("address", addressJson);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for SaveShippingAddresses request
     * @param shippingAddresses ArrayList containing ShippingAddress objects for shipping addresses
     *                          which have to be updated.
     * @return prepared JSONObject
     * @see ShippingAddress
     */
    protected static JSONObject buildJsonForSaveAddresses(ArrayList<ShippingAddress> shippingAddresses) {
        if (shippingAddresses == null) return null;

        // Create JSONArray from ArrayList<ShippingAddress>
        JSONArray data = new JSONArray();
        for (ShippingAddress shippingAddress : shippingAddresses) {
            JSONObject addressJson = buildAddressJson(shippingAddress, false);
            if (addressJson == null) return null;
            data.put(addressJson);
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
     * Prepare JSONObject for SaveCustomer request
     * @param address1 user's main address
     * @param address2 user's secondary address
     * @param city user's city
     * @param countryIso user's country ISO. List of country ISO codes can be received using method
     *                   getCountriesStatesAndLanguages from InternationalSDK SDK
     * @param postalCode user's address postal code
     * @param stateIso user's state ISO if any. List of state ISO codes can be received using method
     *                 getCountriesStatesAndLanguages from InternationalSDK SDK
     * @param cellNumber cell phone number
     * @param birthDate date of birth
     * @param email user's email
     * @param firstName user's name
     * @param lastName user's surname
     * @param personalNumber user's social security number
     * @param phone user's phone
     * @param profileImageUri URI of an image which should be set as user profile image
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForSaveCustomer(String address1, String address2, String city,
                                String countryIso, String postalCode, String stateIso, String cellNumber,
                                String birthDate, String email, String firstName, String lastName,
                                String personalNumber, String phone, Uri profileImageUri) {
        JSONObject json = new JSONObject();
        try {
            // Create info JSONObject
            JSONObject info = new JSONObject();
            info.put("AddressLine1", address1);
            info.put("AddressLine2", address2);
            info.put("City", city);
            info.put("CountryIso", countryIso);
            info.put("PostalCode", postalCode);
            info.put("StateIso", stateIso);
            info.put("CellNumber", cellNumber);
            info.put("DateOfBirth", TextUtils.isEmpty(birthDate) ? JSONObject.NULL : birthDate);
            info.put("EmailAddress", email);
            info.put("FirstName", firstName);
            info.put("LastName", lastName);
            info.put("PersonalNumber", personalNumber);
            info.put("PhoneNumber", phone);

            // Encode image to add it to JSONObject
            Bitmap scaledBitmap = new BitmapUtils(Coriunder.getContext()).getBitmap(profileImageUri, 300, 300);
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
                    Log.e(Coriunder.getContext().getString(R.string.customer_log),
                            Coriunder.getContext().getString(R.string.customer_add_image_error));
                }
            }

            json.put("info", info);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Create JSONObject from ShippingAddress object
     * @param address ShippingAddress object to be parsed to JSONObject
     * @param isNew detects whether it is a new shipping address
     * @return prepared JSONObject
     * @see ShippingAddress
     */
    protected static JSONObject buildAddressJson(ShippingAddress address, boolean isNew) {
        if (address == null) return null;

        long addressId = isNew ? 0 : address.getAddressId();
        JSONObject json = new JSONObject();
        try {
            json.put("AddressLine1", TextUtils.isEmpty(address.getAddress1()) ? "" : address.getAddress1());
            json.put("AddressLine2", TextUtils.isEmpty(address.getAddress2()) ? "" : address.getAddress2());
            json.put("City", TextUtils.isEmpty(address.getCity()) ? "" : address.getCity());
            json.put("CountryIso", TextUtils.isEmpty(address.getCountryIso()) ? "" : address.getCountryIso());
            json.put("PostalCode", TextUtils.isEmpty(address.getPostalCode()) ? "" : address.getPostalCode());
            json.put("StateIso", TextUtils.isEmpty(address.getStateIso()) ? "" : address.getStateIso());
            json.put("Comment", TextUtils.isEmpty(address.getComment()) ? "" : address.getComment());
            json.put("ID", addressId);
            json.put("IsDefault", address.isDefault());
            json.put("Title", TextUtils.isEmpty(address.getTitle()) ? "" : address.getTitle());
            return json;
        } catch (JSONException e) {
            return null;
        }
    }
}
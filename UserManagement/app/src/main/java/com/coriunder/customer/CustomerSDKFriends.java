package com.coriunder.customer;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.callbacks.ReceivedServiceCallback;
import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.base.common.requests.CustomHeadersJsonObjectRequest;
import com.coriunder.base.utils.CommonRequests;
import com.coriunder.customer.callbacks.ReceivedFriendsCallback;
import com.coriunder.customer.models.Friend;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods to perform requests to the Customer service related to friends
 */
@SuppressWarnings("unused")
public class CustomerSDKFriends {
    private static CustomerSDKFriends instance;
    public static final String SERVICE_URL_PART = "Customer.svc";

    public CustomerSDKFriends() {
    }

    /**
     * Get instance for CustomerSDKFriends class.
     * In case there is no current instance, a new one will be created
     * @return CustomerSDKFriends instance
     */
    public static CustomerSDKFriends getInstance() {
        if (instance == null) instance = new CustomerSDKFriends();
        return instance;
    }

    /**
     * Find user by name or id.
     *
     * @param nameOrId user's name or id to perform search
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results amount per page
     * @param callback will be called after request completion
     * @see ReceivedFriendsCallback
     */
    public void findFriend(String nameOrId, int page, int pageSize, final ReceivedFriendsCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForFindFriend(nameOrId, page, pageSize);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), new ArrayList<Friend>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("FindFriend"), json, createFriendsSuccessListener(callback), createFriendsErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Send friend request to exact user.
     *
     * @param friendId id of the user you want to send friend request to
     * @param callback will be called after request completion
     * @see ReceivedFriendsCallback
     */
    public void sendFriendRequest(String friendId, final ReceivedFriendsCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForFriendRequest(friendId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), new ArrayList<Friend>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("FriendRequest"), json, createFriendsSuccessListener(callback), createFriendsErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get all friend requests for current user.
     *
     * @param callback will be called after request completion
     * @see ReceivedFriendsCallback
     */
    public void getFriendRequests(final ReceivedFriendsCallback callback) {
        getFriendRequest(null, callback);
    }

    /**
     * Get exact friend request for current user.
     *
     * @param friendId id of the user whose request you want to get info about
     * @param callback will be called after request completion
     * @see ReceivedFriendsCallback
     */
    public void getFriendRequest(String friendId, final ReceivedFriendsCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetFriendRequests(friendId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), new ArrayList<Friend>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetFriendRequests"), json, createFriendsSuccessListener(callback), createFriendsErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Get list of all friends of current user and info about each friend (profile images are not
     * included, call getImageForUser from the CustomerSDKCustomers for each user to get corresponding image).
     *
     * @param callback will be called after request completion
     * @see ReceivedFriendsCallback
     */
    public void getFriendsList(final ReceivedFriendsCallback callback) {
        getFriend(null, callback);
    }

    /**
     * Get info about exact friend of the current user (profile image is not included, call
     * getImageForUser from the CustomerSDKCustomers to get friend's profile image).
     *
     * @param friendId id of the user which info has to be loaded
     * @param callback will be called after request completion
     * @see ReceivedFriendsCallback
     */
    public void getFriend(String friendId, final ReceivedFriendsCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForGetFriends(friendId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(), new ArrayList<Friend>(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("GetFriends"), json, createFriendsSuccessListener(callback), createFriendsErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Import user's friends from Facebook
     *
     * @param accessToken user's Facebook access token
     * @param callback will be called after request completion
     * @see ReceivedBasicCallback
     */
    public void importFacebookFriends(String accessToken, final ReceivedBasicCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForImportFb(accessToken);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("ImportFriendsFromFacebook"), json, CommonRequests.createBasicSuccessListener(callback),
                CommonRequests.createBasicErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Remove exact user from the friends' list of current user.
     *
     * @param friendId id of the user which has to be removed from the friends' list
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void removeFriend(String friendId, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForRemoveFriend(friendId);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("RemoveFriend"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Aprrove or decline exact friend request.
     *
     * @param friendId id of the user whose friend request has to approved or declined
     * @param approve defines whether friend request has to approved or declined
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void replyFriendRequest(String friendId, boolean approve, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForReplyRequest(friendId, approve);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("ReplyFriendRequest"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * Set relation with exact user from the friend list of current user.
     *
     * @param friendId id of the user you are going to set relation with
     * @param relation relation type to set
     * @param callback will be called after request completion
     * @see ReceivedServiceCallback
     */
    public void setFriendRelation(String friendId, int relation, final ReceivedServiceCallback callback) {
        // Create json object to send with request
        JSONObject json = JsonBuilder.buildJsonForRelation(friendId, relation);
        if (json == null) {
            if (callback != null) callback.onResultReceived(false, new ServiceResult(),
                    getContext().getString(R.string.create_request_error));
            return;
        }

        // Create request
        CustomHeadersJsonObjectRequest postRequest = new CustomHeadersJsonObjectRequest(Request.Method.POST,
                createUrl("SetFriendRelation"), json, CommonRequests.createServiceSuccessListener(callback),
                CommonRequests.createServiceErrorListener(callback));
        CommonRequests.performRequest(postRequest);
    }

    /**
     * @param callback ReceivedFriendsCallback to be called on request result
     * @return success listener which parses result to ServiceResult object
     *         and ArrayList<Friend> and calls ReceivedFriendsCallback
     * @see ServiceResult
     * @see Friend
     * @see ReceivedFriendsCallback
     */
    private Response.Listener<JSONObject> createFriendsSuccessListener(final ReceivedFriendsCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    // Parse response
                    JSONObject jsonResult = Parser.getJsonObject(response, "d");
                    ServiceResult result = Parser.parseServiceResult(jsonResult);
                    ArrayList<Friend> friendsArray = Parser.parseFriendsResponse(jsonResult);
                    if (callback != null) callback.onResultReceived(result.isSuccess(),
                            result, friendsArray, result.getMessage());
                } else {
                    if (callback != null) callback.onResultReceived(false, new ServiceResult(),
                            new ArrayList<Friend>(), getContext().getString(R.string.empty_response_error));
                }
            }
        };
    }

    /**
     * @param callback ReceivedFriendsCallback to be called on request result
     * @return error listener which parses error and calls ReceivedFriendsCallback
     * @see ReceivedFriendsCallback
     */
    private Response.ErrorListener createFriendsErrorListener(final ReceivedFriendsCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null) callback.onResultReceived(false, new ServiceResult(),
                        new ArrayList<Friend>(), Parser.getErrorText(error));
            }
        };
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
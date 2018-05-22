package com.coriunder.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.coriunder.base.utils.Constants;
import com.coriunder.base.utils.SimpleCrypto;

/**
 * Class which holds data about the user's session and deals with it
 */
public class UserSession {
    private static UserSession instance;
    private String credentialsToken;
    private String credentialsHeader;

    protected UserSession() {
    }

    /**
     * Get instance for UserSession class.
     * In case there is no current instance, a new one will be created
     * @return UserSession instance
     */
    public static UserSession getInstance() {
        if (instance == null) instance = new UserSession();
        return instance;
    }

    /**
     * Get credentials token for currently logged in user
     * @return credentials token or empty String in case user is not logged in
     */
    protected String getCredentialsToken() {
        if (TextUtils.isEmpty(credentialsToken)) {
            // We have no credentialsToken in memory, try to restore it from SharedPreferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Coriunder.getContext());
            try {
                credentialsToken = SimpleCrypto.decrypt(prefs.getString(Constants.SP_CRED_TOKEN, ""));
            } catch (Exception e) {
                credentialsToken = "";
            }
        }
        return credentialsToken;
    }

    /**
     * Set credentials token for currently logged in user
     * @param credentialsToken token to set
     */
    public void setCredentialsToken(String credentialsToken) {
        this.credentialsToken = credentialsToken;
    }

    /**
     * Get header for credentials token
     * @return credentials token header or empty String in case user is not logged in
     */
    protected String getCredentialsHeader() {
        if (TextUtils.isEmpty(credentialsHeader)) {
            // We have no credentialsHeader in memory, try to restore it from SharedPreferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Coriunder.getContext());
            try {
                credentialsHeader = SimpleCrypto.decrypt(prefs.getString(Constants.SP_HEADER, ""));
            } catch (Exception e) {
                credentialsHeader = "";
            }
        }
        return credentialsHeader;
    }

    /**
     * Set header for credentials token
     * @param credentialsHeader header to set
     */
    public void setCredentialsHeader(String credentialsHeader) {
        this.credentialsHeader = credentialsHeader;
    }

    /**
     * Reset current session
     */
    public void resetSession() {
        Intent intent = new Intent(Coriunder.SESSION_EXPIRED);
        LocalBroadcastManager.getInstance(Coriunder.getContext()).sendBroadcast(intent);

        this.credentialsToken = null;
        this.credentialsHeader = null;
        instance = null;
    }
}
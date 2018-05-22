package com.coriunder.account.models;

import com.coriunder.base.common.models.ServiceResult;

import java.util.Date;

/**
 * Login result data
 */
@SuppressWarnings("unused")
public class LoginResult extends ServiceResult {
    private String credentialsHeaderName;
    private String credentialsToken;
    private String encodedCookie;
    private boolean isDeviceActivated;
    private boolean isDeviceBlocked;
    private boolean isDeviceRegistered;
    private boolean isDeviceRegistrationRequired;
    private boolean isFirstLogin;
    private Date lastLogin;
    private boolean versionUpdateRequired;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public LoginResult() {
        code = 0;
        isSuccess = false;
        key = "";
        message = "";
        number = "";
        credentialsHeaderName = "";
        credentialsToken = "";
        encodedCookie = "";
        isDeviceActivated = false;
        isDeviceBlocked = false;
        isDeviceRegistered = false;
        isDeviceRegistrationRequired = false;
        isFirstLogin = false;
        lastLogin = new Date();
        versionUpdateRequired = false;
    }

    public String getCredentialsHeaderName() { return credentialsHeaderName; }
    public void setCredentialsHeaderName(String credentialsHeaderName) { this.credentialsHeaderName = credentialsHeaderName; }

    public String getCredentialsToken() { return credentialsToken; }
    public void setCredentialsToken(String credentialsToken) { this.credentialsToken = credentialsToken; }

    public String getEncodedCookie() { return encodedCookie; }
    public void setEncodedCookie(String encodedCookie) { this.encodedCookie = encodedCookie; }

    public boolean isDeviceActivated() { return isDeviceActivated; }
    public void setDeviceActivated(boolean deviceActivated) { isDeviceActivated = deviceActivated; }

    public boolean isDeviceBlocked() { return isDeviceBlocked; }
    public void setDeviceBlocked(boolean deviceBlocked) { isDeviceBlocked = deviceBlocked; }

    public boolean isDeviceRegistered() { return isDeviceRegistered; }
    public void setDeviceRegistered(boolean deviceRegistered) { isDeviceRegistered = deviceRegistered; }

    public boolean isDeviceRegistrationRequired() { return isDeviceRegistrationRequired; }
    public void setDeviceRegistrationRequired(boolean deviceRegistrationRequired) { isDeviceRegistrationRequired = deviceRegistrationRequired; }

    public boolean isFirstLogin() { return isFirstLogin; }
    public void setFirstLogin(boolean firstLogin) { isFirstLogin = firstLogin; }

    public Date getLastLogin() { return lastLogin; }
    public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }

    public boolean isVersionUpdateRequired() { return versionUpdateRequired; }
    public void setVersionUpdateRequired(boolean versionUpdateRequired) { this.versionUpdateRequired = versionUpdateRequired; }
}
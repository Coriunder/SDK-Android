package com.coriunder.account.models;

import com.coriunder.base.common.models.ServiceResult;

/**
 * Result of cookie decoding
 */
@SuppressWarnings("unused")
public class CookieDecodeResult extends ServiceResult {
    /*
    ToDoV2:
    excluded image
     */

    private String email;
    private String fullName;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public CookieDecodeResult() {
        email = "";
        fullName = "";
        code = 0;
        isSuccess = false;
        key = "";
        message = "";
        number = "";
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
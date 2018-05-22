package com.coriunder.base.common.models;

/**
 * Common service response data
 */
@SuppressWarnings("unused")
public class ServiceResult {
    protected int code;
    protected boolean isSuccess;
    protected String key;
    protected String message;
    protected String number;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public ServiceResult() {
        code = 0;
        isSuccess = false;
        key = "";
        message = "";
        number = "";
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean isSuccess) { this.isSuccess = isSuccess; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
}
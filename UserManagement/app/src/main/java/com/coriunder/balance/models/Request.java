package com.coriunder.balance.models;

import java.util.Date;

/**
 * Information about a transaction request
 */
@SuppressWarnings("unused")
public class Request {

    private double amount;
    private Date confirmDate;
    private String currencyISOCode;
    private long requestId;
    private boolean isApproved;
    private boolean isPush;
    private Date requestDate;
    private long sourceAccountId;
    private String sourceAccountName;
    private String sourceText;
    private long targetAccountId;
    private String targetAccountName;
    private String targetText;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Request() {
        amount = 0;
        confirmDate = new Date();
        currencyISOCode = "";
        requestId = 0;
        isApproved = false;
        isPush = false;
        requestDate = new Date();
        sourceAccountId = 0;
        sourceAccountName = "";
        sourceText = "";
        targetAccountId = 0;
        targetAccountName = "";
        targetText = "";
    }

    public long getRequestId() { return requestId; }
    public void setRequestId(long requestId) { this.requestId = requestId; }

    public Date getRequestDate() { return requestDate; }
    public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }

    public long getSourceAccountId() { return sourceAccountId; }
    public void setSourceAccountId(long sourceAccountId) { this.sourceAccountId = sourceAccountId; }

    public long getTargetAccountId() { return targetAccountId; }
    public void setTargetAccountId(long targetAccountId) { this.targetAccountId = targetAccountId; }

    public String getSourceText() { return sourceText; }
    public void setSourceText(String sourceText) { this.sourceText = sourceText; }

    public String getTargetText() { return targetText; }
    public void setTargetText(String targetText) { this.targetText = targetText; }

    public String getSourceAccountName() { return sourceAccountName; }
    public void setSourceAccountName(String sourceAccountName) { this.sourceAccountName = sourceAccountName; }

    public String getTargetAccountName() { return targetAccountName; }
    public void setTargetAccountName(String targetAccountName) { this.targetAccountName = targetAccountName; }

    public boolean isPush() { return isPush; }
    public void setPush(boolean isPush) { this.isPush = isPush; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrencyISOCode() { return currencyISOCode; }
    public void setCurrencyISOCode(String currencyISOCode) { this.currencyISOCode = currencyISOCode; }

    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean isApproved) { this.isApproved = isApproved; }

    public Date getConfirmDate() { return confirmDate; }
    public void setConfirmDate(Date confirmDate) { this.confirmDate = confirmDate; }
}
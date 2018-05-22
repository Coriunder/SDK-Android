package com.coriunder.unused.v1.balancesdk.models;

import java.util.Date;

/**
 * Created by 1 on 22.02.2016.
 */
public class RequestItem {

    private String requestId;
    private Date requestDate;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private String sourceText;
    private String targetText;
    private String sourceAccountName;
    private String targetAccountName;
    private boolean isPush;
    private String amount;
    private String currencyISOCode;
    private boolean isApproved;
    private Date confirmDate;

    public RequestItem() {
        requestId = "";
        requestDate = new Date();
        sourceAccountNumber = "";
        targetAccountNumber = "";
        sourceText = "";
        targetText = "";
        sourceAccountName = "";
        targetAccountName = "";
        isPush = false;
        amount = "";
        currencyISOCode = "";
        isApproved = false;
        confirmDate = new Date();
    }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public Date getRequestDate() { return requestDate; }
    public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }

    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }

    public String getTargetAccountNumber() { return targetAccountNumber; }
    public void setTargetAccountNumber(String targetAccountNumber) { this.targetAccountNumber = targetAccountNumber; }

    public String getSourceText() { return sourceText; }
    public void setSourceText(String sourceText) { this.sourceText = sourceText; }

    public String getTargetText() { return targetText; }
    public void setTargetText(String targetText) { this.targetText = targetText; }

    public String getSourceAccountName() { return sourceAccountName; }
    public void setSourceAccountName(String sourceAccountName) { this.sourceAccountName = sourceAccountName; }

    public String getTargetAccountName() { return targetAccountName; }
    public void setTargetAccountName(String targetAccountName) { this.targetAccountName = targetAccountName; }

    public boolean isPush() { return isPush; }
    public void setIsPush(boolean isPush) { this.isPush = isPush; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getCurrencyISOCode() { return currencyISOCode; }
    public void setCurrencyISOCode(String currencyISOCode) { this.currencyISOCode = currencyISOCode; }

    public boolean isApproved() { return isApproved; }
    public void setIsApproved(boolean isApproved) { this.isApproved = isApproved; }

    public Date getConfirmDate() { return confirmDate; }
    public void setConfirmDate(Date confirmDate) { this.confirmDate = confirmDate; }
}
package com.coriunder.transactions.models;

import java.util.Date;

/**
 * Brief information about transaction returned after lookup
 */
@SuppressWarnings("unused")
public class TransactionLookup {
    private String merchantName;
    private String merchantSupportEmail;
    private String merchantSupportPhone;
    private String merchantWebSite;
    private String methodString;
    private Date transactionDate;
    private long transactionID;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public TransactionLookup() {
        merchantName = "";
        merchantSupportEmail = "";
        merchantSupportPhone = "";
        merchantWebSite = "";
        methodString = "";
        transactionDate = new Date();
        transactionID = 0;
    }

    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public String getMerchantSupportEmail() { return merchantSupportEmail; }
    public void setMerchantSupportEmail(String merchantSupportEmail) { this.merchantSupportEmail = merchantSupportEmail; }

    public String getMerchantSupportPhone() { return merchantSupportPhone; }
    public void setMerchantSupportPhone(String merchantSupportPhone) { this.merchantSupportPhone = merchantSupportPhone; }

    public String getMerchantWebSite() { return merchantWebSite; }
    public void setMerchantWebSite(String merchantWebSite) { this.merchantWebSite = merchantWebSite; }

    public String getMethodString() { return methodString; }
    public void setMethodString(String methodString) { this.methodString = methodString; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public long getTransactionID() { return transactionID; }
    public void setTransactionID(long transactionID) { this.transactionID = transactionID; }
}
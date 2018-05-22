package com.coriunder.transactions.models;

import com.coriunder.base.common.models.Address;
import com.coriunder.base.common.models.Merchant;

import java.util.Date;

/**
 * Information about exact transaction
 */
@SuppressWarnings("unused")
public class Transaction {
    private double amount;
    private String authCode;
    private String comment;
    private String currencyIso;
    private long transactionId;
    private Date insertDate;
    private int installments;
    private boolean isManual;
    private boolean isRefunded;
    private Merchant merchant;
    private String payerEmail;
    private String payerFullName;
    private String payerPhone;
    private Address payerShippingAddress;
    private Address paymentDataBillingAddress;
    private String paymentDataBin;
    private String paymentDataBinCountry;
    private int paymentDataExpirationMonth;
    private int paymentDataExpirationYear;
    private String paymentDataLast4;
    private String paymentDataType;
    private String paymentDisplay;
    private String paymentMethodGroupKey;
    private String paymentMethodKey;
    private String receiptLink;
    private String receiptText;
    private String text;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Transaction() {
        amount = 0;
        authCode = "";
        comment = "";
        currencyIso = "";
        transactionId = 0;
        insertDate = new Date();
        installments = 0;
        isManual = false;
        isRefunded = false;
        merchant = new Merchant();
        payerEmail = "";
        payerFullName = "";
        payerPhone = "";
        payerShippingAddress = new Address();
        paymentDataBillingAddress = new Address();
        paymentDataBin = "";
        paymentDataBinCountry = "";
        paymentDataExpirationMonth = 0;
        paymentDataExpirationYear = 0;
        paymentDataLast4 = "";
        paymentDataType = "";
        paymentDisplay = "";
        paymentMethodGroupKey = "";
        paymentMethodKey = "";
        receiptLink = "";
        receiptText = "";
        text = "";
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getAuthCode() { return authCode; }
    public void setAuthCode(String authCode) { this.authCode = authCode; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getCurrencyIso() { return currencyIso; }
    public void setCurrencyIso(String currencyIso) { this.currencyIso = currencyIso; }

    public long getTransactionId() { return transactionId; }
    public void setTransactionId(long transactionId) { this.transactionId = transactionId; }

    public Date getInsertDate() { return insertDate; }
    public void setInsertDate(Date insertDate) { this.insertDate = insertDate; }

    public int getInstallments() { return installments; }
    public void setInstallments(int installments) { this.installments = installments; }

    public boolean isManual() { return isManual; }
    public void setManual(boolean manual) { isManual = manual; }

    public boolean isRefunded() { return isRefunded; }
    public void setRefunded(boolean refunded) { isRefunded = refunded; }

    public Merchant getMerchant() { return merchant; }
    public void setMerchant(Merchant merchant) { this.merchant = merchant; }

    public String getPayerEmail() { return payerEmail; }
    public void setPayerEmail(String payerEmail) { this.payerEmail = payerEmail; }

    public String getPayerFullName() { return payerFullName; }
    public void setPayerFullName(String payerFullName) { this.payerFullName = payerFullName; }

    public String getPayerPhone() { return payerPhone; }
    public void setPayerPhone(String payerPhone) { this.payerPhone = payerPhone; }

    public Address getPayerShippingAddress() { return payerShippingAddress; }
    public void setPayerShippingAddress(Address payerShippingAddress) { this.payerShippingAddress = payerShippingAddress; }

    public Address getPaymentDataBillingAddress() { return paymentDataBillingAddress; }
    public void setPaymentDataBillingAddress(Address paymentDataBillingAddress) { this.paymentDataBillingAddress = paymentDataBillingAddress; }

    public String getPaymentDataBin() { return paymentDataBin; }
    public void setPaymentDataBin(String paymentDataBin) { this.paymentDataBin = paymentDataBin; }

    public String getPaymentDataBinCountry() { return paymentDataBinCountry; }
    public void setPaymentDataBinCountry(String paymentDataBinCountry) { this.paymentDataBinCountry = paymentDataBinCountry; }

    public int getPaymentDataExpirationMonth() { return paymentDataExpirationMonth; }
    public void setPaymentDataExpirationMonth(int paymentDataExpirationMonth) { this.paymentDataExpirationMonth = paymentDataExpirationMonth; }

    public int getPaymentDataExpirationYear() { return paymentDataExpirationYear; }
    public void setPaymentDataExpirationYear(int paymentDataExpirationYear) { this.paymentDataExpirationYear = paymentDataExpirationYear; }

    public String getPaymentDataLast4() { return paymentDataLast4; }
    public void setPaymentDataLast4(String paymentDataLast4) { this.paymentDataLast4 = paymentDataLast4; }

    public String getPaymentDataType() { return paymentDataType; }
    public void setPaymentDataType(String paymentDataType) { this.paymentDataType = paymentDataType; }

    public String getPaymentDisplay() { return paymentDisplay; }
    public void setPaymentDisplay(String paymentDisplay) { this.paymentDisplay = paymentDisplay; }

    public String getPaymentMethodGroupKey() { return paymentMethodGroupKey; }
    public void setPaymentMethodGroupKey(String paymentMethodGroupKey) { this.paymentMethodGroupKey = paymentMethodGroupKey; }

    public String getPaymentMethodKey() { return paymentMethodKey; }
    public void setPaymentMethodKey(String paymentMethodKey) { this.paymentMethodKey = paymentMethodKey; }

    public String getReceiptLink() { return receiptLink; }
    public void setReceiptLink(String receiptLink) { this.receiptLink = receiptLink; }

    public String getReceiptText() { return receiptText; }
    public void setReceiptText(String receiptText) { this.receiptText = receiptText; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
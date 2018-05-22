package com.coriunder.unused.v1.usersdk.models;

import java.util.Date;

/**
 * Created by 1 on 18.02.2016.
 */
public class Transaction {
    private String transactionId;
    private String paymentMethodKey;
    private String paymentMethodGroupKey;
    private String autoCode;
    private String amount;
    private String displayName;
    private Date insertDate;
    private String currencyIso;
    private String comment;
    private String phone;
    private String email;
    private String text;
    private String fullName;
    private String receiptText;
    private String receiptLink;
    private TransactionMerchant merchant;
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;
    
    public Transaction() {
        transactionId = "";
        paymentMethodKey = "";
        paymentMethodGroupKey = "";
        autoCode = "";
        amount = "";
        displayName = "";
        insertDate = new Date();
        currencyIso = "";
        comment = "";
        phone = "";
        email = "";
        text = "";
        fullName = "";
        receiptText = "";
        receiptLink = "";
        merchant = new TransactionMerchant();
        shippingAddress = new ShippingAddress();
        billingAddress = new BillingAddress();        
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getPaymentMethodKey() { return paymentMethodKey; }
    public void setPaymentMethodKey(String paymentMethodKey) { this.paymentMethodKey = paymentMethodKey; }

    public String getPaymentMethodGroupKey() { return paymentMethodGroupKey; }
    public void setPaymentMethodGroupKey(String paymentMethodGroupKey) { this.paymentMethodGroupKey = paymentMethodGroupKey; }

    public String getAutoCode() { return autoCode; }
    public void setAutoCode(String autoCode) { this.autoCode = autoCode; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public Date getInsertDate() { return insertDate; }
    public void setInsertDate(Date insertDate) { this.insertDate = insertDate; }

    public String getCurrencyIso() { return currencyIso; }
    public void setCurrencyIso(String currencyIso) { this.currencyIso = currencyIso; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getReceiptText() { return receiptText; }
    public void setReceiptText(String receiptText) { this.receiptText = receiptText; }

    public String getReceiptLink() { return receiptLink; }
    public void setReceiptLink(String receiptLink) { this.receiptLink = receiptLink; }

    public TransactionMerchant getMerchant() { return merchant; }
    public void setMerchant(TransactionMerchant merchant) { this.merchant = merchant; }

    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }

    public BillingAddress getBillingAddress() { return billingAddress; }
    public void setBillingAddress(BillingAddress billingAddress) { this.billingAddress = billingAddress; }
}
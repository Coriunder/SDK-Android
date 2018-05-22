package com.coriunder.unused.v1.usersdk.models;

import java.util.Date;

/**
 * Created by 1 on 18.02.2016.
 */
public class PaymentMethod {

    private String paymentMethodId;
    private String title;
    private String displayName;
    private Date expirationDate;
    private String iconURL;
    private boolean isDefault;
    private String ownerName;
    private String paymentMethodGroupKey;
    private String paymentMethodKey;
    private boolean usesBillingAddress;
    private BillingAddress address;
    private String last4Digits;
    private String issuerCountryIsoCode;
    private String cardNumber;
    private String accountValue2;

    public PaymentMethod() {
        paymentMethodId = "";
        title = "";
        displayName = "";
        expirationDate = new Date();
        iconURL = "";
        isDefault = false;
        ownerName = "";
        paymentMethodGroupKey = "";
        paymentMethodKey = "";
        usesBillingAddress = false;
        address = new BillingAddress();
        last4Digits = "";
        issuerCountryIsoCode = "";
        cardNumber = "";
        accountValue2 = "";
    }

    public String getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }

    public String getIconURL() { return iconURL; }
    public void setIconURL(String iconURL) { this.iconURL = iconURL; }

    public boolean isDefault() { return isDefault; }
    public void setIsDefault(boolean isDefault) { this.isDefault = isDefault; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getPaymentMethodGroupKey() { return paymentMethodGroupKey; }
    public void setPaymentMethodGroupKey(String paymentMethodGroupKey) { this.paymentMethodGroupKey = paymentMethodGroupKey; }

    public String getPaymentMethodKey() { return paymentMethodKey; }
    public void setPaymentMethodKey(String paymentMethodKey) { this.paymentMethodKey = paymentMethodKey; }

    public boolean isUsesBillingAddress() { return usesBillingAddress; }
    public void setUsesBillingAddress(boolean usesBillingAddress) { this.usesBillingAddress = usesBillingAddress; }

    public BillingAddress getAddress() { return address; }
    public void setAddress(BillingAddress address) { this.address = address; }

    public String getLast4Digits() { return last4Digits; }
    public void setLast4Digits(String last4Digits) { this.last4Digits = last4Digits; }

    public String getIssuerCountryIsoCode() { return issuerCountryIsoCode; }
    public void setIssuerCountryIsoCode(String issuerCountryIsoCode) { this.issuerCountryIsoCode = issuerCountryIsoCode; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getAccountValue2() { return accountValue2; }
    public void setAccountValue2(String accountValue2) { this.accountValue2 = accountValue2; }
}
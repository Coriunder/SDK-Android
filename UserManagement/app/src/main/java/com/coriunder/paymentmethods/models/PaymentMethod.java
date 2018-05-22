package com.coriunder.paymentmethods.models;

import com.coriunder.base.common.models.Address;

import java.util.Date;

/**
 * Payment method data
 */
@SuppressWarnings("unused")
public class PaymentMethod {
    private String accountValue1;
    private String accountValue2;
    private Address address;
    private String display;
    private Date expirationDate;
    private long paymentMethodId;
    private String iconURL;
    private boolean isDefault;
    private String issuerCountryIsoCode;
    private String last4Digits;
    private String ownerName;
    private String paymentMethodGroupKey;
    private String paymentMethodKey;
    private String title;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public PaymentMethod() {
        accountValue1 = "";
        accountValue2 = "";
        address = new Address();
        display = "";
        expirationDate = new Date();
        paymentMethodId = 0;
        iconURL = "";
        isDefault = false;
        issuerCountryIsoCode = "";
        last4Digits = "";
        ownerName = "";
        paymentMethodGroupKey = "";
        paymentMethodKey = "";
        title = "";
    }

    public long getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(long paymentMethodId) { this.paymentMethodId = paymentMethodId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDisplay() { return display; }
    public void setDisplay(String display) { this.display = display; }

    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }

    public String getIconURL() { return iconURL; }
    public void setIconURL(String iconURL) { this.iconURL = iconURL; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getPaymentMethodGroupKey() { return paymentMethodGroupKey; }
    public void setPaymentMethodGroupKey(String paymentMethodGroupKey) { this.paymentMethodGroupKey = paymentMethodGroupKey; }

    public String getPaymentMethodKey() { return paymentMethodKey; }
    public void setPaymentMethodKey(String paymentMethodKey) { this.paymentMethodKey = paymentMethodKey; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public String getLast4Digits() { return last4Digits; }
    public void setLast4Digits(String last4Digits) { this.last4Digits = last4Digits; }

    public String getIssuerCountryIsoCode() { return issuerCountryIsoCode; }
    public void setIssuerCountryIsoCode(String issuerCountryIsoCode) { this.issuerCountryIsoCode = issuerCountryIsoCode; }

    public String getAccountValue1() { return accountValue1; }
    public void setAccountValue1(String accountValue1) { this.accountValue1 = accountValue1; }

    public String getAccountValue2() { return accountValue2; }
    public void setAccountValue2(String accountValue2) { this.accountValue2 = accountValue2; }
}
package com.coriunder.customer.models;

import com.coriunder.base.common.models.Address;

/**
 * Information about shipping address
 */
@SuppressWarnings("unused")
public class ShippingAddress extends Address {
    private String comment;
    private long addressId;
    private boolean isDefault;
    private String title;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public ShippingAddress() {
        address1 = "";
        address2 = "";
        city = "";
        countryIso = "";
        postalCode = "";
        stateIso = "";
        comment = "";
        addressId = 0;
        isDefault = false;
        title = "";
    }

    public long getAddressId() { return addressId; }
    public void setAddressId(long addressId) { this.addressId = addressId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
}
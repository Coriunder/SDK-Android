package com.coriunder.shop.models;

/**
 * Information about shop location
 */
@SuppressWarnings("unused")
public class ShopLocation {
    private String isoCode;
    private String name;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public ShopLocation() {
        isoCode = "";
        name = "";
    }

    public String getIsoCode() { return isoCode; }
    public void setIsoCode(String isoCode) { this.isoCode = isoCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
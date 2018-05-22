package com.coriunder.base.common.models;

/**
 * Base address info
 */
@SuppressWarnings("unused")
public class Address {
    protected String address1;
    protected String address2;
    protected String city;
    protected String countryIso;
    protected String postalCode;
    protected String stateIso;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Address() {
        address1 = "";
        address2 = "";
        city = "";
        countryIso = "";
        postalCode = "";
        stateIso = "";
    }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getStateIso() { return stateIso; }
    public void setStateIso(String stateIso) { this.stateIso = stateIso; }

    public String getCountryIso() { return countryIso; }
    public void setCountryIso(String countryIso) { this.countryIso = countryIso; }
}
package com.coriunder.unused.v1.usersdk.models;

/**
 * Created by 1 on 17.02.2016.
 */
public class BillingAddress {

    private String address1;
    private String address2;
    private String city;
    private String zipcode;
    private String stateIso;
    private String countryIso;

    public BillingAddress() {
        address1 = "";
        address2 = "";
        city = "";
        zipcode = "";
        stateIso = "";
        countryIso = "";
    }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }

    public String getStateIso() { return stateIso; }
    public void setStateIso(String stateIso) { this.stateIso = stateIso; }

    public String getCountryIso() { return countryIso; }
    public void setCountryIso(String countryIso) { this.countryIso = countryIso; }
}
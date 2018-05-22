package com.coriunder.unused.v1.usersdk.models;

/**
 * Created by 1 on 18.02.2016.
 */
public class TransactionMerchant {
    private String number;
    private String name;
    private String email;
    private String phone;
    private String address1;
    private String address2;
    private String city;
    private String countryIso;
    private String stateIso;
    private String zipCode;
    private String website;
    private String logoUrl;
    
    public TransactionMerchant() {
        number = "";
        name = "";
        email = "";
        phone = "";
        address1 = "";
        address2 = "";
        city = "";
        countryIso = "";
        stateIso = "";
        zipCode = "";
        website = "";
        logoUrl = "";        
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountryIso() { return countryIso; }
    public void setCountryIso(String countryIso) { this.countryIso = countryIso; }

    public String getStateIso() { return stateIso; }
    public void setStateIso(String stateIso) { this.stateIso = stateIso; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
}
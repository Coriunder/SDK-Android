package com.coriunder.base.common.models;

import java.util.ArrayList;

/**
 * Information about the merchant
 */
@SuppressWarnings("unused")
public class Merchant {
    private Address address;
    private ArrayList<String> currencies;
    private String email;
    private String faxNumber;
    private String group;
    private ArrayList<String> languages;
    private String name;
    private String number;
    private String phone;
    private String website;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Merchant() {
        address = new Address();
        currencies = new ArrayList<>();
        email = "";
        faxNumber = "";
        group = "";
        languages = new ArrayList<>();
        name = "";
        number = "";
        phone = "";
        website = "";
    }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFaxNumber() { return faxNumber; }
    public void setFaxNumber(String faxNumber) { this.faxNumber = faxNumber; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public ArrayList<String> getCurrencies() { return currencies; }
    public void setCurrencies(ArrayList<String> currencies) { this.currencies = currencies; }

    public ArrayList<String> getLanguages() { return languages; }
    public void setLanguages(ArrayList<String> languages) { this.languages = languages; }
}
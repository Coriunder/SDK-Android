package com.coriunder.unused.v1.usersdk.models;

import android.graphics.Bitmap;
import java.util.Date;

public class Customer {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String customerNumber; //user id
    private String address1;
    private String address2;
    private String city;
    private String zipCode;
    private String stateIso;
    private String countryIso;
    private Bitmap userImage;
    private String cellNumber;
    private Date dateOfBirth;
    private String personalNumber;
    private String profileImageSize;
    private Date registrationDate;

    public Customer() {
        firstName = "";
        lastName = "";
        phone = "";
        email = "";
        customerNumber = ""; //user id
        address1 = "";
        address2 = "";
        city = "";
        zipCode = "";
        stateIso = "";
        countryIso = "";
        userImage = null;
        cellNumber = "";
        dateOfBirth = new Date();
        personalNumber = "";
        profileImageSize = "";
        registrationDate = new Date();
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getStateIso() { return stateIso; }
    public void setStateIso(String stateIso) { this.stateIso = stateIso; }

    public String getCountryIso() { return countryIso; }
    public void setCountryIso(String countryIso) { this.countryIso = countryIso; }

    public Bitmap getUserImage() { return userImage; }
    public void setUserImage(Bitmap userImage) { this.userImage = userImage; }

    public String getCellNumber() { return cellNumber; }
    public void setCellNumber(String cellNumber) { this.cellNumber = cellNumber; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPersonalNumber() { return personalNumber; }
    public void setPersonalNumber(String personalNumber) { this.personalNumber = personalNumber; }

    public String getProfileImageSize() { return profileImageSize; }
    public void setProfileImageSize(String profileImageSize) { this.profileImageSize = profileImageSize; }

    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }
}
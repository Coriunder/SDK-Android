package com.coriunder.customer.models;

import android.graphics.Bitmap;

import com.coriunder.base.common.models.Address;

import java.util.Date;

/**
 * Information about logged in user
 */
@SuppressWarnings("unused")
public class Customer {
    private Address address;
    private String cellNumber;
    private String customerNumber; //user id
    private Date dateOfBirth;
    private String email;
    private String firstName;
    private String lastName;
    private String personalNumber;
    private String phone;
    private Bitmap userImage;
    private long profileImageSize;
    private Date registrationDate;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Customer() {
        address = new Address();
        cellNumber = "";
        customerNumber = ""; //user id
        dateOfBirth = new Date();
        email = "";
        firstName = "";
        lastName = "";
        personalNumber = "";
        phone = "";
        userImage = null;
        profileImageSize = 0;
        registrationDate = new Date();
    }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

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

    public Bitmap getUserImage() { return userImage; }
    public void setUserImage(Bitmap userImage) { this.userImage = userImage; }

    public String getCellNumber() { return cellNumber; }
    public void setCellNumber(String cellNumber) { this.cellNumber = cellNumber; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPersonalNumber() { return personalNumber; }
    public void setPersonalNumber(String personalNumber) { this.personalNumber = personalNumber; }

    public long getProfileImageSize() { return profileImageSize; }
    public void setProfileImageSize(long profileImageSize) { this.profileImageSize = profileImageSize; }

    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }
}
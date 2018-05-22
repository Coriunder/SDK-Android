package com.coriunder.merchant.models;

import java.util.Date;

public class RegistrationMerchant {
    private String address;
    private double anticipatedAverageTransactionAmount;
    private double anticipatedLargestTransactionAmount;
    private double anticipatedMonthlyVolume;
    private String bankAccountNumber;
    private String bankRoutingNumber;
    private String businessDescription;
    private Date businessStartDate;
    private String canceledCheckImage; //base64Binary
    private String city;
    private String dbaName;
    private String email;
    private String fax;
    private String firstName;
    private int industry;
    private String lastName;
    private String legalBusinessName;
    private String legalBusinessNumber;
    private Date ownerDob;
    private String ownerSsn;
    private int percentDelivery0to7;
    private int percentDelivery15to30;
    private int percentDelivery8to14;
    private int percentDeliveryOver30;
    private String physicalAddress;
    private String physicalCity;
    private String physicalState;
    private String physicalZip;
    private String phone;
    private String state;
    private String stateOfIncorporation;
    private int typeOfBusiness;
    private String url;
    private String zipcode;

    public RegistrationMerchant() {
        address = "";
        anticipatedAverageTransactionAmount = 0;
        anticipatedLargestTransactionAmount = 0;
        anticipatedMonthlyVolume = 0;
        bankAccountNumber = "";
        bankRoutingNumber = "";
        businessDescription = "";
        businessStartDate = new Date();
        canceledCheckImage = "";
        city = "";
        dbaName = "";
        email = "";
        fax = "";
        firstName = "";
        industry = 0;
        lastName = "";
        legalBusinessName = "";
        legalBusinessNumber = "";
        ownerDob = new Date();
        ownerSsn = "";
        percentDelivery0to7 = 0;
        percentDelivery15to30 = 0;
        percentDelivery8to14 = 0;
        percentDeliveryOver30 = 0;
        physicalAddress = "";
        physicalCity = "";
        physicalState = "";
        physicalZip = "";
        phone = "";
        state = "";
        stateOfIncorporation = "";
        typeOfBusiness = 0;
        url = "";
        zipcode = "";
    }

    public String getCanceledCheckImage() {
        return canceledCheckImage;
    }

    public void setCanceledCheckImage(String canceledCheckImage) {
        this.canceledCheckImage = canceledCheckImage;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getAnticipatedAverageTransactionAmount() {
        return anticipatedAverageTransactionAmount;
    }

    public void setAnticipatedAverageTransactionAmount(double anticipatedAverageTransactionAmount) {
        this.anticipatedAverageTransactionAmount = anticipatedAverageTransactionAmount;
    }

    public double getAnticipatedLargestTransactionAmount() {
        return anticipatedLargestTransactionAmount;
    }

    public void setAnticipatedLargestTransactionAmount(double anticipatedLargestTransactionAmount) {
        this.anticipatedLargestTransactionAmount = anticipatedLargestTransactionAmount;
    }

    public double getAnticipatedMonthlyVolume() {
        return anticipatedMonthlyVolume;
    }

    public void setAnticipatedMonthlyVolume(double anticipatedMonthlyVolume) {
        this.anticipatedMonthlyVolume = anticipatedMonthlyVolume;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankRoutingNumber() {
        return bankRoutingNumber;
    }

    public void setBankRoutingNumber(String bankRoutingNumber) {
        this.bankRoutingNumber = bankRoutingNumber;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public Date getBusinessStartDate() {
        return businessStartDate;
    }

    public void setBusinessStartDate(Date businessStartDate) {
        this.businessStartDate = businessStartDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDbaName() {
        return dbaName;
    }

    public void setDbaName(String dbaName) {
        this.dbaName = dbaName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getIndustry() {
        return industry;
    }

    public void setIndustry(int industry) {
        this.industry = industry;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLegalBusinessName() {
        return legalBusinessName;
    }

    public void setLegalBusinessName(String legalBusinessName) {
        this.legalBusinessName = legalBusinessName;
    }

    public String getLegalBusinessNumber() {
        return legalBusinessNumber;
    }

    public void setLegalBusinessNumber(String legalBusinessNumber) {
        this.legalBusinessNumber = legalBusinessNumber;
    }

    public Date getOwnerDob() {
        return ownerDob;
    }

    public void setOwnerDob(Date ownerDob) {
        this.ownerDob = ownerDob;
    }

    public String getOwnerSsn() {
        return ownerSsn;
    }

    public void setOwnerSsn(String ownerSsn) {
        this.ownerSsn = ownerSsn;
    }

    public int getPercentDelivery0to7() {
        return percentDelivery0to7;
    }

    public void setPercentDelivery0to7(int percentDelivery0to7) {
        this.percentDelivery0to7 = percentDelivery0to7;
    }

    public int getPercentDelivery15to30() {
        return percentDelivery15to30;
    }

    public void setPercentDelivery15to30(int percentDelivery15to30) {
        this.percentDelivery15to30 = percentDelivery15to30;
    }

    public int getPercentDelivery8to14() {
        return percentDelivery8to14;
    }

    public void setPercentDelivery8to14(int percentDelivery8to14) {
        this.percentDelivery8to14 = percentDelivery8to14;
    }

    public int getPercentDeliveryOver30() {
        return percentDeliveryOver30;
    }

    public void setPercentDeliveryOver30(int percentDeliveryOver30) {
        this.percentDeliveryOver30 = percentDeliveryOver30;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPhysicalCity() {
        return physicalCity;
    }

    public void setPhysicalCity(String physicalCity) {
        this.physicalCity = physicalCity;
    }

    public String getPhysicalState() {
        return physicalState;
    }

    public void setPhysicalState(String physicalState) {
        this.physicalState = physicalState;
    }

    public String getPhysicalZip() {
        return physicalZip;
    }

    public void setPhysicalZip(String physicalZip) {
        this.physicalZip = physicalZip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateOfIncorporation() {
        return stateOfIncorporation;
    }

    public void setStateOfIncorporation(String stateOfIncorporation) {
        this.stateOfIncorporation = stateOfIncorporation;
    }

    public int getTypeOfBusiness() {
        return typeOfBusiness;
    }

    public void setTypeOfBusiness(int typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
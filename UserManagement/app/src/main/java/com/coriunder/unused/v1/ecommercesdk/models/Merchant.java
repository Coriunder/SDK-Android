package com.coriunder.unused.v1.ecommercesdk.models;

import java.util.ArrayList;

/**
 * Created by 1 on 22.02.2016.
 */
public class Merchant {

    private String number;
    private String name;
    private String email;
    private String phone;
    private String fax;
    private String address1;
    private String address2;
    private String city;
    private String countryIso;
    private String stateIso;
    private String zipCode;
    private String website;
    private String group;
    private String baseColor;
    private ArrayList<String> currencies;
    private String bannerUrl;
    private String bannerLinkUrl;
    private String logoUrl;
    private String twitterUrl;
    private String googlePlusUrl;
    private String vimeoUrl;
    private String pinterestUrl;
    private String youtubeUrl;
    private String linkedinUrl;
    private String facebookUrl;

    public Merchant() {
        number = "";
        name = "";
        email = "";
        phone = "";
        fax = "";
        address1 = "";
        address2 = "";
        city = "";
        countryIso = "";
        stateIso = "";
        zipCode = "";
        website = "";
        group = "";
        baseColor = "";
        currencies = new ArrayList<>();
        bannerUrl = "";
        bannerLinkUrl = "";
        logoUrl = "";
        twitterUrl = "";
        googlePlusUrl = "";
        vimeoUrl = "";
        pinterestUrl = "";
        youtubeUrl = "";
        linkedinUrl = "";
        facebookUrl = "";
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFax() { return fax; }
    public void setFax(String fax) { this.fax = fax; }

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

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public String getBaseColor() { return baseColor; }
    public void setBaseColor(String baseColor) { this.baseColor = baseColor; }

    public ArrayList<String> getCurrencies() { return currencies; }
    public void setCurrencies(ArrayList<String> currencies) { this.currencies = currencies; }

    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }

    public String getBannerLinkUrl() { return bannerLinkUrl; }
    public void setBannerLinkUrl(String bannerLinkUrl) { this.bannerLinkUrl = bannerLinkUrl; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getTwitterUrl() { return twitterUrl; }
    public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }

    public String getGooglePlusUrl() { return googlePlusUrl; }
    public void setGooglePlusUrl(String googlePlusUrl) { this.googlePlusUrl = googlePlusUrl; }

    public String getVimeoUrl() { return vimeoUrl; }
    public void setVimeoUrl(String vimeoUrl) { this.vimeoUrl = vimeoUrl; }

    public String getPinterestUrl() { return pinterestUrl; }
    public void setPinterestUrl(String pinterestUrl) { this.pinterestUrl = pinterestUrl; }

    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public String getFacebookUrl() { return facebookUrl; }
    public void setFacebookUrl(String facebookUrl) { this.facebookUrl = facebookUrl; }
}
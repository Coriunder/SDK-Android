package com.coriunder.shop.models;

import java.util.ArrayList;

/**
 * Information about exact shop
 */
@SuppressWarnings("unused")
public class Shop {
    private String bannerLinkUrl;
    private String bannerUrl;
    private ArrayList<ShopLocation> countries;
    private String currencyIsoCode;
    private String facebookUrl;
    private String googlePlusUrl;
    private String linkedinUrl;
    private String locationsString;
    private String logoUrl;
    private String pinterestUrl;
    private ArrayList<ShopLocation> regions;
    private long shopId;
    private String twitterUrl;
    private String uiBaseColor;
    private String vimeoUrl;
    private String youtubeUrl;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Shop() {
        bannerLinkUrl = "";
        bannerUrl = "";
        countries = new ArrayList<>();
        currencyIsoCode = "";
        facebookUrl = "";
        googlePlusUrl = "";
        linkedinUrl = "";
        locationsString = "";
        logoUrl = "";
        pinterestUrl = "";
        regions = new ArrayList<>();
        shopId = 0;
        twitterUrl = "";
        uiBaseColor = "";
        vimeoUrl = "";
        youtubeUrl = "";
    }

    public String getBannerLinkUrl() { return bannerLinkUrl; }
    public void setBannerLinkUrl(String bannerLinkUrl) { this.bannerLinkUrl = bannerLinkUrl; }

    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }

    public ArrayList<ShopLocation> getCountries() { return countries; }
    public void setCountries(ArrayList<ShopLocation> countries) { this.countries = countries; }

    public String getCurrencyIsoCode() { return currencyIsoCode; }
    public void setCurrencyIsoCode(String currencyIsoCode) { this.currencyIsoCode = currencyIsoCode; }

    public String getFacebookUrl() { return facebookUrl; }
    public void setFacebookUrl(String facebookUrl) { this.facebookUrl = facebookUrl; }

    public String getGooglePlusUrl() { return googlePlusUrl; }
    public void setGooglePlusUrl(String googlePlusUrl) { this.googlePlusUrl = googlePlusUrl; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public String getLocationsString() { return locationsString; }
    public void setLocationsString(String locationsString) { this.locationsString = locationsString; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getPinterestUrl() { return pinterestUrl; }
    public void setPinterestUrl(String pinterestUrl) { this.pinterestUrl = pinterestUrl; }

    public ArrayList<ShopLocation> getRegions() { return regions; }
    public void setRegions(ArrayList<ShopLocation> regions) { this.regions = regions; }

    public long getShopId() { return shopId; }
    public void setShopId(long shopId) { this.shopId = shopId; }

    public String getTwitterUrl() { return twitterUrl; }
    public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }

    public String getUiBaseColor() { return uiBaseColor; }
    public void setUiBaseColor(String uiBaseColor) { this.uiBaseColor = uiBaseColor; }

    public String getVimeoUrl() { return vimeoUrl; }
    public void setVimeoUrl(String vimeoUrl) { this.vimeoUrl = vimeoUrl; }

    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }
}

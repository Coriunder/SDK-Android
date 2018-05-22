package com.coriunder.appidentity.models;

/**
 * Application identity data
 */
@SuppressWarnings("unused")
public class Identity {
    private String brandName;
    private String companyName;
    private String copyRightText;
    private String domainName;
    private boolean isActive;
    private String name;
    private String theme;
    private String urlDevCenter;
    private String urlMerchantCP;
    private String urlProcess;
    private String urlWallet;
    private String urlWebsite;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Identity() {
        brandName = "";
        companyName = "";
        copyRightText = "";
        domainName = "";
        isActive = false;
        name = "";
        theme = "";
        urlDevCenter = "";
        urlMerchantCP = "";
        urlProcess = "";
        urlWallet = "";
        urlWebsite = "";
    }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCopyRightText() { return copyRightText; }
    public void setCopyRightText(String copyRightText) { this.copyRightText = copyRightText; }

    public String getDomainName() { return domainName; }
    public void setDomainName(String domainName) { this.domainName = domainName; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getUrlDevCenter() { return urlDevCenter; }
    public void setUrlDevCenter(String urlDevCenter) {  this.urlDevCenter = urlDevCenter; }

    public String getUrlMerchantCP() { return urlMerchantCP; }
    public void setUrlMerchantCP(String urlMerchantCP) { this.urlMerchantCP = urlMerchantCP; }

    public String getUrlProcess() { return urlProcess; }
    public void setUrlProcess(String urlProcess) { this.urlProcess = urlProcess; }

    public String getUrlWallet() { return urlWallet; }
    public void setUrlWallet(String urlWallet) { this.urlWallet = urlWallet; }

    public String getUrlWebsite() { return urlWebsite; }
    public void setUrlWebsite(String urlWebsite) { this.urlWebsite = urlWebsite; }
}
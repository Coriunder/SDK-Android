package com.coriunder.unused.v1.ecommercesdk.models;

import java.util.ArrayList;

/**
 * Created by 1 on 22.02.2016.
 */
public class Product {

    private String productId;
    private String imageUrl;
    private String insertDate;
    private String name;
    private String prodDescription;
    private String quantity;
    private String price;
    private String currencyISOCode;
    private String minQuantity;
    private String maxQuantity;
    private String stepQuantity;
    private String type;
    private String sku;
    private String productUrl;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private boolean isDynamicAmount;
    private ArrayList<Property> properties;
    private ArrayList<Stock> stocks;
    private ArrayList<Integer> categories;
    private ArrayList<Integer> paymentMethods;
    private String cookie;
    private Merchant merchant;

    public Product() {
        productId = "";
        imageUrl = "";
        insertDate = "";
        name = "";
        prodDescription = "";
        quantity = "";
        price = "";
        currencyISOCode = "";
        minQuantity = "";
        maxQuantity = "";
        stepQuantity = "";
        type = "";
        sku = "";
        productUrl = "";
        metaTitle = "";
        metaDescription = "";
        metaKeywords = "";
        isDynamicAmount = false;
        properties = new ArrayList<>();
        stocks = new ArrayList<>();
        categories = new ArrayList<>();
        paymentMethods = new ArrayList<>();
        cookie = "";
        merchant = new Merchant();
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getInsertDate() { return insertDate; }
    public void setInsertDate(String insertDate) { this.insertDate = insertDate; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProdDescription() { return prodDescription; }
    public void setProdDescription(String prodDescription) { this.prodDescription = prodDescription; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getCurrencyISOCode() { return currencyISOCode; }
    public void setCurrencyISOCode(String currencyISOCode) { this.currencyISOCode = currencyISOCode; }

    public String getMinQuantity() { return minQuantity; }
    public void setMinQuantity(String minQuantity) { this.minQuantity = minQuantity; }

    public String getMaxQuantity() { return maxQuantity; }
    public void setMaxQuantity(String maxQuantity) { this.maxQuantity = maxQuantity; }

    public String getStepQuantity() { return stepQuantity; }
    public void setStepQuantity(String stepQuantity) { this.stepQuantity = stepQuantity; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getProductUrl() { return productUrl; }
    public void setProductUrl(String productUrl) { this.productUrl = productUrl; }

    public String getMetaTitle() { return metaTitle; }
    public void setMetaTitle(String metaTitle) { this.metaTitle = metaTitle; }

    public String getMetaDescription() { return metaDescription; }
    public void setMetaDescription(String metaDescription) { this.metaDescription = metaDescription; }

    public String getMetaKeywords() { return metaKeywords; }
    public void setMetaKeywords(String metaKeywords) { this.metaKeywords = metaKeywords; }

    public boolean isDynamicAmount() { return isDynamicAmount; }
    public void setIsDynamicAmount(boolean isDynamicAmount) { this.isDynamicAmount = isDynamicAmount; }

    public ArrayList<Property> getProperties() { return properties; }
    public void setProperties(ArrayList<Property> properties) { this.properties = properties; }

    public ArrayList<Stock> getStocks() { return stocks; }
    public void setStocks(ArrayList<Stock> stocks) { this.stocks = stocks; }

    public ArrayList<Integer> getCategories() { return categories; }
    public void setCategories(ArrayList<Integer> categories) { this.categories = categories; }

    public ArrayList<Integer> getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(ArrayList<Integer> paymentMethods) { this.paymentMethods = paymentMethods; }

    public String getCookie() { return cookie; }
    public void setCookie(String cookie) { this.cookie = cookie; }

    public Merchant getMerchant() { return merchant; }
    public void setMerchant(Merchant merchant) { this.merchant = merchant; }
}
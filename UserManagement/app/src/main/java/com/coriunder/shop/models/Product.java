package com.coriunder.shop.models;

import com.coriunder.base.common.models.Merchant;

import java.util.ArrayList;

/**
 * Information about exact product
 */
@SuppressWarnings("unused")
public class Product {
    /*
    ToDoV2:
    type is String but should be unsignedByte
     */

    private ArrayList<Integer> categories;
    private long categoryId;
    private String categoryName;
    private String checkoutUrl;
    private String currencyIso;
    private String productDescription;
    private long productId;
    private String imageUrl;
    private boolean isDynamicAmount;
    private boolean isRecurring;
    private Merchant merchant;
    private String metaDescription;
    private String metaKeywords;
    private String metaTitle;
    private String name;
    private long nextProductId;
    private ArrayList<Integer> paymentMethods;
    private long previousProductId;
    private double price;
    private String productUrl;
    private ArrayList<Property> properties;
    private int quantityAvailable;
    private int quantityInterval;
    private int quantityMax;
    private int quantityMin;
    private String recurringDisplay;
    private String sku;
    private ArrayList<Stock> stocks;
    private String type;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Product() {
        categories = new ArrayList<>();
        categoryId = 0;
        categoryName = "";
        checkoutUrl = "";
        currencyIso = "";
        productDescription = "";
        productId = 0;
        imageUrl = "";
        isDynamicAmount = false;
        isRecurring = false;
        merchant = new Merchant();
        metaDescription = "";
        metaKeywords = "";
        metaTitle = "";
        name = "";
        nextProductId = 0;
        paymentMethods = new ArrayList<>();
        previousProductId = 0;
        price = 0;
        productUrl = "";
        properties = new ArrayList<>();
        quantityAvailable = 0;
        quantityInterval = 0;
        quantityMax = 0;
        quantityMin = 0;
        recurringDisplay = "";
        sku = "";
        stocks = new ArrayList<>();
        type = "";
    }

    public long getCategoryId() { return categoryId; }
    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public int getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(int quantityAvailable) { this.quantityAvailable = quantityAvailable; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCurrencyIso() { return currencyIso; }
    public void setCurrencyIso(String currencyIso) { this.currencyIso = currencyIso; }

    public int getQuantityMin() { return quantityMin; }
    public void setQuantityMin(int quantityMin) { this.quantityMin = quantityMin; }

    public int getQuantityMax() { return quantityMax; }
    public void setQuantityMax(int quantityMax) { this.quantityMax = quantityMax; }

    public int getQuantityInterval() { return quantityInterval; }
    public void setQuantityInterval(int quantityInterval) { this.quantityInterval = quantityInterval; }

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
    public void setDynamicAmount(boolean isDynamicAmount) { this.isDynamicAmount = isDynamicAmount; }

    public ArrayList<Property> getProperties() { return properties; }
    public void setProperties(ArrayList<Property> properties) { this.properties = properties; }

    public ArrayList<Stock> getStocks() { return stocks; }
    public void setStocks(ArrayList<Stock> stocks) { this.stocks = stocks; }

    public ArrayList<Integer> getCategories() { return categories; }
    public void setCategories(ArrayList<Integer> categories) { this.categories = categories; }

    public ArrayList<Integer> getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(ArrayList<Integer> paymentMethods) { this.paymentMethods = paymentMethods; }

    public Merchant getMerchant() { return merchant; }
    public void setMerchant(Merchant merchant) { this.merchant = merchant; }

    public String getCheckoutUrl() { return checkoutUrl; }
    public void setCheckoutUrl(String checkoutUrl) { this.checkoutUrl = checkoutUrl; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }

    public long getNextProductId() { return nextProductId; }
    public void setNextProductId(long nextProductId) { this.nextProductId = nextProductId; }

    public long getPreviousProductId() { return previousProductId; }
    public void setPreviousProductId(long previousProductId) { this.previousProductId = previousProductId; }

    public String getRecurringDisplay() { return recurringDisplay; }
    public void setRecurringDisplay(String recurringDisplay) { this.recurringDisplay = recurringDisplay; }
}
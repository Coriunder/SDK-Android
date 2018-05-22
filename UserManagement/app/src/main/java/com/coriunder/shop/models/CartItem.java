package com.coriunder.shop.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Information about exact cart item
 */
@SuppressWarnings("unused")
public class CartItem {
    /*
    ToDoV2:
    type is String but should be unsignedByte
     */
    private String changedCurrencyIso;
    private double changedPrice;
    private double changedTotal;
    private double currencyFxRate;
    private String currencyIsoCode;
    private String downloadMediaType;
    private String guestDownloadUrl;
    private long itemId;
    private Date insertDate;
    private boolean isAvailable;
    private boolean isChanged;
    private ArrayList<Property> itemProperties;
    private int maxQuantity;
    private int minQuantity;
    private String name;
    private double price;
    private long productId;
    private String imageUrl;
    private long productStockId;
    private int quantity;
    private String receiptLink;
    private String receiptText;
    private double shippingFee;
    private int stepQuantity;
    private double total;
    private double totalProduct;
    private double totalShipping;
    private String type;
    private double VatPercent;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public CartItem() {
        changedCurrencyIso = "";
        changedPrice = 0;
        changedTotal = 0;
        currencyFxRate = 0;
        currencyIsoCode = "";
        downloadMediaType = "";
        guestDownloadUrl = "";
        itemId = 0;
        insertDate = new Date();
        isAvailable = false;
        isChanged = false;
        itemProperties = new ArrayList<>();
        maxQuantity = 0;
        minQuantity = 0;
        name = "";
        price = 0;
        productId = 0;
        imageUrl = "";
        productStockId = 0;
        quantity = 0;
        receiptLink = "";
        receiptText = "";
        shippingFee = 0;
        stepQuantity = 0;
        total = 0;
        totalProduct = 0;
        totalShipping = 0;
        type = "";
        VatPercent = 0;
    }

    public long getItemId() { return itemId; }
    public void setItemId(long itemId) { this.itemId = itemId; }

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }

    public long getProductStockId() { return productStockId; }
    public void setProductStockId(long productStockId) { this.productStockId = productStockId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Date getInsertDate() { return insertDate; }
    public void setInsertDate(Date insertDate) { this.insertDate = insertDate; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getCurrencyIsoCode() { return currencyIsoCode; }
    public void setCurrencyIsoCode(String currencyIsoCode) { this.currencyIsoCode = currencyIsoCode; }

    public double getCurrencyFxRate() { return currencyFxRate; }
    public void setCurrencyFxRate(double currencyFxRate) { this.currencyFxRate = currencyFxRate; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDownloadMediaType() { return downloadMediaType; }
    public void setDownloadMediaType(String downloadMediaType) { this.downloadMediaType = downloadMediaType; }

    public double getShippingFee() { return shippingFee; }
    public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }

    public double getVatPercent() { return VatPercent; }
    public void setVatPercent(double vatPercent) { this.VatPercent = vatPercent; }
    
    public double getTotalShipping() { return totalShipping; }
    public void setTotalShipping(double totalShipping) { this.totalShipping = totalShipping; }

    public double getTotalProduct() { return totalProduct; }
    public void setTotalProduct(double totalProduct) { this.totalProduct = totalProduct; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getMinQuantity() { return minQuantity; }
    public void setMinQuantity(int minQuantity) { this.minQuantity = minQuantity; }

    public int getMaxQuantity() { return maxQuantity; }
    public void setMaxQuantity(int maxQuantity) { this.maxQuantity = maxQuantity; }

    public int getStepQuantity() { return stepQuantity; }
    public void setStepQuantity(int stepQuantity) { this.stepQuantity = stepQuantity; }

    public ArrayList<Property> getItemProperties() { return itemProperties; }
    public void setItemProperties(ArrayList<Property> itemProperties) { this.itemProperties = itemProperties; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }

    public String getReceiptLink() { return receiptLink; }
    public void setReceiptLink(String receiptLink) { this.receiptLink = receiptLink; }

    public String getReceiptText() { return receiptText; }
    public void setReceiptText(String receiptText) { this.receiptText = receiptText; }

    public String getGuestDownloadUrl() { return guestDownloadUrl; }
    public void setGuestDownloadUrl(String guestDownloadUrl) { this.guestDownloadUrl = guestDownloadUrl; }

    public boolean isChanged() { return isChanged; }
    public void setChanged(boolean changed) { this.isChanged = changed; }

    public double getChangedPrice() { return changedPrice; }
    public void setChangedPrice(double changedPrice) { this.changedPrice = changedPrice; }

    public String getChangedCurrencyIso() { return changedCurrencyIso; }
    public void setChangedCurrencyIso(String changedCurrencyIso) { this.changedCurrencyIso = changedCurrencyIso; }

    public double getChangedTotal() { return changedTotal; }
    public void setChangedTotal(double changedTotal) { this.changedTotal = changedTotal; }
}
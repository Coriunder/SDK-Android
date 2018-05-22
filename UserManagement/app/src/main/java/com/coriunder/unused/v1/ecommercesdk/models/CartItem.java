package com.coriunder.unused.v1.ecommercesdk.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 1 on 22.02.2016.
 */
public class CartItem {

    private String itemId;
    private String itemProductId;
    private long itemProductStockId;
    private String itemImageUrl;
    private Date itemInsertDate;
    private String itemName;
    private String itemQuantity;
    private String itemPrice;
    private String itemCurrencyIsoCode;
    private String itemCurrencyFxRate;
    private String itemType;
    private String itemDownloadMediaType;
    private String itemShippingFee;
    private String itemVATPercent;
    private String itemTotalShipping;
    private double itemTotalProduct;
    private String itemTotal;
    private String itemMinQuantity;
    private String itemMaxQuantity;
    private String itemStepQuantity;
    private String itemQuantityAvailable;
    private ArrayList<Property> itemProperties;
    private boolean itemIsAvailable;
    private String itemReceiptLink;
    private String itemReceiptText;
    private String itemGuestDownloadUrl;
    private boolean itemIsChanged;
    private String itemChangedPrice;
    private String itemChangedCurrencyIso;
    private String itemChangedTotal;

    public CartItem() {
        itemId = "";
        itemProductId = "";
        itemProductStockId = 0;
        itemImageUrl = "";
        itemInsertDate = new Date();
        itemName = "";
        itemQuantity = "";
        itemPrice = "";
        itemCurrencyIsoCode = "";
        itemCurrencyFxRate = "";
        itemType = "";
        itemDownloadMediaType = "";
        itemShippingFee = "";
        itemVATPercent = "";
        itemTotalShipping = "";
        itemTotalProduct = 0;
        itemTotal = "";
        itemMinQuantity = "";
        itemMaxQuantity = "";
        itemStepQuantity = "";
        itemQuantityAvailable = "";
        itemProperties = new ArrayList<>();
        itemIsAvailable = false;
        itemReceiptLink = "";
        itemReceiptText = "";
        itemGuestDownloadUrl = "";
        itemIsChanged = false;
        itemChangedPrice = "";
        itemChangedCurrencyIso = "";
        itemChangedTotal = "";
    }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getItemProductId() { return itemProductId; }
    public void setItemProductId(String itemProductId) { this.itemProductId = itemProductId; }

    public long getItemProductStockId() { return itemProductStockId; }
    public void setItemProductStockId(long itemProductStockId) { this.itemProductStockId = itemProductStockId; }

    public String getItemImageUrl() { return itemImageUrl; }
    public void setItemImageUrl(String itemImageUrl) { this.itemImageUrl = itemImageUrl; }

    public Date getItemInsertDate() { return itemInsertDate; }
    public void setItemInsertDate(Date itemInsertDate) { this.itemInsertDate = itemInsertDate; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemQuantity() { return itemQuantity; }
    public void setItemQuantity(String itemQuantity) { this.itemQuantity = itemQuantity; }

    public String getItemPrice() { return itemPrice; }
    public void setItemPrice(String itemPrice) { this.itemPrice = itemPrice; }
    
    public String getItemCurrencyIsoCode() { return itemCurrencyIsoCode; }
    public void setItemCurrencyIsoCode(String itemCurrencyIsoCode) { this.itemCurrencyIsoCode = itemCurrencyIsoCode; }

    public String getItemCurrencyFxRate() { return itemCurrencyFxRate; }
    public void setItemCurrencyFxRate(String itemCurrencyFxRate) { this.itemCurrencyFxRate = itemCurrencyFxRate; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getItemDownloadMediaType() { return itemDownloadMediaType; }
    public void setItemDownloadMediaType(String itemDownloadMediaType) { this.itemDownloadMediaType = itemDownloadMediaType; }

    public String getItemShippingFee() { return itemShippingFee; }
    public void setItemShippingFee(String itemShippingFee) { this.itemShippingFee = itemShippingFee; }

    public String getItemVATPercent() { return itemVATPercent; }
    public void setItemVATPercent(String itemVATPercent) { this.itemVATPercent = itemVATPercent; }
    
    public String getItemTotalShipping() { return itemTotalShipping; }
    public void setItemTotalShipping(String itemTotalShipping) { this.itemTotalShipping = itemTotalShipping; }

    public double getItemTotalProduct() { return itemTotalProduct; }
    public void setItemTotalProduct(double itemTotalProduct) { this.itemTotalProduct = itemTotalProduct; }

    public String getItemTotal() { return itemTotal; }
    public void setItemTotal(String itemTotal) { this.itemTotal = itemTotal; }

    public String getItemMinQuantity() { return itemMinQuantity; }
    public void setItemMinQuantity(String itemMinQuantity) { this.itemMinQuantity = itemMinQuantity; }

    public String getItemMaxQuantity() { return itemMaxQuantity; }
    public void setItemMaxQuantity(String itemMaxQuantity) { this.itemMaxQuantity = itemMaxQuantity; }

    public String getItemStepQuantity() { return itemStepQuantity; }
    public void setItemStepQuantity(String itemStepQuantity) { this.itemStepQuantity = itemStepQuantity; }

    public String getItemQuantityAvailable() { return itemQuantityAvailable; }
    public void setItemQuantityAvailable(String itemQuantityAvailable) { this.itemQuantityAvailable = itemQuantityAvailable; }

    public ArrayList<Property> getItemProperties() { return itemProperties; }
    public void setItemProperties(ArrayList<Property> itemProperties) { this.itemProperties = itemProperties; }

    public boolean isItemIsAvailable() { return itemIsAvailable; }
    public void setItemIsAvailable(boolean itemIsAvailable) { this.itemIsAvailable = itemIsAvailable; }

    public String getItemReceiptLink() { return itemReceiptLink; }
    public void setItemReceiptLink(String itemReceiptLink) { this.itemReceiptLink = itemReceiptLink; }

    public String getItemReceiptText() { return itemReceiptText; }
    public void setItemReceiptText(String itemReceiptText) { this.itemReceiptText = itemReceiptText; }

    public String getItemGuestDownloadUrl() { return itemGuestDownloadUrl; }
    public void setItemGuestDownloadUrl(String itemGuestDownloadUrl) { this.itemGuestDownloadUrl = itemGuestDownloadUrl; }

    public boolean isItemIsChanged() { return itemIsChanged; }
    public void setItemIsChanged(boolean itemIsChanged) { this.itemIsChanged = itemIsChanged; }

    public String getItemChangedPrice() { return itemChangedPrice; }
    public void setItemChangedPrice(String itemChangedPrice) { this.itemChangedPrice = itemChangedPrice; }

    public String getItemChangedCurrencyIso() { return itemChangedCurrencyIso; }
    public void setItemChangedCurrencyIso(String itemChangedCurrencyIso) { this.itemChangedCurrencyIso = itemChangedCurrencyIso; }

    public String getItemChangedTotal() { return itemChangedTotal; }
    public void setItemChangedTotal(String itemChangedTotal) { this.itemChangedTotal = itemChangedTotal; }
}
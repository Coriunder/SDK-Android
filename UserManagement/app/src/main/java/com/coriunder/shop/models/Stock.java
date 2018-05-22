package com.coriunder.shop.models;

import java.util.ArrayList;

/**
 * Information about exact product stock
 */
@SuppressWarnings("unused")
public class Stock {
    private long stockId;
    private ArrayList<Long> propertyIds;
    private int quantityAvailable;
    private String sku;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Stock() {
        stockId = 0;
        propertyIds = new ArrayList<>();
        quantityAvailable = 0;
        sku = "";
    }

    public long getStockId() { return stockId; }
    public void setStockId(long stockId) { this.stockId = stockId; }

    public int getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(int quantityAvailable) { this.quantityAvailable = quantityAvailable; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public ArrayList<Long> getPropertyIds() { return propertyIds; }
    public void setPropertyIds(ArrayList<Long> propertyIds) { this.propertyIds = propertyIds; }
}
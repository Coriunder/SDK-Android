package com.coriunder.unused.v1.ecommercesdk.models;

import java.util.ArrayList;

/**
 * Created by 1 on 22.02.2016.
 */
public class Stock {

    private long stockId;
    private long stockQuantity;
    private String sku;
    private ArrayList<Long> propertyIds;

    public Stock() {
        stockId = 0;
        stockQuantity = 0;
        sku = "";
        propertyIds = new ArrayList<>();
    }

    public long getStockId() { return stockId; }
    public void setStockId(long stockId) { this.stockId = stockId; }

    public long getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(long stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public ArrayList<Long> getPropertyIds() { return propertyIds; }
    public void setPropertyIds(ArrayList<Long> propertyIds) { this.propertyIds = propertyIds; }
}
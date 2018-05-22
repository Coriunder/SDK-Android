package com.coriunder.unused.v1.mobilesdk.models;

import java.math.BigDecimal;

/**
 * Created by 1 on 26.02.2016.
 */
public class Product {
    private long productId;
    private BigDecimal productPrice;
    private String productName;
    private String productCurrency;
    private String productDescription;
    
    public Product() {
        productId = 0;
        productPrice = new BigDecimal(0);
        productName = "";
        productCurrency = "";
        productDescription = "";
    }

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }

    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductCurrency() { return productCurrency; }
    public void setProductCurrency(String productCurrency) { this.productCurrency = productCurrency; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
}
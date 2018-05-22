package com.coriunder.unused.v1.ecommercesdk.models;

import java.util.ArrayList;

/**
 * Created by 1 on 22.02.2016.
 */
public class ProductCategory {

    private String categoryId;
    private String categoryName;
    private ArrayList<ProductCategory> subcategories;

    public ProductCategory() {
        categoryId = "";
        categoryName = "";
        subcategories = new ArrayList<>();
    }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public ArrayList<ProductCategory> getSubcategories() { return subcategories; }
    public void setSubcategories(ArrayList<ProductCategory> subcategories) { this.subcategories = subcategories; }
}
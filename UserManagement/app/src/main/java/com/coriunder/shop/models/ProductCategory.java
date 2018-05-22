package com.coriunder.shop.models;

import java.util.ArrayList;

/**
 * Information about product category
 */
@SuppressWarnings("unused")
public class ProductCategory {
    private int categoryId;
    private String name;
    private ArrayList<ProductCategory> subCategories;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public ProductCategory() {
        categoryId = 0;
        name = "";
        subCategories = new ArrayList<>();
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ArrayList<ProductCategory> getSubCategories() { return subCategories; }
    public void setSubCategories(ArrayList<ProductCategory> subCategories) { this.subCategories = subCategories; }
}
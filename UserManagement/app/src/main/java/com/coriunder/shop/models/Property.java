package com.coriunder.shop.models;

import java.util.ArrayList;

/**
 * Information about product property
 */
@SuppressWarnings("unused")
public class Property {
    private long propertyId;
    private String text;
    private String type;
    private String value;
    private ArrayList<Property> subProperties;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Property() {
        propertyId = 0;
        text = "";
        type = "";
        value = "";
        subProperties = new ArrayList<>();
    }

    public long getPropertyId() { return propertyId; }
    public void setPropertyId(long propertyId) { this.propertyId = propertyId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public ArrayList<Property> getSubProperties() { return subProperties; }
    public void setSubProperties(ArrayList<Property> subProperties) { this.subProperties = subProperties; }
}
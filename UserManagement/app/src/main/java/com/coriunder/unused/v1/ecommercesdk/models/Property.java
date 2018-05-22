package com.coriunder.unused.v1.ecommercesdk.models;

import java.util.ArrayList;

/**
 * Created by 1 on 22.02.2016.
 */
public class Property {

    private long propertyId;
    private String propertyText;
    private String propertyType;
    private String propertyValue;
    private ArrayList<Property> subproperties;

    public Property() {
        propertyId = 0;
        propertyText = "";
        propertyType = "";
        propertyValue = "";
        subproperties = new ArrayList<>();
    }

    public long getPropertyId() { return propertyId; }
    public void setPropertyId(long propertyId) { this.propertyId = propertyId; }

    public String getPropertyText() { return propertyText; }
    public void setPropertyText(String propertyText) { this.propertyText = propertyText; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public String getPropertyValue() { return propertyValue; }
    public void setPropertyValue(String propertyValue) { this.propertyValue = propertyValue; }

    public ArrayList<Property> getSubproperties() { return subproperties; }
    public void setSubproperties(ArrayList<Property> subproperties) { this.subproperties = subproperties; }
}
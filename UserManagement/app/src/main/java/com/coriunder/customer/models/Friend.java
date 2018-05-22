package com.coriunder.customer.models;

import android.graphics.Bitmap;

/**
 * Information about other user's account
 */
@SuppressWarnings("unused")
public class Friend {
    private String userId;
    private String fullName;
    private Bitmap profileImage;
    private int relationType;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Friend() {
        userId = "";
        fullName = "";
        profileImage = null;
        relationType = 0;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public int getRelationType() { return relationType; }
    public void setRelationType(int relationType) { this.relationType = relationType; }

    public Bitmap getProfileImage() { return profileImage; }
    public void setProfileImage(Bitmap profileImage) { this.profileImage = profileImage; }
}
package com.coriunder.unused.v1.usersdk.models;

import android.graphics.Bitmap;

/**
 * Created by 1 on 18.02.2016.
 */
public class Friend {

    private String userId;
    private String fullName;
    private int relationType;
    private Bitmap profileImage;

    public Friend() {
        userId = "";
        fullName = "";
        relationType = 0;
        profileImage = null;
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
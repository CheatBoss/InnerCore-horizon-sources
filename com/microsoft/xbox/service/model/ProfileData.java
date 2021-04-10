package com.microsoft.xbox.service.model;

import com.microsoft.xbox.service.network.managers.*;

public class ProfileData
{
    private IUserProfileResult.UserProfileResult profileResult;
    private boolean shareRealName;
    private String shareRealNameStatus;
    private boolean sharingRealNameTransitively;
    
    public ProfileData(final IUserProfileResult.UserProfileResult profileResult, final boolean shareRealName) {
        this.profileResult = profileResult;
        this.shareRealName = shareRealName;
        this.shareRealNameStatus = null;
    }
    
    public ProfileData(final IUserProfileResult.UserProfileResult profileResult, final boolean shareRealName, final String shareRealNameStatus, final boolean sharingRealNameTransitively) {
        this.profileResult = profileResult;
        this.shareRealName = shareRealName;
        this.shareRealNameStatus = shareRealNameStatus;
        this.sharingRealNameTransitively = sharingRealNameTransitively;
    }
    
    public IUserProfileResult.UserProfileResult getProfileResult() {
        return this.profileResult;
    }
    
    public boolean getShareRealName() {
        return this.shareRealName;
    }
    
    public String getShareRealNameStatus() {
        return this.shareRealNameStatus;
    }
    
    public boolean getSharingRealNameTransitively() {
        return this.sharingRealNameTransitively;
    }
}

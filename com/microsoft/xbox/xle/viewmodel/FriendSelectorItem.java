package com.microsoft.xbox.xle.viewmodel;

import com.microsoft.xbox.service.model.*;

public final class FriendSelectorItem extends FollowersData
{
    private static final long serialVersionUID = 5799344980951867134L;
    private boolean selected;
    
    public FriendSelectorItem(final FollowersData followersData) {
        super(followersData);
        this.selected = false;
    }
    
    public FriendSelectorItem(final ProfileModel profileModel) {
        this.xuid = profileModel.getXuid();
        this.userProfileData = new UserProfileData();
        this.userProfileData.gamerTag = profileModel.getGamerTag();
        this.userProfileData.xuid = profileModel.getXuid();
        this.userProfileData.profileImageUrl = profileModel.getGamerPicImageUrl();
        this.userProfileData.gamerScore = profileModel.getGamerScore();
        this.userProfileData.appDisplayName = profileModel.getAppDisplayName();
        this.userProfileData.accountTier = profileModel.getAccountTier();
        this.userProfileData.gamerRealName = profileModel.getRealName();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final FriendSelectorItem friendSelectorItem = (FriendSelectorItem)o;
        if (this.userProfileData != null && this.userProfileData.gamerTag != null) {
            if (!this.userProfileData.gamerTag.equals(friendSelectorItem.userProfileData.gamerTag)) {
                return false;
            }
        }
        else {
            if (friendSelectorItem.userProfileData != null) {
                return false;
            }
            if (friendSelectorItem.userProfileData.gamerTag != null) {
                return false;
            }
        }
        return true;
    }
    
    public boolean getIsSelected() {
        return this.selected;
    }
    
    @Override
    public int hashCode() {
        int hashCode;
        if (this.userProfileData != null && this.userProfileData.gamerTag != null) {
            hashCode = this.userProfileData.gamerTag.hashCode();
        }
        else {
            hashCode = 0;
        }
        return hashCode + 31;
    }
    
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
    
    public void toggleSelection() {
        this.selected ^= true;
    }
}

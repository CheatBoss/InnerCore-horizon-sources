package com.microsoft.xbox.service.model.sls;

import java.util.*;
import com.microsoft.xbox.toolkit.*;

public class UserProfileRequest
{
    public ArrayList<String> settings;
    public ArrayList<String> userIds;
    
    public UserProfileRequest() {
        this.settings = new ArrayList<String>();
        this.userIds = new ArrayList<String>();
        this.setDefaultProfileSettingsRequest(false);
    }
    
    public UserProfileRequest(final ArrayList<String> list) {
        this(list, false);
    }
    
    public UserProfileRequest(final ArrayList<String> userIds, final ArrayList<String> settings) {
        this.userIds = userIds;
        this.settings = settings;
    }
    
    public UserProfileRequest(final ArrayList<String> userIds, final boolean defaultProfileSettingsRequest) {
        this.userIds = userIds;
        this.settings = new ArrayList<String>();
        this.setDefaultProfileSettingsRequest(defaultProfileSettingsRequest);
    }
    
    public static String getUserProfileRequestBody(final UserProfileRequest userProfileRequest) {
        return GsonUtil.toJsonString(userProfileRequest);
    }
    
    private void setDefaultProfileSettingsRequest(final boolean b) {
        final ArrayList<String> settings = this.settings;
        if (settings != null) {
            settings.add(UserProfileSetting.GameDisplayName.toString());
            this.settings.add(UserProfileSetting.AppDisplayName.toString());
            this.settings.add(UserProfileSetting.AppDisplayPicRaw.toString());
            this.settings.add(UserProfileSetting.Gamerscore.toString());
            this.settings.add(UserProfileSetting.Gamertag.toString());
            this.settings.add(UserProfileSetting.GameDisplayPicRaw.toString());
            this.settings.add(UserProfileSetting.AccountTier.toString());
            this.settings.add(UserProfileSetting.TenureLevel.toString());
            this.settings.add(UserProfileSetting.XboxOneRep.toString());
            this.settings.add(UserProfileSetting.PreferredColor.toString());
            this.settings.add(UserProfileSetting.Location.toString());
            this.settings.add(UserProfileSetting.Bio.toString());
            this.settings.add(UserProfileSetting.Watermarks.toString());
            if (!b) {
                this.settings.add(UserProfileSetting.RealName.toString());
            }
        }
    }
}

package com.microsoft.xbox.service.model.privacy;

import com.microsoft.xbox.toolkit.*;
import java.util.*;

public class PrivacySettingsResult
{
    public ArrayList<PrivacySettings.PrivacySetting> settings;
    
    public PrivacySettingsResult() {
    }
    
    public PrivacySettingsResult(final ArrayList<PrivacySettings.PrivacySetting> list) {
        this.settings = new ArrayList<PrivacySettings.PrivacySetting>(list);
    }
    
    public static PrivacySettingsResult deserialize(final String s) {
        return GsonUtil.deserializeJson(s, PrivacySettingsResult.class);
    }
    
    public static String getPrivacySettingRequestBody(final PrivacySettingsResult privacySettingsResult) {
        try {
            return GsonUtil.toJsonString(privacySettingsResult);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public String getShareRealNameStatus() {
        for (final PrivacySettings.PrivacySetting privacySetting : this.settings) {
            if (privacySetting.getPrivacySettingId() == PrivacySettings.PrivacySettingId.ShareIdentity) {
                return privacySetting.value;
            }
        }
        return PrivacySettings.PrivacySettingValue.PeopleOnMyList.name();
    }
    
    public boolean getSharingRealNameTransitively() {
        for (final PrivacySettings.PrivacySetting privacySetting : this.settings) {
            if (privacySetting.getPrivacySettingId() == PrivacySettings.PrivacySettingId.ShareIdentityTransitively) {
                return privacySetting.value.equalsIgnoreCase(PrivacySettings.PrivacySettingValue.Everyone.name());
            }
        }
        return false;
    }
}

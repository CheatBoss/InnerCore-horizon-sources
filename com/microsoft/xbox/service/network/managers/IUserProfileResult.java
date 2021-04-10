package com.microsoft.xbox.service.network.managers;

import com.microsoft.xbox.service.model.sls.*;
import java.util.*;
import com.microsoft.xbox.toolkit.*;

public interface IUserProfileResult
{
    public static class ProfileUser
    {
        private static final long FORCE_MATURITY_LEVEL_UPDATE_TIME = 10800000L;
        public boolean canViewTVAdultContent;
        public ProfilePreferredColor colors;
        public String id;
        private int maturityLevel;
        private int[] privileges;
        public ArrayList<Settings> settings;
        private long updateMaturityLevelTimer;
        
        public ProfileUser() {
            this.updateMaturityLevelTimer = -1L;
        }
        
        private void fetchMaturityLevel() {
            try {
                final FamilySettings familySettings = ServiceManagerFactory.getInstance().getSLSServiceManager().getFamilySettings(this.id);
                if (familySettings != null && familySettings.familyUsers != null) {
                    int i = 0;
                Block_5:
                    for (i = 0; i < familySettings.familyUsers.size(); ++i) {
                        if (familySettings.familyUsers.get(i).xuid.equalsIgnoreCase(this.id)) {
                            break Block_5;
                        }
                    }
                    this.canViewTVAdultContent = familySettings.familyUsers.get(i).canViewTVAdultContent;
                    this.maturityLevel = familySettings.familyUsers.get(i).maturityLevel;
                }
            }
            finally {}
            this.updateMaturityLevelTimer = System.currentTimeMillis();
        }
        
        public int getMaturityLevel() {
            if (this.updateMaturityLevelTimer < 0L || System.currentTimeMillis() - this.updateMaturityLevelTimer > 10800000L) {
                this.fetchMaturityLevel();
            }
            return this.maturityLevel;
        }
        
        public int[] getPrivileges() {
            return this.privileges;
        }
        
        public String getSettingValue(final UserProfileSetting userProfileSetting) {
            final ArrayList<Settings> settings = this.settings;
            if (settings != null) {
                for (final Settings settings2 : settings) {
                    if (settings2.id != null && settings2.id.equals(userProfileSetting.toString())) {
                        return settings2.value;
                    }
                }
            }
            return null;
        }
        
        public void setPrivilieges(final int[] privileges) {
            this.privileges = privileges;
        }
        
        public void setmaturityLevel(final int maturityLevel) {
            this.maturityLevel = maturityLevel;
            this.updateMaturityLevelTimer = System.currentTimeMillis();
        }
    }
    
    public static class Settings
    {
        public String id;
        public String value;
    }
    
    public static class UserProfileResult
    {
        public ArrayList<ProfileUser> profileUsers;
        
        public static UserProfileResult deserialize(final String s) {
            return GsonUtil.deserializeJson(s, UserProfileResult.class);
        }
    }
}

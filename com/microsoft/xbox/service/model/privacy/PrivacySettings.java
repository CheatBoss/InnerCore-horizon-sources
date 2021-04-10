package com.microsoft.xbox.service.model.privacy;

public class PrivacySettings
{
    public static class PrivacySetting
    {
        public String setting;
        private PrivacySettingId settingId;
        private PrivacySettingValue settingValue;
        public String value;
        
        public PrivacySetting() {
        }
        
        public PrivacySetting(final PrivacySettingId privacySettingId, final PrivacySettingValue privacySettingValue) {
            this.setting = privacySettingId.name();
            this.value = privacySettingValue.name();
        }
        
        public PrivacySettingId getPrivacySettingId() {
            return this.settingId = PrivacySettingId.getPrivacySettingId(this.setting);
        }
        
        public PrivacySettingValue getPrivacySettingValue() {
            return this.settingValue = PrivacySettingValue.getPrivacySettingValue(this.value);
        }
        
        public void setPrivacySettingId(final PrivacySettingId settingId) {
            this.setting = settingId.name();
            this.settingId = settingId;
        }
    }
    
    public enum PrivacySettingId
    {
        CanShareIdentity, 
        CollectVoiceData, 
        CommunicateUsingTextAndVoice, 
        CommunicateUsingVideo, 
        None, 
        ShareExerciseInfo, 
        ShareFriendList, 
        ShareGameHistory, 
        ShareIdentity, 
        ShareIdentityTransitively, 
        SharePresence, 
        ShareProfile, 
        ShareRecordedGameSessions, 
        ShareVideoAndMusicStatus, 
        ShareXboxMusicActivity;
        
        public static PrivacySettingId getPrivacySettingId(final String s) {
            final PrivacySettingId[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final PrivacySettingId privacySettingId = values[i];
                if (privacySettingId.name().equalsIgnoreCase(s)) {
                    return privacySettingId;
                }
            }
            return PrivacySettingId.None;
        }
    }
    
    public enum PrivacySettingValue
    {
        Blocked, 
        Everyone, 
        FriendCategoryShareIdentity, 
        NotSet, 
        PeopleOnMyList;
        
        public static PrivacySettingValue getPrivacySettingValue(final String s) {
            final PrivacySettingValue[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final PrivacySettingValue privacySettingValue = values[i];
                if (privacySettingValue.name().equalsIgnoreCase(s)) {
                    return privacySettingValue;
                }
            }
            return PrivacySettingValue.NotSet;
        }
    }
}

package com.microsoft.xbox.service.network.managers.xblshared;

import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.service.network.managers.*;
import com.microsoft.xbox.service.model.privacy.*;

public interface ISLSServiceManager
{
    IUserProfileResult.UserProfileResult SearchGamertag(final String p0) throws XLEException;
    
    boolean addFriendToShareIdentitySetting(final String p0, final String p1) throws XLEException;
    
    boolean addUserToFavoriteList(final String p0) throws XLEException;
    
    AddFollowingUserResponseContainer.AddFollowingUserResponse addUserToFollowingList(final String p0) throws XLEException;
    
    boolean addUserToMutedList(final String p0, final String p1) throws XLEException;
    
    boolean addUserToNeverList(final String p0, final String p1) throws XLEException;
    
    FamilySettings getFamilySettings(final String p0) throws XLEException;
    
    MutedListResultContainer.MutedListResult getMutedListInfo(final String p0) throws XLEException;
    
    NeverListResultContainer.NeverListResult getNeverListInfo(final String p0) throws XLEException;
    
    IPeopleHubResult.PeopleHubPeopleSummary getPeopleHubRecommendations() throws XLEException;
    
    PrivacySettings.PrivacySetting getPrivacySetting(final PrivacySettings.PrivacySettingId p0) throws XLEException;
    
    ProfilePreferredColor getProfilePreferredColor(final String p0) throws XLEException;
    
    ProfileSummaryResultContainer.ProfileSummaryResult getProfileSummaryInfo(final String p0) throws XLEException;
    
    IUserProfileResult.UserProfileResult getUserProfileInfo(final String p0) throws XLEException;
    
    PrivacySettingsResult getUserProfilePrivacySettings() throws XLEException;
    
    int[] getXTokenPrivileges() throws XLEException;
    
    boolean removeFriendFromShareIdentitySetting(final String p0, final String p1) throws XLEException;
    
    boolean removeUserFromFavoriteList(final String p0) throws XLEException;
    
    boolean removeUserFromFollowingList(final String p0) throws XLEException;
    
    boolean removeUserFromMutedList(final String p0, final String p1) throws XLEException;
    
    boolean removeUserFromNeverList(final String p0, final String p1) throws XLEException;
    
    boolean setPrivacySettings(final PrivacySettingsResult p0) throws XLEException;
    
    boolean submitFeedback(final String p0, final String p1) throws XLEException;
}

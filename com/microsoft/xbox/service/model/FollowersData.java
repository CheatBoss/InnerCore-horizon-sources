package com.microsoft.xbox.service.model;

import java.io.*;
import java.util.*;
import com.microsoft.xbox.service.network.managers.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.xle.app.*;

public class FollowersData implements Serializable
{
    private static final long serialVersionUID = 6714889261254600161L;
    private String followerText;
    public boolean isCurrentlyPlaying;
    protected boolean isDummy;
    public boolean isFavorite;
    public transient boolean isNew;
    protected DummyType itemDummyType;
    private Date lastPlayedWithDateTime;
    private IPeopleHubResult.PeopleHubPersonSummary personSummary;
    public String presenceString;
    private String recentPlayerText;
    private SearchResultPerson searchResultPerson;
    public UserStatus status;
    private Date timeStamp;
    public long titleId;
    public UserProfileData userProfileData;
    public String xuid;
    
    public FollowersData() {
        this.personSummary = null;
        this.isCurrentlyPlaying = false;
        this.isDummy = false;
        this.isNew = false;
    }
    
    public FollowersData(final FollowersData followersData) {
        this.personSummary = null;
        this.isCurrentlyPlaying = false;
        this.isDummy = false;
        this.isNew = false;
        this.xuid = followersData.xuid;
        this.isFavorite = followersData.isFavorite;
        this.status = followersData.status;
        this.presenceString = followersData.presenceString;
        this.titleId = followersData.titleId;
        this.userProfileData = followersData.userProfileData;
        this.isCurrentlyPlaying = followersData.isCurrentlyPlaying;
        this.timeStamp = followersData.timeStamp;
        this.isDummy = followersData.isDummy;
    }
    
    public FollowersData(final IPeopleHubResult.PeopleHubPersonSummary personSummary) {
        this.personSummary = null;
        this.isCurrentlyPlaying = false;
        this.isDummy = false;
        this.isNew = false;
        XLEAssert.assertNotNull(personSummary);
        this.personSummary = personSummary;
        this.xuid = personSummary.xuid;
        this.userProfileData = new UserProfileData(personSummary);
        this.isFavorite = personSummary.isFavorite;
        this.status = UserStatus.getStatusFromString(personSummary.presenceState);
        this.presenceString = personSummary.presenceText;
        if (personSummary.titleHistory != null) {
            this.titleId = personSummary.titleHistory.TitleId;
            this.timeStamp = personSummary.titleHistory.LastTimePlayed;
        }
        if (personSummary.recentPlayer != null) {
            this.recentPlayerText = personSummary.recentPlayer.text;
            if (!XLEUtil.isNullOrEmpty(personSummary.recentPlayer.titles)) {
                this.lastPlayedWithDateTime = personSummary.recentPlayer.titles.get(0).lastPlayedWithDateTime;
            }
        }
        if (personSummary.follower != null) {
            this.followerText = personSummary.follower.text;
        }
        if (personSummary.titlePresence != null) {
            this.isCurrentlyPlaying = personSummary.titlePresence.IsCurrentlyPlaying;
            this.presenceString = personSummary.titlePresence.PresenceText;
        }
    }
    
    public FollowersData(final boolean b) {
        this(b, DummyType.NOT_SET);
    }
    
    public FollowersData(final boolean isDummy, final DummyType itemDummyType) {
        this.personSummary = null;
        this.isCurrentlyPlaying = false;
        this.isDummy = false;
        this.isNew = false;
        this.isDummy = isDummy;
        this.itemDummyType = itemDummyType;
    }
    
    public String getFollowersTitleText() {
        return this.followerText;
    }
    
    public int getGameScore() {
        final UserProfileData userProfileData = this.userProfileData;
        if (userProfileData != null) {
            return Integer.parseInt(userProfileData.gamerScore);
        }
        return 0;
    }
    
    public String getGamerName() {
        final UserProfileData userProfileData = this.userProfileData;
        if (userProfileData != null) {
            return userProfileData.appDisplayName;
        }
        return "";
    }
    
    public String getGamerPicUrl() {
        final UserProfileData userProfileData = this.userProfileData;
        if (userProfileData != null) {
            return userProfileData.profileImageUrl;
        }
        return null;
    }
    
    public String getGamerRealName() {
        final UserProfileData userProfileData = this.userProfileData;
        if (userProfileData == null) {
            return null;
        }
        return userProfileData.gamerRealName;
    }
    
    public String getGamertag() {
        final UserProfileData userProfileData = this.userProfileData;
        if (userProfileData != null) {
            return userProfileData.gamerTag;
        }
        return "";
    }
    
    public boolean getIsDummy() {
        return this.isDummy;
    }
    
    public boolean getIsOnline() {
        return this.status == UserStatus.Online;
    }
    
    public DummyType getItemDummyType() {
        return this.itemDummyType;
    }
    
    public Date getLastPlayedWithDateTime() {
        return this.lastPlayedWithDateTime;
    }
    
    public IPeopleHubResult.PeopleHubPersonSummary getPersonSummary() {
        return this.personSummary;
    }
    
    public String getRecentPlayerTitleText() {
        return this.recentPlayerText;
    }
    
    public SearchResultPerson getSearchResultPerson() {
        return this.searchResultPerson;
    }
    
    public Date getTimeStamp() {
        return this.timeStamp;
    }
    
    public void setItemDummyType(final DummyType itemDummyType) {
        this.isDummy = true;
        this.itemDummyType = itemDummyType;
    }
    
    public void setSearchResultPerson(final SearchResultPerson searchResultPerson) {
        this.searchResultPerson = searchResultPerson;
    }
    
    public void setTimeStamp(final Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public enum DummyType
    {
        DUMMY_ERROR, 
        DUMMY_FRIENDS_HEADER, 
        DUMMY_FRIENDS_WHO_PLAY, 
        DUMMY_HEADER, 
        DUMMY_LINK_TO_FACEBOOK, 
        DUMMY_LOADING, 
        DUMMY_NO_DATA, 
        DUMMY_VIPS, 
        NOT_SET;
    }
}

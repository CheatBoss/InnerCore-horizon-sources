package com.microsoft.xbox.service.network.managers;

import java.util.*;
import com.microsoft.xbox.toolkit.*;

public interface IPeopleHubResult
{
    public static class Follower
    {
        public Date followedDateTime;
        public String text;
    }
    
    public static class MultiplayerSummary
    {
        public int InMultiplayerSession;
        public int InParty;
    }
    
    public static class PeopleHubPeopleSummary
    {
        public ArrayList<PeopleHubPersonSummary> people;
        public RecommendationSummary recommendationSummary;
    }
    
    public static class PeopleHubPersonSummary
    {
        public String displayName;
        public String displayPicRaw;
        public Follower follower;
        public String gamerScore;
        public String gamertag;
        public boolean isFavorite;
        public boolean isFollowedByCaller;
        public boolean isFollowingCaller;
        public boolean isIdentityShared;
        public MultiplayerSummary multiplayerSummary;
        public PeopleHubPreferredColor preferredColor;
        public String presenceState;
        public String presenceText;
        public String realName;
        public RecentPlayer recentPlayer;
        public PeopleHubRecommendation recommendation;
        public PeopleHubTitleHistory titleHistory;
        public PeopleHubTitlePresence titlePresence;
        public ArrayList<PeopleHubTitleSummary> titleSummaries;
        public boolean useAvatar;
        public String xboxOneRep;
        public String xuid;
        
        public String getRealNameFromRecommendationOrDefault() {
            String realName;
            final String s = realName = this.realName;
            if (JavaUtil.isNullOrEmpty(s)) {
                final PeopleHubRecommendation recommendation = this.recommendation;
                realName = s;
                if (recommendation != null) {
                    realName = s;
                    if (recommendation.Reasons != null) {
                        realName = s;
                        if (this.recommendation.Reasons.size() > 0) {
                            realName = this.recommendation.Reasons.get(0);
                        }
                    }
                }
            }
            return realName;
        }
    }
    
    public static class PeopleHubPreferredColor
    {
        public String primaryColor;
        public String secondaryColor;
        public String tertiaryColor;
    }
    
    public static class PeopleHubRecommendation
    {
        public ArrayList<String> Reasons;
        public String Type;
        
        public RecommendationType getRecommendationType() {
            return RecommendationType.getRecommendationType(this.Type);
        }
    }
    
    public static class PeopleHubTitleHistory
    {
        public Date LastTimePlayed;
        public long TitleId;
        public String TitleName;
    }
    
    public static class PeopleHubTitlePresence
    {
        public boolean IsCurrentlyPlaying;
        public String PresenceText;
        public String TitleId;
        public String TitleName;
    }
    
    public static class PeopleHubTitleSummary
    {
    }
    
    public static class RecentPlayer
    {
        public String text;
        public ArrayList<Title> titles;
    }
    
    public static class RecommendationSummary
    {
        public int VIP;
        public int facebookFriend;
        public int follower;
        public int friendOfFriend;
        public int phoneContact;
        public boolean promoteSuggestions;
    }
    
    public enum RecommendationType
    {
        Dummy, 
        FacebookFriend, 
        Follower, 
        FriendOfFriend, 
        PhoneContact, 
        Unknown, 
        VIP;
        
        public static RecommendationType getRecommendationType(final String s) {
            final RecommendationType[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final RecommendationType recommendationType = values[i];
                if (recommendationType.name().equalsIgnoreCase(s)) {
                    return recommendationType;
                }
            }
            return RecommendationType.Unknown;
        }
    }
    
    public static class Title
    {
        public Date lastPlayedWithDateTime;
        public String titleName;
    }
}

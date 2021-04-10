package com.microsoft.xbox.telemetry.helpers;

import java.util.*;
import com.microsoft.xbox.toolkit.*;

public class UTCChangeRelationship
{
    private static CharSequence currentActivityTitle;
    private static String currentXUID = "";
    
    static {
        UTCChangeRelationship.currentActivityTitle = "";
    }
    
    private static HashMap<String, Object> getAdditionalInfo(final String s) {
        final HashMap<String, String> hashMap = (HashMap<String, String>)new HashMap<String, Object>();
        final StringBuilder sb = new StringBuilder();
        sb.append("x:");
        sb.append(s);
        hashMap.put("targetXUID", sb.toString());
        return (HashMap<String, Object>)hashMap;
    }
    
    public static void trackChangeRelationshipAction(final CharSequence charSequence, final String s, final boolean b, final boolean b2) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                final HashMap access$200 = getAdditionalInfo(s);
                Relationship relationship;
                if (b) {
                    relationship = Relationship.EXISTINGFRIEND;
                }
                else {
                    relationship = Relationship.ADDFRIEND;
                }
                access$200.put("relationship", relationship.getValue());
                UTCPageAction.track("Change Relationship - Action", charSequence, access$200);
                if (b2) {
                    UTCChangeRelationship.trackChangeRelationshipDone(charSequence, s, Relationship.ADDFRIEND, RealNameStatus.SHARINGON, FavoriteStatus.NOTFAVORITED, GamerType.FACEBOOK);
                }
            }
        });
    }
    
    public static void trackChangeRelationshipAction(final boolean b, final boolean b2) {
        verifyTrackedDefaults();
        trackChangeRelationshipAction(UTCChangeRelationship.currentActivityTitle, UTCChangeRelationship.currentXUID, b, b2);
    }
    
    public static void trackChangeRelationshipDone(final Relationship relationship, final RealNameStatus realNameStatus, final FavoriteStatus favoriteStatus, final GamerType gamerType) {
        verifyTrackedDefaults();
        trackChangeRelationshipDone(UTCChangeRelationship.currentActivityTitle, UTCChangeRelationship.currentXUID, relationship, realNameStatus, favoriteStatus, gamerType);
    }
    
    public static void trackChangeRelationshipDone(final CharSequence charSequence, final String s, final Relationship relationship, final RealNameStatus realNameStatus, final FavoriteStatus favoriteStatus, final GamerType gamerType) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                final HashMap access$200 = getAdditionalInfo(s);
                access$200.put("relationship", relationship.getValue());
                access$200.put("favorite", favoriteStatus.getValue());
                access$200.put("realname", realNameStatus.getValue());
                access$200.put("gamertype", gamerType.getValue());
                UTCPageAction.track("Change Relationship - Done", charSequence, access$200);
            }
        });
    }
    
    public static void trackChangeRelationshipRemoveFriend() {
        verifyTrackedDefaults();
        trackChangeRelationshipRemoveFriend(UTCChangeRelationship.currentActivityTitle, UTCChangeRelationship.currentXUID);
    }
    
    public static void trackChangeRelationshipRemoveFriend(final CharSequence charSequence, final String s) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                final HashMap access$200 = getAdditionalInfo(s);
                access$200.put("relationship", Relationship.REMOVEFRIEND);
                UTCPageAction.track("Change Relationship - Action", charSequence, access$200);
            }
        });
    }
    
    public static void trackChangeRelationshipView(final CharSequence charSequence, final String s) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                UTCChangeRelationship.currentActivityTitle = charSequence;
                UTCChangeRelationship.currentXUID = s;
                UTCPageView.track("Change Relationship - Change Relationship View", UTCChangeRelationship.currentActivityTitle, getAdditionalInfo(UTCChangeRelationship.currentXUID));
            }
        });
    }
    
    private static void verifyTrackedDefaults() {
        XLEAssert.assertFalse("Called trackPeopleHubView without set currentXUID", UTCChangeRelationship.currentXUID.equals(""));
        XLEAssert.assertFalse("Called trackPeopleHubView without set activityTitle", UTCChangeRelationship.currentActivityTitle.toString().equals(""));
    }
    
    public enum FavoriteStatus
    {
        EXISTINGFAVORITE(4), 
        EXISTINGNOTFAVORITED(5), 
        FAVORITED(1), 
        NOTFAVORITED(3), 
        UNFAVORITED(2), 
        UNKNOWN(0);
        
        private int value;
        
        private FavoriteStatus(final int value) {
            this.value = value;
        }
        
        public int getValue() {
            return this.value;
        }
    }
    
    public enum GamerType
    {
        FACEBOOK(2), 
        NORMAL(1), 
        SUGGESTED(3), 
        UNKNOWN(0);
        
        private int value;
        
        private GamerType(final int value) {
            this.value = value;
        }
        
        public int getValue() {
            return this.value;
        }
    }
    
    public enum RealNameStatus
    {
        EXISTINGNOTSHARED(4), 
        EXISTINGSHARED(3), 
        SHARINGOFF(2), 
        SHARINGON(1), 
        UNKNOWN(0);
        
        private int value;
        
        private RealNameStatus(final int value) {
            this.value = value;
        }
        
        public int getValue() {
            return this.value;
        }
    }
    
    public enum Relationship
    {
        ADDFRIEND(1), 
        EXISTINGFRIEND(3), 
        NOTCHANGED(4), 
        REMOVEFRIEND(2), 
        UNKNOWN(0);
        
        private int value;
        
        private Relationship(final int value) {
            this.value = value;
        }
        
        public int getValue() {
            return this.value;
        }
    }
}

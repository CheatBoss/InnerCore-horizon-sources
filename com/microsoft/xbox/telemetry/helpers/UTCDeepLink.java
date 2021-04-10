package com.microsoft.xbox.telemetry.helpers;

import java.util.*;
import com.microsoft.xbox.telemetry.utc.*;

public class UTCDeepLink
{
    public static final String CALLING_APP_KEY = "deepLinkCaller";
    public static final String DEEPLINK_KEY_NAME = "deepLinkId";
    public static final String INTENDED_ACTION_KEY = "intendedAction";
    public static final String TARGET_TITLE_KEY = "targetTitleId";
    public static final String TARGET_XUID_KEY = "targetXUID";
    
    private static String generateCorrelationId() {
        return CommonData.getApplicationSession();
    }
    
    private static HashMap<String, Object> getAdditionalInfo(final String s) {
        final HashMap<String, String> hashMap = (HashMap<String, String>)new HashMap<String, Object>();
        hashMap.put("deepLinkId", generateCorrelationId());
        hashMap.put("deepLinkCaller", s);
        return (HashMap<String, Object>)hashMap;
    }
    
    public static String trackFriendSuggestionsLink(final CharSequence charSequence, final String s) {
        return UTCEventTracker.callStringTrackWrapper((UTCEventTracker.UTCStringEventDelegate)new UTCEventTracker.UTCStringEventDelegate() {
            @Override
            public String call() {
                final HashMap access$000 = getAdditionalInfo(s);
                UTCPageAction.track("DeepLink - Friend Suggestions", charSequence, access$000);
                return access$000.get("deepLinkId").toString();
            }
        });
    }
    
    public static String trackGameHubAchievementsLink(final CharSequence charSequence, final String s, final String s2) {
        return UTCEventTracker.callStringTrackWrapper((UTCEventTracker.UTCStringEventDelegate)new UTCEventTracker.UTCStringEventDelegate() {
            @Override
            public String call() {
                final HashMap access$000 = getAdditionalInfo(s);
                access$000.put("targetTitleId", s2);
                UTCPageAction.track("DeepLink - GameHub", charSequence, access$000);
                return access$000.get("deepLinkId").toString();
            }
        });
    }
    
    public static String trackGameHubLink(final CharSequence charSequence, final String s, final String s2) {
        return UTCEventTracker.callStringTrackWrapper((UTCEventTracker.UTCStringEventDelegate)new UTCEventTracker.UTCStringEventDelegate() {
            @Override
            public String call() {
                final HashMap access$000 = getAdditionalInfo(s);
                access$000.put("targetXUID", s2);
                UTCPageAction.track("DeepLink - GameHub", charSequence, access$000);
                return access$000.get("deepLinkId").toString();
            }
        });
    }
    
    public static String trackUserProfileLink(final CharSequence charSequence, final String s, final String s2) {
        return UTCEventTracker.callStringTrackWrapper((UTCEventTracker.UTCStringEventDelegate)new UTCEventTracker.UTCStringEventDelegate() {
            @Override
            public String call() {
                final HashMap access$000 = getAdditionalInfo(s);
                final StringBuilder sb = new StringBuilder();
                sb.append("x:");
                sb.append(s2);
                access$000.put("targetXUID", sb.toString());
                UTCPageAction.track("DeepLink - User Profile", charSequence, access$000);
                return access$000.get("deepLinkId").toString();
            }
        });
    }
    
    public static void trackUserSendToStore(final CharSequence charSequence, final String s, final String s2) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                final HashMap access$000 = getAdditionalInfo(s);
                access$000.put("intendedAction", s2);
                UTCPageAction.track("DeepLink - Store Redirect", charSequence, access$000);
            }
        });
    }
    
    public static String trackUserSettingsLink(final CharSequence charSequence, final String s) {
        return UTCEventTracker.callStringTrackWrapper((UTCEventTracker.UTCStringEventDelegate)new UTCEventTracker.UTCStringEventDelegate() {
            @Override
            public String call() {
                final HashMap access$000 = getAdditionalInfo(s);
                UTCPageAction.track("DeepLink - User Settings", charSequence, access$000);
                return access$000.get("deepLinkId").toString();
            }
        });
    }
}

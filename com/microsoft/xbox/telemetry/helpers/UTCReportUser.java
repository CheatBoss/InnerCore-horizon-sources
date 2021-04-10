package com.microsoft.xbox.telemetry.helpers;

import java.util.*;
import com.microsoft.xbox.toolkit.*;

public class UTCReportUser
{
    private static CharSequence currentActivityTitle;
    private static String currentXUID = "";
    
    static {
        UTCReportUser.currentActivityTitle = "";
    }
    
    private static HashMap<String, Object> getAdditionalInfo(final String s) {
        final HashMap<String, String> hashMap = (HashMap<String, String>)new HashMap<String, Object>();
        final StringBuilder sb = new StringBuilder();
        sb.append("x:");
        sb.append(s);
        hashMap.put("targetXUID", sb.toString());
        return (HashMap<String, Object>)hashMap;
    }
    
    public static void trackReportDialogOK(final CharSequence charSequence, final String s, final String s2) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                final HashMap access$200 = getAdditionalInfo(s);
                access$200.put("reason", s2);
                UTCPageAction.track("People Hub - Report OK", charSequence, access$200);
            }
        });
    }
    
    public static void trackReportDialogOK(final String s) {
        verifyTrackedDefaults();
        trackReportDialogOK(UTCReportUser.currentActivityTitle, UTCReportUser.currentXUID, s);
    }
    
    public static void trackReportView(final CharSequence charSequence, final String s) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                UTCReportUser.currentActivityTitle = charSequence;
                UTCReportUser.currentXUID = s;
                UTCPageView.track("People Hub Report view", UTCReportUser.currentActivityTitle, getAdditionalInfo(s));
            }
        });
    }
    
    private static void verifyTrackedDefaults() {
        XLEAssert.assertFalse("Called trackPeopleHubView without set currentXUID", UTCReportUser.currentXUID.equals(""));
        XLEAssert.assertFalse("Called trackPeopleHubView without set activityTitle", UTCReportUser.currentActivityTitle.toString().equals(""));
    }
}

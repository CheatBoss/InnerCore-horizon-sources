package com.microsoft.xbox.telemetry.helpers;

import java.util.*;
import com.microsoft.xbox.toolkit.*;

public class UTCPeopleHub
{
    private static CharSequence currentActivityTitle;
    private static String currentXUID = "";
    
    static {
        UTCPeopleHub.currentActivityTitle = "";
    }
    
    private static HashMap<String, Object> getAdditionalInfo(final String s) {
        final HashMap<String, String> hashMap = (HashMap<String, String>)new HashMap<String, Object>();
        final StringBuilder sb = new StringBuilder();
        sb.append("x:");
        sb.append(s);
        hashMap.put("targetXUID", sb.toString());
        return (HashMap<String, Object>)hashMap;
    }
    
    public static void trackBlock() {
        verifyTrackedDefaults();
        trackBlock(UTCPeopleHub.currentActivityTitle, UTCPeopleHub.currentXUID);
    }
    
    public static void trackBlock(final CharSequence charSequence, final String s) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                UTCPageAction.track("People Hub - Block", charSequence, getAdditionalInfo(s));
            }
        });
    }
    
    public static void trackBlockDialogComplete() {
        verifyTrackedDefaults();
        trackBlockDialogComplete(UTCPeopleHub.currentActivityTitle, UTCPeopleHub.currentXUID);
    }
    
    public static void trackBlockDialogComplete(final CharSequence charSequence, final String s) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                UTCPageAction.track("People Hub - Block OK", charSequence, getAdditionalInfo(s));
            }
        });
    }
    
    public static void trackMute(final CharSequence charSequence, final String s, final boolean b) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                final HashMap access$200 = getAdditionalInfo(s);
                access$200.put("isMuted", b);
                UTCPageAction.track("People Hub - Mute", charSequence, access$200);
            }
        });
    }
    
    public static void trackMute(final boolean b) {
        verifyTrackedDefaults();
        trackMute(UTCPeopleHub.currentActivityTitle, UTCPeopleHub.currentXUID, b);
    }
    
    public static void trackPeopleHubView(final CharSequence charSequence, final String s, final boolean b) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                UTCPeopleHub.currentXUID = s;
                UTCPeopleHub.currentActivityTitle = charSequence;
                String s;
                if (b) {
                    s = "People Hub - ME View";
                }
                else {
                    s = "People Hub - You View";
                }
                UTCPageView.track(s, UTCPeopleHub.currentActivityTitle, getAdditionalInfo(s));
            }
        });
    }
    
    public static void trackReport() {
        verifyTrackedDefaults();
        trackReport(UTCPeopleHub.currentActivityTitle, UTCPeopleHub.currentXUID);
    }
    
    public static void trackReport(final CharSequence charSequence, final String s) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                UTCPageAction.track("People Hub - Report", charSequence, getAdditionalInfo(s));
            }
        });
    }
    
    public static void trackUnblock() {
        verifyTrackedDefaults();
        trackUnblock(UTCPeopleHub.currentActivityTitle, UTCPeopleHub.currentXUID);
    }
    
    public static void trackUnblock(final CharSequence charSequence, final String s) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                UTCPageAction.track("People Hub - Unblock", charSequence, getAdditionalInfo(s));
            }
        });
    }
    
    public static void trackViewInXboxApp() {
        verifyTrackedDefaults();
        trackViewInXboxApp(UTCPeopleHub.currentActivityTitle, UTCPeopleHub.currentXUID);
    }
    
    public static void trackViewInXboxApp(final CharSequence charSequence, final String s) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                UTCPageAction.track("People Hub - View in Xbox App", charSequence, getAdditionalInfo(s));
            }
        });
    }
    
    public static void trackViewInXboxAppDialogComplete() {
        verifyTrackedDefaults();
        trackViewInXboxAppDialogComplete(UTCPeopleHub.currentActivityTitle, UTCPeopleHub.currentXUID);
    }
    
    public static void trackViewInXboxAppDialogComplete(final CharSequence charSequence, final String s) {
        UTCEventTracker.callTrackWrapper((UTCEventTracker.UTCEventDelegate)new UTCEventTracker.UTCEventDelegate() {
            @Override
            public void call() {
                UTCPageAction.track("People Hub - View in Xbox App OK", charSequence, getAdditionalInfo(s));
            }
        });
    }
    
    private static void verifyTrackedDefaults() {
        XLEAssert.assertFalse("Called trackPeopleHubView without set currentXUID", UTCPeopleHub.currentXUID.equals(""));
        XLEAssert.assertFalse("Called trackPeopleHubView without set activityTitle", UTCPeopleHub.currentActivityTitle.toString().equals(""));
    }
}

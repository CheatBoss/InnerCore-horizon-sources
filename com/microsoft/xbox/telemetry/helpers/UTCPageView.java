package com.microsoft.xbox.telemetry.helpers;

import java.util.*;
import com.microsoft.xbox.telemetry.utc.*;

public class UTCPageView
{
    private static ArrayList<String> pages;
    
    static {
        UTCPageView.pages = new ArrayList<String>();
    }
    
    public static void addPage(final String s) {
        if (UTCPageView.pages == null) {
            UTCPageView.pages = new ArrayList<String>();
        }
        if (!UTCPageView.pages.contains(s) && s != null) {
            UTCPageView.pages.add(s);
        }
    }
    
    public static String getCurrentPage() {
        final int size = getSize();
        if (size == 0) {
            return "Unknown";
        }
        return UTCPageView.pages.get(size - 1);
    }
    
    public static String getPreviousPage() {
        final int size = getSize();
        if (size < 2) {
            return "Unknown";
        }
        return UTCPageView.pages.get(size - 2);
    }
    
    public static int getSize() {
        if (UTCPageView.pages == null) {
            UTCPageView.pages = new ArrayList<String>();
        }
        return UTCPageView.pages.size();
    }
    
    public static void removePage() {
        final int size = getSize();
        if (size > 0) {
            UTCPageView.pages.remove(size - 1);
        }
    }
    
    public static void track(final String s, final CharSequence charSequence) {
        track(s, charSequence, new HashMap<String, Object>());
    }
    
    public static void track(final String pageName, final CharSequence charSequence, final HashMap<String, Object> additionalInfo) {
        Label_0015: {
            if (charSequence == null) {
                break Label_0015;
            }
            while (true) {
                try {
                    additionalInfo.put("activityTitle", charSequence);
                    addPage(pageName);
                    final String previousPage = getPreviousPage();
                    final PageView pageView = new PageView();
                    pageView.pageName = pageName;
                    pageView.fromPage = previousPage;
                    pageView.additionalInfo = additionalInfo;
                    UTCLog.log("pageView:%s, fromPage:%s, additionalInfo:%s", pageName, previousPage, additionalInfo);
                    UTCTelemetry.LogEvent(pageView);
                    return;
                    final Exception ex;
                    UTCError.trackException(ex, "UTCPageView.track");
                    UTCLog.log(ex.getMessage(), new Object[0]);
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
}

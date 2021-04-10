package com.microsoft.xbox.telemetry.helpers;

import java.util.*;
import com.microsoft.xbox.telemetry.utc.*;

public class UTCPageAction
{
    public static void track(final String s, final CharSequence charSequence) {
        track(s, UTCPageView.getCurrentPage(), charSequence, new HashMap<String, Object>());
    }
    
    public static void track(final String s, final CharSequence charSequence, final HashMap<String, Object> hashMap) {
        track(s, UTCPageView.getCurrentPage(), charSequence, hashMap);
    }
    
    public static void track(final String actionName, final String pageName, final CharSequence charSequence, final HashMap<String, Object> additionalInfo) {
        Label_0015: {
            if (charSequence == null) {
                break Label_0015;
            }
            while (true) {
                try {
                    additionalInfo.put("activityTitle", charSequence);
                    final PageAction pageAction = new PageAction();
                    pageAction.actionName = actionName;
                    pageAction.pageName = pageName;
                    pageAction.additionalInfo = additionalInfo;
                    UTCLog.log("pageActions:%s, onPage:%s, additionalInfo:%s", actionName, pageName, additionalInfo);
                    UTCTelemetry.LogEvent(pageAction);
                    return;
                    final Exception ex;
                    UTCError.trackException(ex, "UTCPageAction.track");
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

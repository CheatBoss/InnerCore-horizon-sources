package com.appsflyer.internal;

import com.appsflyer.*;

public final class ah
{
    private static String \u0131;
    private static String \u0399;
    
    ah() {
    }
    
    public static void \u01c3(final String \u0131) {
        ah.\u0131 = \u0131;
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < \u0131.length(); ++i) {
            if (i != 0 && i != \u0131.length() - 1) {
                sb.append("*");
            }
            else {
                sb.append(\u0131.charAt(i));
            }
        }
        ah.\u0399 = sb.toString();
    }
    
    public static void \u0399(final String s) {
        if (ah.\u0131 == null) {
            \u01c3(AppsFlyerProperties.getInstance().getString("AppsFlyerKey"));
        }
        final String \u0131 = ah.\u0131;
        if (\u0131 != null && s.contains(\u0131)) {
            AFLogger.afInfoLog(s.replace(ah.\u0131, ah.\u0399));
        }
    }
}

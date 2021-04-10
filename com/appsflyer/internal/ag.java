package com.appsflyer.internal;

import com.appsflyer.*;

public final class ag
{
    private a \u0269;
    
    public ag() {
        this.\u0269 = (a)new a() {
            @Override
            public final Class<?> \u01c3(final String s) throws ClassNotFoundException {
                return Class.forName(s);
            }
        };
    }
    
    private boolean \u0131(final String s) {
        try {
            this.\u0269.\u01c3(s);
            final StringBuilder sb = new StringBuilder("Class: ");
            sb.append(s);
            sb.append(" is found.");
            AFLogger.afRDLog(sb.toString());
            return true;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog(t.getMessage(), t);
            return false;
        }
    }
    
    public final String \u0131() {
        final c[] values = c.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final c c = values[i];
            if (this.\u0131(c.\u0456)) {
                return c.\u0406;
            }
        }
        return c.\u03b9.\u0406;
    }
    
    interface a
    {
        Class<?> \u01c3(final String p0) throws ClassNotFoundException;
    }
    
    enum c
    {
        \u0131("android_unity", "com.unity3d.player.UnityPlayer"), 
        \u0196("android_adobe_ex", "com.appsflyer.adobeextension"), 
        \u01c3("android_cordova", "org.apache.cordova.CordovaActivity"), 
        \u0269("android_segment", "com.segment.analytics.integrations.Integration"), 
        \u0279("android_cocos2dx", "org.cocos2dx.lib.Cocos2dxActivity"), 
        \u0399("android_reactNative", "com.facebook.react.ReactApplication"), 
        \u03b9("android_native", "android_native"), 
        \u04c0("android_flutter", "com.appsflyer.appsflyersdk.AppsflyerSdkPlugin");
        
        private String \u0406;
        private String \u0456;
        
        private c(final String \u0456, final String \u04562) {
            this.\u0406 = \u0456;
            this.\u0456 = \u04562;
        }
    }
}

package android.support.v4.accessibilityservice;

import android.os.*;
import android.accessibilityservice.*;
import android.content.pm.*;

public final class AccessibilityServiceInfoCompat
{
    public static final int CAPABILITY_CAN_FILTER_KEY_EVENTS = 8;
    public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
    public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
    public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
    public static final int DEFAULT = 1;
    public static final int FEEDBACK_ALL_MASK = -1;
    public static final int FEEDBACK_BRAILLE = 32;
    public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
    public static final int FLAG_REPORT_VIEW_IDS = 16;
    public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
    public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
    public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;
    private static final AccessibilityServiceInfoVersionImpl IMPL;
    
    static {
        AccessibilityServiceInfoVersionImpl impl;
        if (Build$VERSION.SDK_INT >= 18) {
            impl = new AccessibilityServiceInfoJellyBeanMr2();
        }
        else if (Build$VERSION.SDK_INT >= 14) {
            impl = new AccessibilityServiceInfoIcsImpl();
        }
        else {
            impl = new AccessibilityServiceInfoStubImpl();
        }
        IMPL = impl;
    }
    
    private AccessibilityServiceInfoCompat() {
    }
    
    public static String capabilityToString(final int n) {
        if (n == 4) {
            return "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
        }
        if (n == 8) {
            return "CAPABILITY_CAN_FILTER_KEY_EVENTS";
        }
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 2: {
                return "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION";
            }
            case 1: {
                return "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
            }
        }
    }
    
    public static String feedbackTypeToString(int i) {
        final StringBuilder sb = new StringBuilder();
        String s = "[";
    Label_0011:
        while (true) {
            sb.append(s);
            while (i > 0) {
                final int n = 1 << Integer.numberOfTrailingZeros(i);
                i &= ~n;
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                if (n == 4) {
                    s = "FEEDBACK_AUDIBLE";
                    continue Label_0011;
                }
                if (n == 8) {
                    s = "FEEDBACK_VISUAL";
                    continue Label_0011;
                }
                if (n == 16) {
                    s = "FEEDBACK_GENERIC";
                    continue Label_0011;
                }
                switch (n) {
                    default: {
                        continue;
                    }
                    case 2: {
                        s = "FEEDBACK_HAPTIC";
                        continue Label_0011;
                    }
                    case 1: {
                        s = "FEEDBACK_SPOKEN";
                        continue Label_0011;
                    }
                }
            }
            break;
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String flagToString(final int n) {
        if (n == 4) {
            return "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
        }
        if (n == 8) {
            return "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
        }
        if (n == 16) {
            return "FLAG_REPORT_VIEW_IDS";
        }
        if (n == 32) {
            return "FLAG_REQUEST_FILTER_KEY_EVENTS";
        }
        switch (n) {
            default: {
                return null;
            }
            case 2: {
                return "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
            }
            case 1: {
                return "DEFAULT";
            }
        }
    }
    
    public static boolean getCanRetrieveWindowContent(final AccessibilityServiceInfo accessibilityServiceInfo) {
        return AccessibilityServiceInfoCompat.IMPL.getCanRetrieveWindowContent(accessibilityServiceInfo);
    }
    
    public static int getCapabilities(final AccessibilityServiceInfo accessibilityServiceInfo) {
        return AccessibilityServiceInfoCompat.IMPL.getCapabilities(accessibilityServiceInfo);
    }
    
    public static String getDescription(final AccessibilityServiceInfo accessibilityServiceInfo) {
        return AccessibilityServiceInfoCompat.IMPL.getDescription(accessibilityServiceInfo);
    }
    
    public static String getId(final AccessibilityServiceInfo accessibilityServiceInfo) {
        return AccessibilityServiceInfoCompat.IMPL.getId(accessibilityServiceInfo);
    }
    
    public static ResolveInfo getResolveInfo(final AccessibilityServiceInfo accessibilityServiceInfo) {
        return AccessibilityServiceInfoCompat.IMPL.getResolveInfo(accessibilityServiceInfo);
    }
    
    public static String getSettingsActivityName(final AccessibilityServiceInfo accessibilityServiceInfo) {
        return AccessibilityServiceInfoCompat.IMPL.getSettingsActivityName(accessibilityServiceInfo);
    }
    
    static class AccessibilityServiceInfoIcsImpl extends AccessibilityServiceInfoStubImpl
    {
        @Override
        public boolean getCanRetrieveWindowContent(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatIcs.getCanRetrieveWindowContent(accessibilityServiceInfo);
        }
        
        @Override
        public int getCapabilities(final AccessibilityServiceInfo accessibilityServiceInfo) {
            if (this.getCanRetrieveWindowContent(accessibilityServiceInfo)) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public String getDescription(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatIcs.getDescription(accessibilityServiceInfo);
        }
        
        @Override
        public String getId(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatIcs.getId(accessibilityServiceInfo);
        }
        
        @Override
        public ResolveInfo getResolveInfo(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatIcs.getResolveInfo(accessibilityServiceInfo);
        }
        
        @Override
        public String getSettingsActivityName(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatIcs.getSettingsActivityName(accessibilityServiceInfo);
        }
    }
    
    static class AccessibilityServiceInfoJellyBeanMr2 extends AccessibilityServiceInfoIcsImpl
    {
        @Override
        public int getCapabilities(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatJellyBeanMr2.getCapabilities(accessibilityServiceInfo);
        }
    }
    
    static class AccessibilityServiceInfoStubImpl implements AccessibilityServiceInfoVersionImpl
    {
        @Override
        public boolean getCanRetrieveWindowContent(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return false;
        }
        
        @Override
        public int getCapabilities(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return 0;
        }
        
        @Override
        public String getDescription(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return null;
        }
        
        @Override
        public String getId(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return null;
        }
        
        @Override
        public ResolveInfo getResolveInfo(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return null;
        }
        
        @Override
        public String getSettingsActivityName(final AccessibilityServiceInfo accessibilityServiceInfo) {
            return null;
        }
    }
    
    interface AccessibilityServiceInfoVersionImpl
    {
        boolean getCanRetrieveWindowContent(final AccessibilityServiceInfo p0);
        
        int getCapabilities(final AccessibilityServiceInfo p0);
        
        String getDescription(final AccessibilityServiceInfo p0);
        
        String getId(final AccessibilityServiceInfo p0);
        
        ResolveInfo getResolveInfo(final AccessibilityServiceInfo p0);
        
        String getSettingsActivityName(final AccessibilityServiceInfo p0);
    }
}

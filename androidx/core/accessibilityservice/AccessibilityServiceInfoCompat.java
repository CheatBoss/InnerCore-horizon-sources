package androidx.core.accessibilityservice;

import androidx.annotation.*;
import android.accessibilityservice.*;
import android.os.*;
import android.content.pm.*;

public final class AccessibilityServiceInfoCompat
{
    public static final int CAPABILITY_CAN_FILTER_KEY_EVENTS = 8;
    public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
    public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
    public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
    public static final int FEEDBACK_ALL_MASK = -1;
    public static final int FEEDBACK_BRAILLE = 32;
    public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
    public static final int FLAG_REPORT_VIEW_IDS = 16;
    public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
    public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
    public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;
    
    private AccessibilityServiceInfoCompat() {
    }
    
    @NonNull
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
    
    @NonNull
    public static String feedbackTypeToString(int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (i > 0) {
            final int n = 1 << Integer.numberOfTrailingZeros(i);
            i &= ~n;
            if (sb.length() > 1) {
                sb.append(", ");
            }
            if (n != 4) {
                if (n != 8) {
                    if (n != 16) {
                        switch (n) {
                            default: {
                                continue;
                            }
                            case 2: {
                                sb.append("FEEDBACK_HAPTIC");
                                continue;
                            }
                            case 1: {
                                sb.append("FEEDBACK_SPOKEN");
                                continue;
                            }
                        }
                    }
                    else {
                        sb.append("FEEDBACK_GENERIC");
                    }
                }
                else {
                    sb.append("FEEDBACK_VISUAL");
                }
            }
            else {
                sb.append("FEEDBACK_AUDIBLE");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    @Nullable
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
    
    public static int getCapabilities(@NonNull final AccessibilityServiceInfo accessibilityServiceInfo) {
        if (Build$VERSION.SDK_INT >= 18) {
            return accessibilityServiceInfo.getCapabilities();
        }
        if (accessibilityServiceInfo.getCanRetrieveWindowContent()) {
            return 1;
        }
        return 0;
    }
    
    @Nullable
    public static String loadDescription(@NonNull final AccessibilityServiceInfo accessibilityServiceInfo, @NonNull final PackageManager packageManager) {
        if (Build$VERSION.SDK_INT >= 16) {
            return accessibilityServiceInfo.loadDescription(packageManager);
        }
        return accessibilityServiceInfo.getDescription();
    }
}

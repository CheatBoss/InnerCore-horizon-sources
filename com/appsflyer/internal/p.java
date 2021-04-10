package com.appsflyer.internal;

import android.content.*;
import android.location.*;
import com.appsflyer.*;

public final class p
{
    p() {
    }
    
    public static Location \u0131(final Context context) {
    Label_0100_Outer:
        while (true) {
            while (true) {
            Label_0100:
                while (true) {
                    Object lastKnownLocation = null;
                    Location lastKnownLocation2 = null;
                Label_0106:
                    while (true) {
                        Label_0120: {
                        Label_0117:
                            while (true) {
                                Label_0111: {
                                    try {
                                        lastKnownLocation = context.getSystemService("location");
                                        if (!\u01c3(context, new String[] { "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION" })) {
                                            break Label_0111;
                                        }
                                        lastKnownLocation2 = ((LocationManager)lastKnownLocation).getLastKnownLocation("network");
                                        if (\u01c3(context, new String[] { "android.permission.ACCESS_FINE_LOCATION" })) {
                                            lastKnownLocation = ((LocationManager)lastKnownLocation).getLastKnownLocation("gps");
                                            break Label_0120;
                                        }
                                        break Label_0117;
                                        // iftrue(Label_0106:, location == null)
                                        return null;
                                        location = lastKnownLocation2;
                                        continue Label_0100;
                                        final long time = lastKnownLocation2.getTime();
                                        final long time2 = ((Location)lastKnownLocation).getTime();
                                        location = (Location)lastKnownLocation;
                                        // iftrue(Label_0100:, 60000L >= time - time2)
                                        continue Label_0106;
                                    }
                                    finally {
                                        return null;
                                    }
                                }
                                lastKnownLocation2 = null;
                                continue Label_0100_Outer;
                            }
                            lastKnownLocation = null;
                        }
                        if (lastKnownLocation == null && lastKnownLocation2 == null) {
                            final Location location = null;
                            continue Label_0100;
                        }
                        if (lastKnownLocation == null && lastKnownLocation2 != null) {
                            continue Label_0106;
                        }
                        break;
                    }
                    Location location;
                    if (lastKnownLocation2 == null && (location = (Location)lastKnownLocation) != null) {
                        continue Label_0100;
                    }
                    break;
                }
                continue;
            }
        }
    }
    
    private static boolean \u01c3(final Context context, final String[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (AndroidUtils.isPermissionAvailable(context, array[i])) {
                return true;
            }
        }
        return false;
    }
    
    public static final class a
    {
        public static final p \u0399;
        
        static {
            \u0399 = new p();
        }
    }
}

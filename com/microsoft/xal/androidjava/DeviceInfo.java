package com.microsoft.xal.androidjava;

import android.content.*;
import android.provider.*;
import android.os.*;

public class DeviceInfo
{
    public static String GetDeviceId(final Context context) {
        final String string = Settings$Secure.getString(context.getContentResolver(), "android_id");
        final int length = string.length();
        int i = 0;
        String string2 = string;
        if (length < 32) {
            final StringBuilder sb = new StringBuilder();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("%0");
            sb2.append(32 - length);
            sb2.append("d");
            sb.append(String.format(sb2.toString(), 0));
            sb.append(string);
            string2 = sb.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        int n = 0;
        while (i < 5) {
            if (i != 0) {
                sb3.append("-");
            }
            final int n2 = (new int[] { 8, 4, 4, 4, 12 })[i] + n;
            sb3.append(string2.substring(n, n2));
            ++i;
            n = n2;
        }
        return sb3.toString();
    }
    
    public static String GetOsVersion() {
        return Build$VERSION.RELEASE;
    }
}

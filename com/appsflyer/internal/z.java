package com.appsflyer.internal;

import java.util.*;
import java.security.*;
import com.appsflyer.*;

public final class z
{
    z() {
    }
    
    private static String \u0131(final byte[] array) {
        final Formatter formatter = new Formatter();
        for (int length = array.length, i = 0; i < length; ++i) {
            formatter.format("%02x", array[i]);
        }
        final String string = formatter.toString();
        formatter.close();
        return string;
    }
    
    public static String \u01c3(final String s) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(s.getBytes("UTF-8"));
            return \u0131(instance.digest());
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder("Error turning ");
            sb.append(s.substring(0, 6));
            sb.append(".. to MD5");
            AFLogger.afErrorLog(sb.toString(), ex);
            return null;
        }
    }
    
    public static String \u0269(final String s) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(s.getBytes());
            final byte[] digest = instance.digest();
            final StringBuilder sb = new StringBuilder();
            for (int length = digest.length, i = 0; i < length; ++i) {
                sb.append(Integer.toString((digest[i] & 0xFF) + 256, 16).substring(1));
            }
            return sb.toString();
        }
        catch (Exception ex) {
            final StringBuilder sb2 = new StringBuilder("Error turning ");
            sb2.append(s.substring(0, 6));
            sb2.append(".. to SHA-256");
            AFLogger.afErrorLog(sb2.toString(), ex);
            return null;
        }
    }
    
    public static String \u0399(final String s) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.reset();
            instance.update(s.getBytes("UTF-8"));
            return \u0131(instance.digest());
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder("Error turning ");
            sb.append(s.substring(0, 6));
            sb.append(".. to SHA1");
            AFLogger.afErrorLog(sb.toString(), ex);
            return null;
        }
    }
    
    public static String \u03b9(final long n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(AppsFlyerProperties.getInstance().getString("AppsFlyerKey"));
        sb.append(n);
        return \u0399(sb.toString());
    }
}

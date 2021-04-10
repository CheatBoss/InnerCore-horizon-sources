package com.appsflyer;

import java.io.*;
import java.math.*;
import android.content.pm.*;
import java.security.cert.*;
import java.security.*;
import android.os.*;
import android.content.*;
import android.net.*;
import java.text.*;
import java.util.*;

public class AndroidUtils
{
    public static long getVersionCode(final Context context, final String s) {
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(s, 0);
            if (Build$VERSION.SDK_INT >= 28) {
                return packageInfo.getLongVersionCode();
            }
            return packageInfo.versionCode;
        }
        catch (PackageManager$NameNotFoundException ex) {
            AFLogger.afErrorLog(((Throwable)ex).getMessage(), (Throwable)ex);
            return 0L;
        }
    }
    
    public static String getVersionName(final Context context, final String s) {
        try {
            return context.getPackageManager().getPackageInfo(s, 0).versionName;
        }
        catch (PackageManager$NameNotFoundException ex) {
            AFLogger.afErrorLog(((Throwable)ex).getMessage(), (Throwable)ex);
            return "";
        }
    }
    
    public static boolean isPermissionAvailable(final Context context, final String s) {
        if (s != null) {
            final int checkPermission = context.checkPermission(s, Process.myPid(), Process.myUid());
            final StringBuilder sb = new StringBuilder("is Permission Available: ");
            sb.append(s);
            sb.append("; res: ");
            sb.append(checkPermission);
            AFLogger.afRDLog(sb.toString());
            return checkPermission == 0;
        }
        throw new IllegalArgumentException("permission is null");
    }
    
    public static String signature(final PackageManager packageManager, final String s) throws PackageManager$NameNotFoundException, CertificateException, NoSuchAlgorithmException {
        final Signature[] signatures = packageManager.getPackageInfo(s, 64).signatures;
        if (signatures == null) {
            return null;
        }
        final X509Certificate x509Certificate = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(signatures[0].toByteArray()));
        final MessageDigest instance = MessageDigest.getInstance("SHA256");
        instance.update(x509Certificate.getEncoded());
        return String.format("%032X", new BigInteger(1, instance.digest()));
    }
    
    static boolean \u0269() {
        return Build.BRAND.equals("OPPO");
    }
    
    public static boolean \u0269(final Context context, final Intent intent) {
        return context.getPackageManager().queryIntentServices(intent, 0).size() > 0;
    }
    
    static Map<String, String> \u03b9(final Context context, final Map<String, String> map, final Uri uri) {
        int n3;
        if (uri.getQuery() != null) {
            final String[] split = uri.getQuery().split("&");
            final int length = split.length;
            int n = 0;
            int n2 = 0;
            while (true) {
                n3 = n;
                if (n2 >= length) {
                    break;
                }
                final String s = split[n2];
                final int index = s.indexOf("=");
                String substring;
                if (index > 0) {
                    substring = s.substring(0, index);
                }
                else {
                    substring = s;
                }
                int n4 = n;
                String s2 = substring;
                if (!map.containsKey(substring)) {
                    if (substring.equals("c")) {
                        s2 = "campaign";
                    }
                    else if (substring.equals("pid")) {
                        s2 = "media_source";
                    }
                    else {
                        s2 = substring;
                        if (substring.equals("af_prt")) {
                            s2 = "agency";
                            n = 1;
                        }
                    }
                    map.put(s2, "");
                    n4 = n;
                }
                String substring2 = null;
                Label_0200: {
                    if (index > 0) {
                        final int length2 = s.length();
                        final int n5 = index + 1;
                        if (length2 > n5) {
                            substring2 = s.substring(n5);
                            break Label_0200;
                        }
                    }
                    substring2 = null;
                }
                map.put(s2, substring2);
                ++n2;
                n = n4;
            }
        }
        else {
            n3 = 0;
        }
        try {
            if (!map.containsKey("install_time")) {
                final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                final long firstInstallTime = packageInfo.firstInstallTime;
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                map.put("install_time", simpleDateFormat.format(new Date(firstInstallTime)));
            }
        }
        catch (Exception ex) {
            AFLogger.afErrorLog("Could not fetch install time. ", ex);
        }
        if (uri.getQueryParameter("af_deeplink") != null && !map.containsKey("af_status")) {
            map.put("af_status", "Non-organic");
        }
        if (n3 != 0) {
            map.remove("media_source");
        }
        final String path = uri.getPath();
        if (path != null) {
            map.put("path", path);
        }
        final String scheme = uri.getScheme();
        if (scheme != null) {
            map.put("scheme", scheme);
        }
        final String host = uri.getHost();
        if (host != null) {
            map.put("host", host);
        }
        return map;
    }
}

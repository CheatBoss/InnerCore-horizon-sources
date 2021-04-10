package com.google.android.gms.common;

import java.util.concurrent.atomic.*;
import android.content.*;
import android.util.*;
import android.content.res.*;
import android.os.*;
import com.google.android.gms.common.wrappers.*;
import java.util.*;
import android.content.pm.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.util.*;

public class GooglePlayServicesUtilLight
{
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = 12451000;
    public static boolean sIsTestMode;
    public static boolean sTestIsUserBuild;
    private static boolean zzbr;
    private static boolean zzbs;
    static final AtomicBoolean zzbt;
    private static final AtomicBoolean zzbu;
    
    static {
        zzbt = new AtomicBoolean();
        zzbu = new AtomicBoolean();
    }
    
    @Deprecated
    public static int getApkVersion(final Context context) {
        try {
            return context.getPackageManager().getPackageInfo("com.google.android.gms", 0).versionCode;
        }
        catch (PackageManager$NameNotFoundException ex) {
            Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return 0;
        }
    }
    
    @Deprecated
    public static String getErrorString(final int n) {
        return ConnectionResult.zza(n);
    }
    
    public static Context getRemoteContext(Context packageContext) {
        try {
            packageContext = packageContext.createPackageContext("com.google.android.gms", 3);
            return packageContext;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return null;
        }
    }
    
    public static Resources getRemoteResource(final Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication("com.google.android.gms");
        }
        catch (PackageManager$NameNotFoundException ex) {
            return null;
        }
    }
    
    public static boolean honorsDebugCertificates(final Context context) {
        return isTestKeysBuild(context) || !isUserBuildDevice();
    }
    
    @Deprecated
    public static int isGooglePlayServicesAvailable(final Context context, int google_PLAY_SERVICES_VERSION_CODE) {
        try {
            context.getResources().getString(R$string.common_google_play_services_unknown_issue);
        }
        finally {
            Log.e("GooglePlayServicesUtil", "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included.");
        }
        if (!"com.google.android.gms".equals(context.getPackageName()) && !GooglePlayServicesUtilLight.zzbu.get()) {
            final int googlePlayServicesVersion = MetadataValueReader.getGooglePlayServicesVersion(context);
            if (googlePlayServicesVersion == 0) {
                throw new IllegalStateException("A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
            }
            if (googlePlayServicesVersion != GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE) {
                google_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
                final StringBuilder sb = new StringBuilder(320);
                sb.append("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected ");
                sb.append(google_PLAY_SERVICES_VERSION_CODE);
                sb.append(" but found ");
                sb.append(googlePlayServicesVersion);
                sb.append(".  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
                throw new IllegalStateException(sb.toString());
            }
        }
        return zza(context, !DeviceProperties.isWearableWithoutPlayStore(context) && !DeviceProperties.isIoT(context), google_PLAY_SERVICES_VERSION_CODE);
    }
    
    @Deprecated
    public static boolean isGooglePlayServicesUid(final Context context, final int n) {
        return UidVerifier.isGooglePlayServicesUid(context, n);
    }
    
    @Deprecated
    public static boolean isPlayServicesPossiblyUpdating(final Context context, final int n) {
        return n == 18 || (n == 1 && isUninstalledAppPossiblyUpdating(context, "com.google.android.gms"));
    }
    
    public static boolean isRestrictedUserProfile(final Context context) {
        if (PlatformVersion.isAtLeastJellyBeanMR2()) {
            final Bundle applicationRestrictions = ((UserManager)context.getSystemService("user")).getApplicationRestrictions(context.getPackageName());
            if (applicationRestrictions != null && "true".equals(applicationRestrictions.getString("restricted_profile"))) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isTestKeysBuild(final Context context) {
        if (!GooglePlayServicesUtilLight.zzbs) {
            try {
                try {
                    final PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo("com.google.android.gms", 64);
                    final GoogleSignatureVerifier instance = GoogleSignatureVerifier.getInstance(context);
                    if (packageInfo != null && !instance.isGooglePublicSignedPackage(packageInfo, false) && instance.isGooglePublicSignedPackage(packageInfo, true)) {
                        GooglePlayServicesUtilLight.zzbr = true;
                    }
                    GooglePlayServicesUtilLight.zzbr = false;
                }
                finally {}
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.w("GooglePlayServicesUtil", "Cannot find Google Play services package name.", (Throwable)ex);
            }
            GooglePlayServicesUtilLight.zzbs = true;
            return GooglePlayServicesUtilLight.zzbr;
            GooglePlayServicesUtilLight.zzbs = true;
        }
        return GooglePlayServicesUtilLight.zzbr;
    }
    
    static boolean isUninstalledAppPossiblyUpdating(final Context context, final String s) {
        final boolean equals = s.equals("com.google.android.gms");
        if (PlatformVersion.isAtLeastLollipop()) {
            try {
                final Iterator<PackageInstaller$SessionInfo> iterator = context.getPackageManager().getPackageInstaller().getAllSessions().iterator();
                while (iterator.hasNext()) {
                    if (s.equals(iterator.next().getAppPackageName())) {
                        return true;
                    }
                }
            }
            catch (Exception ex) {
                return false;
            }
        }
        final PackageManager packageManager = context.getPackageManager();
        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(s, 8192);
            if (equals) {
                return applicationInfo.enabled;
            }
            return applicationInfo.enabled && !isRestrictedUserProfile(context);
        }
        catch (PackageManager$NameNotFoundException ex2) {
            return false;
        }
    }
    
    @Deprecated
    public static boolean isUserBuildDevice() {
        return DeviceProperties.isUserBuild();
    }
    
    @Deprecated
    public static boolean isUserRecoverableError(final int n) {
        return n == 1 || n == 2 || n == 3 || n == 9;
    }
    
    @Deprecated
    public static boolean uidHasPackageName(final Context context, final int n, final String s) {
        return UidVerifier.uidHasPackageName(context, n, s);
    }
    
    private static int zza(final Context context, final boolean b, final int n) {
        Preconditions.checkArgument(n >= 0);
        final PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        Label_0126: {
            if (b) {
                try {
                    packageInfo = packageManager.getPackageInfo("com.android.vending", 8256);
                }
                catch (PackageManager$NameNotFoundException ex2) {
                    final String s = "Google Play Store is missing.";
                    break Label_0126;
                }
            }
            try {
                final PackageInfo packageInfo2 = packageManager.getPackageInfo("com.google.android.gms", 64);
                final GoogleSignatureVerifier instance = GoogleSignatureVerifier.getInstance(context);
                String s;
                if (!instance.isGooglePublicSignedPackage(packageInfo2, true)) {
                    s = "Google Play services signature invalid.";
                }
                else if (b && (!instance.isGooglePublicSignedPackage(packageInfo, true) || !packageInfo.signatures[0].equals((Object)packageInfo2.signatures[0]))) {
                    s = "Google Play Store signature invalid.";
                }
                else {
                    if (GmsVersionParser.parseBuildVersion(packageInfo2.versionCode) < GmsVersionParser.parseBuildVersion(n)) {
                        final int versionCode = packageInfo2.versionCode;
                        final StringBuilder sb = new StringBuilder(77);
                        sb.append("Google Play services out of date.  Requires ");
                        sb.append(n);
                        sb.append(" but found ");
                        sb.append(versionCode);
                        Log.w("GooglePlayServicesUtil", sb.toString());
                        return 2;
                    }
                    ApplicationInfo applicationInfo;
                    if ((applicationInfo = packageInfo2.applicationInfo) == null) {
                        try {
                            applicationInfo = packageManager.getApplicationInfo("com.google.android.gms", 0);
                        }
                        catch (PackageManager$NameNotFoundException ex) {
                            Log.wtf("GooglePlayServicesUtil", "Google Play services missing when getting application info.", (Throwable)ex);
                            return 1;
                        }
                    }
                    if (!applicationInfo.enabled) {
                        return 3;
                    }
                    return 0;
                }
                Log.w("GooglePlayServicesUtil", s);
                return 9;
            }
            catch (PackageManager$NameNotFoundException ex3) {
                Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
                return 1;
            }
        }
    }
}

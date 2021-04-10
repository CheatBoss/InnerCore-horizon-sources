package com.google.android.gms.common.wrappers;

import android.content.*;
import android.content.pm.*;
import android.os.*;
import com.google.android.gms.common.util.*;
import android.app.*;

public class PackageManagerWrapper
{
    private final Context zzjp;
    
    public PackageManagerWrapper(final Context zzjp) {
        this.zzjp = zzjp;
    }
    
    public int checkCallingOrSelfPermission(final String s) {
        return this.zzjp.checkCallingOrSelfPermission(s);
    }
    
    public ApplicationInfo getApplicationInfo(final String s, final int n) throws PackageManager$NameNotFoundException {
        return this.zzjp.getPackageManager().getApplicationInfo(s, n);
    }
    
    public CharSequence getApplicationLabel(final String s) throws PackageManager$NameNotFoundException {
        return this.zzjp.getPackageManager().getApplicationLabel(this.zzjp.getPackageManager().getApplicationInfo(s, 0));
    }
    
    public PackageInfo getPackageInfo(final String s, final int n) throws PackageManager$NameNotFoundException {
        return this.zzjp.getPackageManager().getPackageInfo(s, n);
    }
    
    public String[] getPackagesForUid(final int n) {
        return this.zzjp.getPackageManager().getPackagesForUid(n);
    }
    
    public boolean isCallerInstantApp() {
        if (Binder.getCallingUid() == Process.myUid()) {
            return InstantApps.isInstantApp(this.zzjp);
        }
        if (PlatformVersion.isAtLeastO()) {
            final String nameForUid = this.zzjp.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (nameForUid != null) {
                return this.zzjp.getPackageManager().isInstantApp(nameForUid);
            }
        }
        return false;
    }
    
    public boolean uidHasPackageName(int i, final String s) {
        if (PlatformVersion.isAtLeastKitKat()) {
            try {
                ((AppOpsManager)this.zzjp.getSystemService("appops")).checkPackage(i, s);
                return true;
            }
            catch (SecurityException ex) {
                return false;
            }
        }
        final String[] packagesForUid = this.zzjp.getPackageManager().getPackagesForUid(i);
        if (s != null && packagesForUid != null) {
            for (i = 0; i < packagesForUid.length; ++i) {
                if (s.equals(packagesForUid[i])) {
                    return true;
                }
            }
        }
        return false;
    }
}

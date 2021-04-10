package com.google.android.gms.common.wrappers;

import android.content.*;

public class Wrappers
{
    private static Wrappers zzabb;
    private PackageManagerWrapper zzaba;
    
    static {
        Wrappers.zzabb = new Wrappers();
    }
    
    public Wrappers() {
        this.zzaba = null;
    }
    
    public static PackageManagerWrapper packageManager(final Context context) {
        return Wrappers.zzabb.getPackageManagerWrapper(context);
    }
    
    public PackageManagerWrapper getPackageManagerWrapper(Context applicationContext) {
        synchronized (this) {
            if (this.zzaba == null) {
                if (applicationContext.getApplicationContext() != null) {
                    applicationContext = applicationContext.getApplicationContext();
                }
                this.zzaba = new PackageManagerWrapper(applicationContext);
            }
            return this.zzaba;
        }
    }
}

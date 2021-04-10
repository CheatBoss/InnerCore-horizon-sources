package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.wrappers.*;
import com.google.android.gms.internal.measurement.*;
import android.os.*;
import com.google.android.gms.common.stats.*;
import android.content.*;
import android.content.pm.*;
import java.util.*;

public final class zzbg
{
    final zzbt zzadj;
    
    zzbg(final zzbt zzadj) {
        this.zzadj = zzadj;
    }
    
    private final boolean zzka() {
        boolean b = false;
        try {
            final PackageManagerWrapper packageManager = Wrappers.packageManager(this.zzadj.getContext());
            if (packageManager == null) {
                this.zzadj.zzgo().zzjj().zzbx("Failed to retrieve Package Manager to check Play Store compatibility");
                return false;
            }
            if (packageManager.getPackageInfo("com.android.vending", 128).versionCode >= 80837300) {
                b = true;
            }
            return b;
        }
        catch (Exception ex) {
            this.zzadj.zzgo().zzjj().zzg("Failed to retrieve Play Store version", ex);
            return false;
        }
    }
    
    final Bundle zza(final String s, final zzu zzu) {
        this.zzadj.zzgn().zzaf();
        if (zzu == null) {
            this.zzadj.zzgo().zzjg().zzbx("Attempting to use Install Referrer Service while it is not initialized");
            return null;
        }
        final Bundle bundle = new Bundle();
        bundle.putString("package_name", s);
        try {
            final Bundle zza = zzu.zza(bundle);
            if (zza == null) {
                this.zzadj.zzgo().zzjd().zzbx("Install Referrer Service returned a null response");
                return null;
            }
            return zza;
        }
        catch (Exception ex) {
            this.zzadj.zzgo().zzjd().zzg("Exception occurred while retrieving the Install Referrer", ex.getMessage());
            return null;
        }
    }
    
    protected final void zzcd(String s) {
        if (s == null || s.isEmpty()) {
            this.zzadj.zzgo().zzjj().zzbx("Install Referrer Reporter was called with invalid app package name");
            return;
        }
        this.zzadj.zzgn().zzaf();
        if (!this.zzka()) {
            this.zzadj.zzgo().zzjj().zzbx("Install Referrer Reporter is not available");
            return;
        }
        this.zzadj.zzgo().zzjj().zzbx("Install Referrer Reporter is initializing");
        final zzbh zzbh = new zzbh(this, s);
        this.zzadj.zzgn().zzaf();
        final Intent intent = new Intent("com.google.android.finsky.BIND_GET_INSTALL_REFERRER_SERVICE");
        intent.setComponent(new ComponentName("com.android.vending", "com.google.android.finsky.externalreferrer.GetInstallReferrerService"));
        final PackageManager packageManager = this.zzadj.getContext().getPackageManager();
        if (packageManager == null) {
            this.zzadj.zzgo().zzjg().zzbx("Failed to obtain Package Manager to verify binding conditions");
            return;
        }
        final List queryIntentServices = packageManager.queryIntentServices(intent, 0);
        if (queryIntentServices != null && !queryIntentServices.isEmpty()) {
            final ResolveInfo resolveInfo = queryIntentServices.get(0);
            if (resolveInfo.serviceInfo != null) {
                final String packageName = resolveInfo.serviceInfo.packageName;
                if (resolveInfo.serviceInfo.name != null && "com.android.vending".equals(packageName) && this.zzka()) {
                    final Intent intent2 = new Intent(intent);
                    try {
                        final boolean bindService = ConnectionTracker.getInstance().bindService(this.zzadj.getContext(), intent2, (ServiceConnection)zzbh, 1);
                        final zzar zzjj = this.zzadj.zzgo().zzjj();
                        if (bindService) {
                            s = "available";
                        }
                        else {
                            s = "not available";
                        }
                        zzjj.zzg("Install Referrer Service is", s);
                        return;
                    }
                    catch (Exception ex) {
                        this.zzadj.zzgo().zzjd().zzg("Exception occurred while binding to Install Referrer Service", ex.getMessage());
                        return;
                    }
                }
                this.zzadj.zzgo().zzjj().zzbx("Play Store missing or incompatible. Version 8.3.73 or later required");
            }
            return;
        }
        this.zzadj.zzgo().zzjj().zzbx("Play Service for fetching Install Referrer is unavailable on device");
    }
}

package com.google.firebase.iid;

import com.google.firebase.*;
import android.util.*;
import java.security.*;
import com.google.android.gms.common.util.*;
import android.content.*;
import android.content.pm.*;
import java.util.*;

public final class zzam
{
    private String zzcf;
    private String zzcg;
    private int zzch;
    private int zzci;
    private final Context zzv;
    
    public zzam(final Context zzv) {
        this.zzci = 0;
        this.zzv = zzv;
    }
    
    public static String zza(final FirebaseApp firebaseApp) {
        final String gcmSenderId = firebaseApp.getOptions().getGcmSenderId();
        if (gcmSenderId != null) {
            return gcmSenderId;
        }
        final String applicationId = firebaseApp.getOptions().getApplicationId();
        if (!applicationId.startsWith("1:")) {
            return applicationId;
        }
        final String[] split = applicationId.split(":");
        if (split.length < 2) {
            return null;
        }
        final String s = split[1];
        if (s.isEmpty()) {
            return null;
        }
        return s;
    }
    
    public static String zza(final KeyPair keyPair) {
        final byte[] encoded = keyPair.getPublic().getEncoded();
        try {
            final byte[] digest = MessageDigest.getInstance("SHA1").digest(encoded);
            digest[0] = (byte)((digest[0] & 0xF) + 112);
            return Base64.encodeToString(digest, 0, 8, 11);
        }
        catch (NoSuchAlgorithmException ex) {
            Log.w("FirebaseInstanceId", "Unexpected error, device missing required algorithms");
            return null;
        }
    }
    
    private final void zzaf() {
        synchronized (this) {
            final PackageInfo zze = this.zze(this.zzv.getPackageName());
            if (zze != null) {
                this.zzcf = Integer.toString(zze.versionCode);
                this.zzcg = zze.versionName;
            }
        }
    }
    
    private final PackageInfo zze(String value) {
        try {
            return this.zzv.getPackageManager().getPackageInfo(value, 0);
        }
        catch (PackageManager$NameNotFoundException ex) {
            value = String.valueOf(ex);
            final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 23);
            sb.append("Failed to find package ");
            sb.append(value);
            Log.w("FirebaseInstanceId", sb.toString());
            return null;
        }
    }
    
    public final int zzab() {
        synchronized (this) {
            if (this.zzci != 0) {
                return this.zzci;
            }
            final PackageManager packageManager = this.zzv.getPackageManager();
            if (packageManager.checkPermission("com.google.android.c2dm.permission.SEND", "com.google.android.gms") == -1) {
                Log.e("FirebaseInstanceId", "Google Play services missing or without correct permission.");
                return 0;
            }
            if (!PlatformVersion.isAtLeastO()) {
                final Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
                intent.setPackage("com.google.android.gms");
                final List queryIntentServices = packageManager.queryIntentServices(intent, 0);
                if (queryIntentServices != null && queryIntentServices.size() > 0) {
                    return this.zzci = 1;
                }
            }
            final Intent intent2 = new Intent("com.google.iid.TOKEN_REQUEST");
            intent2.setPackage("com.google.android.gms");
            final List queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent2, 0);
            if (queryBroadcastReceivers != null && queryBroadcastReceivers.size() > 0) {
                return this.zzci = 2;
            }
            Log.w("FirebaseInstanceId", "Failed to resolve IID implementation package, falling back");
            if (PlatformVersion.isAtLeastO()) {
                this.zzci = 2;
            }
            else {
                this.zzci = 1;
            }
            return this.zzci;
        }
    }
    
    public final String zzac() {
        synchronized (this) {
            if (this.zzcf == null) {
                this.zzaf();
            }
            return this.zzcf;
        }
    }
    
    public final String zzad() {
        synchronized (this) {
            if (this.zzcg == null) {
                this.zzaf();
            }
            return this.zzcg;
        }
    }
    
    public final int zzae() {
        synchronized (this) {
            if (this.zzch == 0) {
                final PackageInfo zze = this.zze("com.google.android.gms");
                if (zze != null) {
                    this.zzch = zze.versionCode;
                }
            }
            return this.zzch;
        }
    }
}

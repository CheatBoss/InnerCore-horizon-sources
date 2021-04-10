package com.google.firebase.iid;

import android.os.*;
import android.util.*;
import java.io.*;
import android.content.*;
import android.net.*;

final class zzax implements Runnable
{
    private final zzam zzak;
    private final zzaz zzan;
    private final long zzde;
    private final PowerManager$WakeLock zzdf;
    private final FirebaseInstanceId zzdg;
    
    zzax(final FirebaseInstanceId zzdg, final zzam zzak, final zzaz zzan, final long zzde) {
        this.zzdg = zzdg;
        this.zzak = zzak;
        this.zzan = zzan;
        this.zzde = zzde;
        (this.zzdf = ((PowerManager)this.getContext().getSystemService("power")).newWakeLock(1, "fiid-sync")).setReferenceCounted(false);
    }
    
    private final boolean zzal() {
        try {
            if (!this.zzdg.zzn()) {
                this.zzdg.zzo();
            }
            return true;
        }
        catch (IOException ex) {
            final String value = String.valueOf(ex.getMessage());
            String concat;
            if (value.length() != 0) {
                concat = "Build channel failed: ".concat(value);
            }
            else {
                concat = new String("Build channel failed: ");
            }
            Log.e("FirebaseInstanceId", concat);
            return false;
        }
    }
    
    private final boolean zzam() {
        final zzaw zzi = this.zzdg.zzi();
        if (zzi != null && !zzi.zzj(this.zzak.zzac())) {
            return true;
        }
        try {
            final String zzj = this.zzdg.zzj();
            if (zzj == null) {
                Log.e("FirebaseInstanceId", "Token retrieval failed: null");
                return false;
            }
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                Log.d("FirebaseInstanceId", "Token successfully retrieved");
            }
            if (zzi == null || (zzi != null && !zzj.equals(zzi.zzbn))) {
                final Context context = this.getContext();
                final Intent intent = new Intent("com.google.firebase.messaging.NEW_TOKEN");
                intent.putExtra("token", zzj);
                zzau.zzc(context, intent);
                zzau.zzb(context, new Intent("com.google.firebase.iid.TOKEN_REFRESH"));
            }
            return true;
        }
        catch (IOException | SecurityException ex) {
            final Throwable t;
            final String value = String.valueOf(t.getMessage());
            String concat;
            if (value.length() != 0) {
                concat = "Token retrieval failed: ".concat(value);
            }
            else {
                concat = new String("Token retrieval failed: ");
            }
            Log.e("FirebaseInstanceId", concat);
            return false;
        }
    }
    
    final Context getContext() {
        return this.zzdg.zzg().getApplicationContext();
    }
    
    @Override
    public final void run() {
        this.zzdf.acquire();
        try {
            this.zzdg.zza(true);
            FirebaseInstanceId firebaseInstanceId;
            if (!this.zzdg.zzm()) {
                firebaseInstanceId = this.zzdg;
            }
            else {
                if (!this.zzan()) {
                    new zzay(this).zzao();
                    return;
                }
                if (!this.zzal() || !this.zzam() || !this.zzan.zzc(this.zzdg)) {
                    this.zzdg.zza(this.zzde);
                    return;
                }
                firebaseInstanceId = this.zzdg;
            }
            firebaseInstanceId.zza(false);
        }
        finally {
            this.zzdf.release();
        }
    }
    
    final boolean zzan() {
        final ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        else {
            activeNetworkInfo = null;
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

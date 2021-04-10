package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.content.pm.*;
import android.net.*;
import android.content.*;
import android.os.*;

public final class zzbj
{
    private final zzbm zzaoi;
    
    public zzbj(final zzbm zzaoi) {
        Preconditions.checkNotNull(zzaoi);
        this.zzaoi = zzaoi;
    }
    
    public static boolean zza(final Context context) {
        Preconditions.checkNotNull(context);
        try {
            final PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            final ActivityInfo receiverInfo = packageManager.getReceiverInfo(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementReceiver"), 0);
            return receiverInfo != null && receiverInfo.enabled;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return false;
        }
    }
    
    public final void onReceive(final Context context, Intent setClassName) {
        final zzbt zza = zzbt.zza(context, null);
        final zzap zzgo = zza.zzgo();
        if (setClassName == null) {
            zzgo.zzjg().zzbx("Receiver called with null intent");
            return;
        }
        zza.zzgr();
        final String action = setClassName.getAction();
        zzgo.zzjl().zzg("Local receiver got", action);
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            setClassName = new Intent().setClassName(context, "com.google.android.gms.measurement.AppMeasurementService");
            setClassName.setAction("com.google.android.gms.measurement.UPLOAD");
            zzgo.zzjl().zzbx("Starting wakeful intent.");
            this.zzaoi.doStartService(context, setClassName);
            return;
        }
        if ("com.android.vending.INSTALL_REFERRER".equals(action)) {
            try {
                zza.zzgn().zzc(new zzbk(this, zza, zzgo));
            }
            catch (Exception ex) {
                zzgo.zzjg().zzg("Install Referrer Reporter encountered a problem", ex);
            }
            final BroadcastReceiver$PendingResult doGoAsync = this.zzaoi.doGoAsync();
            final String stringExtra = setClassName.getStringExtra("referrer");
            if (stringExtra == null) {
                zzgo.zzjl().zzbx("Install referrer extras are null");
                if (doGoAsync != null) {
                    doGoAsync.finish();
                }
                return;
            }
            zzgo.zzjj().zzg("Install referrer extras are", stringExtra);
            String concat = stringExtra;
            if (!stringExtra.contains("?")) {
                final String value = String.valueOf(stringExtra);
                if (value.length() != 0) {
                    concat = "?".concat(value);
                }
                else {
                    concat = new String("?");
                }
            }
            final Bundle zza2 = zza.zzgm().zza(Uri.parse(concat));
            if (zza2 == null) {
                zzgo.zzjl().zzbx("No campaign defined in install referrer broadcast");
                if (doGoAsync != null) {
                    doGoAsync.finish();
                }
            }
            else {
                final long n = setClassName.getLongExtra("referrer_timestamp_seconds", 0L) * 1000L;
                if (n == 0L) {
                    zzgo.zzjg().zzbx("Install referrer is missing timestamp");
                }
                zza.zzgn().zzc(new zzbl(this, zza, n, zza2, context, zzgo, doGoAsync));
            }
        }
    }
}

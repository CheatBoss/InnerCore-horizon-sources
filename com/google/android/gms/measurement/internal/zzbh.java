package com.google.android.gms.measurement.internal;

import android.content.*;
import android.os.*;
import com.google.android.gms.internal.measurement.*;

public final class zzbh implements ServiceConnection
{
    private final String packageName;
    final /* synthetic */ zzbg zzaoe;
    
    zzbh(final zzbg zzaoe, final String packageName) {
        this.zzaoe = zzaoe;
        this.packageName = packageName;
    }
    
    public final void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        if (binder == null) {
            this.zzaoe.zzadj.zzgo().zzjg().zzbx("Install Referrer connection returned with null binder");
            return;
        }
        try {
            final zzu zza = zzv.zza(binder);
            if (zza == null) {
                this.zzaoe.zzadj.zzgo().zzjg().zzbx("Install Referrer Service implementation was not found");
                return;
            }
            this.zzaoe.zzadj.zzgo().zzjj().zzbx("Install Referrer Service connected");
            this.zzaoe.zzadj.zzgn().zzc(new zzbi(this, zza, (ServiceConnection)this));
        }
        catch (Exception ex) {
            this.zzaoe.zzadj.zzgo().zzjg().zzg("Exception occurred while calling Install Referrer API", ex);
        }
    }
    
    public final void onServiceDisconnected(final ComponentName componentName) {
        this.zzaoe.zzadj.zzgo().zzjj().zzbx("Install Referrer Service disconnected");
    }
}

package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.*;
import android.content.*;
import android.net.*;
import com.google.android.gms.common.stats.*;
import android.os.*;

final class zzbi implements Runnable
{
    private final /* synthetic */ zzu zzaof;
    private final /* synthetic */ ServiceConnection zzaog;
    private final /* synthetic */ zzbh zzaoh;
    
    zzbi(final zzbh zzaoh, final zzu zzaof, final ServiceConnection zzaog) {
        this.zzaoh = zzaoh;
        this.zzaof = zzaof;
        this.zzaog = zzaog;
    }
    
    @Override
    public final void run() {
        final zzbg zzaoe = this.zzaoh.zzaoe;
        final String zza = this.zzaoh.packageName;
        final zzu zzaof = this.zzaof;
        final ServiceConnection zzaog = this.zzaog;
        final Bundle zza2 = zzaoe.zza(zza, zzaof);
        zzaoe.zzadj.zzgn().zzaf();
        Label_0465: {
            if (zza2 != null) {
                final long n = zza2.getLong("install_begin_timestamp_seconds", 0L) * 1000L;
                zzar zzar = null;
                String s = null;
                Label_0093: {
                    if (n == 0L) {
                        zzar = zzaoe.zzadj.zzgo().zzjd();
                        s = "Service response is missing Install Referrer install timestamp";
                    }
                    else {
                        final String string = zza2.getString("install_referrer");
                        if (string != null && !string.isEmpty()) {
                            zzaoe.zzadj.zzgo().zzjl().zzg("InstallReferrer API result", string);
                            final zzfk zzgm = zzaoe.zzadj.zzgm();
                            final String value = String.valueOf(string);
                            String concat;
                            if (value.length() != 0) {
                                concat = "?".concat(value);
                            }
                            else {
                                concat = new String("?");
                            }
                            final Bundle zza3 = zzgm.zza(Uri.parse(concat));
                            if (zza3 == null) {
                                zzar = zzaoe.zzadj.zzgo().zzjd();
                                s = "No campaign params defined in install referrer result";
                            }
                            else {
                                final String string2 = zza3.getString("medium");
                                if (string2 != null && !"(not set)".equalsIgnoreCase(string2) && !"organic".equalsIgnoreCase(string2)) {
                                    final long n2 = zza2.getLong("referrer_click_timestamp_seconds", 0L) * 1000L;
                                    if (n2 == 0L) {
                                        zzar = zzaoe.zzadj.zzgo().zzjd();
                                        s = "Install Referrer is missing click timestamp for ad campaign";
                                        break Label_0093;
                                    }
                                    zza3.putLong("click_timestamp", n2);
                                }
                                if (n != zzaoe.zzadj.zzgp().zzank.get()) {
                                    zzaoe.zzadj.zzgp().zzank.set(n);
                                    zzaoe.zzadj.zzgr();
                                    zzaoe.zzadj.zzgo().zzjl().zzg("Logging Install Referrer campaign from sdk with ", "referrer API");
                                    zza3.putString("_cis", "referrer API");
                                    zzaoe.zzadj.zzge().logEvent("auto", "_cmp", zza3);
                                    break Label_0465;
                                }
                                zzaoe.zzadj.zzgr();
                                zzar = zzaoe.zzadj.zzgo().zzjl();
                                s = "Campaign has already been logged";
                            }
                        }
                        else {
                            zzar = zzaoe.zzadj.zzgo().zzjd();
                            s = "No referrer defined in install referrer response";
                        }
                    }
                }
                zzar.zzbx(s);
            }
        }
        if (zzaog != null) {
            ConnectionTracker.getInstance().unbindService(zzaoe.zzadj.getContext(), zzaog);
        }
    }
}

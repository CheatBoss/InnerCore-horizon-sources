package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.*;
import com.google.android.gms.common.internal.*;
import android.content.*;
import com.google.android.gms.measurement.*;
import com.google.android.gms.common.util.*;
import android.os.*;
import java.util.*;

public final class zzan extends zzcp
{
    private static final AtomicReference<String[]> zzalt;
    private static final AtomicReference<String[]> zzalu;
    private static final AtomicReference<String[]> zzalv;
    
    static {
        zzalt = new AtomicReference<String[]>();
        zzalu = new AtomicReference<String[]>();
        zzalv = new AtomicReference<String[]>();
    }
    
    zzan(final zzbt zzbt) {
        super(zzbt);
    }
    
    private static String zza(String s, final String[] array, final String[] array2, final AtomicReference<String[]> atomicReference) {
        Preconditions.checkNotNull(array);
        Preconditions.checkNotNull(array2);
        Preconditions.checkNotNull(atomicReference);
        final int length = array.length;
        final int length2 = array2.length;
        int i = 0;
        Preconditions.checkArgument(length == length2);
        while (i < array.length) {
            if (zzfk.zzu(s, array[i])) {
                synchronized (atomicReference) {
                    String[] array3;
                    if ((array3 = atomicReference.get()) == null) {
                        array3 = new String[array2.length];
                        atomicReference.set(array3);
                    }
                    if (array3[i] == null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(array2[i]);
                        sb.append("(");
                        sb.append(array[i]);
                        sb.append(")");
                        array3[i] = sb.toString();
                    }
                    s = array3[i];
                    return s;
                }
            }
            ++i;
        }
        return s;
    }
    
    private final String zzb(final zzaa zzaa) {
        if (zzaa == null) {
            return null;
        }
        if (!this.zzjc()) {
            return zzaa.toString();
        }
        return this.zzd(zzaa.zziv());
    }
    
    private final boolean zzjc() {
        this.zzgr();
        return this.zzadj.zzkj() && this.zzadj.zzgo().isLoggable(3);
    }
    
    protected final String zza(final zzy zzy) {
        if (zzy == null) {
            return null;
        }
        if (!this.zzjc()) {
            return zzy.toString();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Event{appId='");
        sb.append(zzy.zztt);
        sb.append("', name='");
        sb.append(this.zzbs(zzy.name));
        sb.append("', params=");
        sb.append(this.zzb(zzy.zzaid));
        sb.append("}");
        return sb.toString();
    }
    
    protected final String zzb(final zzad zzad) {
        if (zzad == null) {
            return null;
        }
        if (!this.zzjc()) {
            return zzad.toString();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("origin=");
        sb.append(zzad.origin);
        sb.append(",name=");
        sb.append(this.zzbs(zzad.name));
        sb.append(",params=");
        sb.append(this.zzb(zzad.zzaid));
        return sb.toString();
    }
    
    protected final String zzbs(final String s) {
        if (s == null) {
            return null;
        }
        if (!this.zzjc()) {
            return s;
        }
        return zza(s, AppMeasurement.Event.zzadl, AppMeasurement.Event.zzadk, zzan.zzalt);
    }
    
    protected final String zzbt(final String s) {
        if (s == null) {
            return null;
        }
        if (!this.zzjc()) {
            return s;
        }
        return zza(s, AppMeasurement.Param.zzadn, AppMeasurement.Param.zzadm, zzan.zzalu);
    }
    
    protected final String zzbu(final String s) {
        if (s == null) {
            return null;
        }
        if (!this.zzjc()) {
            return s;
        }
        if (s.startsWith("_exp_")) {
            final StringBuilder sb = new StringBuilder();
            sb.append("experiment_id");
            sb.append("(");
            sb.append(s);
            sb.append(")");
            return sb.toString();
        }
        return zza(s, AppMeasurement.UserProperty.zzadp, AppMeasurement.UserProperty.zzado, zzan.zzalv);
    }
    
    protected final String zzd(final Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (!this.zzjc()) {
            return bundle.toString();
        }
        final StringBuilder sb = new StringBuilder();
        for (final String s : bundle.keySet()) {
            String s2;
            if (sb.length() != 0) {
                s2 = ", ";
            }
            else {
                s2 = "Bundle[{";
            }
            sb.append(s2);
            sb.append(this.zzbt(s));
            sb.append("=");
            sb.append(bundle.get(s));
        }
        sb.append("}]");
        return sb.toString();
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
}

package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.content.*;
import android.util.*;

public final class zzbe
{
    private final long zzabv;
    private final /* synthetic */ zzba zzany;
    private final String zzaoa;
    private final String zzaob;
    private final String zzaoc;
    
    private zzbe(final zzba zzany, final String s, final long zzabv) {
        this.zzany = zzany;
        Preconditions.checkNotEmpty(s);
        Preconditions.checkArgument(zzabv > 0L);
        this.zzaoa = String.valueOf(s).concat(":start");
        this.zzaob = String.valueOf(s).concat(":count");
        this.zzaoc = String.valueOf(s).concat(":value");
        this.zzabv = zzabv;
    }
    
    private final void zzfl() {
        this.zzany.zzaf();
        final long currentTimeMillis = this.zzany.zzbx().currentTimeMillis();
        final SharedPreferences$Editor edit = this.zzany.zzjr().edit();
        edit.remove(this.zzaob);
        edit.remove(this.zzaoc);
        edit.putLong(this.zzaoa, currentTimeMillis);
        edit.apply();
    }
    
    private final long zzfn() {
        return this.zzany.zzjr().getLong(this.zzaoa, 0L);
    }
    
    public final void zzc(final String s, long nextLong) {
        this.zzany.zzaf();
        if (this.zzfn() == 0L) {
            this.zzfl();
        }
        String s2;
        if ((s2 = s) == null) {
            s2 = "";
        }
        final long long1 = this.zzany.zzjr().getLong(this.zzaob, 0L);
        if (long1 <= 0L) {
            final SharedPreferences$Editor edit = this.zzany.zzjr().edit();
            edit.putString(this.zzaoc, s2);
            edit.putLong(this.zzaob, 1L);
            edit.apply();
            return;
        }
        nextLong = this.zzany.zzgm().zzmd().nextLong();
        final long n = long1 + 1L;
        final boolean b = (nextLong & Long.MAX_VALUE) < Long.MAX_VALUE / n;
        final SharedPreferences$Editor edit2 = this.zzany.zzjr().edit();
        if (b) {
            edit2.putString(this.zzaoc, s2);
        }
        edit2.putLong(this.zzaob, n);
        edit2.apply();
    }
    
    public final Pair<String, Long> zzfm() {
        this.zzany.zzaf();
        this.zzany.zzaf();
        final long zzfn = this.zzfn();
        long abs;
        if (zzfn == 0L) {
            this.zzfl();
            abs = 0L;
        }
        else {
            abs = Math.abs(zzfn - this.zzany.zzbx().currentTimeMillis());
        }
        final long zzabv = this.zzabv;
        if (abs < zzabv) {
            return null;
        }
        if (abs > zzabv << 1) {
            this.zzfl();
            return null;
        }
        final String string = this.zzany.zzjr().getString(this.zzaoc, (String)null);
        final long long1 = this.zzany.zzjr().getLong(this.zzaob, 0L);
        this.zzfl();
        if (string != null && long1 > 0L) {
            return (Pair<String, Long>)new Pair((Object)string, (Object)long1);
        }
        return zzba.zzanc;
    }
}

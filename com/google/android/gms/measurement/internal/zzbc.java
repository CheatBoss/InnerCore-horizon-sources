package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.content.*;

public final class zzbc
{
    private boolean value;
    private final boolean zzanw;
    private boolean zzanx;
    private final /* synthetic */ zzba zzany;
    private final String zzoj;
    
    public zzbc(final zzba zzany, final String zzoj, final boolean b) {
        this.zzany = zzany;
        Preconditions.checkNotEmpty(zzoj);
        this.zzoj = zzoj;
        this.zzanw = true;
    }
    
    public final boolean get() {
        if (!this.zzanx) {
            this.zzanx = true;
            this.value = this.zzany.zzjr().getBoolean(this.zzoj, this.zzanw);
        }
        return this.value;
    }
    
    public final void set(final boolean value) {
        final SharedPreferences$Editor edit = this.zzany.zzjr().edit();
        edit.putBoolean(this.zzoj, value);
        edit.apply();
        this.value = value;
    }
}

package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.content.*;

public final class zzbf
{
    private String value;
    private boolean zzanx;
    private final /* synthetic */ zzba zzany;
    private final String zzaod;
    private final String zzoj;
    
    public zzbf(final zzba zzany, final String zzoj, final String s) {
        this.zzany = zzany;
        Preconditions.checkNotEmpty(zzoj);
        this.zzoj = zzoj;
        this.zzaod = null;
    }
    
    public final void zzcc(final String value) {
        if (zzfk.zzu(value, this.value)) {
            return;
        }
        final SharedPreferences$Editor edit = this.zzany.zzjr().edit();
        edit.putString(this.zzoj, value);
        edit.apply();
        this.value = value;
    }
    
    public final String zzjz() {
        if (!this.zzanx) {
            this.zzanx = true;
            this.value = this.zzany.zzjr().getString(this.zzoj, (String)null);
        }
        return this.value;
    }
}

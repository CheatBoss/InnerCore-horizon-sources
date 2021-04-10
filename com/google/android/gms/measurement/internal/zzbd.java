package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.content.*;

public final class zzbd
{
    private long value;
    private boolean zzanx;
    private final /* synthetic */ zzba zzany;
    private final long zzanz;
    private final String zzoj;
    
    public zzbd(final zzba zzany, final String zzoj, final long zzanz) {
        this.zzany = zzany;
        Preconditions.checkNotEmpty(zzoj);
        this.zzoj = zzoj;
        this.zzanz = zzanz;
    }
    
    public final long get() {
        if (!this.zzanx) {
            this.zzanx = true;
            this.value = this.zzany.zzjr().getLong(this.zzoj, this.zzanz);
        }
        return this.value;
    }
    
    public final void set(final long value) {
        final SharedPreferences$Editor edit = this.zzany.zzjr().edit();
        edit.putLong(this.zzoj, value);
        edit.apply();
        this.value = value;
    }
}

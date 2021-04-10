package com.google.android.gms.measurement.internal;

import android.content.*;
import com.google.android.gms.common.internal.*;

public final class zzcr
{
    boolean zzadv;
    String zzadx;
    String zzapm;
    String zzapn;
    Boolean zzaqg;
    final Context zzri;
    
    public zzcr(Context applicationContext, final zzak zzak) {
        this.zzadv = true;
        Preconditions.checkNotNull(applicationContext);
        applicationContext = applicationContext.getApplicationContext();
        Preconditions.checkNotNull(applicationContext);
        this.zzri = applicationContext;
        if (zzak != null) {
            this.zzadx = zzak.zzadx;
            this.zzapm = zzak.origin;
            this.zzapn = zzak.zzadw;
            this.zzadv = zzak.zzadv;
            if (zzak.zzady != null) {
                this.zzaqg = zzak.zzady.getBoolean("dataCollectionDefaultEnabled", true);
            }
        }
    }
}

package com.google.android.gms.common;

import javax.annotation.*;
import android.util.*;

@CheckReturnValue
class zzg
{
    private static final zzg zzbk;
    private final Throwable cause;
    final boolean zzbl;
    private final String zzbm;
    
    static {
        zzbk = new zzg(true, null, null);
    }
    
    zzg(final boolean zzbl, @Nullable final String zzbm, @Nullable final Throwable cause) {
        this.zzbl = zzbl;
        this.zzbm = zzbm;
        this.cause = cause;
    }
    
    static zzg zza(final String s, final GoogleCertificates.CertData certData, final boolean b, final boolean b2) {
        return new zzi(s, certData, b, b2, null);
    }
    
    static zzg zza(final String s, final Throwable t) {
        return new zzg(false, s, t);
    }
    
    static zzg zze(final String s) {
        return new zzg(false, s, null);
    }
    
    static zzg zzg() {
        return zzg.zzbk;
    }
    
    @Nullable
    String getErrorMessage() {
        return this.zzbm;
    }
    
    final void zzi() {
        if (!this.zzbl) {
            if (this.cause != null) {
                Log.d("GoogleCertificatesRslt", this.getErrorMessage(), this.cause);
                return;
            }
            Log.d("GoogleCertificatesRslt", this.getErrorMessage());
        }
    }
}

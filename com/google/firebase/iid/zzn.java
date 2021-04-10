package com.google.firebase.iid;

import com.google.android.gms.tasks.*;

final class zzn implements zzar
{
    private final FirebaseInstanceId zzaq;
    private final String zzar;
    private final String zzas;
    private final String zzau;
    private final String zzav;
    
    zzn(final FirebaseInstanceId zzaq, final String zzar, final String zzas, final String zzav, final String zzau) {
        this.zzaq = zzaq;
        this.zzar = zzar;
        this.zzas = zzas;
        this.zzav = zzav;
        this.zzau = zzau;
    }
    
    @Override
    public final Task zzr() {
        return this.zzaq.zza(this.zzar, this.zzas, this.zzav, this.zzau);
    }
}

package com.google.firebase.iid;

import com.google.android.gms.tasks.*;

final class zzo implements OnCompleteListener
{
    private final FirebaseInstanceId zzaq;
    private final String zzar;
    private final String zzas;
    private final TaskCompletionSource zzat;
    private final String zzau;
    
    zzo(final FirebaseInstanceId zzaq, final String zzar, final String zzas, final TaskCompletionSource zzat, final String zzau) {
        this.zzaq = zzaq;
        this.zzar = zzar;
        this.zzas = zzas;
        this.zzat = zzat;
        this.zzau = zzau;
    }
    
    @Override
    public final void onComplete(final Task task) {
        this.zzaq.zza(this.zzar, this.zzas, this.zzat, this.zzau, task);
    }
}

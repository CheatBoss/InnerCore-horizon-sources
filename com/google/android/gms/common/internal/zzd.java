package com.google.android.gms.common.internal;

import android.content.*;
import com.google.android.gms.common.api.internal.*;

final class zzd extends DialogRedirect
{
    private final /* synthetic */ int val$requestCode;
    private final /* synthetic */ Intent zzsh;
    private final /* synthetic */ LifecycleFragment zzsi;
    
    zzd(final Intent zzsh, final LifecycleFragment zzsi, final int val$requestCode) {
        this.zzsh = zzsh;
        this.zzsi = zzsi;
        this.val$requestCode = val$requestCode;
    }
    
    public final void redirect() {
        final Intent zzsh = this.zzsh;
        if (zzsh != null) {
            this.zzsi.startActivityForResult(zzsh, this.val$requestCode);
        }
    }
}

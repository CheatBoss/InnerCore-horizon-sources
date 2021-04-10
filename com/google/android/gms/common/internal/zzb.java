package com.google.android.gms.common.internal;

import android.app.*;
import android.content.*;

final class zzb extends DialogRedirect
{
    private final /* synthetic */ Activity val$activity;
    private final /* synthetic */ int val$requestCode;
    private final /* synthetic */ Intent zzsh;
    
    zzb(final Intent zzsh, final Activity val$activity, final int val$requestCode) {
        this.zzsh = zzsh;
        this.val$activity = val$activity;
        this.val$requestCode = val$requestCode;
    }
    
    public final void redirect() {
        final Intent zzsh = this.zzsh;
        if (zzsh != null) {
            this.val$activity.startActivityForResult(zzsh, this.val$requestCode);
        }
    }
}

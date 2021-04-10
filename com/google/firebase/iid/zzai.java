package com.google.firebase.iid;

import android.os.*;

final class zzai extends zzaj<Void>
{
    zzai(final int n, final int n2, final Bundle bundle) {
        super(n, 2, bundle);
    }
    
    @Override
    final boolean zzaa() {
        return true;
    }
    
    @Override
    final void zzb(final Bundle bundle) {
        if (bundle.getBoolean("ack", false)) {
            this.finish(null);
            return;
        }
        this.zza(new zzak(4, "Invalid response to one way request"));
    }
}

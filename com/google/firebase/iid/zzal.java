package com.google.firebase.iid;

import android.os.*;

final class zzal extends zzaj<Bundle>
{
    zzal(final int n, final int n2, final Bundle bundle) {
        super(n, 1, bundle);
    }
    
    @Override
    final boolean zzaa() {
        return false;
    }
    
    @Override
    final void zzb(Bundle bundle) {
        if ((bundle = bundle.getBundle("data")) == null) {
            bundle = Bundle.EMPTY;
        }
        this.finish(bundle);
    }
}

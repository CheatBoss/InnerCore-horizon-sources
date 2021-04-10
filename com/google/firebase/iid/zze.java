package com.google.firebase.iid;

import android.content.*;
import android.util.*;

final class zze implements Runnable
{
    private final /* synthetic */ Intent zzl;
    private final /* synthetic */ zzd zzr;
    
    zze(final zzd zzr, final Intent zzl) {
        this.zzr = zzr;
        this.zzl = zzl;
    }
    
    @Override
    public final void run() {
        final String action = this.zzl.getAction();
        final StringBuilder sb = new StringBuilder(String.valueOf(action).length() + 61);
        sb.append("Service took too long to process intent: ");
        sb.append(action);
        sb.append(" App may get closed.");
        Log.w("EnhancedIntentService", sb.toString());
        this.zzr.finish();
    }
}

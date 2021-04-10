package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.content.*;

class zzay extends BroadcastReceiver
{
    private static final String zzabi;
    private boolean zzabj;
    private boolean zzabk;
    private final zzfa zzamz;
    
    static {
        zzabi = zzay.class.getName();
    }
    
    zzay(final zzfa zzamz) {
        Preconditions.checkNotNull(zzamz);
        this.zzamz = zzamz;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        this.zzamz.zzlr();
        final String action = intent.getAction();
        this.zzamz.zzgo().zzjl().zzg("NetworkBroadcastReceiver received action", action);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            final boolean zzfb = this.zzamz.zzlo().zzfb();
            if (this.zzabk != zzfb) {
                this.zzabk = zzfb;
                this.zzamz.zzgn().zzc(new zzaz(this, zzfb));
            }
            return;
        }
        this.zzamz.zzgo().zzjg().zzg("NetworkBroadcastReceiver received unknown action", action);
    }
    
    public final void unregister() {
        this.zzamz.zzlr();
        this.zzamz.zzgn().zzaf();
        this.zzamz.zzgn().zzaf();
        if (!this.zzabj) {
            return;
        }
        this.zzamz.zzgo().zzjl().zzbx("Unregistering connectivity change receiver");
        this.zzabj = false;
        this.zzabk = false;
        final Context context = this.zzamz.getContext();
        try {
            context.unregisterReceiver((BroadcastReceiver)this);
        }
        catch (IllegalArgumentException ex) {
            this.zzamz.zzgo().zzjd().zzg("Failed to unregister the network broadcast receiver", ex);
        }
    }
    
    public final void zzey() {
        this.zzamz.zzlr();
        this.zzamz.zzgn().zzaf();
        if (this.zzabj) {
            return;
        }
        this.zzamz.getContext().registerReceiver((BroadcastReceiver)this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.zzabk = this.zzamz.zzlo().zzfb();
        this.zzamz.zzgo().zzjl().zzg("Registering connectivity change receiver. Network connected", this.zzabk);
        this.zzabj = true;
    }
}

package com.google.firebase.iid;

import com.google.firebase.events.*;

final class zzp implements EventHandler
{
    private final FirebaseInstanceId.zza zzbb;
    
    zzp(final FirebaseInstanceId.zza zzbb) {
        this.zzbb = zzbb;
    }
    
    @Override
    public final void handle(Event zzbb) {
        zzbb = this.zzbb;
        synchronized (zzbb) {
            if (zzbb.isEnabled()) {
                FirebaseInstanceId.this.zzf();
            }
        }
    }
}

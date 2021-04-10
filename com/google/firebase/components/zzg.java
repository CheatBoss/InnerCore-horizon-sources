package com.google.firebase.components;

import java.util.*;
import com.google.firebase.events.*;

final class zzg implements Runnable
{
    private final Map.Entry zza;
    private final Event zzb;
    
    zzg(final Map.Entry zza, final Event zzb) {
        this.zza = zza;
        this.zzb = zzb;
    }
    
    @Override
    public final void run() {
        zzf.zza(this.zza, this.zzb);
    }
}

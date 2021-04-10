package com.google.firebase.components;

import com.google.firebase.inject.*;

final class zzi implements Provider
{
    private final ComponentFactory zza;
    private final ComponentContainer zzb;
    
    zzi(final ComponentFactory zza, final ComponentContainer zzb) {
        this.zza = zza;
        this.zzb = zzb;
    }
    
    @Override
    public final Object get() {
        return this.zza.create(this.zzb);
    }
}

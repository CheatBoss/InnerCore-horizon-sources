package com.google.firebase.components;

final class zzb implements ComponentFactory
{
    private final Object zza;
    
    zzb(final Object zza) {
        this.zza = zza;
    }
    
    @Override
    public final Object create(final ComponentContainer componentContainer) {
        return Component.zza(this.zza);
    }
}

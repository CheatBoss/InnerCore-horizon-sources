package com.google.firebase.components;

import com.google.firebase.inject.*;

final class zzh<T> implements Provider<T>
{
    private static final Object zza;
    private volatile Object zzb;
    private volatile Provider<T> zzc;
    
    static {
        zza = new Object();
    }
    
    zzh(final ComponentFactory<T> componentFactory, final ComponentContainer componentContainer) {
        this.zzb = zzh.zza;
        this.zzc = (Provider<T>)new zzi(componentFactory, componentContainer);
    }
    
    @Override
    public final T get() {
        final Object zzb = this.zzb;
        if (zzb == zzh.zza) {
            synchronized (this) {
                Object zzb2;
                if ((zzb2 = this.zzb) == zzh.zza) {
                    zzb2 = this.zzc.get();
                    this.zzb = zzb2;
                    this.zzc = null;
                }
                return (T)zzb2;
            }
        }
        return (T)zzb;
    }
}

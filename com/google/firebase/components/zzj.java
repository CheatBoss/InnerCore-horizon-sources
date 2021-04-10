package com.google.firebase.components;

import com.google.firebase.events.*;
import java.util.*;
import com.google.firebase.inject.*;

final class zzj implements ComponentContainer
{
    private final Set<Class<?>> zza;
    private final Set<Class<?>> zzb;
    private final Set<Class<?>> zzc;
    private final ComponentContainer zzd;
    
    zzj(final Component<?> component, final ComponentContainer zzd) {
        final HashSet<Class<?>> set = new HashSet<Class<?>>();
        final HashSet<Class<?>> set2 = new HashSet<Class<?>>();
        for (final Dependency dependency : component.zzb()) {
            if (dependency.zzc()) {
                set.add(dependency.zza());
            }
            else {
                set2.add(dependency.zza());
            }
        }
        if (!component.zzd().isEmpty()) {
            set.add(Publisher.class);
        }
        this.zza = (Set<Class<?>>)Collections.unmodifiableSet((Set<?>)set);
        this.zzb = (Set<Class<?>>)Collections.unmodifiableSet((Set<?>)set2);
        this.zzc = (Set<Class<?>>)component.zzd();
        this.zzd = zzd;
    }
    
    @Override
    public final <T> T get(final Class<T> clazz) {
        if (!this.zza.contains(clazz)) {
            throw new IllegalArgumentException(String.format("Requesting %s is not allowed.", clazz));
        }
        final T value = this.zzd.get(clazz);
        if (!clazz.equals(Publisher.class)) {
            return value;
        }
        return (T)new zza(this.zzc, (Publisher)value);
    }
    
    @Override
    public final <T> Provider<T> getProvider(final Class<T> clazz) {
        if (this.zzb.contains(clazz)) {
            return this.zzd.getProvider(clazz);
        }
        throw new IllegalArgumentException(String.format("Requesting Provider<%s> is not allowed.", clazz));
    }
    
    static final class zza implements Publisher
    {
        private final Set<Class<?>> zza;
        private final Publisher zzb;
        
        public zza(final Set<Class<?>> zza, final Publisher zzb) {
            this.zza = zza;
            this.zzb = zzb;
        }
    }
}

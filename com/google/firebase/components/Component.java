package com.google.firebase.components;

import com.google.android.gms.common.internal.*;
import java.util.*;

public final class Component<T>
{
    private final Set<Class<? super T>> zza;
    private final Set<Dependency> zzb;
    private final int zzc;
    private final ComponentFactory<T> zzd;
    private final Set<Class<?>> zze;
    
    private Component(final Set<Class<? super T>> set, final Set<Dependency> set2, final int zzc, final ComponentFactory<T> zzd, final Set<Class<?>> set3) {
        this.zza = Collections.unmodifiableSet((Set<? extends Class<? super T>>)set);
        this.zzb = Collections.unmodifiableSet((Set<? extends Dependency>)set2);
        this.zzc = zzc;
        this.zzd = zzd;
        this.zze = Collections.unmodifiableSet((Set<? extends Class<?>>)set3);
    }
    
    public static <T> Builder<T> builder(final Class<T> clazz) {
        return new Builder<T>(clazz, new Class[0], (byte)0);
    }
    
    public static <T> Builder<T> builder(final Class<T> clazz, final Class<? super T>... array) {
        return new Builder<T>(clazz, array, (byte)0);
    }
    
    @SafeVarargs
    public static <T> Component<T> of(final T t, final Class<T> clazz, final Class<? super T>... array) {
        return builder(clazz, array).factory(new zzb(t)).build();
    }
    
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder("Component<");
        sb.append(Arrays.toString(this.zza.toArray()));
        sb.append(">{");
        sb.append(this.zzc);
        sb.append(", deps=");
        sb.append(Arrays.toString(this.zzb.toArray()));
        sb.append("}");
        return sb.toString();
    }
    
    public final Set<Class<? super T>> zza() {
        return this.zza;
    }
    
    public final Set<Dependency> zzb() {
        return this.zzb;
    }
    
    public final ComponentFactory<T> zzc() {
        return this.zzd;
    }
    
    public final Set<Class<?>> zzd() {
        return this.zze;
    }
    
    public final boolean zze() {
        return this.zzc == 1;
    }
    
    public final boolean zzf() {
        return this.zzc == 2;
    }
    
    public static class Builder<T>
    {
        private final Set<Class<? super T>> zza;
        private final Set<Dependency> zzb;
        private int zzc;
        private ComponentFactory<T> zzd;
        private Set<Class<?>> zze;
        
        private Builder(final Class<T> clazz, final Class<? super T>... array) {
            this.zza = new HashSet<Class<? super T>>();
            this.zzb = new HashSet<Dependency>();
            int i = 0;
            this.zzc = 0;
            this.zze = new HashSet<Class<?>>();
            Preconditions.checkNotNull(clazz, "Null interface");
            this.zza.add(clazz);
            while (i < array.length) {
                Preconditions.checkNotNull(array[i], "Null interface");
                ++i;
            }
            Collections.addAll(this.zza, array);
        }
        
        private Builder<T> zza(final int zzc) {
            Preconditions.checkState(this.zzc == 0, "Instantiation type has already been set.");
            this.zzc = zzc;
            return this;
        }
        
        public Builder<T> add(final Dependency dependency) {
            Preconditions.checkNotNull(dependency, "Null dependency");
            Preconditions.checkArgument(this.zza.contains(dependency.zza()) ^ true, "Components are not allowed to depend on interfaces they themselves provide.");
            this.zzb.add(dependency);
            return this;
        }
        
        public Builder<T> alwaysEager() {
            return this.zza(1);
        }
        
        public Component<T> build() {
            Preconditions.checkState(this.zzd != null, "Missing required property: factory.");
            return new Component<T>(new HashSet(this.zza), new HashSet(this.zzb), this.zzc, this.zzd, this.zze, (byte)0);
        }
        
        public Builder<T> eagerInDefaultApp() {
            return this.zza(2);
        }
        
        public Builder<T> factory(final ComponentFactory<T> componentFactory) {
            this.zzd = Preconditions.checkNotNull(componentFactory, "Null factory");
            return this;
        }
    }
}

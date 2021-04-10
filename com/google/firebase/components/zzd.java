package com.google.firebase.components;

import java.util.concurrent.*;
import com.google.firebase.events.*;
import java.util.*;
import com.google.firebase.inject.*;
import com.google.android.gms.common.internal.*;

public final class zzd implements ComponentContainer
{
    private final List<Component<?>> zza;
    private final Map<Class<?>, zzh<?>> zzb;
    private final zzf zzc;
    
    public zzd(final Executor executor, final Iterable<ComponentRegistrar> iterable, final Component<?>... array) {
        this.zzb = new HashMap<Class<?>, zzh<?>>();
        this.zzc = new zzf(executor);
        final ArrayList<Object> list = new ArrayList<Object>();
        list.add(Component.of(this.zzc, zzf.class, Subscriber.class, Publisher.class));
        final Iterator<ComponentRegistrar> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.addAll(iterator.next().getComponents());
        }
        Collections.addAll(list, array);
        final List<Object> unmodifiableList = Collections.unmodifiableList((List<?>)zze.zza((List<Component<?>>)list));
        this.zza = (List<Component<?>>)unmodifiableList;
        final Iterator<Component<T>> iterator2 = unmodifiableList.iterator();
        while (iterator2.hasNext()) {
            this.zza((Component<Object>)iterator2.next());
        }
        this.zza();
    }
    
    private void zza() {
        for (final Component<?> component : this.zza) {
            for (final Dependency dependency : component.zzb()) {
                if (dependency.zzb()) {
                    if (this.zzb.containsKey(dependency.zza())) {
                        continue;
                    }
                    throw new MissingDependencyException(String.format("Unsatisfied dependency for component %s: %s", component, dependency.zza()));
                }
            }
        }
    }
    
    private <T> void zza(final Component<T> component) {
        final zzh<Object> zzh = new zzh<Object>((ComponentFactory<Object>)component.zzc(), new zzj(component, this));
        final Iterator<Class<? super T>> iterator = component.zza().iterator();
        while (iterator.hasNext()) {
            this.zzb.put(iterator.next(), zzh);
        }
    }
    
    @Override
    public final Object get(final Class clazz) {
        return ComponentContainer$$CC.get(this, clazz);
    }
    
    @Override
    public final <T> Provider<T> getProvider(final Class<T> clazz) {
        Preconditions.checkNotNull(clazz, "Null interface requested.");
        return (Provider<T>)this.zzb.get(clazz);
    }
    
    public final void zza(final boolean b) {
        for (final Component<?> component : this.zza) {
            if (component.zze() || (component.zzf() && b)) {
                this.get(component.zza().iterator().next());
            }
        }
        this.zzc.zza();
    }
}

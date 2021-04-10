package com.google.firebase.components;

import java.util.concurrent.*;
import com.google.firebase.events.*;
import com.google.android.gms.common.internal.*;
import java.util.*;

class zzf implements Publisher, Subscriber
{
    private final Map<Class<?>, ConcurrentHashMap<EventHandler<Object>, Executor>> zza;
    private Queue<Event<?>> zzb;
    private final Executor zzc;
    
    zzf(final Executor zzc) {
        this.zza = new HashMap<Class<?>, ConcurrentHashMap<EventHandler<Object>, Executor>>();
        this.zzb = new ArrayDeque<Event<?>>();
        this.zzc = zzc;
    }
    
    private Set<Map.Entry<EventHandler<Object>, Executor>> zza(final Event<?> event) {
        synchronized (this) {
            final ConcurrentHashMap<EventHandler<Object>, Executor> concurrentHashMap = this.zza.get(event.getType());
            if (concurrentHashMap == null) {
                return Collections.emptySet();
            }
            return (Set<Map.Entry<EventHandler<Object>, Executor>>)concurrentHashMap.entrySet();
        }
    }
    
    public void publish(final Event<?> event) {
        Preconditions.checkNotNull(event);
        synchronized (this) {
            if (this.zzb != null) {
                this.zzb.add(event);
                return;
            }
            // monitorexit(this)
            for (final Map.Entry<EventHandler<Object>, Executor> entry : this.zza(event)) {
                entry.getValue().execute(new zzg((Map.Entry)entry, event));
            }
        }
    }
    
    @Override
    public <T> void subscribe(final Class<T> clazz, final EventHandler<? super T> eventHandler) {
        this.subscribe(clazz, this.zzc, eventHandler);
    }
    
    @Override
    public <T> void subscribe(final Class<T> clazz, final Executor executor, final EventHandler<? super T> eventHandler) {
        synchronized (this) {
            Preconditions.checkNotNull(clazz);
            Preconditions.checkNotNull(eventHandler);
            Preconditions.checkNotNull(executor);
            if (!this.zza.containsKey(clazz)) {
                this.zza.put(clazz, new ConcurrentHashMap<EventHandler<Object>, Executor>());
            }
            this.zza.get(clazz).put((EventHandler<Object>)eventHandler, executor);
        }
    }
    
    final void zza() {
        while (true) {
            while (true) {
                synchronized (this) {
                    if (this.zzb != null) {
                        final Collection<Event<?>> zzb = this.zzb;
                        this.zzb = null;
                        // monitorexit(this)
                        if (zzb != null) {
                            final Iterator<Event<?>> iterator = zzb.iterator();
                            while (iterator.hasNext()) {
                                this.publish(iterator.next());
                            }
                        }
                        return;
                    }
                }
                final Collection<Event<?>> zzb = null;
                continue;
            }
        }
    }
}

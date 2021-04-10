package com.google.firebase.components;

import java.util.*;

final class zze
{
    static List<Component<?>> zza(final List<Component<?>> list) {
        final HashMap<Object, Object> hashMap = (HashMap<Object, Object>)new HashMap<Object, zza>(list.size());
        for (final Component<?> component : list) {
            final zza zza = new zza(component);
            for (final Class<?> clazz : component.zza()) {
                if (hashMap.put(clazz, zza) == null) {
                    continue;
                }
                throw new IllegalArgumentException(String.format("Multiple components provide %s.", clazz));
            }
        }
        for (final zza zza2 : hashMap.values()) {
            for (final Dependency dependency : zza2.zzb().zzb()) {
                if (dependency.zzc()) {
                    final zza zza3 = hashMap.get(dependency.zza());
                    if (zza3 == null) {
                        continue;
                    }
                    zza2.zza(zza3);
                    zza3.zzb(zza2);
                }
            }
        }
        final HashSet set = new HashSet<Object>(hashMap.values());
        final Set<zza> zza4 = zza((Set<zza>)set);
        final ArrayList<Component<?>> list2 = new ArrayList<Component<?>>();
        while (!zza4.isEmpty()) {
            final zza zza5 = zza4.iterator().next();
            zza4.remove(zza5);
            list2.add(zza5.zzb());
            for (final zza zza6 : zza5.zza()) {
                zza6.zzc(zza5);
                if (zza6.zzc()) {
                    zza4.add(zza6);
                }
            }
        }
        if (list2.size() == list.size()) {
            Collections.reverse(list2);
            return list2;
        }
        final ArrayList<Component<?>> list3 = new ArrayList<Component<?>>();
        for (final zza zza7 : set) {
            if (!zza7.zzc() && !zza7.zzd()) {
                list3.add(zza7.zzb());
            }
        }
        throw new DependencyCycleException(list3);
    }
    
    private static Set<zza> zza(final Set<zza> set) {
        final HashSet<zza> set2 = new HashSet<zza>();
        for (final zza zza : set) {
            if (zza.zzc()) {
                set2.add(zza);
            }
        }
        return set2;
    }
    
    static final class zza
    {
        private final Component<?> zza;
        private final Set<zza> zzb;
        private final Set<zza> zzc;
        
        zza(final Component<?> zza) {
            this.zzb = new HashSet<zza>();
            this.zzc = new HashSet<zza>();
            this.zza = zza;
        }
        
        final Set<zza> zza() {
            return this.zzb;
        }
        
        final void zza(final zza zza) {
            this.zzb.add(zza);
        }
        
        final Component<?> zzb() {
            return this.zza;
        }
        
        final void zzb(final zza zza) {
            this.zzc.add(zza);
        }
        
        final void zzc(final zza zza) {
            this.zzc.remove(zza);
        }
        
        final boolean zzc() {
            return this.zzc.isEmpty();
        }
        
        final boolean zzd() {
            return this.zzb.isEmpty();
        }
    }
}

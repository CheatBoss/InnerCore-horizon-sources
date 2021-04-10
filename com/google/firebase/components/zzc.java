package com.google.firebase.components;

import android.util.*;
import android.os.*;
import android.content.*;
import android.content.pm.*;
import java.util.*;

public final class zzc<T>
{
    private final T zza;
    private final zzb<T> zzb;
    
    private zzc(final T zza, final zzb<T> zzb) {
        this.zza = zza;
        this.zzb = zzb;
    }
    
    public static zzc<Context> zza(final Context context) {
        return new zzc<Context>(context, (zzb<Context>)new zza((byte)0));
    }
    
    private static List<ComponentRegistrar> zza(final List<String> list) {
        final ArrayList<ComponentRegistrar> list2 = new ArrayList<ComponentRegistrar>();
        for (final String s : list) {
            String s2;
            Object[] array;
            try {
                final Class<?> forName = Class.forName(s);
                if (!ComponentRegistrar.class.isAssignableFrom(forName)) {
                    Log.w("ComponentDiscovery", String.format("Class %s is not an instance of %s", s, "com.google.firebase.components.ComponentRegistrar"));
                    continue;
                }
                list2.add((ComponentRegistrar)forName.newInstance());
                continue;
            }
            catch (InstantiationException ex) {
                s2 = "Could not instantiate %s.";
                array = new Object[] { s };
            }
            catch (IllegalAccessException ex) {
                s2 = "Could not instantiate %s.";
                array = new Object[] { s };
            }
            catch (ClassNotFoundException ex) {
                s2 = "Class %s is not an found.";
                array = new Object[] { s };
            }
            final InstantiationException ex;
            Log.w("ComponentDiscovery", String.format(s2, array), (Throwable)ex);
        }
        return list2;
    }
    
    public final List<ComponentRegistrar> zza() {
        return zza(this.zzb.zza(this.zza));
    }
    
    static final class zza implements zzb<Context>
    {
        private zza() {
        }
        
        private static Bundle zza(final Context context) {
            try {
                final PackageManager packageManager = context.getPackageManager();
                if (packageManager == null) {
                    Log.w("ComponentDiscovery", "Context has no PackageManager.");
                    return null;
                }
                final ServiceInfo serviceInfo = packageManager.getServiceInfo(new ComponentName(context, (Class)ComponentDiscoveryService.class), 128);
                if (serviceInfo == null) {
                    Log.w("ComponentDiscovery", "ComponentDiscoveryService has no service info.");
                    return null;
                }
                return serviceInfo.metaData;
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.w("ComponentDiscovery", "Application info not found.");
                return null;
            }
        }
    }
    
    interface zzb<T>
    {
        List<String> zza(final T p0);
    }
}

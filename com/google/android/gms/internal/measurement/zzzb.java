package com.google.android.gms.internal.measurement;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public final class zzzb<M extends zzza<M>, T>
{
    public final int tag;
    private final int type;
    protected final Class<T> zzcfd;
    protected final boolean zzcfe;
    
    private final Object zze(final zzyx zzyx) {
        Class<?> clazz;
        if (this.zzcfe) {
            clazz = this.zzcfd.getComponentType();
        }
        else {
            clazz = this.zzcfd;
        }
        try {
            final int type = this.type;
            if (type == 10) {
                final zzzg zzzg = (zzzg)clazz.newInstance();
                zzyx.zza(zzzg, this.tag >>> 3);
                return zzzg;
            }
            if (type == 11) {
                final zzzg zzzg2 = (zzzg)clazz.newInstance();
                zzyx.zza(zzzg2);
                return zzzg2;
            }
            final int type2 = this.type;
            final StringBuilder sb = new StringBuilder(24);
            sb.append("Unknown type ");
            sb.append(type2);
            throw new IllegalArgumentException(sb.toString());
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Error reading extension field", ex);
        }
        catch (IllegalAccessException ex2) {
            final String value = String.valueOf(clazz);
            final StringBuilder sb2 = new StringBuilder(String.valueOf(value).length() + 33);
            sb2.append("Error creating instance of class ");
            sb2.append(value);
            throw new IllegalArgumentException(sb2.toString(), ex2);
        }
        catch (InstantiationException ex3) {
            final String value2 = String.valueOf(clazz);
            final StringBuilder sb3 = new StringBuilder(String.valueOf(value2).length() + 33);
            sb3.append("Error creating instance of class ");
            sb3.append(value2);
            throw new IllegalArgumentException(sb3.toString(), ex3);
        }
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof zzzb)) {
            return false;
        }
        final zzzb zzzb = (zzzb)o;
        return this.type == zzzb.type && this.zzcfd == zzzb.zzcfd && this.tag == zzzb.tag && this.zzcfe == zzzb.zzcfe;
    }
    
    @Override
    public final int hashCode() {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: \r\n\tat com.googlecode.dex2jar.ir.ts.NewTransformer.transform(NewTransformer.java:134)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:148)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    protected final void zza(final Object o, final zzyy zzyy) {
        try {
            zzyy.zzca(this.tag);
            final int type = this.type;
            if (type == 10) {
                final int tag = this.tag;
                ((zzzg)o).zza(zzyy);
                zzyy.zzc(tag >>> 3, 4);
                return;
            }
            if (type == 11) {
                zzyy.zzb((zzzg)o);
                return;
            }
            final int type2 = this.type;
            final StringBuilder sb = new StringBuilder(24);
            sb.append("Unknown type ");
            sb.append(type2);
            throw new IllegalArgumentException(sb.toString());
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    final T zzah(final List<zzzi> list) {
        if (list == null) {
            return null;
        }
        if (this.zzcfe) {
            final ArrayList<Object> list2 = new ArrayList<Object>();
            final int n = 0;
            for (int i = 0; i < list.size(); ++i) {
                final zzzi zzzi = list.get(i);
                if (zzzi.zzbug.length != 0) {
                    list2.add(this.zze(zzyx.zzn(zzzi.zzbug)));
                }
            }
            final int size = list2.size();
            if (size == 0) {
                return null;
            }
            final Class<T> zzcfd = this.zzcfd;
            final T cast = zzcfd.cast(Array.newInstance(zzcfd.getComponentType(), size));
            for (int j = n; j < size; ++j) {
                Array.set(cast, j, list2.get(j));
            }
            return cast;
        }
        else {
            if (list.isEmpty()) {
                return null;
            }
            return this.zzcfd.cast(this.zze(zzyx.zzn(list.get(list.size() - 1).zzbug)));
        }
    }
    
    protected final int zzak(final Object o) {
        final int n = this.tag >>> 3;
        final int type = this.type;
        if (type == 10) {
            return (zzyy.zzbb(n) << 1) + ((zzzg)o).zzvu();
        }
        if (type == 11) {
            return zzyy.zzb(n, (zzzg)o);
        }
        final int type2 = this.type;
        final StringBuilder sb = new StringBuilder(24);
        sb.append("Unknown type ");
        sb.append(type2);
        throw new IllegalArgumentException(sb.toString());
    }
}

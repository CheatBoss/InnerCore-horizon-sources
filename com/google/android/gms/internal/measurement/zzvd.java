package com.google.android.gms.internal.measurement;

import java.io.*;
import java.util.*;

final class zzvd<FieldDescriptorType extends zzvf<FieldDescriptorType>>
{
    private static final zzvd zzbvs;
    private boolean zzbpo;
    private final zzxm<FieldDescriptorType, Object> zzbvq;
    private boolean zzbvr;
    
    static {
        zzbvs = new zzvd(true);
    }
    
    private zzvd() {
        this.zzbvr = false;
        this.zzbvq = zzxm.zzbt(16);
    }
    
    private zzvd(final boolean b) {
        this.zzbvr = false;
        this.zzbvq = zzxm.zzbt(0);
        this.zzsm();
    }
    
    static int zza(final zzyq zzyq, int zzbb, final Object o) {
        final int n = zzbb = zzut.zzbb(zzbb);
        if (zzyq == zzyq.zzcdz) {
            zzvo.zzf((zzwt)o);
            zzbb = n << 1;
        }
        return zzbb + zzb(zzyq, o);
    }
    
    private final Object zza(final FieldDescriptorType fieldDescriptorType) {
        Object o;
        if ((o = this.zzbvq.get(fieldDescriptorType)) instanceof zzvw) {
            o = zzvw.zzwt();
        }
        return o;
    }
    
    static void zza(final zzut zzut, final zzyq zzyq, final int n, final Object o) throws IOException {
        if (zzyq == zzyq.zzcdz) {
            final zzwt zzwt = (zzwt)o;
            zzvo.zzf(zzwt);
            zzut.zzc(n, 3);
            zzwt.zzb(zzut);
            zzut.zzc(n, 4);
            return;
        }
        zzut.zzc(n, zzyq.zzyq());
        switch (zzve.zzbuu[zzyq.ordinal()]) {
            default: {}
            case 18: {
                if (o instanceof zzvp) {
                    zzut.zzax(((zzvp)o).zzc());
                    return;
                }
                zzut.zzax((int)o);
            }
            case 17: {
                zzut.zzaw((long)o);
            }
            case 16: {
                zzut.zzaz((int)o);
            }
            case 15: {
                zzut.zzax((long)o);
            }
            case 14: {
                zzut.zzba((int)o);
            }
            case 13: {
                zzut.zzay((int)o);
            }
            case 12: {
                if (o instanceof zzud) {
                    zzut.zza((zzud)o);
                    return;
                }
                final byte[] array = (byte[])o;
                zzut.zze(array, 0, array.length);
            }
            case 11: {
                if (o instanceof zzud) {
                    zzut.zza((zzud)o);
                    return;
                }
                zzut.zzfw((String)o);
            }
            case 10: {
                zzut.zzb((zzwt)o);
            }
            case 9: {
                ((zzwt)o).zzb(zzut);
            }
            case 8: {
                zzut.zzu((boolean)o);
            }
            case 7: {
                zzut.zzba((int)o);
            }
            case 6: {
                zzut.zzax((long)o);
            }
            case 5: {
                zzut.zzax((int)o);
            }
            case 4: {
                zzut.zzav((long)o);
            }
            case 3: {
                zzut.zzav((long)o);
            }
            case 2: {
                zzut.zza((float)o);
            }
            case 1: {
                zzut.zzb((double)o);
            }
        }
    }
    
    private final void zza(final FieldDescriptorType fieldDescriptorType, Object o) {
        if (fieldDescriptorType.zzvy()) {
            if (!(o instanceof List)) {
                throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
            }
            final ArrayList<Object> list = new ArrayList<Object>();
            list.addAll((Collection<?>)o);
            final ArrayList<Object> list2 = list;
            final int size = list2.size();
            int i = 0;
            while (i < size) {
                final Object value = list2.get(i);
                ++i;
                zza(fieldDescriptorType.zzvw(), value);
            }
            o = list;
        }
        else {
            zza(fieldDescriptorType.zzvw(), o);
        }
        if (o instanceof zzvw) {
            this.zzbvr = true;
        }
        this.zzbvq.zza(fieldDescriptorType, o);
    }
    
    private static void zza(final zzyq zzyq, final Object o) {
        zzvo.checkNotNull(o);
        final int n = zzve.zzbvt[zzyq.zzyp().ordinal()];
        boolean b = false;
        Label_0173: {
            switch (n) {
                default: {
                    break Label_0173;
                }
                case 9: {
                    if (o instanceof zzwt) {
                        break;
                    }
                    if (o instanceof zzvw) {
                        break;
                    }
                    break Label_0173;
                }
                case 8: {
                    if (o instanceof Integer) {
                        break;
                    }
                    if (o instanceof zzvp) {
                        break;
                    }
                    break Label_0173;
                }
                case 7: {
                    if (o instanceof zzud || o instanceof byte[]) {
                        break;
                    }
                    break Label_0173;
                }
                case 6: {
                    b = (o instanceof String);
                    break Label_0173;
                }
                case 5: {
                    b = (o instanceof Boolean);
                    break Label_0173;
                }
                case 4: {
                    b = (o instanceof Double);
                    break Label_0173;
                }
                case 3: {
                    b = (o instanceof Float);
                    break Label_0173;
                }
                case 2: {
                    b = (o instanceof Long);
                    break Label_0173;
                }
                case 1: {
                    b = (o instanceof Integer);
                    break Label_0173;
                }
            }
            b = true;
        }
        if (b) {
            return;
        }
        throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
    }
    
    private static int zzb(final zzvf<?> zzvf, final Object o) {
        final zzyq zzvw = zzvf.zzvw();
        final int zzc = zzvf.zzc();
        if (!zzvf.zzvy()) {
            return zza(zzvw, zzc, o);
        }
        final boolean zzvz = zzvf.zzvz();
        final int n = 0;
        int n2 = 0;
        if (zzvz) {
            final Iterator<Object> iterator = (Iterator<Object>)((List)o).iterator();
            while (iterator.hasNext()) {
                n2 += zzb(zzvw, iterator.next());
            }
            return zzut.zzbb(zzc) + n2 + zzut.zzbj(n2);
        }
        final Iterator<Object> iterator2 = (Iterator<Object>)((List)o).iterator();
        int n3 = n;
        while (iterator2.hasNext()) {
            n3 += zza(zzvw, zzc, iterator2.next());
        }
        return n3;
    }
    
    private static int zzb(final zzyq zzyq, final Object o) {
        switch (zzve.zzbuu[zzyq.ordinal()]) {
            default: {
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
            }
            case 18: {
                if (o instanceof zzvp) {
                    return zzut.zzbh(((zzvp)o).zzc());
                }
                return zzut.zzbh((int)o);
            }
            case 17: {
                return zzut.zzba((long)o);
            }
            case 16: {
                return zzut.zzbe((int)o);
            }
            case 15: {
                return zzut.zzbc((long)o);
            }
            case 14: {
                return zzut.zzbg((int)o);
            }
            case 13: {
                return zzut.zzbd((int)o);
            }
            case 12: {
                if (o instanceof zzud) {
                    return zzut.zzb((zzud)o);
                }
                return zzut.zzk((byte[])o);
            }
            case 11: {
                if (o instanceof zzud) {
                    return zzut.zzb((zzud)o);
                }
                return zzut.zzfx((String)o);
            }
            case 10: {
                if (o instanceof zzvw) {
                    return zzut.zza((zzwa)o);
                }
                return zzut.zzc((zzwt)o);
            }
            case 9: {
                return zzut.zzd((zzwt)o);
            }
            case 8: {
                return zzut.zzv((boolean)o);
            }
            case 7: {
                return zzut.zzbf((int)o);
            }
            case 6: {
                return zzut.zzbb((long)o);
            }
            case 5: {
                return zzut.zzbc((int)o);
            }
            case 4: {
                return zzut.zzaz((long)o);
            }
            case 3: {
                return zzut.zzay((long)o);
            }
            case 2: {
                return zzut.zzb((float)o);
            }
            case 1: {
                return zzut.zzc((double)o);
            }
        }
    }
    
    private static boolean zzc(final Map.Entry<FieldDescriptorType, Object> entry) {
        final zzvf<FieldDescriptorType> zzvf = entry.getKey();
        if (zzvf.zzvx() == zzyv.zzcet) {
            if (zzvf.zzvy()) {
                final Iterator<zzwt> iterator = entry.getValue().iterator();
                while (iterator.hasNext()) {
                    if (!iterator.next().isInitialized()) {
                        return false;
                    }
                }
            }
            else {
                final List<zzwt> value = entry.getValue();
                if (value instanceof zzwt) {
                    if (!((zzwt)value).isInitialized()) {
                        return false;
                    }
                }
                else {
                    if (value instanceof zzvw) {
                        return true;
                    }
                    throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
                }
            }
        }
        return true;
    }
    
    private final void zzd(final Map.Entry<FieldDescriptorType, Object> entry) {
        final zzvf<FieldDescriptorType> zzvf = entry.getKey();
        zzwt zzwt;
        if ((zzwt = entry.getValue()) instanceof zzvw) {
            zzwt = zzvw.zzwt();
        }
        if (zzvf.zzvy()) {
            Object zza;
            if ((zza = this.zza((FieldDescriptorType)zzvf)) == null) {
                zza = new ArrayList<Object>();
            }
            final Iterator<Object> iterator = ((List<Object>)zzwt).iterator();
            while (iterator.hasNext()) {
                ((List<Object>)zza).add(zzv(iterator.next()));
            }
            this.zzbvq.zza((FieldDescriptorType)zzvf, zza);
            return;
        }
        if (zzvf.zzvx() != zzyv.zzcet) {
            this.zzbvq.zza((FieldDescriptorType)zzvf, zzv(zzwt));
            return;
        }
        final Object zza2 = this.zza((FieldDescriptorType)zzvf);
        if (zza2 == null) {
            this.zzbvq.zza((FieldDescriptorType)zzvf, zzv(zzwt));
            return;
        }
        zzwt zzwt2;
        if (zza2 instanceof zzwz) {
            zzwt2 = zzvf.zza((zzwz)zza2, (zzwz)zzwt);
        }
        else {
            zzwt2 = zzvf.zza(((zzwz)zza2).zzwd(), zzwt).zzwj();
        }
        this.zzbvq.zza((FieldDescriptorType)zzvf, zzwt2);
    }
    
    private static int zze(final Map.Entry<FieldDescriptorType, Object> entry) {
        final zzvf<FieldDescriptorType> zzvf = entry.getKey();
        final zzvw value = entry.getValue();
        if (zzvf.zzvx() != zzyv.zzcet || zzvf.zzvy() || zzvf.zzvz()) {
            return zzb(zzvf, value);
        }
        if (value instanceof zzvw) {
            return zzut.zzb(entry.getKey().zzc(), value);
        }
        return zzut.zzd(entry.getKey().zzc(), (zzwt)value);
    }
    
    private static Object zzv(final Object o) {
        if (o instanceof zzwz) {
            return ((zzwz)o).zzxj();
        }
        if (o instanceof byte[]) {
            final byte[] array = (byte[])o;
            final byte[] array2 = new byte[array.length];
            System.arraycopy(array, 0, array2, 0, array.length);
            return array2;
        }
        return o;
    }
    
    public static <T extends zzvf<T>> zzvd<T> zzvt() {
        return (zzvd<T>)zzvd.zzbvs;
    }
    
    final Iterator<Map.Entry<FieldDescriptorType, Object>> descendingIterator() {
        if (this.zzbvr) {
            return new zzvz<FieldDescriptorType>(this.zzbvq.zzxy().iterator());
        }
        return this.zzbvq.zzxy().iterator();
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o || (o instanceof zzvd && this.zzbvq.equals(((zzvd)o).zzbvq));
    }
    
    @Override
    public final int hashCode() {
        return this.zzbvq.hashCode();
    }
    
    final boolean isEmpty() {
        return this.zzbvq.isEmpty();
    }
    
    public final boolean isImmutable() {
        return this.zzbpo;
    }
    
    public final boolean isInitialized() {
        for (int i = 0; i < this.zzbvq.zzxw(); ++i) {
            if (!zzc(this.zzbvq.zzbu(i))) {
                return false;
            }
        }
        final Iterator<Map.Entry<FieldDescriptorType, Object>> iterator = this.zzbvq.zzxx().iterator();
        while (iterator.hasNext()) {
            if (!zzc((Map.Entry)iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    public final Iterator<Map.Entry<FieldDescriptorType, Object>> iterator() {
        if (this.zzbvr) {
            return new zzvz<FieldDescriptorType>(this.zzbvq.entrySet().iterator());
        }
        return this.zzbvq.entrySet().iterator();
    }
    
    public final void zza(final zzvd<FieldDescriptorType> zzvd) {
        for (int i = 0; i < zzvd.zzbvq.zzxw(); ++i) {
            this.zzd(zzvd.zzbvq.zzbu(i));
        }
        final Iterator<Map.Entry<FieldDescriptorType, Object>> iterator = zzvd.zzbvq.zzxx().iterator();
        while (iterator.hasNext()) {
            this.zzd((Map.Entry)iterator.next());
        }
    }
    
    public final void zzsm() {
        if (this.zzbpo) {
            return;
        }
        this.zzbvq.zzsm();
        this.zzbpo = true;
    }
    
    public final int zzvu() {
        int i = 0;
        int n = 0;
        while (i < this.zzbvq.zzxw()) {
            final Map.Entry<FieldDescriptorType, Object> zzbu = this.zzbvq.zzbu(i);
            n += zzb(zzbu.getKey(), zzbu.getValue());
            ++i;
        }
        for (final Map.Entry<zzvf<?>, V> entry : this.zzbvq.zzxx()) {
            n += zzb(entry.getKey(), entry.getValue());
        }
        return n;
    }
    
    public final int zzvv() {
        int i = 0;
        int n = 0;
        while (i < this.zzbvq.zzxw()) {
            n += zze(this.zzbvq.zzbu(i));
            ++i;
        }
        final Iterator<Map.Entry<FieldDescriptorType, Object>> iterator = this.zzbvq.zzxx().iterator();
        while (iterator.hasNext()) {
            n += zze((Map.Entry)iterator.next());
        }
        return n;
    }
}

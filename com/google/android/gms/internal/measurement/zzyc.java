package com.google.android.gms.internal.measurement;

import java.util.*;
import java.io.*;

public final class zzyc
{
    private static final zzyc zzcco;
    private int count;
    private boolean zzbtu;
    private int zzbyn;
    private Object[] zzcba;
    private int[] zzccp;
    
    static {
        zzcco = new zzyc(0, new int[0], new Object[0], false);
    }
    
    private zzyc() {
        this(0, new int[8], new Object[8], true);
    }
    
    private zzyc(final int count, final int[] zzccp, final Object[] zzcba, final boolean zzbtu) {
        this.zzbyn = -1;
        this.count = count;
        this.zzccp = zzccp;
        this.zzcba = zzcba;
        this.zzbtu = zzbtu;
    }
    
    static zzyc zza(final zzyc zzyc, final zzyc zzyc2) {
        final int n = zzyc.count + zzyc2.count;
        final int[] copy = Arrays.copyOf(zzyc.zzccp, n);
        System.arraycopy(zzyc2.zzccp, 0, copy, zzyc.count, zzyc2.count);
        final Object[] copy2 = Arrays.copyOf(zzyc.zzcba, n);
        System.arraycopy(zzyc2.zzcba, 0, copy2, zzyc.count, zzyc2.count);
        return new zzyc(n, copy, copy2, true);
    }
    
    private static void zzb(int n, final Object o, final zzyw zzyw) throws IOException {
        final int n2 = n >>> 3;
        n &= 0x7;
        if (n == 0) {
            zzyw.zzi(n2, (long)o);
            return;
        }
        if (n == 1) {
            zzyw.zzc(n2, (long)o);
            return;
        }
        if (n == 2) {
            zzyw.zza(n2, (zzud)o);
            return;
        }
        if (n != 3) {
            if (n == 5) {
                zzyw.zzg(n2, (int)o);
                return;
            }
            throw new RuntimeException(zzvt.zzwo());
        }
        else {
            if (zzyw.zzvj() == zzvm.zze.zzbze) {
                zzyw.zzbk(n2);
                ((zzyc)o).zzb(zzyw);
                zzyw.zzbl(n2);
                return;
            }
            zzyw.zzbl(n2);
            ((zzyc)o).zzb(zzyw);
            zzyw.zzbk(n2);
        }
    }
    
    public static zzyc zzyf() {
        return zzyc.zzcco;
    }
    
    static zzyc zzyg() {
        return new zzyc();
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof zzyc)) {
            return false;
        }
        final zzyc zzyc = (zzyc)o;
        final int count = this.count;
        if (count == zzyc.count) {
            final int[] zzccp = this.zzccp;
            final int[] zzccp2 = zzyc.zzccp;
            int i = 0;
            while (true) {
                while (i < count) {
                    if (zzccp[i] != zzccp2[i]) {
                        final boolean b = false;
                        if (b) {
                            final Object[] zzcba = this.zzcba;
                            final Object[] zzcba2 = zzyc.zzcba;
                            for (int count2 = this.count, j = 0; j < count2; ++j) {
                                if (!zzcba[j].equals(zzcba2[j])) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                    else {
                        ++i;
                    }
                }
                final boolean b = true;
                continue;
            }
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        final int count = this.count;
        final int[] zzccp = this.zzccp;
        int n = 17;
        final int n2 = 0;
        int i = 0;
        int n3 = 17;
        while (i < count) {
            n3 = n3 * 31 + zzccp[i];
            ++i;
        }
        final Object[] zzcba = this.zzcba;
        for (int count2 = this.count, j = n2; j < count2; ++j) {
            n = n * 31 + zzcba[j].hashCode();
        }
        return ((count + 527) * 31 + n3) * 31 + n;
    }
    
    final void zza(final zzyw zzyw) throws IOException {
        if (zzyw.zzvj() == zzvm.zze.zzbzf) {
            int count = this.count;
            while (true) {
                --count;
                if (count < 0) {
                    break;
                }
                zzyw.zza(this.zzccp[count] >>> 3, this.zzcba[count]);
            }
            return;
        }
        for (int i = 0; i < this.count; ++i) {
            zzyw.zza(this.zzccp[i] >>> 3, this.zzcba[i]);
        }
    }
    
    final void zzb(final int n, final Object o) {
        if (this.zzbtu) {
            final int count = this.count;
            if (count == this.zzccp.length) {
                int n2;
                if (count < 4) {
                    n2 = 8;
                }
                else {
                    n2 = count >> 1;
                }
                final int n3 = this.count + n2;
                this.zzccp = Arrays.copyOf(this.zzccp, n3);
                this.zzcba = Arrays.copyOf(this.zzcba, n3);
            }
            final int[] zzccp = this.zzccp;
            final int count2 = this.count;
            zzccp[count2] = n;
            this.zzcba[count2] = o;
            this.count = count2 + 1;
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    public final void zzb(final zzyw zzyw) throws IOException {
        if (this.count == 0) {
            return;
        }
        if (zzyw.zzvj() == zzvm.zze.zzbze) {
            for (int i = 0; i < this.count; ++i) {
                zzb(this.zzccp[i], this.zzcba[i], zzyw);
            }
            return;
        }
        int count = this.count;
        while (true) {
            --count;
            if (count < 0) {
                break;
            }
            zzb(this.zzccp[count], this.zzcba[count], zzyw);
        }
    }
    
    final void zzb(final StringBuilder sb, final int n) {
        for (int i = 0; i < this.count; ++i) {
            zzww.zzb(sb, n, String.valueOf(this.zzccp[i] >>> 3), this.zzcba[i]);
        }
    }
    
    public final void zzsm() {
        this.zzbtu = false;
    }
    
    public final int zzvu() {
        final int zzbyn = this.zzbyn;
        if (zzbyn != -1) {
            return zzbyn;
        }
        int i = 0;
        int zzbyn2 = 0;
        while (i < this.count) {
            final int n = this.zzccp[i];
            final int n2 = n >>> 3;
            final int n3 = n & 0x7;
            int n4;
            if (n3 != 0) {
                if (n3 != 1) {
                    if (n3 != 2) {
                        if (n3 != 3) {
                            if (n3 != 5) {
                                throw new IllegalStateException(zzvt.zzwo());
                            }
                            n4 = zzut.zzk(n2, (int)this.zzcba[i]);
                        }
                        else {
                            n4 = (zzut.zzbb(n2) << 1) + ((zzyc)this.zzcba[i]).zzvu();
                        }
                    }
                    else {
                        n4 = zzut.zzc(n2, (zzud)this.zzcba[i]);
                    }
                }
                else {
                    n4 = zzut.zzg(n2, (long)this.zzcba[i]);
                }
            }
            else {
                n4 = zzut.zze(n2, (long)this.zzcba[i]);
            }
            zzbyn2 += n4;
            ++i;
        }
        return this.zzbyn = zzbyn2;
    }
    
    public final int zzyh() {
        final int zzbyn = this.zzbyn;
        if (zzbyn != -1) {
            return zzbyn;
        }
        int i = 0;
        int zzbyn2 = 0;
        while (i < this.count) {
            zzbyn2 += zzut.zzd(this.zzccp[i] >>> 3, (zzud)this.zzcba[i]);
            ++i;
        }
        return this.zzbyn = zzbyn2;
    }
}

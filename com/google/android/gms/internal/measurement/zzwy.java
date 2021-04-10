package com.google.android.gms.internal.measurement;

import java.io.*;
import java.util.*;

final class zzwy<T> implements zzxj<T>
{
    private final zzwt zzcbd;
    private final boolean zzcbe;
    private final zzyb<?, ?> zzcbn;
    private final zzva<?> zzcbo;
    
    private zzwy(final zzyb<?, ?> zzcbn, final zzva<?> zzcbo, final zzwt zzcbd) {
        this.zzcbn = zzcbn;
        this.zzcbe = zzcbo.zze(zzcbd);
        this.zzcbo = zzcbo;
        this.zzcbd = zzcbd;
    }
    
    static <T> zzwy<T> zza(final zzyb<?, ?> zzyb, final zzva<?> zzva, final zzwt zzwt) {
        return new zzwy<T>(zzyb, zzva, zzwt);
    }
    
    @Override
    public final boolean equals(final T t, final T t2) {
        return this.zzcbn.zzah(t).equals(this.zzcbn.zzah(t2)) && (!this.zzcbe || this.zzcbo.zzs(t).equals(this.zzcbo.zzs(t2)));
    }
    
    @Override
    public final int hashCode(final T t) {
        int hashCode = this.zzcbn.zzah(t).hashCode();
        if (this.zzcbe) {
            hashCode = hashCode * 53 + this.zzcbo.zzs(t).hashCode();
        }
        return hashCode;
    }
    
    @Override
    public final T newInstance() {
        return (T)this.zzcbd.zzwe().zzwi();
    }
    
    @Override
    public final void zza(final T t, final zzxi zzxi, final zzuz zzuz) throws IOException {
        final zzyb<?, ?> zzcbn = this.zzcbn;
        final zzva<?> zzcbo = this.zzcbo;
        final Object zzai = zzcbn.zzai(t);
        final zzvd<?> zzt = zzcbo.zzt(t);
        int tag;
        Object zza;
        boolean b;
        zzud zzuo;
        Object zza2;
        int tag2;
        int zzup;
        Block_12_Outer:Label_0135_Outer:
        while (true) {
        Label_0135:
            while (true) {
                Label_0311: {
                    try {
                        // iftrue(Label_0135:, zzxi.zzvf())
                        // iftrue(Label_0223:, tag2 != 26)
                        // iftrue(Label_0186:, tag2 != 16)
                        // iftrue(Label_0279:, zzuo == null)
                        // iftrue(Label_0296:, zzxi.getTag() != 12)
                        // iftrue(Label_0268:, zza2 == null)
                    Block_9_Outer:
                        while (true) {
                            while (true) {
                            Block_8:
                                while (true) {
                                Block_11:
                                    while (true) {
                                        Label_0232: {
                                            while (true) {
                                                while (zzxi.zzve() != Integer.MAX_VALUE) {
                                                    tag = zzxi.getTag();
                                                    if (tag == 11) {
                                                        break Label_0311;
                                                    }
                                                    if ((tag & 0x7) == 0x2) {
                                                        zza = zzcbo.zza(zzuz, this.zzcbd, tag >>> 3);
                                                        if (zza != null) {
                                                            zzcbo.zza(zzxi, zza, zzuz, zzt);
                                                            b = true;
                                                        }
                                                        else {
                                                            b = zzcbn.zza(zzai, zzxi);
                                                        }
                                                    }
                                                    else {
                                                        b = zzxi.zzvf();
                                                    }
                                                    if (!b) {
                                                        return;
                                                    }
                                                }
                                                return;
                                                Label_0223: {
                                                    break Label_0232;
                                                }
                                                zzcbo.zza(zzuo, zza2, zzuz, zzt);
                                                continue Block_12_Outer;
                                                Label_0296:
                                                throw zzvt.zzwn();
                                                Label_0186:
                                                break Block_8;
                                                Label_0212:
                                                zzuo = zzxi.zzuo();
                                                break Label_0135;
                                                Label_0268:
                                                zzcbn.zza(zzai, zzup, zzuo);
                                                continue Block_12_Outer;
                                            }
                                            tag2 = zzxi.getTag();
                                            zzup = zzxi.zzup();
                                            zza2 = zzcbo.zza(zzuz, this.zzcbd, zzup);
                                            break Label_0135;
                                            break Block_11;
                                            zzcbo.zza(zzxi, zza2, zzuz, zzt);
                                            break Label_0135;
                                        }
                                        continue Block_9_Outer;
                                    }
                                    continue Label_0135_Outer;
                                }
                                continue;
                            }
                            continue Block_9_Outer;
                        }
                    }
                    // iftrue(Label_0212:, zza2 == null)
                    // iftrue(Label_0232:, zzxi.zzve() == 2147483647)
                    finally {
                        zzcbn.zzg(t, zzai);
                    }
                }
                zza2 = null;
                zzuo = null;
                zzup = 0;
                continue Label_0135;
            }
        }
    }
    
    @Override
    public final void zza(final T t, final zzyw zzyw) throws IOException {
        for (final Map.Entry<zzvf, V> entry : this.zzcbo.zzs(t)) {
            final zzvf zzvf = entry.getKey();
            if (zzvf.zzvx() != zzyv.zzcet || zzvf.zzvy() || zzvf.zzvz()) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            }
            int n;
            Object o;
            if (entry instanceof zzvy) {
                n = zzvf.zzc();
                o = ((zzvy)entry).zzwu().zztt();
            }
            else {
                n = zzvf.zzc();
                o = entry.getValue();
            }
            zzyw.zza(n, o);
        }
        final zzyb<?, ?> zzcbn = this.zzcbn;
        zzcbn.zzc(zzcbn.zzah(t), zzyw);
    }
    
    @Override
    public final int zzae(final T t) {
        final zzyb<?, ?> zzcbn = this.zzcbn;
        int n = zzcbn.zzaj(zzcbn.zzah(t)) + 0;
        if (this.zzcbe) {
            n += this.zzcbo.zzs(t).zzvv();
        }
        return n;
    }
    
    @Override
    public final boolean zzaf(final T t) {
        return this.zzcbo.zzs(t).isInitialized();
    }
    
    @Override
    public final void zzd(final T t, final T t2) {
        zzxl.zza(this.zzcbn, t, t2);
        if (this.zzcbe) {
            zzxl.zza(this.zzcbo, t, t2);
        }
    }
    
    @Override
    public final void zzu(final T t) {
        this.zzcbn.zzu(t);
        this.zzcbo.zzu(t);
    }
}

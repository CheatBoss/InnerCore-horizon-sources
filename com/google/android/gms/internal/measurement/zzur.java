package com.google.android.gms.internal.measurement;

import java.io.*;
import java.util.*;

final class zzur implements zzxi
{
    private int tag;
    private final zzuo zzbur;
    private int zzbus;
    private int zzbut;
    
    private zzur(zzuo zzbur) {
        this.zzbut = 0;
        zzbur = zzvo.zza(zzbur, "input");
        this.zzbur = zzbur;
        zzbur.zzbuk = this;
    }
    
    public static zzur zza(final zzuo zzuo) {
        if (zzuo.zzbuk != null) {
            return zzuo.zzbuk;
        }
        return new zzur(zzuo);
    }
    
    private final Object zza(final zzyq zzyq, final Class<?> clazz, final zzuz zzuz) throws IOException {
        switch (zzus.zzbuu[zzyq.ordinal()]) {
            default: {
                throw new RuntimeException("unsupported field type.");
            }
            case 17: {
                return this.zzuh();
            }
            case 16: {
                return this.zzup();
            }
            case 15: {
                return this.zzun();
            }
            case 14: {
                return this.zzuu();
            }
            case 13: {
                return this.zzut();
            }
            case 12: {
                return this.zzus();
            }
            case 11: {
                return this.zzur();
            }
            case 10: {
                this.zzat(2);
                return this.zzc((zzxj<Object>)zzxf.zzxn().zzi(clazz), zzuz);
            }
            case 9: {
                return this.zzui();
            }
            case 8: {
                return this.zzuj();
            }
            case 7: {
                return this.readFloat();
            }
            case 6: {
                return this.zzuk();
            }
            case 5: {
                return this.zzul();
            }
            case 4: {
                return this.zzuq();
            }
            case 3: {
                return this.readDouble();
            }
            case 2: {
                return this.zzuo();
            }
            case 1: {
                return this.zzum();
            }
        }
    }
    
    private final void zza(final List<String> list, final boolean b) throws IOException {
        if ((this.tag & 0x7) != 0x2) {
            throw zzvt.zzwo();
        }
        if (list instanceof zzwc && !b) {
            final zzwc zzwc = (zzwc)list;
            int i;
            do {
                zzwc.zzc(this.zzuo());
                if (this.zzbur.zzuw()) {
                    return;
                }
                i = this.zzbur.zzug();
            } while (i == this.tag);
            this.zzbut = i;
            return;
        }
        int j;
        do {
            String s;
            if (b) {
                s = this.zzun();
            }
            else {
                s = this.readString();
            }
            list.add(s);
            if (this.zzbur.zzuw()) {
                return;
            }
            j = this.zzbur.zzug();
        } while (j == this.tag);
        this.zzbut = j;
    }
    
    private final void zzat(final int n) throws IOException {
        if ((this.tag & 0x7) == n) {
            return;
        }
        throw zzvt.zzwo();
    }
    
    private static void zzau(final int n) throws IOException {
        if ((n & 0x7) == 0x0) {
            return;
        }
        throw zzvt.zzwq();
    }
    
    private static void zzav(final int n) throws IOException {
        if ((n & 0x3) == 0x0) {
            return;
        }
        throw zzvt.zzwq();
    }
    
    private final void zzaw(final int n) throws IOException {
        if (this.zzbur.zzux() == n) {
            return;
        }
        throw zzvt.zzwk();
    }
    
    private final <T> T zzc(final zzxj<T> zzxj, final zzuz zzuz) throws IOException {
        final int zzup = this.zzbur.zzup();
        if (this.zzbur.zzbuh < this.zzbur.zzbui) {
            final int zzaq = this.zzbur.zzaq(zzup);
            final T instance = zzxj.newInstance();
            final zzuo zzbur = this.zzbur;
            ++zzbur.zzbuh;
            zzxj.zza(instance, this, zzuz);
            zzxj.zzu(instance);
            this.zzbur.zzan(0);
            final zzuo zzbur2 = this.zzbur;
            --zzbur2.zzbuh;
            this.zzbur.zzar(zzaq);
            return instance;
        }
        throw zzvt.zzwp();
    }
    
    private final <T> T zzd(final zzxj<T> zzxj, final zzuz zzuz) throws IOException {
        final int zzbus = this.zzbus;
        this.zzbus = (this.tag >>> 3 << 3 | 0x4);
        try {
            final T instance = zzxj.newInstance();
            zzxj.zza(instance, this, zzuz);
            zzxj.zzu(instance);
            if (this.tag == this.zzbus) {
                return instance;
            }
            throw zzvt.zzwq();
        }
        finally {
            this.zzbus = zzbus;
        }
    }
    
    @Override
    public final int getTag() {
        return this.tag;
    }
    
    @Override
    public final double readDouble() throws IOException {
        this.zzat(1);
        return this.zzbur.readDouble();
    }
    
    @Override
    public final float readFloat() throws IOException {
        this.zzat(5);
        return this.zzbur.readFloat();
    }
    
    @Override
    public final String readString() throws IOException {
        this.zzat(2);
        return this.zzbur.readString();
    }
    
    @Override
    public final void readStringList(final List<String> list) throws IOException {
        this.zza(list, false);
    }
    
    @Override
    public final <T> T zza(final zzxj<T> zzxj, final zzuz zzuz) throws IOException {
        this.zzat(2);
        return (T)this.zzc((zzxj<Object>)zzxj, zzuz);
    }
    
    @Override
    public final <T> void zza(final List<T> list, final zzxj<T> zzxj, final zzuz zzuz) throws IOException {
        final int tag = this.tag;
        if ((tag & 0x7) == 0x2) {
            int i;
            do {
                list.add(this.zzc(zzxj, zzuz));
                if (this.zzbur.zzuw()) {
                    return;
                }
                if (this.zzbut != 0) {
                    return;
                }
                i = this.zzbur.zzug();
            } while (i == tag);
            this.zzbut = i;
            return;
        }
        throw zzvt.zzwo();
    }
    
    @Override
    public final <K, V> void zza(final Map<K, V> map, final zzwm<K, V> zzwm, final zzuz zzuz) throws IOException {
        this.zzat(2);
        final int zzaq = this.zzbur.zzaq(this.zzbur.zzup());
        Object o = zzwm.zzcas;
        Object o2 = zzwm.zzbre;
        try {
            while (true) {
                final int zzve = this.zzve();
                if (zzve != Integer.MAX_VALUE && !this.zzbur.zzuw()) {
                    Label_0123: {
                        if (zzve == 1) {
                            break Label_0123;
                        }
                        Label_0098: {
                            if (zzve == 2) {
                                break Label_0098;
                            }
                            try {
                                if (this.zzvf()) {
                                    continue;
                                }
                                throw new zzvt("Unable to parse map entry.");
                                o = this.zza(zzwm.zzcar, null, null);
                                continue;
                                o2 = this.zza(zzwm.zzcat, zzwm.zzbre.getClass(), zzuz);
                                continue;
                            }
                            catch (zzvu zzvu) {
                                if (this.zzvf()) {
                                    continue;
                                }
                                throw new zzvt("Unable to parse map entry.");
                            }
                        }
                    }
                    break;
                }
                break;
            }
            map.put((K)o, (V)o2);
        }
        finally {
            this.zzbur.zzar(zzaq);
        }
    }
    
    @Override
    public final <T> T zzb(final zzxj<T> zzxj, final zzuz zzuz) throws IOException {
        this.zzat(3);
        return (T)this.zzd((zzxj<Object>)zzxj, zzuz);
    }
    
    @Override
    public final <T> void zzb(final List<T> list, final zzxj<T> zzxj, final zzuz zzuz) throws IOException {
        final int tag = this.tag;
        if ((tag & 0x7) == 0x3) {
            int i;
            do {
                list.add(this.zzd(zzxj, zzuz));
                if (this.zzbur.zzuw()) {
                    return;
                }
                if (this.zzbut != 0) {
                    return;
                }
                i = this.zzbur.zzug();
            } while (i == tag);
            this.zzbut = i;
            return;
        }
        throw zzvt.zzwo();
    }
    
    @Override
    public final void zzh(final List<Double> list) throws IOException {
        if (list instanceof zzuw) {
            final zzuw zzuw = (zzuw)list;
            final int n = this.tag & 0x7;
            if (n == 1) {
                int i;
                do {
                    zzuw.zzd(this.zzbur.readDouble());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int zzup = this.zzbur.zzup();
                zzau(zzup);
                do {
                    zzuw.zzd(this.zzbur.readDouble());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n2 = this.tag & 0x7;
            if (n2 == 1) {
                int j;
                do {
                    list.add(this.zzbur.readDouble());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n2 == 2) {
                final int zzup2 = this.zzbur.zzup();
                zzau(zzup2);
                do {
                    list.add(this.zzbur.readDouble());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup2);
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzi(final List<Float> list) throws IOException {
        if (list instanceof zzvj) {
            final zzvj zzvj = (zzvj)list;
            final int n = this.tag & 0x7;
            if (n == 2) {
                final int zzup = this.zzbur.zzup();
                zzav(zzup);
                do {
                    zzvj.zzc(this.zzbur.readFloat());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup);
                return;
            }
            if (n == 5) {
                int i;
                do {
                    zzvj.zzc(this.zzbur.readFloat());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n2 = this.tag & 0x7;
            if (n2 == 2) {
                final int zzup2 = this.zzbur.zzup();
                zzav(zzup2);
                do {
                    list.add(this.zzbur.readFloat());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup2);
                return;
            }
            if (n2 == 5) {
                int j;
                do {
                    list.add(this.zzbur.readFloat());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzj(final List<Long> list) throws IOException {
        if (list instanceof zzwh) {
            final zzwh zzwh = (zzwh)list;
            final int n = this.tag & 0x7;
            if (n == 0) {
                int i;
                do {
                    zzwh.zzbg(this.zzbur.zzuh());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int n2 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    zzwh.zzbg(this.zzbur.zzuh());
                } while (this.zzbur.zzux() < n2);
                this.zzaw(n2);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n3 = this.tag & 0x7;
            if (n3 == 0) {
                int j;
                do {
                    list.add(this.zzbur.zzuh());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n3 == 2) {
                final int n4 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    list.add(this.zzbur.zzuh());
                } while (this.zzbur.zzux() < n4);
                this.zzaw(n4);
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzk(final List<Long> list) throws IOException {
        if (list instanceof zzwh) {
            final zzwh zzwh = (zzwh)list;
            final int n = this.tag & 0x7;
            if (n == 0) {
                int i;
                do {
                    zzwh.zzbg(this.zzbur.zzui());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int n2 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    zzwh.zzbg(this.zzbur.zzui());
                } while (this.zzbur.zzux() < n2);
                this.zzaw(n2);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n3 = this.tag & 0x7;
            if (n3 == 0) {
                int j;
                do {
                    list.add(this.zzbur.zzui());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n3 == 2) {
                final int n4 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    list.add(this.zzbur.zzui());
                } while (this.zzbur.zzux() < n4);
                this.zzaw(n4);
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzl(final List<Integer> list) throws IOException {
        if (list instanceof zzvn) {
            final zzvn zzvn = (zzvn)list;
            final int n = this.tag & 0x7;
            if (n == 0) {
                int i;
                do {
                    zzvn.zzbm(this.zzbur.zzuj());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int n2 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    zzvn.zzbm(this.zzbur.zzuj());
                } while (this.zzbur.zzux() < n2);
                this.zzaw(n2);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n3 = this.tag & 0x7;
            if (n3 == 0) {
                int j;
                do {
                    list.add(this.zzbur.zzuj());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n3 == 2) {
                final int n4 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    list.add(this.zzbur.zzuj());
                } while (this.zzbur.zzux() < n4);
                this.zzaw(n4);
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzm(final List<Long> list) throws IOException {
        if (list instanceof zzwh) {
            final zzwh zzwh = (zzwh)list;
            final int n = this.tag & 0x7;
            if (n == 1) {
                int i;
                do {
                    zzwh.zzbg(this.zzbur.zzuk());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int zzup = this.zzbur.zzup();
                zzau(zzup);
                do {
                    zzwh.zzbg(this.zzbur.zzuk());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n2 = this.tag & 0x7;
            if (n2 == 1) {
                int j;
                do {
                    list.add(this.zzbur.zzuk());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n2 == 2) {
                final int zzup2 = this.zzbur.zzup();
                zzau(zzup2);
                do {
                    list.add(this.zzbur.zzuk());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup2);
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzn(final List<Integer> list) throws IOException {
        if (list instanceof zzvn) {
            final zzvn zzvn = (zzvn)list;
            final int n = this.tag & 0x7;
            if (n == 2) {
                final int zzup = this.zzbur.zzup();
                zzav(zzup);
                do {
                    zzvn.zzbm(this.zzbur.zzul());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup);
                return;
            }
            if (n == 5) {
                int i;
                do {
                    zzvn.zzbm(this.zzbur.zzul());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n2 = this.tag & 0x7;
            if (n2 == 2) {
                final int zzup2 = this.zzbur.zzup();
                zzav(zzup2);
                do {
                    list.add(this.zzbur.zzul());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup2);
                return;
            }
            if (n2 == 5) {
                int j;
                do {
                    list.add(this.zzbur.zzul());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzo(final List<Boolean> list) throws IOException {
        if (list instanceof zzub) {
            final zzub zzub = (zzub)list;
            final int n = this.tag & 0x7;
            if (n == 0) {
                int i;
                do {
                    zzub.addBoolean(this.zzbur.zzum());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int n2 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    zzub.addBoolean(this.zzbur.zzum());
                } while (this.zzbur.zzux() < n2);
                this.zzaw(n2);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n3 = this.tag & 0x7;
            if (n3 == 0) {
                int j;
                do {
                    list.add(this.zzbur.zzum());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n3 == 2) {
                final int n4 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    list.add(this.zzbur.zzum());
                } while (this.zzbur.zzux() < n4);
                this.zzaw(n4);
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzp(final List<String> list) throws IOException {
        this.zza(list, true);
    }
    
    @Override
    public final void zzq(final List<zzud> list) throws IOException {
        if ((this.tag & 0x7) == 0x2) {
            int i;
            do {
                list.add(this.zzuo());
                if (this.zzbur.zzuw()) {
                    return;
                }
                i = this.zzbur.zzug();
            } while (i == this.tag);
            this.zzbut = i;
            return;
        }
        throw zzvt.zzwo();
    }
    
    @Override
    public final void zzr(final List<Integer> list) throws IOException {
        if (list instanceof zzvn) {
            final zzvn zzvn = (zzvn)list;
            final int n = this.tag & 0x7;
            if (n == 0) {
                int i;
                do {
                    zzvn.zzbm(this.zzbur.zzup());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int n2 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    zzvn.zzbm(this.zzbur.zzup());
                } while (this.zzbur.zzux() < n2);
                this.zzaw(n2);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n3 = this.tag & 0x7;
            if (n3 == 0) {
                int j;
                do {
                    list.add(this.zzbur.zzup());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n3 == 2) {
                final int n4 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    list.add(this.zzbur.zzup());
                } while (this.zzbur.zzux() < n4);
                this.zzaw(n4);
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzs(final List<Integer> list) throws IOException {
        if (list instanceof zzvn) {
            final zzvn zzvn = (zzvn)list;
            final int n = this.tag & 0x7;
            if (n == 0) {
                int i;
                do {
                    zzvn.zzbm(this.zzbur.zzuq());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int n2 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    zzvn.zzbm(this.zzbur.zzuq());
                } while (this.zzbur.zzux() < n2);
                this.zzaw(n2);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n3 = this.tag & 0x7;
            if (n3 == 0) {
                int j;
                do {
                    list.add(this.zzbur.zzuq());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n3 == 2) {
                final int n4 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    list.add(this.zzbur.zzuq());
                } while (this.zzbur.zzux() < n4);
                this.zzaw(n4);
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzt(final List<Integer> list) throws IOException {
        if (list instanceof zzvn) {
            final zzvn zzvn = (zzvn)list;
            final int n = this.tag & 0x7;
            if (n == 2) {
                final int zzup = this.zzbur.zzup();
                zzav(zzup);
                do {
                    zzvn.zzbm(this.zzbur.zzur());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup);
                return;
            }
            if (n == 5) {
                int i;
                do {
                    zzvn.zzbm(this.zzbur.zzur());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n2 = this.tag & 0x7;
            if (n2 == 2) {
                final int zzup2 = this.zzbur.zzup();
                zzav(zzup2);
                do {
                    list.add(this.zzbur.zzur());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup2);
                return;
            }
            if (n2 == 5) {
                int j;
                do {
                    list.add(this.zzbur.zzur());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final void zzu(final List<Long> list) throws IOException {
        if (list instanceof zzwh) {
            final zzwh zzwh = (zzwh)list;
            final int n = this.tag & 0x7;
            if (n == 1) {
                int i;
                do {
                    zzwh.zzbg(this.zzbur.zzus());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int zzup = this.zzbur.zzup();
                zzau(zzup);
                do {
                    zzwh.zzbg(this.zzbur.zzus());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n2 = this.tag & 0x7;
            if (n2 == 1) {
                int j;
                do {
                    list.add(this.zzbur.zzus());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n2 == 2) {
                final int zzup2 = this.zzbur.zzup();
                zzau(zzup2);
                do {
                    list.add(this.zzbur.zzus());
                } while (this.zzbur.zzux() < this.zzbur.zzux() + zzup2);
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final long zzuh() throws IOException {
        this.zzat(0);
        return this.zzbur.zzuh();
    }
    
    @Override
    public final long zzui() throws IOException {
        this.zzat(0);
        return this.zzbur.zzui();
    }
    
    @Override
    public final int zzuj() throws IOException {
        this.zzat(0);
        return this.zzbur.zzuj();
    }
    
    @Override
    public final long zzuk() throws IOException {
        this.zzat(1);
        return this.zzbur.zzuk();
    }
    
    @Override
    public final int zzul() throws IOException {
        this.zzat(5);
        return this.zzbur.zzul();
    }
    
    @Override
    public final boolean zzum() throws IOException {
        this.zzat(0);
        return this.zzbur.zzum();
    }
    
    @Override
    public final String zzun() throws IOException {
        this.zzat(2);
        return this.zzbur.zzun();
    }
    
    @Override
    public final zzud zzuo() throws IOException {
        this.zzat(2);
        return this.zzbur.zzuo();
    }
    
    @Override
    public final int zzup() throws IOException {
        this.zzat(0);
        return this.zzbur.zzup();
    }
    
    @Override
    public final int zzuq() throws IOException {
        this.zzat(0);
        return this.zzbur.zzuq();
    }
    
    @Override
    public final int zzur() throws IOException {
        this.zzat(5);
        return this.zzbur.zzur();
    }
    
    @Override
    public final long zzus() throws IOException {
        this.zzat(1);
        return this.zzbur.zzus();
    }
    
    @Override
    public final int zzut() throws IOException {
        this.zzat(0);
        return this.zzbur.zzut();
    }
    
    @Override
    public final long zzuu() throws IOException {
        this.zzat(0);
        return this.zzbur.zzuu();
    }
    
    @Override
    public final void zzv(final List<Integer> list) throws IOException {
        if (list instanceof zzvn) {
            final zzvn zzvn = (zzvn)list;
            final int n = this.tag & 0x7;
            if (n == 0) {
                int i;
                do {
                    zzvn.zzbm(this.zzbur.zzut());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int n2 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    zzvn.zzbm(this.zzbur.zzut());
                } while (this.zzbur.zzux() < n2);
                this.zzaw(n2);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n3 = this.tag & 0x7;
            if (n3 == 0) {
                int j;
                do {
                    list.add(this.zzbur.zzut());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n3 == 2) {
                final int n4 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    list.add(this.zzbur.zzut());
                } while (this.zzbur.zzux() < n4);
                this.zzaw(n4);
                return;
            }
            throw zzvt.zzwo();
        }
    }
    
    @Override
    public final int zzve() throws IOException {
        final int zzbut = this.zzbut;
        if (zzbut != 0) {
            this.tag = zzbut;
            this.zzbut = 0;
        }
        else {
            this.tag = this.zzbur.zzug();
        }
        final int tag = this.tag;
        if (tag != 0 && tag != this.zzbus) {
            return tag >>> 3;
        }
        return Integer.MAX_VALUE;
    }
    
    @Override
    public final boolean zzvf() throws IOException {
        if (!this.zzbur.zzuw()) {
            final int tag = this.tag;
            if (tag != this.zzbus) {
                return this.zzbur.zzao(tag);
            }
        }
        return false;
    }
    
    @Override
    public final void zzw(final List<Long> list) throws IOException {
        if (list instanceof zzwh) {
            final zzwh zzwh = (zzwh)list;
            final int n = this.tag & 0x7;
            if (n == 0) {
                int i;
                do {
                    zzwh.zzbg(this.zzbur.zzuu());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    i = this.zzbur.zzug();
                } while (i == this.tag);
                this.zzbut = i;
                return;
            }
            if (n == 2) {
                final int n2 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    zzwh.zzbg(this.zzbur.zzuu());
                } while (this.zzbur.zzux() < n2);
                this.zzaw(n2);
                return;
            }
            throw zzvt.zzwo();
        }
        else {
            final int n3 = this.tag & 0x7;
            if (n3 == 0) {
                int j;
                do {
                    list.add(this.zzbur.zzuu());
                    if (this.zzbur.zzuw()) {
                        return;
                    }
                    j = this.zzbur.zzug();
                } while (j == this.tag);
                this.zzbut = j;
                return;
            }
            if (n3 == 2) {
                final int n4 = this.zzbur.zzux() + this.zzbur.zzup();
                do {
                    list.add(this.zzbur.zzuu());
                } while (this.zzbur.zzux() < n4);
                this.zzaw(n4);
                return;
            }
            throw zzvt.zzwo();
        }
    }
}

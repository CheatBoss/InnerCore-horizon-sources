package com.google.android.gms.internal.measurement;

import android.content.*;
import android.os.*;
import android.util.*;
import javax.annotation.*;
import android.support.v4.content.*;

public abstract class zzsl<T>
{
    private static final Object zzbqy;
    private static boolean zzbqz;
    private static volatile Boolean zzbra;
    private static Context zzri;
    private final zzsv zzbrb;
    final String zzbrc;
    private final String zzbrd;
    private final T zzbre;
    private T zzbrf;
    private volatile zzsi zzbrg;
    private volatile SharedPreferences zzbrh;
    
    static {
        zzbqy = new Object();
        zzsl.zzri = null;
        zzsl.zzbqz = false;
        zzsl.zzbra = null;
    }
    
    private zzsl(final zzsv zzbrb, String value, final T zzbre) {
        this.zzbrf = null;
        this.zzbrg = null;
        this.zzbrh = null;
        if (zzbrb.zzbrn != null) {
            this.zzbrb = zzbrb;
            final String value2 = String.valueOf(zzbrb.zzbro);
            final String value3 = String.valueOf(value);
            String concat;
            if (value3.length() != 0) {
                concat = value2.concat(value3);
            }
            else {
                concat = new String(value2);
            }
            this.zzbrd = concat;
            final String value4 = String.valueOf(zzbrb.zzbrp);
            value = String.valueOf(value);
            String concat2;
            if (value.length() != 0) {
                concat2 = value4.concat(value);
            }
            else {
                concat2 = new String(value4);
            }
            this.zzbrc = concat2;
            this.zzbre = zzbre;
            return;
        }
        throw new IllegalArgumentException("Must pass a valid SharedPreferences file name or ContentProvider URI");
    }
    
    public static void init(Context zzri) {
        while (true) {
            while (true) {
                Context applicationContext = null;
                Label_0063: {
                    synchronized (zzsl.zzbqy) {
                        if (Build$VERSION.SDK_INT < 24 || !zzri.isDeviceProtectedStorage()) {
                            applicationContext = zzri.getApplicationContext();
                            if (applicationContext != null) {
                                break Label_0063;
                            }
                        }
                        if (zzsl.zzri != zzri) {
                            zzsl.zzbra = null;
                        }
                        zzsl.zzri = zzri;
                        // monitorexit(zzsl.zzbqy)
                        zzsl.zzbqz = false;
                        return;
                    }
                }
                zzri = applicationContext;
                continue;
            }
        }
    }
    
    private static zzsl<Double> zza(final zzsv zzsv, final String s, final double n) {
        return new zzss(zzsv, s, n);
    }
    
    private static zzsl<Integer> zza(final zzsv zzsv, final String s, final int n) {
        return new zzsq(zzsv, s, n);
    }
    
    private static zzsl<Long> zza(final zzsv zzsv, final String s, final long n) {
        return new zzsp(zzsv, s, n);
    }
    
    private static zzsl<String> zza(final zzsv zzsv, final String s, final String s2) {
        return new zzst(zzsv, s, s2);
    }
    
    private static zzsl<Boolean> zza(final zzsv zzsv, final String s, final boolean b) {
        return new zzsr(zzsv, s, b);
    }
    
    private static <V> V zza(final zzsu<V> zzsu) {
        try {
            return zzsu.zztj();
        }
        catch (SecurityException ex) {
            final long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return zzsu.zztj();
            }
            finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }
    
    static boolean zzd(final String s, final boolean b) {
        try {
            return zzth() && zza((zzsu<Boolean>)new zzso(s, false));
        }
        catch (SecurityException ex) {
            Log.e("PhenotypeFlag", "Unable to read GServices, returning default value.", (Throwable)ex);
            return false;
        }
    }
    
    @Nullable
    private final T zzte() {
        if (!zzd("gms:phenotype:phenotype_flag:debug_bypass_phenotype", false)) {
            if (this.zzbrb.zzbrn != null) {
                final zzsi zztg = this.zztg();
                if (zztg != null) {
                    final String s = zza((zzsu<String>)new zzsm(this, zztg));
                    if (s != null) {
                        return this.zzfj(s);
                    }
                }
            }
        }
        else {
            final String value = String.valueOf(this.zzbrc);
            String concat;
            if (value.length() != 0) {
                concat = "Bypass reading Phenotype values for flag: ".concat(value);
            }
            else {
                concat = new String("Bypass reading Phenotype values for flag: ");
            }
            Log.w("PhenotypeFlag", concat);
        }
        return null;
    }
    
    @Nullable
    private final T zztf() {
        if (zzth()) {
            try {
                final String s = zza((zzsu<String>)new zzsn(this));
                if (s != null) {
                    return this.zzfj(s);
                }
            }
            catch (SecurityException ex) {
                final String value = String.valueOf(this.zzbrc);
                String concat;
                if (value.length() != 0) {
                    concat = "Unable to read GServices for flag: ".concat(value);
                }
                else {
                    concat = new String("Unable to read GServices for flag: ");
                }
                Log.e("PhenotypeFlag", concat, (Throwable)ex);
            }
        }
        return null;
    }
    
    private final zzsi zztg() {
        if (this.zzbrg == null) {
            try {
                this.zzbrg = zzsi.zza(zzsl.zzri.getContentResolver(), this.zzbrb.zzbrn);
            }
            catch (SecurityException ex) {}
        }
        return this.zzbrg;
    }
    
    private static boolean zzth() {
        if (zzsl.zzbra == null) {
            final Context zzri = zzsl.zzri;
            boolean b = false;
            if (zzri == null) {
                return false;
            }
            if (PermissionChecker.checkSelfPermission(zzri, "com.google.android.providers.gsf.permission.READ_GSERVICES") == 0) {
                b = true;
            }
            zzsl.zzbra = b;
        }
        return zzsl.zzbra;
    }
    
    public final T get() {
        if (zzsl.zzri == null) {
            throw new IllegalStateException("Must call PhenotypeFlag.init() first");
        }
        final T zzte = this.zzte();
        if (zzte != null) {
            return zzte;
        }
        final T zztf = this.zztf();
        if (zztf != null) {
            return zztf;
        }
        return this.zzbre;
    }
    
    public final T getDefaultValue() {
        return this.zzbre;
    }
    
    protected abstract T zzfj(final String p0);
}

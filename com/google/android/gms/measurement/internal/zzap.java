package com.google.android.gms.measurement.internal;

import com.google.android.gms.measurement.*;
import android.text.*;
import android.content.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.util.*;
import android.util.*;

public final class zzap extends zzcp
{
    private long zzadt;
    private char zzalw;
    private String zzalx;
    private final zzar zzaly;
    private final zzar zzalz;
    private final zzar zzama;
    private final zzar zzamb;
    private final zzar zzamc;
    private final zzar zzamd;
    private final zzar zzame;
    private final zzar zzamf;
    private final zzar zzamg;
    
    zzap(final zzbt zzbt) {
        super(zzbt);
        this.zzalw = '\0';
        this.zzadt = -1L;
        this.zzaly = new zzar(this, 6, false, false);
        this.zzalz = new zzar(this, 6, true, false);
        this.zzama = new zzar(this, 6, false, true);
        this.zzamb = new zzar(this, 5, false, false);
        this.zzamc = new zzar(this, 5, true, false);
        this.zzamd = new zzar(this, 5, false, true);
        this.zzame = new zzar(this, 4, false, false);
        this.zzamf = new zzar(this, 3, false, false);
        this.zzamg = new zzar(this, 2, false, false);
    }
    
    private static String zza(final boolean b, Object o) {
        final String s = "";
        if (o == null) {
            return "";
        }
        Object value = o;
        if (o instanceof Integer) {
            value = o;
        }
        final boolean b2 = value instanceof Long;
        int i = 0;
        if (b2) {
            if (!b) {
                return String.valueOf(value);
            }
            final Long n = (Long)value;
            if (Math.abs(n) < 100L) {
                return String.valueOf(value);
            }
            String s2 = s;
            if (String.valueOf(value).charAt(0) == '-') {
                s2 = "-";
            }
            final String value2 = String.valueOf(Math.abs(n));
            final long round = Math.round(Math.pow(10.0, value2.length() - 1));
            final long round2 = Math.round(Math.pow(10.0, value2.length()) - 1.0);
            final StringBuilder sb = new StringBuilder(s2.length() + 43 + s2.length());
            sb.append(s2);
            sb.append(round);
            sb.append("...");
            sb.append(s2);
            sb.append(round2);
            return sb.toString();
        }
        else {
            if (value instanceof Boolean) {
                return String.valueOf(value);
            }
            if (value instanceof Throwable) {
                final Throwable t = (Throwable)value;
                String s3;
                if (b) {
                    s3 = t.getClass().getName();
                }
                else {
                    s3 = t.toString();
                }
                o = new StringBuilder(s3);
                final String zzbw = zzbw(AppMeasurement.class.getCanonicalName());
                final String zzbw2 = zzbw(zzbt.class.getCanonicalName());
                for (StackTraceElement[] stackTrace = t.getStackTrace(); i < stackTrace.length; ++i) {
                    final StackTraceElement stackTraceElement = stackTrace[i];
                    if (!stackTraceElement.isNativeMethod()) {
                        final String className = stackTraceElement.getClassName();
                        if (className != null) {
                            final String zzbw3 = zzbw(className);
                            if (zzbw3.equals(zzbw) || zzbw3.equals(zzbw2)) {
                                ((StringBuilder)o).append(": ");
                                ((StringBuilder)o).append(stackTraceElement);
                                break;
                            }
                        }
                    }
                }
                return ((StringBuilder)o).toString();
            }
            if (value instanceof zzas) {
                return ((zzas)value).zzamp;
            }
            if (b) {
                return "-";
            }
            return String.valueOf(value);
        }
    }
    
    static String zza(final boolean b, String s, final Object o, final Object o2, final Object o3) {
        final String s2 = "";
        String s3 = s;
        if (s == null) {
            s3 = "";
        }
        final String zza = zza(b, o);
        final String zza2 = zza(b, o2);
        final String zza3 = zza(b, o3);
        final StringBuilder sb = new StringBuilder();
        s = s2;
        if (!TextUtils.isEmpty((CharSequence)s3)) {
            sb.append(s3);
            s = ": ";
        }
        String s4 = s;
        if (!TextUtils.isEmpty((CharSequence)zza)) {
            sb.append(s);
            sb.append(zza);
            s4 = ", ";
        }
        s = s4;
        if (!TextUtils.isEmpty((CharSequence)zza2)) {
            sb.append(s4);
            sb.append(zza2);
            s = ", ";
        }
        if (!TextUtils.isEmpty((CharSequence)zza3)) {
            sb.append(s);
            sb.append(zza3);
        }
        return sb.toString();
    }
    
    protected static Object zzbv(final String s) {
        if (s == null) {
            return null;
        }
        return new zzas(s);
    }
    
    private static String zzbw(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return "";
        }
        final int lastIndex = s.lastIndexOf(46);
        if (lastIndex == -1) {
            return s;
        }
        return s.substring(0, lastIndex);
    }
    
    private final String zzjm() {
        synchronized (this) {
            if (this.zzalx == null) {
                String zzalx;
                if (this.zzadj.zzkm() != null) {
                    zzalx = this.zzadj.zzkm();
                }
                else {
                    zzalx = zzn.zzht();
                }
                this.zzalx = zzalx;
            }
            return this.zzalx;
        }
    }
    
    protected final boolean isLoggable(final int n) {
        return Log.isLoggable(this.zzjm(), n);
    }
    
    protected final void zza(final int n, final String s) {
        Log.println(n, this.zzjm(), s);
    }
    
    protected final void zza(final int n, final boolean b, final boolean b2, String s, final Object o, final Object o2, final Object o3) {
        if (!b && this.isLoggable(n)) {
            this.zza(n, zza(false, s, o, o2, o3));
        }
        if (!b2 && n >= 5) {
            Preconditions.checkNotNull(s);
            final zzbo zzkh = this.zzadj.zzkh();
            if (zzkh == null) {
                s = "Scheduler not set. Not logging error/warn";
            }
            else {
                if (zzkh.isInitialized()) {
                    int n2;
                    if ((n2 = n) < 0) {
                        n2 = 0;
                    }
                    if (n2 >= 9) {
                        n2 = 8;
                    }
                    zzkh.zzc(new zzaq(this, n2, s, o, o2, o3));
                    return;
                }
                s = "Scheduler not initialized. Not logging error/warn";
            }
            this.zza(6, s);
        }
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
    
    public final zzar zzjd() {
        return this.zzaly;
    }
    
    public final zzar zzje() {
        return this.zzalz;
    }
    
    public final zzar zzjf() {
        return this.zzama;
    }
    
    public final zzar zzjg() {
        return this.zzamb;
    }
    
    public final zzar zzjh() {
        return this.zzamc;
    }
    
    public final zzar zzji() {
        return this.zzamd;
    }
    
    public final zzar zzjj() {
        return this.zzame;
    }
    
    public final zzar zzjk() {
        return this.zzamf;
    }
    
    public final zzar zzjl() {
        return this.zzamg;
    }
    
    public final String zzjn() {
        final Pair<String, Long> zzfm = this.zzgp().zzand.zzfm();
        if (zzfm != null && zzfm != zzba.zzanc) {
            final String value = String.valueOf(zzfm.second);
            final String s = (String)zzfm.first;
            final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 1 + String.valueOf(s).length());
            sb.append(value);
            sb.append(":");
            sb.append(s);
            return sb.toString();
        }
        return null;
    }
}

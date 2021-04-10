package com.google.firebase.analytics.connector.internal;

import com.google.android.gms.measurement.*;
import com.google.android.gms.common.util.*;
import android.os.*;
import java.util.*;

public final class zzc
{
    private static final Set<String> zzbsm;
    private static final List<String> zzbsn;
    private static final List<String> zzbso;
    private static final List<String> zzbsp;
    private static final List<String> zzbsq;
    private static final List<String> zzbsr;
    
    static {
        zzbsm = new HashSet<String>(Arrays.asList("_in", "_xa", "_xu", "_aq", "_aa", "_ai", "_ac", "campaign_details", "_ug", "_iapx", "_exp_set", "_exp_clear", "_exp_activate", "_exp_timeout", "_exp_expire"));
        zzbsn = Arrays.asList("_e", "_f", "_iap", "_s", "_au", "_ui", "_cd", "app_open");
        zzbso = Arrays.asList("auto", "app", "am");
        zzbsp = Arrays.asList("_r", "_dbg");
        zzbsq = Arrays.asList((String[])ArrayUtils.concat((T[][])new String[][] { AppMeasurement.UserProperty.zzado, AppMeasurement.UserProperty.zzadp }));
        zzbsr = Arrays.asList("^_ltv_[A-Z]{3}$", "^_cc[1-5]{1}$");
    }
    
    public static boolean zza(final String s, final Bundle bundle) {
        if (zzc.zzbsn.contains(s)) {
            return false;
        }
        if (bundle != null) {
            final Iterator<String> iterator = zzc.zzbsp.iterator();
            while (iterator.hasNext()) {
                if (bundle.containsKey((String)iterator.next())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean zzb(String s, final String s2, final Bundle bundle) {
        if (!"_cmp".equals(s2)) {
            return true;
        }
        if (!zzfo(s)) {
            return false;
        }
        if (bundle == null) {
            return false;
        }
        final Iterator<String> iterator = zzc.zzbsp.iterator();
        while (iterator.hasNext()) {
            if (bundle.containsKey((String)iterator.next())) {
                return false;
            }
        }
        int n = -1;
        final int hashCode = s.hashCode();
        if (hashCode != 101200) {
            if (hashCode != 101230) {
                if (hashCode == 3142703) {
                    if (s.equals("fiam")) {
                        n = 2;
                    }
                }
            }
            else if (s.equals("fdl")) {
                n = 1;
            }
        }
        else if (s.equals("fcm")) {
            n = 0;
        }
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    return false;
                }
                s = "fiam_integration";
            }
            else {
                s = "fdl_integration";
            }
        }
        else {
            s = "fcm_integration";
        }
        bundle.putString("_cis", s);
        return true;
    }
    
    public static boolean zzfo(final String s) {
        return !zzc.zzbso.contains(s);
    }
    
    public static boolean zzy(final String s, final String s2) {
        if ("_ce1".equals(s2) || "_ce2".equals(s2)) {
            return s.equals("fcm") || s.equals("frc");
        }
        if ("_ln".equals(s2)) {
            return s.equals("fcm") || s.equals("fiam");
        }
        if (zzc.zzbsq.contains(s2)) {
            return false;
        }
        final Iterator<String> iterator = zzc.zzbsr.iterator();
        while (iterator.hasNext()) {
            if (s2.matches(iterator.next())) {
                return false;
            }
        }
        return true;
    }
}

package com.google.android.gms.measurement.internal;

import java.util.*;
import android.support.v4.util.*;
import java.io.*;
import android.text.*;
import com.google.android.gms.measurement.*;
import com.google.android.gms.common.internal.*;
import android.content.*;
import android.database.sqlite.*;
import com.google.android.gms.internal.measurement.*;
import com.google.android.gms.common.util.*;

public final class zzbn extends zzez implements zzp
{
    private static int zzaon = 65535;
    private static int zzaoo = 2;
    private final Map<String, Map<String, String>> zzaop;
    private final Map<String, Map<String, Boolean>> zzaoq;
    private final Map<String, Map<String, Boolean>> zzaor;
    private final Map<String, zzgb> zzaos;
    private final Map<String, Map<String, Integer>> zzaot;
    private final Map<String, String> zzaou;
    
    zzbn(final zzfa zzfa) {
        super(zzfa);
        this.zzaop = new ArrayMap<String, Map<String, String>>();
        this.zzaoq = new ArrayMap<String, Map<String, Boolean>>();
        this.zzaor = new ArrayMap<String, Map<String, Boolean>>();
        this.zzaos = new ArrayMap<String, zzgb>();
        this.zzaou = new ArrayMap<String, String>();
        this.zzaot = new ArrayMap<String, Map<String, Integer>>();
    }
    
    private final zzgb zza(final String s, final byte[] array) {
        if (array == null) {
            return new zzgb();
        }
        final zzyx zzj = zzyx.zzj(array, 0, array.length);
        final zzgb zzgb = new zzgb();
        try {
            zzgb.zza(zzj);
            this.zzgo().zzjl().zze("Parsed config. version, gmp_app_id", zzgb.zzawe, zzgb.zzafx);
            return zzgb;
        }
        catch (IOException ex) {
            this.zzgo().zzjg().zze("Unable to merge remote config. appId", zzap.zzbv(s), ex);
            return new zzgb();
        }
    }
    
    private static Map<String, String> zza(final zzgb zzgb) {
        final ArrayMap<String, String> arrayMap = new ArrayMap<String, String>();
        if (zzgb != null && zzgb.zzawg != null) {
            final zzgc[] zzawg = zzgb.zzawg;
            for (int length = zzawg.length, i = 0; i < length; ++i) {
                final zzgc zzgc = zzawg[i];
                if (zzgc != null) {
                    arrayMap.put(zzgc.zzoj, zzgc.value);
                }
            }
        }
        return arrayMap;
    }
    
    private final void zza(final String s, final zzgb zzgb) {
        final ArrayMap<String, Boolean> arrayMap = new ArrayMap<String, Boolean>();
        final ArrayMap<String, Boolean> arrayMap2 = new ArrayMap<String, Boolean>();
        final ArrayMap<String, Integer> arrayMap3 = new ArrayMap<String, Integer>();
        if (zzgb != null && zzgb.zzawh != null) {
            final zzga[] zzawh = zzgb.zzawh;
            for (int length = zzawh.length, i = 0; i < length; ++i) {
                final zzga zzga = zzawh[i];
                if (TextUtils.isEmpty((CharSequence)zzga.name)) {
                    this.zzgo().zzjg().zzbx("EventConfig contained null event name");
                }
                else {
                    final String zzal = AppMeasurement.Event.zzal(zzga.name);
                    if (!TextUtils.isEmpty((CharSequence)zzal)) {
                        zzga.name = zzal;
                    }
                    arrayMap.put(zzga.name, zzga.zzawb);
                    arrayMap2.put(zzga.name, zzga.zzawc);
                    if (zzga.zzawd != null) {
                        if (zzga.zzawd >= zzbn.zzaoo && zzga.zzawd <= zzbn.zzaon) {
                            arrayMap3.put(zzga.name, zzga.zzawd);
                        }
                        else {
                            this.zzgo().zzjg().zze("Invalid sampling rate. Event name, sample rate", zzga.name, zzga.zzawd);
                        }
                    }
                }
            }
        }
        this.zzaoq.put(s, arrayMap);
        this.zzaor.put(s, arrayMap2);
        this.zzaot.put(s, arrayMap3);
    }
    
    private final void zzce(final String s) {
        this.zzcl();
        this.zzaf();
        Preconditions.checkNotEmpty(s);
        if (this.zzaos.get(s) == null) {
            final byte[] zzbn = this.zzjq().zzbn(s);
            if (zzbn == null) {
                this.zzaop.put(s, null);
                this.zzaoq.put(s, null);
                this.zzaor.put(s, null);
                this.zzaos.put(s, null);
                this.zzaou.put(s, null);
                this.zzaot.put(s, null);
                return;
            }
            final zzgb zza = this.zza(s, zzbn);
            this.zzaop.put(s, zza(zza));
            this.zza(s, zza);
            this.zzaos.put(s, zza);
            this.zzaou.put(s, null);
        }
    }
    
    protected final boolean zza(final String s, byte[] array, String zzjq) {
        this.zzcl();
        this.zzaf();
        Preconditions.checkNotEmpty(s);
        final zzgb zza = this.zza(s, array);
        if (zza == null) {
            return false;
        }
        this.zza(s, zza);
        this.zzaos.put(s, zza);
        this.zzaou.put(s, zzjq);
        this.zzaop.put(s, zza(zza));
        final zzj zzjp = this.zzjp();
        final zzfu[] zzawi = zza.zzawi;
        Preconditions.checkNotNull(zzawi);
        for (int length = zzawi.length, i = 0; i < length; ++i) {
            final zzfu zzfu = zzawi[i];
            final zzfv[] zzava = zzfu.zzava;
            for (int length2 = zzava.length, j = 0; j < length2; ++j) {
                final zzfv zzfv = zzava[j];
                final String zzal = AppMeasurement.Event.zzal(zzfv.zzavf);
                if (zzal != null) {
                    zzfv.zzavf = zzal;
                }
                final zzfw[] zzavg = zzfv.zzavg;
                for (int length3 = zzavg.length, k = 0; k < length3; ++k) {
                    final zzfw zzfw = zzavg[k];
                    final String zzal2 = AppMeasurement.Param.zzal(zzfw.zzavn);
                    if (zzal2 != null) {
                        zzfw.zzavn = zzal2;
                    }
                }
            }
            final zzfy[] zzauz = zzfu.zzauz;
            for (int length4 = zzauz.length, l = 0; l < length4; ++l) {
                final zzfy zzfy = zzauz[l];
                final String zzal3 = AppMeasurement.UserProperty.zzal(zzfy.zzavu);
                if (zzal3 != null) {
                    zzfy.zzavu = zzal3;
                }
            }
        }
        zzjp.zzjq().zza(s, zzawi);
        try {
            zza.zzawi = null;
            final int zzvu = zza.zzvu();
            final byte[] array2 = new byte[zzvu];
            zza.zza(zzyy.zzk(array2, 0, zzvu));
            array = array2;
        }
        catch (IOException ex) {
            this.zzgo().zzjg().zze("Unable to serialize reduced-size config. Storing full config instead. appId", zzap.zzbv(s), ex);
        }
        zzjq = (String)this.zzjq();
        Preconditions.checkNotEmpty(s);
        ((zzco)zzjq).zzaf();
        ((zzez)zzjq).zzcl();
        final ContentValues contentValues = new ContentValues();
        contentValues.put("remote_config", array);
        try {
            if (((zzq)zzjq).getWritableDatabase().update("apps", contentValues, "app_id = ?", new String[] { s }) == 0L) {
                ((zzco)zzjq).zzgo().zzjd().zzg("Failed to update remote config (got 0). appId", zzap.zzbv(s));
                return true;
            }
        }
        catch (SQLiteException ex2) {
            ((zzco)zzjq).zzgo().zzjd().zze("Error storing remote config. appId", zzap.zzbv(s), ex2);
        }
        return true;
    }
    
    protected final zzgb zzcf(final String s) {
        this.zzcl();
        this.zzaf();
        Preconditions.checkNotEmpty(s);
        this.zzce(s);
        return this.zzaos.get(s);
    }
    
    protected final String zzcg(final String s) {
        this.zzaf();
        return this.zzaou.get(s);
    }
    
    protected final void zzch(final String s) {
        this.zzaf();
        this.zzaou.put(s, null);
    }
    
    final void zzci(final String s) {
        this.zzaf();
        this.zzaos.remove(s);
    }
    
    final long zzcj(final String s) {
        final String zzf = this.zzf(s, "measurement.account.time_zone_offset_minutes");
        if (!TextUtils.isEmpty((CharSequence)zzf)) {
            try {
                return Long.parseLong(zzf);
            }
            catch (NumberFormatException ex) {
                this.zzgo().zzjg().zze("Unable to parse timezone offset. appId", zzap.zzbv(s), ex);
            }
        }
        return 0L;
    }
    
    final boolean zzck(final String s) {
        return "1".equals(this.zzf(s, "measurement.upload.blacklist_internal"));
    }
    
    final boolean zzcl(final String s) {
        return "1".equals(this.zzf(s, "measurement.upload.blacklist_public"));
    }
    
    @Override
    public final String zzf(final String s, final String s2) {
        this.zzaf();
        this.zzce(s);
        final Map<String, String> map = this.zzaop.get(s);
        if (map != null) {
            return map.get(s2);
        }
        return null;
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
    
    final boolean zzo(final String s, final String s2) {
        this.zzaf();
        this.zzce(s);
        if (this.zzck(s) && zzfk.zzcv(s2)) {
            return true;
        }
        if (this.zzcl(s) && zzfk.zzcq(s2)) {
            return true;
        }
        final Map<String, Boolean> map = this.zzaoq.get(s);
        if (map != null) {
            final Boolean b = map.get(s2);
            return b != null && b;
        }
        return false;
    }
    
    final boolean zzp(final String s, final String s2) {
        this.zzaf();
        this.zzce(s);
        if ("ecommerce_purchase".equals(s2)) {
            return true;
        }
        final Map<String, Boolean> map = this.zzaor.get(s);
        if (map != null) {
            final Boolean b = map.get(s2);
            return b != null && b;
        }
        return false;
    }
    
    final int zzq(final String s, final String s2) {
        this.zzaf();
        this.zzce(s);
        final Map<String, Integer> map = this.zzaot.get(s);
        if (map == null) {
            return 1;
        }
        final Integer n = map.get(s2);
        if (n == null) {
            return 1;
        }
        return n;
    }
}

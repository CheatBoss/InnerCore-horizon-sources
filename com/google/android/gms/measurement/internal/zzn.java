package com.google.android.gms.measurement.internal;

import android.content.*;
import android.text.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.wrappers.*;
import android.content.pm.*;
import com.google.android.gms.common.util.*;
import java.lang.reflect.*;

public final class zzn extends zzco
{
    private Boolean zzahf;
    private zzp zzahg;
    private Boolean zzyk;
    
    zzn(final zzbt zzbt) {
        super(zzbt);
        this.zzahg = zzo.zzahh;
        zzaf.zza(zzbt);
    }
    
    static String zzht() {
        return zzaf.zzajd.get();
    }
    
    public static long zzhw() {
        return zzaf.zzakg.get();
    }
    
    public static long zzhx() {
        return zzaf.zzajg.get();
    }
    
    public static boolean zzhz() {
        return zzaf.zzajc.get();
    }
    
    static boolean zzia() {
        return zzaf.zzalb.get();
    }
    
    static boolean zzic() {
        return zzaf.zzald.get();
    }
    
    public final long zza(String zzf, final zzaf.zza<Long> zza) {
        if (zzf != null) {
            zzf = this.zzahg.zzf(zzf, zza.getKey());
            if (!TextUtils.isEmpty((CharSequence)zzf)) {
                try {
                    return zza.get(Long.parseLong(zzf));
                }
                catch (NumberFormatException ex) {}
            }
        }
        return zza.get();
    }
    
    final void zza(final zzp zzahg) {
        this.zzahg = zzahg;
    }
    
    public final boolean zza(final zzaf.zza<Boolean> zza) {
        return this.zzd(null, zza);
    }
    
    public final int zzat(final String s) {
        return this.zzb(s, zzaf.zzajr);
    }
    
    final Boolean zzau(final String s) {
        Preconditions.checkNotEmpty(s);
        try {
            if (this.getContext().getPackageManager() == null) {
                this.zzgo().zzjd().zzbx("Failed to load metadata: PackageManager is null");
                return null;
            }
            final ApplicationInfo applicationInfo = Wrappers.packageManager(this.getContext()).getApplicationInfo(this.getContext().getPackageName(), 128);
            if (applicationInfo == null) {
                this.zzgo().zzjd().zzbx("Failed to load metadata: ApplicationInfo is null");
                return null;
            }
            if (applicationInfo.metaData == null) {
                this.zzgo().zzjd().zzbx("Failed to load metadata: Metadata bundle is null");
                return null;
            }
            if (!applicationInfo.metaData.containsKey(s)) {
                return null;
            }
            return applicationInfo.metaData.getBoolean(s);
        }
        catch (PackageManager$NameNotFoundException ex) {
            this.zzgo().zzjd().zzg("Failed to load metadata: Package name not found", ex);
            return null;
        }
    }
    
    public final boolean zzav(final String s) {
        return "1".equals(this.zzahg.zzf(s, "gaia_collection_enabled"));
    }
    
    public final boolean zzaw(final String s) {
        return "1".equals(this.zzahg.zzf(s, "measurement.event_sampling_enabled"));
    }
    
    final boolean zzax(final String s) {
        return this.zzd(s, zzaf.zzakp);
    }
    
    final boolean zzay(final String s) {
        return this.zzd(s, zzaf.zzakr);
    }
    
    final boolean zzaz(final String s) {
        return this.zzd(s, zzaf.zzaks);
    }
    
    public final int zzb(String zzf, final zzaf.zza<Integer> zza) {
        if (zzf != null) {
            zzf = this.zzahg.zzf(zzf, zza.getKey());
            if (!TextUtils.isEmpty((CharSequence)zzf)) {
                try {
                    return zza.get(Integer.parseInt(zzf));
                }
                catch (NumberFormatException ex) {}
            }
        }
        return zza.get();
    }
    
    final boolean zzba(final String s) {
        return this.zzd(s, zzaf.zzakk);
    }
    
    final boolean zzbc(final String s) {
        return this.zzd(s, zzaf.zzakt);
    }
    
    final boolean zzbd(final String s) {
        return this.zzd(s, zzaf.zzaku);
    }
    
    final boolean zzbe(final String s) {
        return this.zzd(s, zzaf.zzakx);
    }
    
    final boolean zzbf(final String s) {
        return this.zzd(s, zzaf.zzaky);
    }
    
    final boolean zzbg(final String s) {
        return this.zzd(s, zzaf.zzala);
    }
    
    final boolean zzbh(final String s) {
        return this.zzd(s, zzaf.zzakz);
    }
    
    final boolean zzbi(final String s) {
        return this.zzd(s, zzaf.zzale);
    }
    
    final boolean zzbj(final String s) {
        return this.zzd(s, zzaf.zzalf);
    }
    
    public final boolean zzd(String zzf, final zzaf.zza<Boolean> zza) {
        if (zzf != null) {
            zzf = this.zzahg.zzf(zzf, zza.getKey());
            if (!TextUtils.isEmpty((CharSequence)zzf)) {
                return zza.get(Boolean.parseBoolean(zzf));
            }
        }
        return zza.get();
    }
    
    public final boolean zzdw() {
        if (this.zzyk == null) {
            while (true) {
                while (true) {
                    Label_0107: {
                        synchronized (this) {
                            if (this.zzyk == null) {
                                final ApplicationInfo applicationInfo = this.getContext().getApplicationInfo();
                                final String myProcessName = ProcessUtils.getMyProcessName();
                                if (applicationInfo != null) {
                                    final String processName = applicationInfo.processName;
                                    if (processName == null || !processName.equals(myProcessName)) {
                                        break Label_0107;
                                    }
                                    final boolean b = true;
                                    this.zzyk = b;
                                }
                                if (this.zzyk == null) {
                                    this.zzyk = Boolean.TRUE;
                                    this.zzgo().zzjd().zzbx("My process not in the list of running processes");
                                }
                            }
                        }
                        break;
                    }
                    final boolean b = false;
                    continue;
                }
            }
        }
        return this.zzyk;
    }
    
    public final boolean zze(final String s, final zzaf.zza<Boolean> zza) {
        return this.zzd(s, zza);
    }
    
    public final long zzhc() {
        this.zzgr();
        return 13001L;
    }
    
    public final boolean zzhu() {
        this.zzgr();
        final Boolean zzau = this.zzau("firebase_analytics_collection_deactivated");
        return zzau != null && zzau;
    }
    
    public final Boolean zzhv() {
        this.zzgr();
        return this.zzau("firebase_analytics_collection_enabled");
    }
    
    public final String zzhy() {
        try {
            return (String)Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class).invoke(null, "debug.firebase.analytics.app", "");
        }
        catch (InvocationTargetException ex) {
            this.zzgo().zzjd();
        }
        catch (IllegalAccessException ex) {
            this.zzgo().zzjd();
            goto Label_0065;
        }
        catch (NoSuchMethodException ex) {
            this.zzgo().zzjd();
            goto Label_0065;
        }
        catch (ClassNotFoundException ex) {
            this.zzgo().zzjd();
            goto Label_0065;
        }
    }
    
    final boolean zzib() {
        if (this.zzahf == null && (this.zzahf = this.zzau("app_measurement_lite")) == null) {
            this.zzahf = false;
        }
        return this.zzahf || !this.zzadj.zzkn();
    }
}

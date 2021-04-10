package com.google.android.gms.measurement.internal;

import java.nio.channels.*;
import com.google.android.gms.common.internal.*;
import java.nio.*;
import android.content.*;
import com.google.android.gms.common.wrappers.*;
import android.text.*;
import android.net.*;
import android.support.v4.util.*;
import java.net.*;
import android.provider.*;
import android.os.*;
import android.util.*;
import java.io.*;
import android.database.sqlite.*;
import com.google.android.gms.internal.measurement.*;
import com.google.android.gms.common.util.*;
import java.util.*;
import android.content.pm.*;
import java.util.concurrent.*;

public class zzfa implements zzcq
{
    private static volatile zzfa zzatc;
    private final zzbt zzadj;
    private zzbn zzatd;
    private zzat zzate;
    private zzq zzatf;
    private zzay zzatg;
    private zzew zzath;
    private zzj zzati;
    private final zzfg zzatj;
    private boolean zzatk;
    private long zzatl;
    private List<Runnable> zzatm;
    private int zzatn;
    private int zzato;
    private boolean zzatp;
    private boolean zzatq;
    private boolean zzatr;
    private FileLock zzats;
    private FileChannel zzatt;
    private List<Long> zzatu;
    private List<Long> zzatv;
    private long zzatw;
    private boolean zzvz;
    
    private zzfa(final zzff zzff) {
        this(zzff, null);
    }
    
    private zzfa(final zzff zzff, final zzbt zzbt) {
        this.zzvz = false;
        Preconditions.checkNotNull(zzff);
        this.zzadj = zzbt.zza(zzff.zzri, null);
        this.zzatw = -1L;
        final zzfg zzatj = new zzfg(this);
        zzatj.zzq();
        this.zzatj = zzatj;
        final zzat zzate = new zzat(this);
        zzate.zzq();
        this.zzate = zzate;
        final zzbn zzatd = new zzbn(this);
        zzatd.zzq();
        this.zzatd = zzatd;
        this.zzadj.zzgn().zzc(new zzfb(this, zzff));
    }
    
    private final int zza(final FileChannel fileChannel) {
        this.zzaf();
        if (fileChannel != null) {
            if (fileChannel.isOpen()) {
                final ByteBuffer allocate = ByteBuffer.allocate(4);
                try {
                    fileChannel.position(0L);
                    final int read = fileChannel.read(allocate);
                    if (read == 4) {
                        allocate.flip();
                        return allocate.getInt();
                    }
                    if (read != -1) {
                        this.zzadj.zzgo().zzjg().zzg("Unexpected data length. Bytes read", read);
                        return 0;
                    }
                    return 0;
                }
                catch (IOException ex) {
                    this.zzadj.zzgo().zzjd().zzg("Failed to read from channel", ex);
                    return 0;
                }
            }
        }
        this.zzadj.zzgo().zzjd().zzbx("Bad channel to read from");
        return 0;
    }
    
    private final zzh zza(final Context context, final String s, final String s2, final boolean b, final boolean b2, final boolean b3, long n, final String s3) {
        final String s4 = "Unknown";
        final PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            this.zzadj.zzgo().zzjd().zzbx("PackageManager is null, can not log app install information");
            return null;
        }
        String installerPackageName;
        try {
            installerPackageName = packageManager.getInstallerPackageName(s);
        }
        catch (IllegalArgumentException ex) {
            this.zzadj.zzgo().zzjd().zzg("Error retrieving installer package name. appId", zzap.zzbv(s));
            installerPackageName = "Unknown";
        }
        String s5;
        if (installerPackageName == null) {
            s5 = "manual_install";
        }
        else {
            s5 = installerPackageName;
            if ("com.android.vending".equals(installerPackageName)) {
                s5 = "";
            }
        }
        String s6 = s4;
        try {
            final PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(s, 0);
            int versionCode;
            String s7;
            if (packageInfo != null) {
                s6 = s4;
                final CharSequence applicationLabel = Wrappers.packageManager(context).getApplicationLabel(s);
                String string = s4;
                s6 = s4;
                if (!TextUtils.isEmpty(applicationLabel)) {
                    s6 = s4;
                    string = applicationLabel.toString();
                }
                s6 = string;
                final String versionName = packageInfo.versionName;
                s6 = string;
                versionCode = packageInfo.versionCode;
                s7 = versionName;
            }
            else {
                versionCode = Integer.MIN_VALUE;
                s7 = "Unknown";
            }
            this.zzadj.zzgr();
            if (!this.zzadj.zzgq().zzbe(s)) {
                n = 0L;
            }
            return new zzh(s, s2, s7, versionCode, s5, this.zzadj.zzgq().zzhc(), this.zzadj.zzgm().zzd(context, s), null, b, false, "", 0L, n, 0, b2, b3, false, s3);
        }
        catch (PackageManager$NameNotFoundException ex2) {
            this.zzadj.zzgo().zzjd().zze("Error retrieving newly installed package info. appId, appName", zzap.zzbv(s), s6);
            return null;
        }
    }
    
    private static void zza(final zzez zzez) {
        if (zzez == null) {
            throw new IllegalStateException("Upload Component not created");
        }
        if (zzez.isInitialized()) {
            return;
        }
        final String value = String.valueOf(zzez.getClass());
        final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 27);
        sb.append("Component not initialized: ");
        sb.append(value);
        throw new IllegalStateException(sb.toString());
    }
    
    private final void zza(final zzff zzff) {
        this.zzadj.zzgn().zzaf();
        final zzq zzatf = new zzq(this);
        zzatf.zzq();
        this.zzatf = zzatf;
        this.zzadj.zzgq().zza(this.zzatd);
        final zzj zzati = new zzj(this);
        zzati.zzq();
        this.zzati = zzati;
        final zzew zzath = new zzew(this);
        zzath.zzq();
        this.zzath = zzath;
        this.zzatg = new zzay(this);
        if (this.zzatn != this.zzato) {
            this.zzadj.zzgo().zzjd().zze("Not all upload components initialized", this.zzatn, this.zzato);
        }
        this.zzvz = true;
    }
    
    private final boolean zza(final int n, final FileChannel fileChannel) {
        this.zzaf();
        if (fileChannel != null) {
            if (fileChannel.isOpen()) {
                final ByteBuffer allocate = ByteBuffer.allocate(4);
                allocate.putInt(n);
                allocate.flip();
                try {
                    fileChannel.truncate(0L);
                    fileChannel.write(allocate);
                    fileChannel.force(true);
                    if (fileChannel.size() != 4L) {
                        this.zzadj.zzgo().zzjd().zzg("Error writing to channel. Bytes written", fileChannel.size());
                    }
                    return true;
                }
                catch (IOException ex) {
                    this.zzadj.zzgo().zzjd().zzg("Failed to write to channel", ex);
                    return false;
                }
            }
        }
        this.zzadj.zzgo().zzjd().zzbx("Bad channel to read from");
        return false;
    }
    
    private final boolean zza(final String s, final zzad zzad) {
        final String string = zzad.zzaid.getString("currency");
        long n3;
        if ("ecommerce_purchase".equals(zzad.name)) {
            double n;
            if ((n = zzad.zzaid.zzbq("value") * 1000000.0) == 0.0) {
                final double n2 = zzad.zzaid.getLong("value");
                Double.isNaN(n2);
                n = n2 * 1000000.0;
            }
            if (n > 9.223372036854776E18 || n < -9.223372036854776E18) {
                this.zzadj.zzgo().zzjg().zze("Data lost. Currency value is too big. appId", zzap.zzbv(s), n);
                return false;
            }
            n3 = Math.round(n);
        }
        else {
            n3 = zzad.zzaid.getLong("value");
        }
        if (!TextUtils.isEmpty((CharSequence)string)) {
            final String upperCase = string.toUpperCase(Locale.US);
            if (upperCase.matches("[A-Z]{3}")) {
                final String value = String.valueOf(upperCase);
                String concat;
                if (value.length() != 0) {
                    concat = "_ltv_".concat(value);
                }
                else {
                    concat = new String("_ltv_");
                }
                final zzfj zzi = this.zzjq().zzi(s, concat);
                zzfj zzfj;
                if (zzi != null && zzi.value instanceof Long) {
                    zzfj = new zzfj(s, zzad.origin, concat, this.zzadj.zzbx().currentTimeMillis(), (long)zzi.value + n3);
                }
                else {
                    final zzq zzjq = this.zzjq();
                    final int zzb = this.zzadj.zzgq().zzb(s, zzaf.zzakh);
                    Preconditions.checkNotEmpty(s);
                    zzjq.zzaf();
                    zzjq.zzcl();
                    try {
                        zzjq.getWritableDatabase().execSQL("delete from user_attributes where app_id=? and name in (select name from user_attributes where app_id=? and name like '_ltv_%' order by set_timestamp desc limit ?,10);", (Object[])new String[] { s, s, String.valueOf(zzb - 1) });
                    }
                    catch (SQLiteException ex) {
                        zzjq.zzgo().zzjd().zze("Error pruning currencies. appId", zzap.zzbv(s), ex);
                    }
                    zzfj = new zzfj(s, zzad.origin, concat, this.zzadj.zzbx().currentTimeMillis(), n3);
                }
                if (!this.zzjq().zza(zzfj)) {
                    this.zzadj.zzgo().zzjd().zzd("Too many unique user properties are set. Ignoring user property. appId", zzap.zzbv(s), this.zzadj.zzgl().zzbu(zzfj.name), zzfj.value);
                    this.zzadj.zzgm().zza(s, 9, null, null, 0);
                }
            }
        }
        return true;
    }
    
    private final zzgd[] zza(final String s, final zzgl[] array, final zzgf[] array2) {
        Preconditions.checkNotEmpty(s);
        return this.zzjp().zza(s, array2, array);
    }
    
    private static zzgg[] zza(final zzgg[] array, final int n) {
        final int n2 = array.length - 1;
        final zzgg[] array2 = new zzgg[n2];
        if (n > 0) {
            System.arraycopy(array, 0, array2, 0, n);
        }
        if (n < n2) {
            System.arraycopy(array, n + 1, array2, n, n2 - n);
        }
        return array2;
    }
    
    private static zzgg[] zza(final zzgg[] array, final int n, final String zzamp) {
        for (int i = 0; i < array.length; ++i) {
            if ("_err".equals(array[i].name)) {
                return array;
            }
        }
        final int n2 = array.length + 2;
        final zzgg[] array2 = new zzgg[n2];
        System.arraycopy(array, 0, array2, 0, array.length);
        final zzgg zzgg = new zzgg();
        zzgg.name = "_err";
        zzgg.zzawx = (long)n;
        final zzgg zzgg2 = new zzgg();
        zzgg2.name = "_ev";
        zzgg2.zzamp = zzamp;
        array2[n2 - 2] = zzgg;
        array2[n2 - 1] = zzgg2;
        return array2;
    }
    
    private static zzgg[] zza(final zzgg[] array, final String s) {
        int i = 0;
        while (true) {
            while (i < array.length) {
                if (s.equals(array[i].name)) {
                    if (i < 0) {
                        return array;
                    }
                    return zza(array, i);
                }
                else {
                    ++i;
                }
            }
            i = -1;
            continue;
        }
    }
    
    private final void zzaf() {
        this.zzadj.zzgn().zzaf();
    }
    
    private final void zzb(final zzg zzg) {
        this.zzaf();
        if (TextUtils.isEmpty((CharSequence)zzg.getGmpAppId()) && (!zzn.zzic() || TextUtils.isEmpty((CharSequence)zzg.zzgw()))) {
            this.zzb(zzg.zzal(), 204, null, null, null);
            return;
        }
        final zzn zzgq = this.zzadj.zzgq();
        final Uri$Builder uri$Builder = new Uri$Builder();
        String s2;
        final String s = s2 = zzg.getGmpAppId();
        if (TextUtils.isEmpty((CharSequence)s)) {
            s2 = s;
            if (zzn.zzic()) {
                s2 = zzg.zzgw();
            }
        }
        final Uri$Builder encodedAuthority = uri$Builder.scheme((String)zzaf.zzajh.get()).encodedAuthority((String)zzaf.zzaji.get());
        final String value = String.valueOf(s2);
        String concat;
        if (value.length() != 0) {
            concat = "config/app/".concat(value);
        }
        else {
            concat = new String("config/app/");
        }
        while (true) {
            encodedAuthority.path(concat).appendQueryParameter("app_instance_id", zzg.getAppInstanceId()).appendQueryParameter("platform", "android").appendQueryParameter("gmp_version", String.valueOf(zzgq.zzhc()));
            final String string = uri$Builder.build().toString();
            while (true) {
                try {
                    final URL url = new URL(string);
                    this.zzadj.zzgo().zzjl().zzg("Fetching remote configuration", zzg.zzal());
                    final zzgb zzcf = this.zzln().zzcf(zzg.zzal());
                    final String zzcg = this.zzln().zzcg(zzg.zzal());
                    if (zzcf != null && !TextUtils.isEmpty((CharSequence)zzcg)) {
                        final Map<String, String> map = new ArrayMap<String, String>();
                        map.put("If-Modified-Since", zzcg);
                        this.zzatp = true;
                        final zzat zzlo = this.zzlo();
                        final String zzal = zzg.zzal();
                        final zzfd zzfd = new zzfd(this);
                        zzlo.zzaf();
                        zzlo.zzcl();
                        Preconditions.checkNotNull(url);
                        Preconditions.checkNotNull(zzfd);
                        zzlo.zzgn().zzd(new zzax(zzal, url, null, map, zzfd));
                        return;
                    }
                }
                catch (MalformedURLException ex) {
                    this.zzadj.zzgo().zzjd().zze("Failed to parse config URL. Not fetching. appId", zzap.zzbv(zzg.zzal()), string);
                    return;
                }
                final Map<String, String> map = null;
                continue;
            }
        }
    }
    
    private final Boolean zzc(final zzg zzg) {
        try {
            if (zzg.zzha() != -2147483648L) {
                if (zzg.zzha() == Wrappers.packageManager(this.zzadj.getContext()).getPackageInfo(zzg.zzal(), 0).versionCode) {
                    return true;
                }
            }
            else {
                final String versionName = Wrappers.packageManager(this.zzadj.getContext()).getPackageInfo(zzg.zzal(), 0).versionName;
                if (zzg.zzak() != null && zzg.zzak().equals(versionName)) {
                    return true;
                }
            }
            return false;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return null;
        }
    }
    
    private final zzh zzco(final String s) {
        Object zzbv = s;
        final zzg zzbl = this.zzjq().zzbl((String)zzbv);
        String s2;
        zzar zzjk;
        if (zzbl != null && !TextUtils.isEmpty((CharSequence)zzbl.zzak())) {
            final Boolean zzc = this.zzc(zzbl);
            if (zzc == null || zzc) {
                return new zzh((String)zzbv, zzbl.getGmpAppId(), zzbl.zzak(), zzbl.zzha(), zzbl.zzhb(), zzbl.zzhc(), zzbl.zzhd(), null, zzbl.isMeasurementEnabled(), false, zzbl.getFirebaseInstanceId(), zzbl.zzhq(), 0L, 0, zzbl.zzhr(), zzbl.zzhs(), false, zzbl.zzgw());
            }
            final zzar zzjd = this.zzadj.zzgo().zzjd();
            s2 = "App version does not match; dropping. appId";
            zzbv = zzap.zzbv(s);
            zzjk = zzjd;
        }
        else {
            zzjk = this.zzadj.zzgo().zzjk();
            s2 = "No app data available; dropping";
        }
        zzjk.zzg(s2, zzbv);
        return null;
    }
    
    private final void zzd(zzad zza, zzh ex) {
        Preconditions.checkNotNull(ex);
        Preconditions.checkNotEmpty(((zzh)ex).packageName);
        final long nanoTime = System.nanoTime();
        this.zzaf();
        this.zzlr();
        final String packageName = ((zzh)ex).packageName;
        if (!this.zzjo().zze((zzad)zza, (zzh)ex)) {
            return;
        }
        if (!((zzh)ex).zzagg) {
            this.zzg((zzh)ex);
            return;
        }
        final boolean zzo = this.zzln().zzo(packageName, ((zzad)zza).name);
        boolean b = false;
        if (zzo) {
            this.zzadj.zzgo().zzjg().zze("Dropping blacklisted event. appId", zzap.zzbv(packageName), this.zzadj.zzgl().zzbs(((zzad)zza).name));
            if (this.zzln().zzck(packageName) || this.zzln().zzcl(packageName)) {
                b = true;
            }
            if (!b && !"_err".equals(((zzad)zza).name)) {
                this.zzadj.zzgm().zza(packageName, 11, "_ev", ((zzad)zza).name, 0);
            }
            if (b) {
                final zzg zzbl = this.zzjq().zzbl(packageName);
                if (zzbl != null && Math.abs(this.zzadj.zzbx().currentTimeMillis() - Math.max(zzbl.zzhg(), zzbl.zzhf())) > zzaf.zzakc.get()) {
                    this.zzadj.zzgo().zzjk().zzbx("Fetching config for blacklisted app");
                    this.zzb(zzbl);
                }
            }
            return;
        }
        if (this.zzadj.zzgo().isLoggable(2)) {
            this.zzadj.zzgo().zzjl().zzg("Logging event", this.zzadj.zzgl().zzb((zzad)zza));
        }
        while (true) {
            this.zzjq().beginTransaction();
            while (true) {
                Label_2172: {
                    try {
                        this.zzg((zzh)ex);
                        if (("_iap".equals(((zzad)zza).name) || "ecommerce_purchase".equals(((zzad)zza).name)) && !this.zza(packageName, (zzad)zza)) {
                            this.zzjq().setTransactionSuccessful();
                            return;
                        }
                        final boolean zzcq = zzfk.zzcq(((zzad)zza).name);
                        final boolean equals = "_err".equals(((zzad)zza).name);
                        final zzr zza2 = this.zzjq().zza(this.zzls(), packageName, true, zzcq, false, equals, false);
                        long n = zza2.zzahr;
                        n -= zzaf.zzajn.get();
                        if (n > 0L) {
                            if (n % 1000L == 1L) {
                                this.zzadj.zzgo().zzjd().zze("Data loss. Too many events logged. appId, count", zzap.zzbv(packageName), zza2.zzahr);
                            }
                            this.zzjq().setTransactionSuccessful();
                            return;
                        }
                        if (zzcq) {
                            n = zza2.zzahq;
                            n -= zzaf.zzajp.get();
                            if (n > 0L) {
                                if (n % 1000L == 1L) {
                                    this.zzadj.zzgo().zzjd().zze("Data loss. Too many public events logged. appId, count", zzap.zzbv(packageName), zza2.zzahq);
                                }
                                this.zzadj.zzgm().zza(packageName, 16, "_ev", ((zzad)zza).name, 0);
                                this.zzjq().setTransactionSuccessful();
                                return;
                            }
                        }
                        if (equals) {
                            n = zza2.zzaht - Math.max(0, Math.min(1000000, this.zzadj.zzgq().zzb(((zzh)ex).packageName, zzaf.zzajo)));
                            if (n > 0L) {
                                if (n == 1L) {
                                    this.zzadj.zzgo().zzjd().zze("Too many error events logged. appId, count", zzap.zzbv(packageName), zza2.zzaht);
                                }
                                this.zzjq().setTransactionSuccessful();
                                return;
                            }
                        }
                        final Bundle zziv = ((zzad)zza).zzaid.zziv();
                        this.zzadj.zzgm().zza(zziv, "_o", ((zzad)zza).origin);
                        if (this.zzadj.zzgm().zzcw(packageName)) {
                            this.zzadj.zzgm().zza(zziv, "_dbg", 1L);
                            this.zzadj.zzgm().zza(zziv, "_r", 1L);
                        }
                        n = this.zzjq().zzbm(packageName);
                        if (n > 0L) {
                            this.zzadj.zzgo().zzjg().zze("Data lost. Too many events stored on disk, deleted. appId", zzap.zzbv(packageName), n);
                        }
                        zza = new zzy(this.zzadj, ((zzad)zza).origin, packageName, ((zzad)zza).name, ((zzad)zza).zzaip, 0L, zziv);
                        final zzz zzg = this.zzjq().zzg(packageName, ((zzy)zza).name);
                        zzz zzai;
                        if (zzg == null) {
                            if (this.zzjq().zzbp(packageName) >= 500L && zzcq) {
                                this.zzadj.zzgo().zzjd().zzd("Too many event names used, ignoring event. appId, name, supported count", zzap.zzbv(packageName), this.zzadj.zzgl().zzbs(((zzy)zza).name), 500);
                                this.zzadj.zzgm().zza(packageName, 8, null, null, 0);
                                return;
                            }
                            zzai = new zzz(packageName, ((zzy)zza).name, 0L, 0L, ((zzy)zza).timestamp, 0L, null, null, null, null);
                        }
                        else {
                            zza = ((zzy)zza).zza(this.zzadj, zzg.zzaig);
                            zzai = zzg.zzai(((zzy)zza).timestamp);
                        }
                        this.zzjq().zza(zzai);
                        this.zzaf();
                        this.zzlr();
                        Preconditions.checkNotNull(zza);
                        Preconditions.checkNotNull(ex);
                        Preconditions.checkNotEmpty(((zzy)zza).zztt);
                        Preconditions.checkArgument(((zzy)zza).zztt.equals(((zzh)ex).packageName));
                        final zzgi zzgi = new zzgi();
                        final boolean b2 = true;
                        zzgi.zzaxa = 1;
                        zzgi.zzaxi = "android";
                        zzgi.zztt = ((zzh)ex).packageName;
                        zzgi.zzage = ((zzh)ex).zzage;
                        zzgi.zzts = ((zzh)ex).zzts;
                        Integer value;
                        if (((zzh)ex).zzagd == -2147483648L) {
                            value = null;
                        }
                        else {
                            value = (int)((zzh)ex).zzagd;
                        }
                        zzgi.zzaxu = value;
                        zzgi.zzaxm = ((zzh)ex).zzadt;
                        zzgi.zzafx = ((zzh)ex).zzafx;
                        zzgi.zzawj = ((zzh)ex).zzagk;
                        Long value2;
                        if (((zzh)ex).zzagf == 0L) {
                            value2 = null;
                        }
                        else {
                            value2 = ((zzh)ex).zzagf;
                        }
                        zzgi.zzaxq = value2;
                        final Pair<String, Boolean> zzby = this.zzadj.zzgp().zzby(((zzh)ex).packageName);
                        if (zzby != null && !TextUtils.isEmpty((CharSequence)zzby.first)) {
                            if (((zzh)ex).zzagi) {
                                zzgi.zzaxo = (String)zzby.first;
                                zzgi.zzaxp = (Boolean)zzby.second;
                            }
                        }
                        else if (!this.zzadj.zzgk().zzl(this.zzadj.getContext()) && ((zzh)ex).zzagj) {
                            final String string = Settings$Secure.getString(this.zzadj.getContext().getContentResolver(), "android_id");
                            String zzaxx;
                            if (string == null) {
                                this.zzadj.zzgo().zzjg().zzg("null secure ID. appId", zzap.zzbv(zzgi.zztt));
                                zzaxx = "null";
                            }
                            else {
                                zzaxx = string;
                                if (string.isEmpty()) {
                                    this.zzadj.zzgo().zzjg().zzg("empty secure ID. appId", zzap.zzbv(zzgi.zztt));
                                    zzaxx = string;
                                }
                            }
                            zzgi.zzaxx = zzaxx;
                        }
                        this.zzadj.zzgk().zzcl();
                        zzgi.zzaxk = Build.MODEL;
                        this.zzadj.zzgk().zzcl();
                        zzgi.zzaxj = Build$VERSION.RELEASE;
                        zzgi.zzaxl = (int)this.zzadj.zzgk().zzis();
                        zzgi.zzaia = this.zzadj.zzgk().zzit();
                        zzgi.zzaxn = null;
                        zzgi.zzaxd = null;
                        zzgi.zzaxe = null;
                        zzgi.zzaxf = null;
                        zzgi.zzaxz = ((zzh)ex).zzagh;
                        if (this.zzadj.isEnabled() && zzn.zzhz()) {
                            zzgi.zzaya = null;
                        }
                        zzg zzbl2;
                        if ((zzbl2 = this.zzjq().zzbl(((zzh)ex).packageName)) == null) {
                            zzbl2 = new zzg(this.zzadj, ((zzh)ex).packageName);
                            zzbl2.zzam(this.zzadj.zzgm().zzmf());
                            zzbl2.zzaq(((zzh)ex).zzafz);
                            zzbl2.zzan(((zzh)ex).zzafx);
                            zzbl2.zzap(this.zzadj.zzgp().zzbz(((zzh)ex).packageName));
                            zzbl2.zzx(0L);
                            zzbl2.zzs(0L);
                            zzbl2.zzt(0L);
                            zzbl2.setAppVersion(((zzh)ex).zzts);
                            zzbl2.zzu(((zzh)ex).zzagd);
                            zzbl2.zzar(((zzh)ex).zzage);
                            zzbl2.zzv(((zzh)ex).zzadt);
                            zzbl2.zzw(((zzh)ex).zzagf);
                            zzbl2.setMeasurementEnabled(((zzh)ex).zzagg);
                            zzbl2.zzag(((zzh)ex).zzagh);
                            this.zzjq().zza(zzbl2);
                        }
                        zzgi.zzafw = zzbl2.getAppInstanceId();
                        zzgi.zzafz = zzbl2.getFirebaseInstanceId();
                        ex = (IOException)this.zzjq().zzbk(((zzh)ex).packageName);
                        zzgi.zzaxc = new zzgl[((List)ex).size()];
                        for (int i = 0; i < ((List)ex).size(); ++i) {
                            final zzgl zzgl = new zzgl();
                            zzgi.zzaxc[i] = zzgl;
                            zzgl.name = ((List<zzfj>)ex).get(i).name;
                            zzgl.zzayl = ((List<zzfj>)ex).get(i).zzaue;
                            this.zzjo().zza(zzgl, ((List<zzfj>)ex).get(i).value);
                        }
                        Label_2069: {
                            try {
                                n = this.zzjq().zza(zzgi);
                                ex = (IOException)this.zzjq();
                                if (((zzy)zza).zzaid == null) {
                                    break Label_2172;
                                }
                                final Iterator<String> iterator = ((zzy)zza).zzaid.iterator();
                                while (iterator.hasNext()) {
                                    if ("_r".equals(iterator.next())) {
                                        if (((zzq)ex).zza((zzy)zza, n, b2)) {
                                            this.zzatl = 0L;
                                        }
                                        break Label_2069;
                                    }
                                }
                                final boolean zzp = this.zzln().zzp(((zzy)zza).zztt, ((zzy)zza).name);
                                final zzr zza3 = this.zzjq().zza(this.zzls(), ((zzy)zza).zztt, false, false, false, false, false);
                                if (zzp && zza3.zzahu < this.zzadj.zzgq().zzat(((zzy)zza).zztt)) {
                                    continue;
                                }
                                break Label_2172;
                            }
                            catch (IOException ex) {
                                this.zzadj.zzgo().zzjd().zze("Data loss. Failed to insert raw event metadata. appId", zzap.zzbv(zzgi.zztt), ex);
                            }
                        }
                        this.zzjq().setTransactionSuccessful();
                        if (this.zzadj.zzgo().isLoggable(2)) {
                            this.zzadj.zzgo().zzjl().zzg("Event recorded", this.zzadj.zzgl().zza((zzy)zza));
                        }
                        this.zzjq().endTransaction();
                        this.zzlv();
                        this.zzadj.zzgo().zzjl().zzg("Background event processing time, ms", (System.nanoTime() - nanoTime + 500000L) / 1000000L);
                        return;
                    }
                    finally {
                        this.zzjq().endTransaction();
                    }
                }
                final boolean b2 = false;
                continue;
            }
        }
    }
    
    private final boolean zzd(final String p0, final long p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //     4: invokevirtual   com/google/android/gms/measurement/internal/zzq.beginTransaction:()V
        //     7: aconst_null    
        //     8: astore          19
        //    10: aconst_null    
        //    11: astore          20
        //    13: new             Lcom/google/android/gms/measurement/internal/zzfa$zza;
        //    16: dup            
        //    17: aload_0        
        //    18: aconst_null    
        //    19: invokespecial   com/google/android/gms/measurement/internal/zzfa$zza.<init>:(Lcom/google/android/gms/measurement/internal/zzfa;Lcom/google/android/gms/measurement/internal/zzfb;)V
        //    22: astore          22
        //    24: aload_0        
        //    25: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //    28: astore          25
        //    30: aload_0        
        //    31: getfield        com/google/android/gms/measurement/internal/zzfa.zzatw:J
        //    34: lstore          11
        //    36: aload           22
        //    38: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotNull:(Ljava/lang/Object;)Ljava/lang/Object;
        //    41: pop            
        //    42: aload           25
        //    44: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //    47: aload           25
        //    49: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //    52: aload           25
        //    54: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    57: astore          26
        //    59: aconst_null    
        //    60: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //    63: istore          15
        //    65: ldc             ""
        //    67: astore          18
        //    69: iload           15
        //    71: ifeq            339
        //    74: lload           11
        //    76: ldc2_w          -1
        //    79: lcmp           
        //    80: ifeq            144
        //    83: aload           20
        //    85: astore_1       
        //    86: iconst_2       
        //    87: anewarray       Ljava/lang/String;
        //    90: astore          21
        //    92: aload           20
        //    94: astore_1       
        //    95: aload           21
        //    97: iconst_0       
        //    98: lload           11
        //   100: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //   103: aastore        
        //   104: aload           20
        //   106: astore_1       
        //   107: aload           21
        //   109: iconst_1       
        //   110: lload_2        
        //   111: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //   114: aastore        
        //   115: aload           21
        //   117: astore_1       
        //   118: goto            156
        //   121: astore          18
        //   123: aload_1        
        //   124: astore          19
        //   126: goto            5136
        //   129: astore_1       
        //   130: aconst_null    
        //   131: astore          20
        //   133: aload           19
        //   135: astore          18
        //   137: aload           20
        //   139: astore          19
        //   141: goto            1169
        //   144: iconst_1       
        //   145: anewarray       Ljava/lang/String;
        //   148: astore_1       
        //   149: aload_1        
        //   150: iconst_0       
        //   151: lload_2        
        //   152: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //   155: aastore        
        //   156: lload           11
        //   158: ldc2_w          -1
        //   161: lcmp           
        //   162: ifeq            170
        //   165: ldc_w           "rowid <= ? and "
        //   168: astore          18
        //   170: new             Ljava/lang/StringBuilder;
        //   173: dup            
        //   174: aload           18
        //   176: invokevirtual   java/lang/String.length:()I
        //   179: sipush          148
        //   182: iadd           
        //   183: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //   186: astore          19
        //   188: aload           19
        //   190: ldc_w           "select app_id, metadata_fingerprint from raw_events where "
        //   193: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   196: pop            
        //   197: aload           19
        //   199: aload           18
        //   201: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   204: pop            
        //   205: aload           19
        //   207: ldc_w           "app_id in (select app_id from apps where config_fetched_time >= ?) order by rowid limit 1;"
        //   210: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   213: pop            
        //   214: aload           26
        //   216: aload           19
        //   218: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   221: aload_1        
        //   222: invokevirtual   android/database/sqlite/SQLiteDatabase.rawQuery:(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
        //   225: astore          18
        //   227: aload           18
        //   229: astore_1       
        //   230: aload           18
        //   232: astore          20
        //   234: aload           18
        //   236: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   241: istore          15
        //   243: iload           15
        //   245: ifne            265
        //   248: aload           18
        //   250: ifnull          1228
        //   253: aload           18
        //   255: astore_1       
        //   256: aload_1        
        //   257: invokeinterface android/database/Cursor.close:()V
        //   262: goto            1228
        //   265: aload           18
        //   267: astore_1       
        //   268: aload           18
        //   270: astore          20
        //   272: aload           18
        //   274: iconst_0       
        //   275: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   280: astore          21
        //   282: aload           18
        //   284: astore_1       
        //   285: aload           18
        //   287: astore          19
        //   289: aload           21
        //   291: astore          20
        //   293: aload           18
        //   295: iconst_1       
        //   296: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   301: astore          24
        //   303: aload           18
        //   305: astore_1       
        //   306: aload           18
        //   308: astore          19
        //   310: aload           21
        //   312: astore          20
        //   314: aload           18
        //   316: invokeinterface android/database/Cursor.close:()V
        //   321: aload           18
        //   323: astore_1       
        //   324: aload           21
        //   326: astore          18
        //   328: goto            512
        //   331: astore_1       
        //   332: aload           20
        //   334: astore          18
        //   336: goto            1158
        //   339: lload           11
        //   341: ldc2_w          -1
        //   344: lcmp           
        //   345: ifeq            368
        //   348: iconst_2       
        //   349: anewarray       Ljava/lang/String;
        //   352: astore_1       
        //   353: aload_1        
        //   354: iconst_0       
        //   355: aconst_null    
        //   356: aastore        
        //   357: aload_1        
        //   358: iconst_1       
        //   359: lload           11
        //   361: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //   364: aastore        
        //   365: goto            377
        //   368: iconst_1       
        //   369: anewarray       Ljava/lang/String;
        //   372: astore_1       
        //   373: aload_1        
        //   374: iconst_0       
        //   375: aconst_null    
        //   376: aastore        
        //   377: lload           11
        //   379: ldc2_w          -1
        //   382: lcmp           
        //   383: ifeq            391
        //   386: ldc_w           " and rowid <= ?"
        //   389: astore          18
        //   391: new             Ljava/lang/StringBuilder;
        //   394: dup            
        //   395: aload           18
        //   397: invokevirtual   java/lang/String.length:()I
        //   400: bipush          84
        //   402: iadd           
        //   403: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //   406: astore          19
        //   408: aload           19
        //   410: ldc_w           "select metadata_fingerprint from raw_events where app_id = ?"
        //   413: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   416: pop            
        //   417: aload           19
        //   419: aload           18
        //   421: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   424: pop            
        //   425: aload           19
        //   427: ldc_w           " order by rowid limit 1;"
        //   430: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   433: pop            
        //   434: aload           26
        //   436: aload           19
        //   438: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   441: aload_1        
        //   442: invokevirtual   android/database/sqlite/SQLiteDatabase.rawQuery:(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
        //   445: astore          19
        //   447: aload           19
        //   449: astore_1       
        //   450: aload           19
        //   452: astore          20
        //   454: aload           19
        //   456: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   461: ifne            475
        //   464: aload           19
        //   466: ifnull          1228
        //   469: aload           19
        //   471: astore_1       
        //   472: goto            256
        //   475: aload           19
        //   477: astore_1       
        //   478: aload           19
        //   480: astore          20
        //   482: aload           19
        //   484: iconst_0       
        //   485: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   490: astore          24
        //   492: aload           19
        //   494: astore_1       
        //   495: aload           19
        //   497: astore          20
        //   499: aload           19
        //   501: invokeinterface android/database/Cursor.close:()V
        //   506: aconst_null    
        //   507: astore          18
        //   509: aload           19
        //   511: astore_1       
        //   512: aload_1        
        //   513: astore          19
        //   515: aload           18
        //   517: astore          20
        //   519: aload_1        
        //   520: astore          21
        //   522: aload           26
        //   524: ldc_w           "raw_events_metadata"
        //   527: iconst_1       
        //   528: anewarray       Ljava/lang/String;
        //   531: dup            
        //   532: iconst_0       
        //   533: ldc_w           "metadata"
        //   536: aastore        
        //   537: ldc_w           "app_id = ? and metadata_fingerprint = ?"
        //   540: iconst_2       
        //   541: anewarray       Ljava/lang/String;
        //   544: dup            
        //   545: iconst_0       
        //   546: aload           18
        //   548: aastore        
        //   549: dup            
        //   550: iconst_1       
        //   551: aload           24
        //   553: aastore        
        //   554: aconst_null    
        //   555: aconst_null    
        //   556: ldc_w           "rowid"
        //   559: ldc_w           "2"
        //   562: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   565: astore          23
        //   567: aload           23
        //   569: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   574: ifne            611
        //   577: aload           25
        //   579: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   582: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   585: ldc_w           "Raw event metadata record is missing. appId"
        //   588: aload           18
        //   590: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   593: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   596: aload           23
        //   598: ifnull          1228
        //   601: aload           23
        //   603: invokeinterface android/database/Cursor.close:()V
        //   608: goto            1228
        //   611: aload           23
        //   613: iconst_0       
        //   614: invokeinterface android/database/Cursor.getBlob:(I)[B
        //   619: astore_1       
        //   620: aload_1        
        //   621: iconst_0       
        //   622: aload_1        
        //   623: arraylength    
        //   624: invokestatic    com/google/android/gms/internal/measurement/zzyx.zzj:([BII)Lcom/google/android/gms/internal/measurement/zzyx;
        //   627: astore_1       
        //   628: new             Lcom/google/android/gms/internal/measurement/zzgi;
        //   631: dup            
        //   632: invokespecial   com/google/android/gms/internal/measurement/zzgi.<init>:()V
        //   635: astore          19
        //   637: aload           19
        //   639: aload_1        
        //   640: invokevirtual   com/google/android/gms/internal/measurement/zzzg.zza:(Lcom/google/android/gms/internal/measurement/zzyx;)Lcom/google/android/gms/internal/measurement/zzzg;
        //   643: pop            
        //   644: aload           23
        //   646: invokeinterface android/database/Cursor.moveToNext:()Z
        //   651: ifeq            673
        //   654: aload           25
        //   656: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   659: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //   662: ldc_w           "Get multiple raw event metadata records, expected one. appId"
        //   665: aload           18
        //   667: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   670: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   673: aload           23
        //   675: invokeinterface android/database/Cursor.close:()V
        //   680: aload           22
        //   682: aload           19
        //   684: invokeinterface com/google/android/gms/measurement/internal/zzs.zzb:(Lcom/google/android/gms/internal/measurement/zzgi;)V
        //   689: lload           11
        //   691: ldc2_w          -1
        //   694: lcmp           
        //   695: ifeq            729
        //   698: ldc_w           "app_id = ? and metadata_fingerprint = ? and rowid <= ?"
        //   701: astore_1       
        //   702: iconst_3       
        //   703: anewarray       Ljava/lang/String;
        //   706: dup            
        //   707: iconst_0       
        //   708: aload           18
        //   710: aastore        
        //   711: dup            
        //   712: iconst_1       
        //   713: aload           24
        //   715: aastore        
        //   716: dup            
        //   717: iconst_2       
        //   718: lload           11
        //   720: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //   723: aastore        
        //   724: astore          19
        //   726: goto            749
        //   729: ldc_w           "app_id = ? and metadata_fingerprint = ?"
        //   732: astore_1       
        //   733: iconst_2       
        //   734: anewarray       Ljava/lang/String;
        //   737: dup            
        //   738: iconst_0       
        //   739: aload           18
        //   741: aastore        
        //   742: dup            
        //   743: iconst_1       
        //   744: aload           24
        //   746: aastore        
        //   747: astore          19
        //   749: aload           26
        //   751: ldc_w           "raw_events"
        //   754: iconst_4       
        //   755: anewarray       Ljava/lang/String;
        //   758: dup            
        //   759: iconst_0       
        //   760: ldc_w           "rowid"
        //   763: aastore        
        //   764: dup            
        //   765: iconst_1       
        //   766: ldc_w           "name"
        //   769: aastore        
        //   770: dup            
        //   771: iconst_2       
        //   772: ldc_w           "timestamp"
        //   775: aastore        
        //   776: dup            
        //   777: iconst_3       
        //   778: ldc_w           "data"
        //   781: aastore        
        //   782: aload_1        
        //   783: aload           19
        //   785: aconst_null    
        //   786: aconst_null    
        //   787: ldc_w           "rowid"
        //   790: aconst_null    
        //   791: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   794: astore          21
        //   796: aload           21
        //   798: astore_1       
        //   799: aload           21
        //   801: astore          19
        //   803: aload           18
        //   805: astore          20
        //   807: aload           21
        //   809: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   814: ifne            858
        //   817: aload           21
        //   819: astore_1       
        //   820: aload           21
        //   822: astore          19
        //   824: aload           18
        //   826: astore          20
        //   828: aload           25
        //   830: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   833: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //   836: ldc_w           "Raw event data disappeared while in transaction. appId"
        //   839: aload           18
        //   841: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   844: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   847: aload           21
        //   849: ifnull          1228
        //   852: aload           21
        //   854: astore_1       
        //   855: goto            256
        //   858: aload           21
        //   860: astore_1       
        //   861: aload           21
        //   863: astore          19
        //   865: aload           18
        //   867: astore          20
        //   869: aload           21
        //   871: iconst_0       
        //   872: invokeinterface android/database/Cursor.getLong:(I)J
        //   877: lstore_2       
        //   878: aload           21
        //   880: astore_1       
        //   881: aload           21
        //   883: astore          19
        //   885: aload           18
        //   887: astore          20
        //   889: aload           21
        //   891: iconst_3       
        //   892: invokeinterface android/database/Cursor.getBlob:(I)[B
        //   897: astore          23
        //   899: aload           21
        //   901: astore_1       
        //   902: aload           21
        //   904: astore          19
        //   906: aload           18
        //   908: astore          20
        //   910: aload           23
        //   912: iconst_0       
        //   913: aload           23
        //   915: arraylength    
        //   916: invokestatic    com/google/android/gms/internal/measurement/zzyx.zzj:([BII)Lcom/google/android/gms/internal/measurement/zzyx;
        //   919: astore          23
        //   921: aload           21
        //   923: astore_1       
        //   924: aload           21
        //   926: astore          19
        //   928: aload           18
        //   930: astore          20
        //   932: new             Lcom/google/android/gms/internal/measurement/zzgf;
        //   935: dup            
        //   936: invokespecial   com/google/android/gms/internal/measurement/zzgf.<init>:()V
        //   939: astore          24
        //   941: aload           21
        //   943: astore_1       
        //   944: aload           21
        //   946: astore          19
        //   948: aload           18
        //   950: astore          20
        //   952: aload           24
        //   954: aload           23
        //   956: invokevirtual   com/google/android/gms/internal/measurement/zzzg.zza:(Lcom/google/android/gms/internal/measurement/zzyx;)Lcom/google/android/gms/internal/measurement/zzzg;
        //   959: pop            
        //   960: aload           21
        //   962: astore_1       
        //   963: aload           21
        //   965: astore          19
        //   967: aload           18
        //   969: astore          20
        //   971: aload           24
        //   973: aload           21
        //   975: iconst_1       
        //   976: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   981: putfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //   984: aload           21
        //   986: astore_1       
        //   987: aload           21
        //   989: astore          19
        //   991: aload           18
        //   993: astore          20
        //   995: aload           24
        //   997: aload           21
        //   999: iconst_2       
        //  1000: invokeinterface android/database/Cursor.getLong:(I)J
        //  1005: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1008: putfield        com/google/android/gms/internal/measurement/zzgf.zzawu:Ljava/lang/Long;
        //  1011: aload           21
        //  1013: astore_1       
        //  1014: aload           21
        //  1016: astore          19
        //  1018: aload           18
        //  1020: astore          20
        //  1022: aload           22
        //  1024: lload_2        
        //  1025: aload           24
        //  1027: invokeinterface com/google/android/gms/measurement/internal/zzs.zza:(JLcom/google/android/gms/internal/measurement/zzgf;)Z
        //  1032: ifne            1080
        //  1035: aload           21
        //  1037: ifnull          1228
        //  1040: aload           21
        //  1042: astore_1       
        //  1043: goto            256
        //  1046: astore          23
        //  1048: aload           21
        //  1050: astore_1       
        //  1051: aload           21
        //  1053: astore          19
        //  1055: aload           18
        //  1057: astore          20
        //  1059: aload           25
        //  1061: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  1064: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //  1067: ldc_w           "Data loss. Failed to merge raw event. appId"
        //  1070: aload           18
        //  1072: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  1075: aload           23
        //  1077: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //  1080: aload           21
        //  1082: astore_1       
        //  1083: aload           21
        //  1085: astore          19
        //  1087: aload           18
        //  1089: astore          20
        //  1091: aload           21
        //  1093: invokeinterface android/database/Cursor.moveToNext:()Z
        //  1098: istore          15
        //  1100: iload           15
        //  1102: ifne            858
        //  1105: aload           21
        //  1107: ifnull          1228
        //  1110: aload           21
        //  1112: astore_1       
        //  1113: goto            256
        //  1116: astore_1       
        //  1117: aload           25
        //  1119: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  1122: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //  1125: ldc_w           "Data loss. Failed to merge raw event metadata. appId"
        //  1128: aload           18
        //  1130: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  1133: aload_1        
        //  1134: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //  1137: aload           23
        //  1139: ifnull          1228
        //  1142: goto            601
        //  1145: astore_1       
        //  1146: aload           23
        //  1148: astore          18
        //  1150: goto            5129
        //  1153: astore_1       
        //  1154: aload           23
        //  1156: astore          19
        //  1158: goto            1169
        //  1161: astore_1       
        //  1162: aload           20
        //  1164: astore          19
        //  1166: aconst_null    
        //  1167: astore          18
        //  1169: aload           19
        //  1171: astore          20
        //  1173: aload_1        
        //  1174: astore          19
        //  1176: aload           20
        //  1178: astore_1       
        //  1179: goto            1197
        //  1182: astore          18
        //  1184: aconst_null    
        //  1185: astore          19
        //  1187: goto            5136
        //  1190: astore          19
        //  1192: aconst_null    
        //  1193: astore_1       
        //  1194: aconst_null    
        //  1195: astore          18
        //  1197: aload_1        
        //  1198: astore          21
        //  1200: aload           25
        //  1202: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  1205: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //  1208: ldc_w           "Data loss. Error selecting raw event. appId"
        //  1211: aload           18
        //  1213: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  1216: aload           19
        //  1218: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //  1221: aload_1        
        //  1222: ifnull          1228
        //  1225: goto            256
        //  1228: aload           22
        //  1230: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzauc:Ljava/util/List;
        //  1233: ifnull          5166
        //  1236: aload           22
        //  1238: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzauc:Ljava/util/List;
        //  1241: invokeinterface java/util/List.isEmpty:()Z
        //  1246: ifeq            5160
        //  1249: goto            5166
        //  1252: iload           4
        //  1254: ifne            5108
        //  1257: aload           22
        //  1259: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  1262: astore          18
        //  1264: aload           18
        //  1266: aload           22
        //  1268: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzauc:Ljava/util/List;
        //  1271: invokeinterface java/util/List.size:()I
        //  1276: anewarray       Lcom/google/android/gms/internal/measurement/zzgf;
        //  1279: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  1282: aload_0        
        //  1283: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  1286: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgq:()Lcom/google/android/gms/measurement/internal/zzn;
        //  1289: aload           18
        //  1291: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  1294: invokevirtual   com/google/android/gms/measurement/internal/zzn.zzax:(Ljava/lang/String;)Z
        //  1297: istore          16
        //  1299: iconst_0       
        //  1300: istore          4
        //  1302: iconst_0       
        //  1303: istore          5
        //  1305: iconst_0       
        //  1306: istore          15
        //  1308: lconst_0       
        //  1309: lstore_2       
        //  1310: iload           4
        //  1312: aload           22
        //  1314: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzauc:Ljava/util/List;
        //  1317: invokeinterface java/util/List.size:()I
        //  1322: if_icmpge       2864
        //  1325: aload           22
        //  1327: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzauc:Ljava/util/List;
        //  1330: iload           4
        //  1332: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  1337: checkcast       Lcom/google/android/gms/internal/measurement/zzgf;
        //  1340: astore          23
        //  1342: aload_0        
        //  1343: invokespecial   com/google/android/gms/measurement/internal/zzfa.zzln:()Lcom/google/android/gms/measurement/internal/zzbn;
        //  1346: aload           22
        //  1348: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  1351: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  1354: aload           23
        //  1356: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  1359: invokevirtual   com/google/android/gms/measurement/internal/zzbn.zzo:(Ljava/lang/String;Ljava/lang/String;)Z
        //  1362: ifeq            1507
        //  1365: aload_0        
        //  1366: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  1369: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  1372: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //  1375: astore_1       
        //  1376: aload           22
        //  1378: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  1381: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  1384: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  1387: astore          19
        //  1389: aload_0        
        //  1390: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  1393: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgl:()Lcom/google/android/gms/measurement/internal/zzan;
        //  1396: astore          20
        //  1398: aload_1        
        //  1399: ldc_w           "Dropping blacklisted raw event. appId"
        //  1402: aload           19
        //  1404: aload           20
        //  1406: aload           23
        //  1408: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  1411: invokevirtual   com/google/android/gms/measurement/internal/zzan.zzbs:(Ljava/lang/String;)Ljava/lang/String;
        //  1414: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //  1417: aload_0        
        //  1418: invokespecial   com/google/android/gms/measurement/internal/zzfa.zzln:()Lcom/google/android/gms/measurement/internal/zzbn;
        //  1421: aload           22
        //  1423: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  1426: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  1429: invokevirtual   com/google/android/gms/measurement/internal/zzbn.zzck:(Ljava/lang/String;)Z
        //  1432: ifne            5178
        //  1435: aload_0        
        //  1436: invokespecial   com/google/android/gms/measurement/internal/zzfa.zzln:()Lcom/google/android/gms/measurement/internal/zzbn;
        //  1439: aload           22
        //  1441: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  1444: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  1447: invokevirtual   com/google/android/gms/measurement/internal/zzbn.zzcl:(Ljava/lang/String;)Z
        //  1450: ifeq            5172
        //  1453: goto            5178
        //  1456: iload           6
        //  1458: ifne            5184
        //  1461: ldc_w           "_err"
        //  1464: aload           23
        //  1466: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  1469: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1472: ifne            5184
        //  1475: aload_0        
        //  1476: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  1479: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgm:()Lcom/google/android/gms/measurement/internal/zzfk;
        //  1482: aload           22
        //  1484: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  1487: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  1490: bipush          11
        //  1492: ldc_w           "_ev"
        //  1495: aload           23
        //  1497: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  1500: iconst_0       
        //  1501: invokevirtual   com/google/android/gms/measurement/internal/zzfk.zza:(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;I)V
        //  1504: goto            5184
        //  1507: aload_0        
        //  1508: invokespecial   com/google/android/gms/measurement/internal/zzfa.zzln:()Lcom/google/android/gms/measurement/internal/zzbn;
        //  1511: aload           22
        //  1513: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  1516: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  1519: aload           23
        //  1521: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  1524: invokevirtual   com/google/android/gms/measurement/internal/zzbn.zzp:(Ljava/lang/String;Ljava/lang/String;)Z
        //  1527: istore          17
        //  1529: iload           17
        //  1531: ifne            1631
        //  1534: aload_0        
        //  1535: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //  1538: pop            
        //  1539: aload           23
        //  1541: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  1544: astore_1       
        //  1545: aload_1        
        //  1546: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //  1549: pop            
        //  1550: aload_1        
        //  1551: invokevirtual   java/lang/String.hashCode:()I
        //  1554: istore          6
        //  1556: iload           6
        //  1558: ldc_w           94660
        //  1561: if_icmpeq       1615
        //  1564: iload           6
        //  1566: ldc_w           95025
        //  1569: if_icmpeq       1599
        //  1572: iload           6
        //  1574: ldc_w           95027
        //  1577: if_icmpeq       1583
        //  1580: goto            5187
        //  1583: aload_1        
        //  1584: ldc_w           "_ui"
        //  1587: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1590: ifeq            5187
        //  1593: iconst_1       
        //  1594: istore          6
        //  1596: goto            5190
        //  1599: aload_1        
        //  1600: ldc_w           "_ug"
        //  1603: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1606: ifeq            5187
        //  1609: iconst_2       
        //  1610: istore          6
        //  1612: goto            5190
        //  1615: aload_1        
        //  1616: ldc_w           "_in"
        //  1619: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1622: ifeq            5187
        //  1625: iconst_0       
        //  1626: istore          6
        //  1628: goto            5190
        //  1631: aload           23
        //  1633: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  1636: ifnonnull       1648
        //  1639: aload           23
        //  1641: iconst_0       
        //  1642: anewarray       Lcom/google/android/gms/internal/measurement/zzgg;
        //  1645: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  1648: aload           23
        //  1650: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  1653: astore_1       
        //  1654: aload_1        
        //  1655: arraylength    
        //  1656: istore          6
        //  1658: iconst_0       
        //  1659: istore          9
        //  1661: iconst_0       
        //  1662: istore          7
        //  1664: iconst_0       
        //  1665: istore          8
        //  1667: iload           9
        //  1669: iload           6
        //  1671: if_icmpge       1746
        //  1674: aload_1        
        //  1675: iload           9
        //  1677: aaload         
        //  1678: astore          19
        //  1680: ldc_w           "_c"
        //  1683: aload           19
        //  1685: getfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  1688: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1691: ifeq            1709
        //  1694: aload           19
        //  1696: lconst_1       
        //  1697: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1700: putfield        com/google/android/gms/internal/measurement/zzgg.zzawx:Ljava/lang/Long;
        //  1703: iconst_1       
        //  1704: istore          10
        //  1706: goto            5235
        //  1709: iload           7
        //  1711: istore          10
        //  1713: ldc_w           "_r"
        //  1716: aload           19
        //  1718: getfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  1721: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1724: ifeq            5235
        //  1727: aload           19
        //  1729: lconst_1       
        //  1730: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1733: putfield        com/google/android/gms/internal/measurement/zzgg.zzawx:Ljava/lang/Long;
        //  1736: iconst_1       
        //  1737: istore          8
        //  1739: iload           7
        //  1741: istore          10
        //  1743: goto            5235
        //  1746: iload           5
        //  1748: istore          6
        //  1750: iload           7
        //  1752: ifne            1851
        //  1755: iload           17
        //  1757: ifeq            1851
        //  1760: aload_0        
        //  1761: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  1764: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  1767: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjl:()Lcom/google/android/gms/measurement/internal/zzar;
        //  1770: ldc_w           "Marking event as conversion"
        //  1773: aload_0        
        //  1774: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  1777: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgl:()Lcom/google/android/gms/measurement/internal/zzan;
        //  1780: aload           23
        //  1782: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  1785: invokevirtual   com/google/android/gms/measurement/internal/zzan.zzbs:(Ljava/lang/String;)Ljava/lang/String;
        //  1788: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //  1791: aload           23
        //  1793: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  1796: aload           23
        //  1798: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  1801: arraylength    
        //  1802: iconst_1       
        //  1803: iadd           
        //  1804: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //  1807: checkcast       [Lcom/google/android/gms/internal/measurement/zzgg;
        //  1810: astore_1       
        //  1811: new             Lcom/google/android/gms/internal/measurement/zzgg;
        //  1814: dup            
        //  1815: invokespecial   com/google/android/gms/internal/measurement/zzgg.<init>:()V
        //  1818: astore          19
        //  1820: aload           19
        //  1822: ldc_w           "_c"
        //  1825: putfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  1828: aload           19
        //  1830: lconst_1       
        //  1831: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1834: putfield        com/google/android/gms/internal/measurement/zzgg.zzawx:Ljava/lang/Long;
        //  1837: aload_1        
        //  1838: aload_1        
        //  1839: arraylength    
        //  1840: iconst_1       
        //  1841: isub           
        //  1842: aload           19
        //  1844: aastore        
        //  1845: aload           23
        //  1847: aload_1        
        //  1848: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  1851: iload           8
        //  1853: ifne            1947
        //  1856: aload_0        
        //  1857: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  1860: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  1863: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjl:()Lcom/google/android/gms/measurement/internal/zzar;
        //  1866: ldc_w           "Marking event as real-time"
        //  1869: aload_0        
        //  1870: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  1873: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgl:()Lcom/google/android/gms/measurement/internal/zzan;
        //  1876: aload           23
        //  1878: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  1881: invokevirtual   com/google/android/gms/measurement/internal/zzan.zzbs:(Ljava/lang/String;)Ljava/lang/String;
        //  1884: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //  1887: aload           23
        //  1889: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  1892: aload           23
        //  1894: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  1897: arraylength    
        //  1898: iconst_1       
        //  1899: iadd           
        //  1900: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //  1903: checkcast       [Lcom/google/android/gms/internal/measurement/zzgg;
        //  1906: astore_1       
        //  1907: new             Lcom/google/android/gms/internal/measurement/zzgg;
        //  1910: dup            
        //  1911: invokespecial   com/google/android/gms/internal/measurement/zzgg.<init>:()V
        //  1914: astore          19
        //  1916: aload           19
        //  1918: ldc_w           "_r"
        //  1921: putfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  1924: aload           19
        //  1926: lconst_1       
        //  1927: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1930: putfield        com/google/android/gms/internal/measurement/zzgg.zzawx:Ljava/lang/Long;
        //  1933: aload_1        
        //  1934: aload_1        
        //  1935: arraylength    
        //  1936: iconst_1       
        //  1937: isub           
        //  1938: aload           19
        //  1940: aastore        
        //  1941: aload           23
        //  1943: aload_1        
        //  1944: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  1947: aload_0        
        //  1948: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  1951: aload_0        
        //  1952: invokespecial   com/google/android/gms/measurement/internal/zzfa.zzls:()J
        //  1955: aload           22
        //  1957: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  1960: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  1963: iconst_0       
        //  1964: iconst_0       
        //  1965: iconst_0       
        //  1966: iconst_0       
        //  1967: iconst_1       
        //  1968: invokevirtual   com/google/android/gms/measurement/internal/zzq.zza:(JLjava/lang/String;ZZZZZ)Lcom/google/android/gms/measurement/internal/zzr;
        //  1971: getfield        com/google/android/gms/measurement/internal/zzr.zzahu:J
        //  1974: aload_0        
        //  1975: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  1978: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgq:()Lcom/google/android/gms/measurement/internal/zzn;
        //  1981: aload           22
        //  1983: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  1986: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  1989: invokevirtual   com/google/android/gms/measurement/internal/zzn.zzat:(Ljava/lang/String;)I
        //  1992: i2l            
        //  1993: lcmp           
        //  1994: ifle            5260
        //  1997: iconst_0       
        //  1998: istore          5
        //  2000: iload           5
        //  2002: aload           23
        //  2004: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2007: arraylength    
        //  2008: if_icmpge       5257
        //  2011: ldc_w           "_r"
        //  2014: aload           23
        //  2016: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2019: iload           5
        //  2021: aaload         
        //  2022: getfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  2025: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  2028: ifeq            5248
        //  2031: aload           23
        //  2033: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2036: arraylength    
        //  2037: iconst_1       
        //  2038: isub           
        //  2039: istore          7
        //  2041: iload           7
        //  2043: anewarray       Lcom/google/android/gms/internal/measurement/zzgg;
        //  2046: astore_1       
        //  2047: iload           5
        //  2049: ifle            2065
        //  2052: aload           23
        //  2054: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2057: iconst_0       
        //  2058: aload_1        
        //  2059: iconst_0       
        //  2060: iload           5
        //  2062: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
        //  2065: iload           5
        //  2067: iload           7
        //  2069: if_icmpge       2092
        //  2072: aload           23
        //  2074: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2077: iload           5
        //  2079: iconst_1       
        //  2080: iadd           
        //  2081: aload_1        
        //  2082: iload           5
        //  2084: iload           7
        //  2086: iload           5
        //  2088: isub           
        //  2089: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
        //  2092: aload           23
        //  2094: aload_1        
        //  2095: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2098: goto            5257
        //  2101: aload           23
        //  2103: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  2106: invokestatic    com/google/android/gms/measurement/internal/zzfk.zzcq:(Ljava/lang/String;)Z
        //  2109: ifeq            5279
        //  2112: iload           17
        //  2114: ifeq            5279
        //  2117: aload_0        
        //  2118: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  2121: aload_0        
        //  2122: invokespecial   com/google/android/gms/measurement/internal/zzfa.zzls:()J
        //  2125: aload           22
        //  2127: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  2130: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  2133: iconst_0       
        //  2134: iconst_0       
        //  2135: iconst_1       
        //  2136: iconst_0       
        //  2137: iconst_0       
        //  2138: invokevirtual   com/google/android/gms/measurement/internal/zzq.zza:(JLjava/lang/String;ZZZZZ)Lcom/google/android/gms/measurement/internal/zzr;
        //  2141: getfield        com/google/android/gms/measurement/internal/zzr.zzahs:J
        //  2144: aload_0        
        //  2145: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  2148: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgq:()Lcom/google/android/gms/measurement/internal/zzn;
        //  2151: aload           22
        //  2153: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  2156: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  2159: getstatic       com/google/android/gms/measurement/internal/zzaf.zzajq:Lcom/google/android/gms/measurement/internal/zzaf$zza;
        //  2162: invokevirtual   com/google/android/gms/measurement/internal/zzn.zzb:(Ljava/lang/String;Lcom/google/android/gms/measurement/internal/zzaf$zza;)I
        //  2165: i2l            
        //  2166: lcmp           
        //  2167: ifle            5279
        //  2170: aload_0        
        //  2171: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  2174: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  2177: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //  2180: ldc_w           "Too many conversions. Not logging as conversion. appId"
        //  2183: aload           22
        //  2185: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  2188: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  2191: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  2194: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //  2197: aload           23
        //  2199: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2202: astore_1       
        //  2203: aload_1        
        //  2204: arraylength    
        //  2205: istore          5
        //  2207: iconst_0       
        //  2208: istore          7
        //  2210: iconst_0       
        //  2211: istore          8
        //  2213: aconst_null    
        //  2214: astore          19
        //  2216: iload           7
        //  2218: iload           5
        //  2220: if_icmpge       2278
        //  2223: aload_1        
        //  2224: iload           7
        //  2226: aaload         
        //  2227: astore          21
        //  2229: ldc_w           "_c"
        //  2232: aload           21
        //  2234: getfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  2237: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  2240: ifeq            2250
        //  2243: aload           21
        //  2245: astore          20
        //  2247: goto            5266
        //  2250: aload           19
        //  2252: astore          20
        //  2254: ldc_w           "_err"
        //  2257: aload           21
        //  2259: getfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  2262: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  2265: ifeq            5266
        //  2268: iconst_1       
        //  2269: istore          8
        //  2271: aload           19
        //  2273: astore          20
        //  2275: goto            5266
        //  2278: iload           8
        //  2280: ifeq            2316
        //  2283: aload           19
        //  2285: ifnull          2316
        //  2288: aload           23
        //  2290: aload           23
        //  2292: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2295: iconst_1       
        //  2296: anewarray       Lcom/google/android/gms/internal/measurement/zzgg;
        //  2299: dup            
        //  2300: iconst_0       
        //  2301: aload           19
        //  2303: aastore        
        //  2304: invokestatic    com/google/android/gms/common/util/ArrayUtils.removeAll:([Ljava/lang/Object;[Ljava/lang/Object;)[Ljava/lang/Object;
        //  2307: checkcast       [Lcom/google/android/gms/internal/measurement/zzgg;
        //  2310: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2313: goto            5279
        //  2316: aload           19
        //  2318: ifnull          2343
        //  2321: aload           19
        //  2323: ldc_w           "_err"
        //  2326: putfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  2329: aload           19
        //  2331: ldc2_w          10
        //  2334: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  2337: putfield        com/google/android/gms/internal/measurement/zzgg.zzawx:Ljava/lang/Long;
        //  2340: goto            5279
        //  2343: aload_0        
        //  2344: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  2347: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  2350: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //  2353: ldc_w           "Did not find conversion parameter. appId"
        //  2356: aload           22
        //  2358: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  2361: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  2364: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  2367: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //  2370: goto            5279
        //  2373: aload_0        
        //  2374: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  2377: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgq:()Lcom/google/android/gms/measurement/internal/zzn;
        //  2380: aload           22
        //  2382: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  2385: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  2388: invokevirtual   com/google/android/gms/measurement/internal/zzn.zzbf:(Ljava/lang/String;)Z
        //  2391: ifeq            5332
        //  2394: iload           17
        //  2396: ifeq            5332
        //  2399: aload           23
        //  2401: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2404: astore          19
        //  2406: iconst_0       
        //  2407: istore          4
        //  2409: iconst_m1      
        //  2410: istore          7
        //  2412: iconst_m1      
        //  2413: istore          8
        //  2415: iload           4
        //  2417: aload           19
        //  2419: arraylength    
        //  2420: if_icmpge       2479
        //  2423: ldc_w           "value"
        //  2426: aload           19
        //  2428: iload           4
        //  2430: aaload         
        //  2431: getfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  2434: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  2437: ifeq            2447
        //  2440: iload           4
        //  2442: istore          9
        //  2444: goto            5286
        //  2447: iload           7
        //  2449: istore          9
        //  2451: ldc_w           "currency"
        //  2454: aload           19
        //  2456: iload           4
        //  2458: aaload         
        //  2459: getfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  2462: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  2465: ifeq            5286
        //  2468: iload           4
        //  2470: istore          8
        //  2472: iload           7
        //  2474: istore          9
        //  2476: goto            5286
        //  2479: aload           19
        //  2481: astore_1       
        //  2482: iload           7
        //  2484: iconst_m1      
        //  2485: if_icmpeq       5329
        //  2488: aload           19
        //  2490: iload           7
        //  2492: aaload         
        //  2493: getfield        com/google/android/gms/internal/measurement/zzgg.zzawx:Ljava/lang/Long;
        //  2496: ifnonnull       5299
        //  2499: aload           19
        //  2501: iload           7
        //  2503: aaload         
        //  2504: getfield        com/google/android/gms/internal/measurement/zzgg.zzauh:Ljava/lang/Double;
        //  2507: ifnonnull       5299
        //  2510: aload_0        
        //  2511: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  2514: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  2517: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzji:()Lcom/google/android/gms/measurement/internal/zzar;
        //  2520: ldc_w           "Value must be specified with a numeric type."
        //  2523: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzbx:(Ljava/lang/String;)V
        //  2526: aload           19
        //  2528: iload           7
        //  2530: invokestatic    com/google/android/gms/measurement/internal/zzfa.zza:([Lcom/google/android/gms/internal/measurement/zzgg;I)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2533: ldc_w           "_c"
        //  2536: invokestatic    com/google/android/gms/measurement/internal/zzfa.zza:([Lcom/google/android/gms/internal/measurement/zzgg;Ljava/lang/String;)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2539: bipush          18
        //  2541: ldc_w           "value"
        //  2544: invokestatic    com/google/android/gms/measurement/internal/zzfa.zza:([Lcom/google/android/gms/internal/measurement/zzgg;ILjava/lang/String;)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2547: astore_1       
        //  2548: goto            5329
        //  2551: aload           19
        //  2553: iload           8
        //  2555: aaload         
        //  2556: getfield        com/google/android/gms/internal/measurement/zzgg.zzamp:Ljava/lang/String;
        //  2559: astore_1       
        //  2560: aload_1        
        //  2561: ifnull          5323
        //  2564: aload_1        
        //  2565: invokevirtual   java/lang/String.length:()I
        //  2568: iconst_3       
        //  2569: if_icmpeq       5311
        //  2572: goto            5323
        //  2575: iload           4
        //  2577: aload_1        
        //  2578: invokevirtual   java/lang/String.length:()I
        //  2581: if_icmpge       5317
        //  2584: aload_1        
        //  2585: iload           4
        //  2587: invokevirtual   java/lang/String.codePointAt:(I)I
        //  2590: istore          8
        //  2592: iload           8
        //  2594: invokestatic    java/lang/Character.isLetter:(I)Z
        //  2597: ifne            2603
        //  2600: goto            5323
        //  2603: iload           4
        //  2605: iload           8
        //  2607: invokestatic    java/lang/Character.charCount:(I)I
        //  2610: iadd           
        //  2611: istore          4
        //  2613: goto            2575
        //  2616: aload           19
        //  2618: astore_1       
        //  2619: iload           4
        //  2621: ifeq            2665
        //  2624: aload_0        
        //  2625: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  2628: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  2631: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzji:()Lcom/google/android/gms/measurement/internal/zzar;
        //  2634: ldc_w           "Value parameter discarded. You must also supply a 3-letter ISO_4217 currency code in the currency parameter."
        //  2637: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzbx:(Ljava/lang/String;)V
        //  2640: aload           19
        //  2642: iload           7
        //  2644: invokestatic    com/google/android/gms/measurement/internal/zzfa.zza:([Lcom/google/android/gms/internal/measurement/zzgg;I)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2647: ldc_w           "_c"
        //  2650: invokestatic    com/google/android/gms/measurement/internal/zzfa.zza:([Lcom/google/android/gms/internal/measurement/zzgg;Ljava/lang/String;)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2653: bipush          19
        //  2655: ldc_w           "currency"
        //  2658: invokestatic    com/google/android/gms/measurement/internal/zzfa.zza:([Lcom/google/android/gms/internal/measurement/zzgg;ILjava/lang/String;)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2661: astore_1       
        //  2662: goto            2665
        //  2665: aload           23
        //  2667: aload_1        
        //  2668: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2671: goto            2674
        //  2674: lload_2        
        //  2675: lstore          11
        //  2677: iload           16
        //  2679: ifeq            2826
        //  2682: lload_2        
        //  2683: lstore          11
        //  2685: ldc_w           "_e"
        //  2688: aload           23
        //  2690: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  2693: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  2696: ifeq            2826
        //  2699: aload           23
        //  2701: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2704: ifnull          2794
        //  2707: aload           23
        //  2709: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  2712: arraylength    
        //  2713: ifne            2719
        //  2716: goto            2794
        //  2719: aload_0        
        //  2720: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //  2723: pop            
        //  2724: aload           23
        //  2726: ldc_w           "_et"
        //  2729: invokestatic    com/google/android/gms/measurement/internal/zzfg.zzb:(Lcom/google/android/gms/internal/measurement/zzgf;Ljava/lang/String;)Ljava/lang/Object;
        //  2732: checkcast       Ljava/lang/Long;
        //  2735: astore_1       
        //  2736: aload_1        
        //  2737: ifnonnull       2783
        //  2740: aload_0        
        //  2741: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  2744: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  2747: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //  2750: astore_1       
        //  2751: ldc_w           "Engagement event does not include duration. appId"
        //  2754: astore          19
        //  2756: aload           22
        //  2758: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  2761: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  2764: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  2767: astore          20
        //  2769: aload_1        
        //  2770: aload           19
        //  2772: aload           20
        //  2774: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //  2777: lload_2        
        //  2778: lstore          11
        //  2780: goto            2826
        //  2783: lload_2        
        //  2784: aload_1        
        //  2785: invokevirtual   java/lang/Long.longValue:()J
        //  2788: ladd           
        //  2789: lstore          11
        //  2791: goto            2826
        //  2794: aload_0        
        //  2795: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  2798: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  2801: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //  2804: astore_1       
        //  2805: ldc_w           "Engagement event does not contain any parameters. appId"
        //  2808: astore          19
        //  2810: aload           22
        //  2812: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  2815: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  2818: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  2821: astore          20
        //  2823: goto            2769
        //  2826: aload           18
        //  2828: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  2831: astore_1       
        //  2832: iload           6
        //  2834: iconst_1       
        //  2835: iadd           
        //  2836: istore          7
        //  2838: aload_1        
        //  2839: iload           6
        //  2841: aload           23
        //  2843: aastore        
        //  2844: iload           5
        //  2846: istore          4
        //  2848: lload           11
        //  2850: lstore_2       
        //  2851: iload           7
        //  2853: istore          5
        //  2855: iload           4
        //  2857: iconst_1       
        //  2858: iadd           
        //  2859: istore          4
        //  2861: goto            1310
        //  2864: iload           5
        //  2866: aload           22
        //  2868: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzauc:Ljava/util/List;
        //  2871: invokeinterface java/util/List.size:()I
        //  2876: if_icmpge       2897
        //  2879: aload           18
        //  2881: aload           18
        //  2883: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  2886: iload           5
        //  2888: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //  2891: checkcast       [Lcom/google/android/gms/internal/measurement/zzgf;
        //  2894: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  2897: iload           16
        //  2899: ifeq            5353
        //  2902: aload_0        
        //  2903: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  2906: aload           18
        //  2908: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  2911: ldc_w           "_lte"
        //  2914: invokevirtual   com/google/android/gms/measurement/internal/zzq.zzi:(Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/gms/measurement/internal/zzfj;
        //  2917: astore_1       
        //  2918: aload_1        
        //  2919: ifnull          2982
        //  2922: aload_1        
        //  2923: getfield        com/google/android/gms/measurement/internal/zzfj.value:Ljava/lang/Object;
        //  2926: ifnonnull       2932
        //  2929: goto            2982
        //  2932: new             Lcom/google/android/gms/measurement/internal/zzfj;
        //  2935: dup            
        //  2936: aload           18
        //  2938: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  2941: ldc_w           "auto"
        //  2944: ldc_w           "_lte"
        //  2947: aload_0        
        //  2948: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  2951: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzbx:()Lcom/google/android/gms/common/util/Clock;
        //  2954: invokeinterface com/google/android/gms/common/util/Clock.currentTimeMillis:()J
        //  2959: aload_1        
        //  2960: getfield        com/google/android/gms/measurement/internal/zzfj.value:Ljava/lang/Object;
        //  2963: checkcast       Ljava/lang/Long;
        //  2966: invokevirtual   java/lang/Long.longValue:()J
        //  2969: lload_2        
        //  2970: ladd           
        //  2971: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  2974: invokespecial   com/google/android/gms/measurement/internal/zzfj.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/Object;)V
        //  2977: astore          19
        //  2979: goto            3018
        //  2982: new             Lcom/google/android/gms/measurement/internal/zzfj;
        //  2985: dup            
        //  2986: aload           18
        //  2988: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  2991: ldc_w           "auto"
        //  2994: ldc_w           "_lte"
        //  2997: aload_0        
        //  2998: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  3001: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzbx:()Lcom/google/android/gms/common/util/Clock;
        //  3004: invokeinterface com/google/android/gms/common/util/Clock.currentTimeMillis:()J
        //  3009: lload_2        
        //  3010: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  3013: invokespecial   com/google/android/gms/measurement/internal/zzfj.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/Object;)V
        //  3016: astore          19
        //  3018: new             Lcom/google/android/gms/internal/measurement/zzgl;
        //  3021: dup            
        //  3022: invokespecial   com/google/android/gms/internal/measurement/zzgl.<init>:()V
        //  3025: astore_1       
        //  3026: aload_1        
        //  3027: ldc_w           "_lte"
        //  3030: putfield        com/google/android/gms/internal/measurement/zzgl.name:Ljava/lang/String;
        //  3033: aload_1        
        //  3034: aload_0        
        //  3035: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  3038: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzbx:()Lcom/google/android/gms/common/util/Clock;
        //  3041: invokeinterface com/google/android/gms/common/util/Clock.currentTimeMillis:()J
        //  3046: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  3049: putfield        com/google/android/gms/internal/measurement/zzgl.zzayl:Ljava/lang/Long;
        //  3052: aload_1        
        //  3053: aload           19
        //  3055: getfield        com/google/android/gms/measurement/internal/zzfj.value:Ljava/lang/Object;
        //  3058: checkcast       Ljava/lang/Long;
        //  3061: putfield        com/google/android/gms/internal/measurement/zzgl.zzawx:Ljava/lang/Long;
        //  3064: iconst_0       
        //  3065: istore          4
        //  3067: iload           4
        //  3069: aload           18
        //  3071: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxc:[Lcom/google/android/gms/internal/measurement/zzgl;
        //  3074: arraylength    
        //  3075: if_icmpge       5344
        //  3078: ldc_w           "_lte"
        //  3081: aload           18
        //  3083: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxc:[Lcom/google/android/gms/internal/measurement/zzgl;
        //  3086: iload           4
        //  3088: aaload         
        //  3089: getfield        com/google/android/gms/internal/measurement/zzgl.name:Ljava/lang/String;
        //  3092: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3095: ifeq            5335
        //  3098: aload           18
        //  3100: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxc:[Lcom/google/android/gms/internal/measurement/zzgl;
        //  3103: iload           4
        //  3105: aload_1        
        //  3106: aastore        
        //  3107: iconst_1       
        //  3108: istore          4
        //  3110: goto            3113
        //  3113: iload           4
        //  3115: ifne            5350
        //  3118: aload           18
        //  3120: aload           18
        //  3122: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxc:[Lcom/google/android/gms/internal/measurement/zzgl;
        //  3125: aload           18
        //  3127: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxc:[Lcom/google/android/gms/internal/measurement/zzgl;
        //  3130: arraylength    
        //  3131: iconst_1       
        //  3132: iadd           
        //  3133: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //  3136: checkcast       [Lcom/google/android/gms/internal/measurement/zzgl;
        //  3139: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxc:[Lcom/google/android/gms/internal/measurement/zzgl;
        //  3142: aload           18
        //  3144: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxc:[Lcom/google/android/gms/internal/measurement/zzgl;
        //  3147: aload           22
        //  3149: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  3152: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxc:[Lcom/google/android/gms/internal/measurement/zzgl;
        //  3155: arraylength    
        //  3156: iconst_1       
        //  3157: isub           
        //  3158: aload_1        
        //  3159: aastore        
        //  3160: goto            3163
        //  3163: aload           22
        //  3165: astore_1       
        //  3166: lload_2        
        //  3167: lconst_0       
        //  3168: lcmp           
        //  3169: ifle            3209
        //  3172: aload_0        
        //  3173: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  3176: aload           19
        //  3178: invokevirtual   com/google/android/gms/measurement/internal/zzq.zza:(Lcom/google/android/gms/measurement/internal/zzfj;)Z
        //  3181: pop            
        //  3182: aload_0        
        //  3183: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  3186: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  3189: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjk:()Lcom/google/android/gms/measurement/internal/zzar;
        //  3192: ldc_w           "Updated lifetime engagement user property with value. Value"
        //  3195: aload           19
        //  3197: getfield        com/google/android/gms/measurement/internal/zzfj.value:Ljava/lang/Object;
        //  3200: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //  3203: aload           22
        //  3205: astore_1       
        //  3206: goto            3209
        //  3209: aload           18
        //  3211: aload_0        
        //  3212: aload           18
        //  3214: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  3217: aload           18
        //  3219: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxc:[Lcom/google/android/gms/internal/measurement/zzgl;
        //  3222: aload           18
        //  3224: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  3227: invokespecial   com/google/android/gms/measurement/internal/zzfa.zza:(Ljava/lang/String;[Lcom/google/android/gms/internal/measurement/zzgl;[Lcom/google/android/gms/internal/measurement/zzgf;)[Lcom/google/android/gms/internal/measurement/zzgd;
        //  3230: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxt:[Lcom/google/android/gms/internal/measurement/zzgd;
        //  3233: aload_0        
        //  3234: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  3237: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgq:()Lcom/google/android/gms/measurement/internal/zzn;
        //  3240: aload_1        
        //  3241: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  3244: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  3247: invokevirtual   com/google/android/gms/measurement/internal/zzn.zzaw:(Ljava/lang/String;)Z
        //  3250: ifeq            5453
        //  3253: new             Ljava/util/HashMap;
        //  3256: dup            
        //  3257: invokespecial   java/util/HashMap.<init>:()V
        //  3260: astore          24
        //  3262: aload           18
        //  3264: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  3267: arraylength    
        //  3268: anewarray       Lcom/google/android/gms/internal/measurement/zzgf;
        //  3271: astore          25
        //  3273: aload_0        
        //  3274: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  3277: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgm:()Lcom/google/android/gms/measurement/internal/zzfk;
        //  3280: invokevirtual   com/google/android/gms/measurement/internal/zzfk.zzmd:()Ljava/security/SecureRandom;
        //  3283: astore          20
        //  3285: aload           18
        //  3287: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  3290: astore          21
        //  3292: aload           21
        //  3294: arraylength    
        //  3295: istore          6
        //  3297: iconst_0       
        //  3298: istore          7
        //  3300: iconst_0       
        //  3301: istore          4
        //  3303: aload_1        
        //  3304: astore          19
        //  3306: iload           7
        //  3308: iload           6
        //  3310: if_icmpge       4361
        //  3313: aload           21
        //  3315: iload           7
        //  3317: aaload         
        //  3318: astore          26
        //  3320: aload           26
        //  3322: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  3325: ldc_w           "_ep"
        //  3328: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3331: ifeq            3511
        //  3334: aload_0        
        //  3335: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //  3338: pop            
        //  3339: aload           26
        //  3341: ldc_w           "_en"
        //  3344: invokestatic    com/google/android/gms/measurement/internal/zzfg.zzb:(Lcom/google/android/gms/internal/measurement/zzgf;Ljava/lang/String;)Ljava/lang/Object;
        //  3347: checkcast       Ljava/lang/String;
        //  3350: astore          23
        //  3352: aload           24
        //  3354: aload           23
        //  3356: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3361: checkcast       Lcom/google/android/gms/measurement/internal/zzz;
        //  3364: astore          22
        //  3366: aload           22
        //  3368: astore_1       
        //  3369: aload           22
        //  3371: ifnonnull       3403
        //  3374: aload_0        
        //  3375: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  3378: aload           19
        //  3380: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  3383: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  3386: aload           23
        //  3388: invokevirtual   com/google/android/gms/measurement/internal/zzq.zzg:(Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/gms/measurement/internal/zzz;
        //  3391: astore_1       
        //  3392: aload           24
        //  3394: aload           23
        //  3396: aload_1        
        //  3397: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3402: pop            
        //  3403: aload_1        
        //  3404: getfield        com/google/android/gms/measurement/internal/zzz.zzaij:Ljava/lang/Long;
        //  3407: ifnonnull       3508
        //  3410: aload_1        
        //  3411: getfield        com/google/android/gms/measurement/internal/zzz.zzaik:Ljava/lang/Long;
        //  3414: invokevirtual   java/lang/Long.longValue:()J
        //  3417: lconst_1       
        //  3418: lcmp           
        //  3419: ifle            3447
        //  3422: aload_0        
        //  3423: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //  3426: pop            
        //  3427: aload           26
        //  3429: aload           26
        //  3431: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  3434: ldc_w           "_sr"
        //  3437: aload_1        
        //  3438: getfield        com/google/android/gms/measurement/internal/zzz.zzaik:Ljava/lang/Long;
        //  3441: invokestatic    com/google/android/gms/measurement/internal/zzfg.zza:([Lcom/google/android/gms/internal/measurement/zzgg;Ljava/lang/String;Ljava/lang/Object;)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  3444: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  3447: aload_1        
        //  3448: getfield        com/google/android/gms/measurement/internal/zzz.zzail:Ljava/lang/Boolean;
        //  3451: ifnull          3492
        //  3454: aload_1        
        //  3455: getfield        com/google/android/gms/measurement/internal/zzz.zzail:Ljava/lang/Boolean;
        //  3458: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //  3461: ifeq            3492
        //  3464: aload_0        
        //  3465: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //  3468: pop            
        //  3469: aload           26
        //  3471: aload           26
        //  3473: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  3476: ldc_w           "_efs"
        //  3479: lconst_1       
        //  3480: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  3483: invokestatic    com/google/android/gms/measurement/internal/zzfg.zza:([Lcom/google/android/gms/internal/measurement/zzgg;Ljava/lang/String;Ljava/lang/Object;)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  3486: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  3489: goto            3492
        //  3492: aload           25
        //  3494: iload           4
        //  3496: aload           26
        //  3498: aastore        
        //  3499: iload           4
        //  3501: iconst_1       
        //  3502: iadd           
        //  3503: istore          4
        //  3505: goto            3508
        //  3508: goto            5404
        //  3511: aload_0        
        //  3512: invokespecial   com/google/android/gms/measurement/internal/zzfa.zzln:()Lcom/google/android/gms/measurement/internal/zzbn;
        //  3515: aload           19
        //  3517: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  3520: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  3523: invokevirtual   com/google/android/gms/measurement/internal/zzbn.zzcj:(Ljava/lang/String;)J
        //  3526: lstore_2       
        //  3527: aload_0        
        //  3528: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  3531: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgm:()Lcom/google/android/gms/measurement/internal/zzfk;
        //  3534: pop            
        //  3535: aload           26
        //  3537: getfield        com/google/android/gms/internal/measurement/zzgf.zzawu:Ljava/lang/Long;
        //  3540: invokevirtual   java/lang/Long.longValue:()J
        //  3543: lload_2        
        //  3544: invokestatic    com/google/android/gms/measurement/internal/zzfk.zzc:(JJ)J
        //  3547: lstore          13
        //  3549: lconst_1       
        //  3550: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  3553: astore          22
        //  3555: ldc_w           "_dbg"
        //  3558: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  3561: ifne            5374
        //  3564: aload           22
        //  3566: ifnonnull       3572
        //  3569: goto            5374
        //  3572: aload           26
        //  3574: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  3577: astore_1       
        //  3578: aload_1        
        //  3579: arraylength    
        //  3580: istore          5
        //  3582: iconst_0       
        //  3583: istore          8
        //  3585: lload_2        
        //  3586: lstore          11
        //  3588: iload           8
        //  3590: iload           5
        //  3592: if_icmpge       5377
        //  3595: aload_1        
        //  3596: iload           8
        //  3598: aaload         
        //  3599: astore          23
        //  3601: ldc_w           "_dbg"
        //  3604: aload           23
        //  3606: getfield        com/google/android/gms/internal/measurement/zzgg.name:Ljava/lang/String;
        //  3609: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3612: ifeq            5365
        //  3615: aload           22
        //  3617: instanceof      Ljava/lang/Long;
        //  3620: ifeq            3636
        //  3623: aload           22
        //  3625: aload           23
        //  3627: getfield        com/google/android/gms/internal/measurement/zzgg.zzawx:Ljava/lang/Long;
        //  3630: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3633: ifne            5359
        //  3636: aload           22
        //  3638: instanceof      Ljava/lang/String;
        //  3641: ifeq            3657
        //  3644: aload           22
        //  3646: aload           23
        //  3648: getfield        com/google/android/gms/internal/measurement/zzgg.zzamp:Ljava/lang/String;
        //  3651: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3654: ifne            5359
        //  3657: lload_2        
        //  3658: lstore          11
        //  3660: aload           22
        //  3662: instanceof      Ljava/lang/Double;
        //  3665: ifeq            5377
        //  3668: lload_2        
        //  3669: lstore          11
        //  3671: aload           22
        //  3673: aload           23
        //  3675: getfield        com/google/android/gms/internal/measurement/zzgg.zzauh:Ljava/lang/Double;
        //  3678: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  3681: ifeq            5377
        //  3684: goto            5359
        //  3687: iload           5
        //  3689: ifne            5386
        //  3692: aload_0        
        //  3693: invokespecial   com/google/android/gms/measurement/internal/zzfa.zzln:()Lcom/google/android/gms/measurement/internal/zzbn;
        //  3696: aload           19
        //  3698: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  3701: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  3704: aload           26
        //  3706: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  3709: invokevirtual   com/google/android/gms/measurement/internal/zzbn.zzq:(Ljava/lang/String;Ljava/lang/String;)I
        //  3712: istore          5
        //  3714: goto            3717
        //  3717: iload           5
        //  3719: ifgt            3764
        //  3722: aload_0        
        //  3723: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  3726: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  3729: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //  3732: ldc_w           "Sample rate must be positive. event, rate"
        //  3735: aload           26
        //  3737: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  3740: iload           5
        //  3742: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3745: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //  3748: aload           25
        //  3750: iload           4
        //  3752: aload           26
        //  3754: aastore        
        //  3755: iload           4
        //  3757: iconst_1       
        //  3758: iadd           
        //  3759: istore          4
        //  3761: goto            5404
        //  3764: aload           24
        //  3766: aload           26
        //  3768: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  3771: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3776: checkcast       Lcom/google/android/gms/measurement/internal/zzz;
        //  3779: astore          22
        //  3781: aload           22
        //  3783: astore_1       
        //  3784: aload           22
        //  3786: ifnonnull       3884
        //  3789: aload_0        
        //  3790: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  3793: aload           19
        //  3795: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  3798: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  3801: aload           26
        //  3803: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  3806: invokevirtual   com/google/android/gms/measurement/internal/zzq.zzg:(Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/gms/measurement/internal/zzz;
        //  3809: astore          22
        //  3811: aload           22
        //  3813: astore_1       
        //  3814: aload           22
        //  3816: ifnonnull       3884
        //  3819: aload_0        
        //  3820: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  3823: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  3826: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //  3829: ldc_w           "Event being bundled has no eventAggregate. appId, eventName"
        //  3832: aload           19
        //  3834: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  3837: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  3840: aload           26
        //  3842: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  3845: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //  3848: new             Lcom/google/android/gms/measurement/internal/zzz;
        //  3851: dup            
        //  3852: aload           19
        //  3854: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  3857: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  3860: aload           26
        //  3862: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  3865: lconst_1       
        //  3866: lconst_1       
        //  3867: aload           26
        //  3869: getfield        com/google/android/gms/internal/measurement/zzgf.zzawu:Ljava/lang/Long;
        //  3872: invokevirtual   java/lang/Long.longValue:()J
        //  3875: lconst_0       
        //  3876: aconst_null    
        //  3877: aconst_null    
        //  3878: aconst_null    
        //  3879: aconst_null    
        //  3880: invokespecial   com/google/android/gms/measurement/internal/zzz.<init>:(Ljava/lang/String;Ljava/lang/String;JJJJLjava/lang/Long;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Boolean;)V
        //  3883: astore_1       
        //  3884: aload_0        
        //  3885: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //  3888: pop            
        //  3889: aload           26
        //  3891: ldc_w           "_eid"
        //  3894: invokestatic    com/google/android/gms/measurement/internal/zzfg.zzb:(Lcom/google/android/gms/internal/measurement/zzgf;Ljava/lang/String;)Ljava/lang/Object;
        //  3897: checkcast       Ljava/lang/Long;
        //  3900: astore          22
        //  3902: aload           22
        //  3904: ifnull          5392
        //  3907: iconst_1       
        //  3908: istore          16
        //  3910: goto            3913
        //  3913: iload           16
        //  3915: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //  3918: astore          23
        //  3920: iload           5
        //  3922: iconst_1       
        //  3923: if_icmpne       3987
        //  3926: aload           25
        //  3928: iload           4
        //  3930: aload           26
        //  3932: aastore        
        //  3933: aload           23
        //  3935: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //  3938: ifeq            5398
        //  3941: aload_1        
        //  3942: getfield        com/google/android/gms/measurement/internal/zzz.zzaij:Ljava/lang/Long;
        //  3945: ifnonnull       3962
        //  3948: aload_1        
        //  3949: getfield        com/google/android/gms/measurement/internal/zzz.zzaik:Ljava/lang/Long;
        //  3952: ifnonnull       3962
        //  3955: aload_1        
        //  3956: getfield        com/google/android/gms/measurement/internal/zzz.zzail:Ljava/lang/Boolean;
        //  3959: ifnull          5398
        //  3962: aload_1        
        //  3963: aconst_null    
        //  3964: aconst_null    
        //  3965: aconst_null    
        //  3966: invokevirtual   com/google/android/gms/measurement/internal/zzz.zza:(Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Boolean;)Lcom/google/android/gms/measurement/internal/zzz;
        //  3969: astore_1       
        //  3970: aload           24
        //  3972: aload           26
        //  3974: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  3977: aload_1        
        //  3978: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3983: pop            
        //  3984: goto            5398
        //  3987: aload           20
        //  3989: iload           5
        //  3991: invokevirtual   java/security/SecureRandom.nextInt:(I)I
        //  3994: ifne            4097
        //  3997: aload_0        
        //  3998: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //  4001: pop            
        //  4002: aload           26
        //  4004: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  4007: astore          22
        //  4009: iload           5
        //  4011: i2l            
        //  4012: lstore_2       
        //  4013: aload           26
        //  4015: aload           22
        //  4017: ldc_w           "_sr"
        //  4020: lload_2        
        //  4021: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4024: invokestatic    com/google/android/gms/measurement/internal/zzfg.zza:([Lcom/google/android/gms/internal/measurement/zzgg;Ljava/lang/String;Ljava/lang/Object;)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  4027: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  4030: aload           25
        //  4032: iload           4
        //  4034: aload           26
        //  4036: aastore        
        //  4037: aload_1        
        //  4038: astore          22
        //  4040: aload           23
        //  4042: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //  4045: ifeq            4060
        //  4048: aload_1        
        //  4049: aconst_null    
        //  4050: lload_2        
        //  4051: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4054: aconst_null    
        //  4055: invokevirtual   com/google/android/gms/measurement/internal/zzz.zza:(Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Boolean;)Lcom/google/android/gms/measurement/internal/zzz;
        //  4058: astore          22
        //  4060: aload           24
        //  4062: aload           26
        //  4064: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  4067: aload           22
        //  4069: aload           26
        //  4071: getfield        com/google/android/gms/internal/measurement/zzgf.zzawu:Ljava/lang/Long;
        //  4074: invokevirtual   java/lang/Long.longValue:()J
        //  4077: lload           13
        //  4079: invokevirtual   com/google/android/gms/measurement/internal/zzz.zza:(JJ)Lcom/google/android/gms/measurement/internal/zzz;
        //  4082: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4087: pop            
        //  4088: iload           4
        //  4090: iconst_1       
        //  4091: iadd           
        //  4092: istore          4
        //  4094: goto            5404
        //  4097: aload_0        
        //  4098: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  4101: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgq:()Lcom/google/android/gms/measurement/internal/zzn;
        //  4104: aload           19
        //  4106: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  4109: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  4112: invokevirtual   com/google/android/gms/measurement/internal/zzn.zzbh:(Ljava/lang/String;)Z
        //  4115: ifeq            4160
        //  4118: aload_1        
        //  4119: getfield        com/google/android/gms/measurement/internal/zzz.zzaii:Ljava/lang/Long;
        //  4122: ifnull          4136
        //  4125: aload_1        
        //  4126: getfield        com/google/android/gms/measurement/internal/zzz.zzaii:Ljava/lang/Long;
        //  4129: invokevirtual   java/lang/Long.longValue:()J
        //  4132: lstore_2       
        //  4133: goto            5411
        //  4136: aload_0        
        //  4137: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  4140: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgm:()Lcom/google/android/gms/measurement/internal/zzfk;
        //  4143: pop            
        //  4144: aload           26
        //  4146: getfield        com/google/android/gms/internal/measurement/zzgf.zzawv:Ljava/lang/Long;
        //  4149: invokevirtual   java/lang/Long.longValue:()J
        //  4152: lload_2        
        //  4153: invokestatic    com/google/android/gms/measurement/internal/zzfk.zzc:(JJ)J
        //  4156: lstore_2       
        //  4157: goto            5411
        //  4160: aload_1        
        //  4161: getfield        com/google/android/gms/measurement/internal/zzz.zzaih:J
        //  4164: lstore_2       
        //  4165: aload           26
        //  4167: getfield        com/google/android/gms/internal/measurement/zzgf.zzawu:Ljava/lang/Long;
        //  4170: invokevirtual   java/lang/Long.longValue:()J
        //  4173: lload_2        
        //  4174: lsub           
        //  4175: invokestatic    java/lang/Math.abs:(J)J
        //  4178: ldc2_w          86400000
        //  4181: lcmp           
        //  4182: iflt            5430
        //  4185: goto            5424
        //  4188: iload           8
        //  4190: ifeq            4321
        //  4193: aload_0        
        //  4194: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //  4197: pop            
        //  4198: aload           26
        //  4200: aload           26
        //  4202: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  4205: ldc_w           "_efs"
        //  4208: lconst_1       
        //  4209: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4212: invokestatic    com/google/android/gms/measurement/internal/zzfg.zza:([Lcom/google/android/gms/internal/measurement/zzgg;Ljava/lang/String;Ljava/lang/Object;)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  4215: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  4218: aload_0        
        //  4219: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //  4222: pop            
        //  4223: aload           26
        //  4225: getfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  4228: astore          22
        //  4230: iload           5
        //  4232: i2l            
        //  4233: lstore_2       
        //  4234: aload           26
        //  4236: aload           22
        //  4238: ldc_w           "_sr"
        //  4241: lload_2        
        //  4242: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4245: invokestatic    com/google/android/gms/measurement/internal/zzfg.zza:([Lcom/google/android/gms/internal/measurement/zzgg;Ljava/lang/String;Ljava/lang/Object;)[Lcom/google/android/gms/internal/measurement/zzgg;
        //  4248: putfield        com/google/android/gms/internal/measurement/zzgf.zzawt:[Lcom/google/android/gms/internal/measurement/zzgg;
        //  4251: aload           25
        //  4253: iload           4
        //  4255: aload           26
        //  4257: aastore        
        //  4258: aload_1        
        //  4259: astore          22
        //  4261: aload           23
        //  4263: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //  4266: ifeq            4284
        //  4269: aload_1        
        //  4270: aconst_null    
        //  4271: lload_2        
        //  4272: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4275: iconst_1       
        //  4276: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //  4279: invokevirtual   com/google/android/gms/measurement/internal/zzz.zza:(Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Boolean;)Lcom/google/android/gms/measurement/internal/zzz;
        //  4282: astore          22
        //  4284: aload           24
        //  4286: aload           26
        //  4288: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  4291: aload           22
        //  4293: aload           26
        //  4295: getfield        com/google/android/gms/internal/measurement/zzgf.zzawu:Ljava/lang/Long;
        //  4298: invokevirtual   java/lang/Long.longValue:()J
        //  4301: lload           13
        //  4303: invokevirtual   com/google/android/gms/measurement/internal/zzz.zza:(JJ)Lcom/google/android/gms/measurement/internal/zzz;
        //  4306: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4311: pop            
        //  4312: iload           4
        //  4314: iconst_1       
        //  4315: iadd           
        //  4316: istore          5
        //  4318: goto            5436
        //  4321: iload           4
        //  4323: istore          5
        //  4325: aload           23
        //  4327: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //  4330: ifeq            5436
        //  4333: aload           24
        //  4335: aload           26
        //  4337: getfield        com/google/android/gms/internal/measurement/zzgf.name:Ljava/lang/String;
        //  4340: aload_1        
        //  4341: aload           22
        //  4343: aconst_null    
        //  4344: aconst_null    
        //  4345: invokevirtual   com/google/android/gms/measurement/internal/zzz.zza:(Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Boolean;)Lcom/google/android/gms/measurement/internal/zzz;
        //  4348: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4353: pop            
        //  4354: iload           4
        //  4356: istore          5
        //  4358: goto            5436
        //  4361: aload           19
        //  4363: astore          20
        //  4365: iload           4
        //  4367: aload           18
        //  4369: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  4372: arraylength    
        //  4373: if_icmpge       4391
        //  4376: aload           18
        //  4378: aload           25
        //  4380: iload           4
        //  4382: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //  4385: checkcast       [Lcom/google/android/gms/internal/measurement/zzgf;
        //  4388: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  4391: aload           24
        //  4393: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //  4398: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //  4403: astore          21
        //  4405: aload           18
        //  4407: astore_1       
        //  4408: aload           20
        //  4410: astore          19
        //  4412: aload           21
        //  4414: invokeinterface java/util/Iterator.hasNext:()Z
        //  4419: ifeq            4452
        //  4422: aload           21
        //  4424: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  4429: checkcast       Ljava/util/Map$Entry;
        //  4432: astore_1       
        //  4433: aload_0        
        //  4434: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  4437: aload_1        
        //  4438: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //  4443: checkcast       Lcom/google/android/gms/measurement/internal/zzz;
        //  4446: invokevirtual   com/google/android/gms/measurement/internal/zzq.zza:(Lcom/google/android/gms/measurement/internal/zzz;)V
        //  4449: goto            4405
        //  4452: aload_1        
        //  4453: ldc2_w          9223372036854775807
        //  4456: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4459: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxe:Ljava/lang/Long;
        //  4462: aload_1        
        //  4463: ldc2_w          -9223372036854775808
        //  4466: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4469: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxf:Ljava/lang/Long;
        //  4472: iconst_0       
        //  4473: istore          4
        //  4475: iload           4
        //  4477: aload_1        
        //  4478: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  4481: arraylength    
        //  4482: if_icmpge       4553
        //  4485: aload_1        
        //  4486: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  4489: iload           4
        //  4491: aaload         
        //  4492: astore          18
        //  4494: aload           18
        //  4496: getfield        com/google/android/gms/internal/measurement/zzgf.zzawu:Ljava/lang/Long;
        //  4499: invokevirtual   java/lang/Long.longValue:()J
        //  4502: aload_1        
        //  4503: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxe:Ljava/lang/Long;
        //  4506: invokevirtual   java/lang/Long.longValue:()J
        //  4509: lcmp           
        //  4510: ifge            4522
        //  4513: aload_1        
        //  4514: aload           18
        //  4516: getfield        com/google/android/gms/internal/measurement/zzgf.zzawu:Ljava/lang/Long;
        //  4519: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxe:Ljava/lang/Long;
        //  4522: aload           18
        //  4524: getfield        com/google/android/gms/internal/measurement/zzgf.zzawu:Ljava/lang/Long;
        //  4527: invokevirtual   java/lang/Long.longValue:()J
        //  4530: aload_1        
        //  4531: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxf:Ljava/lang/Long;
        //  4534: invokevirtual   java/lang/Long.longValue:()J
        //  4537: lcmp           
        //  4538: ifle            5462
        //  4541: aload_1        
        //  4542: aload           18
        //  4544: getfield        com/google/android/gms/internal/measurement/zzgf.zzawu:Ljava/lang/Long;
        //  4547: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxf:Ljava/lang/Long;
        //  4550: goto            5462
        //  4553: aload           19
        //  4555: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  4558: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  4561: astore          20
        //  4563: aload_0        
        //  4564: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  4567: aload           20
        //  4569: invokevirtual   com/google/android/gms/measurement/internal/zzq.zzbl:(Ljava/lang/String;)Lcom/google/android/gms/measurement/internal/zzg;
        //  4572: astore          21
        //  4574: aload           21
        //  4576: ifnonnull       4609
        //  4579: aload_0        
        //  4580: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  4583: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  4586: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //  4589: ldc_w           "Bundling raw events w/o app info. appId"
        //  4592: aload           19
        //  4594: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  4597: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  4600: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  4603: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //  4606: goto            4742
        //  4609: aload_1        
        //  4610: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  4613: arraylength    
        //  4614: ifle            4742
        //  4617: aload           21
        //  4619: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzgz:()J
        //  4622: lstore_2       
        //  4623: lload_2        
        //  4624: lconst_0       
        //  4625: lcmp           
        //  4626: ifeq            5471
        //  4629: lload_2        
        //  4630: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4633: astore          18
        //  4635: goto            4638
        //  4638: aload_1        
        //  4639: aload           18
        //  4641: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxh:Ljava/lang/Long;
        //  4644: aload           21
        //  4646: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzgy:()J
        //  4649: lstore          11
        //  4651: lload           11
        //  4653: lconst_0       
        //  4654: lcmp           
        //  4655: ifne            5477
        //  4658: goto            4661
        //  4661: lload_2        
        //  4662: lconst_0       
        //  4663: lcmp           
        //  4664: ifeq            5483
        //  4667: lload_2        
        //  4668: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4671: astore          18
        //  4673: goto            4676
        //  4676: aload_1        
        //  4677: aload           18
        //  4679: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxg:Ljava/lang/Long;
        //  4682: aload           21
        //  4684: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzhh:()V
        //  4687: aload_1        
        //  4688: aload           21
        //  4690: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzhe:()J
        //  4693: l2i            
        //  4694: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  4697: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxr:Ljava/lang/Integer;
        //  4700: aload           21
        //  4702: aload_1        
        //  4703: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxe:Ljava/lang/Long;
        //  4706: invokevirtual   java/lang/Long.longValue:()J
        //  4709: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzs:(J)V
        //  4712: aload           21
        //  4714: aload_1        
        //  4715: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxf:Ljava/lang/Long;
        //  4718: invokevirtual   java/lang/Long.longValue:()J
        //  4721: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzt:(J)V
        //  4724: aload_1        
        //  4725: aload           21
        //  4727: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzhp:()Ljava/lang/String;
        //  4730: putfield        com/google/android/gms/internal/measurement/zzgi.zzagv:Ljava/lang/String;
        //  4733: aload_0        
        //  4734: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  4737: aload           21
        //  4739: invokevirtual   com/google/android/gms/measurement/internal/zzq.zza:(Lcom/google/android/gms/measurement/internal/zzg;)V
        //  4742: aload_1        
        //  4743: getfield        com/google/android/gms/internal/measurement/zzgi.zzaxb:[Lcom/google/android/gms/internal/measurement/zzgf;
        //  4746: arraylength    
        //  4747: ifle            4870
        //  4750: aload_0        
        //  4751: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  4754: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgr:()Lcom/google/android/gms/measurement/internal/zzk;
        //  4757: pop            
        //  4758: aload_0        
        //  4759: invokespecial   com/google/android/gms/measurement/internal/zzfa.zzln:()Lcom/google/android/gms/measurement/internal/zzbn;
        //  4762: aload           19
        //  4764: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  4767: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  4770: invokevirtual   com/google/android/gms/measurement/internal/zzbn.zzcf:(Ljava/lang/String;)Lcom/google/android/gms/internal/measurement/zzgb;
        //  4773: astore          18
        //  4775: aload           18
        //  4777: ifnull          4807
        //  4780: aload           18
        //  4782: getfield        com/google/android/gms/internal/measurement/zzgb.zzawe:Ljava/lang/Long;
        //  4785: ifnonnull       4791
        //  4788: goto            4807
        //  4791: aload           18
        //  4793: getfield        com/google/android/gms/internal/measurement/zzgb.zzawe:Ljava/lang/Long;
        //  4796: astore          18
        //  4798: aload_1        
        //  4799: aload           18
        //  4801: putfield        com/google/android/gms/internal/measurement/zzgi.zzaxy:Ljava/lang/Long;
        //  4804: goto            4859
        //  4807: aload           19
        //  4809: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  4812: getfield        com/google/android/gms/internal/measurement/zzgi.zzafx:Ljava/lang/String;
        //  4815: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  4818: ifeq            4832
        //  4821: ldc2_w          -1
        //  4824: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4827: astore          18
        //  4829: goto            4798
        //  4832: aload_0        
        //  4833: getfield        com/google/android/gms/measurement/internal/zzfa.zzadj:Lcom/google/android/gms/measurement/internal/zzbt;
        //  4836: invokevirtual   com/google/android/gms/measurement/internal/zzbt.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  4839: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //  4842: ldc_w           "Did not find measurement config or missing version info. appId"
        //  4845: aload           19
        //  4847: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaua:Lcom/google/android/gms/internal/measurement/zzgi;
        //  4850: getfield        com/google/android/gms/internal/measurement/zzgi.zztt:Ljava/lang/String;
        //  4853: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  4856: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //  4859: aload_0        
        //  4860: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  4863: aload_1        
        //  4864: iload           15
        //  4866: invokevirtual   com/google/android/gms/measurement/internal/zzq.zza:(Lcom/google/android/gms/internal/measurement/zzgi;Z)Z
        //  4869: pop            
        //  4870: aload_0        
        //  4871: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  4874: astore_1       
        //  4875: aload           19
        //  4877: getfield        com/google/android/gms/measurement/internal/zzfa$zza.zzaub:Ljava/util/List;
        //  4880: astore          18
        //  4882: aload           18
        //  4884: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotNull:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4887: pop            
        //  4888: aload_1        
        //  4889: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //  4892: aload_1        
        //  4893: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //  4896: new             Ljava/lang/StringBuilder;
        //  4899: dup            
        //  4900: ldc_w           "rowid in ("
        //  4903: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //  4906: astore          19
        //  4908: iconst_0       
        //  4909: istore          4
        //  4911: iload           4
        //  4913: aload           18
        //  4915: invokeinterface java/util/List.size:()I
        //  4920: if_icmpge       4967
        //  4923: iload           4
        //  4925: ifeq            4937
        //  4928: aload           19
        //  4930: ldc_w           ","
        //  4933: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4936: pop            
        //  4937: aload           19
        //  4939: aload           18
        //  4941: iload           4
        //  4943: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  4948: checkcast       Ljava/lang/Long;
        //  4951: invokevirtual   java/lang/Long.longValue:()J
        //  4954: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  4957: pop            
        //  4958: iload           4
        //  4960: iconst_1       
        //  4961: iadd           
        //  4962: istore          4
        //  4964: goto            4911
        //  4967: aload           19
        //  4969: ldc_w           ")"
        //  4972: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4975: pop            
        //  4976: aload_1        
        //  4977: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //  4980: ldc_w           "raw_events"
        //  4983: aload           19
        //  4985: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  4988: aconst_null    
        //  4989: invokevirtual   android/database/sqlite/SQLiteDatabase.delete:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
        //  4992: istore          4
        //  4994: iload           4
        //  4996: aload           18
        //  4998: invokeinterface java/util/List.size:()I
        //  5003: if_icmpeq       5034
        //  5006: aload_1        
        //  5007: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  5010: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //  5013: ldc_w           "Deleted fewer rows from raw events table than expected"
        //  5016: iload           4
        //  5018: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  5021: aload           18
        //  5023: invokeinterface java/util/List.size:()I
        //  5028: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  5031: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //  5034: aload_0        
        //  5035: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  5038: astore_1       
        //  5039: aload_1        
        //  5040: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //  5043: astore          18
        //  5045: aload           18
        //  5047: ldc_w           "delete from raw_events_metadata where app_id=? and metadata_fingerprint not in (select distinct metadata_fingerprint from raw_events where app_id=?)"
        //  5050: iconst_2       
        //  5051: anewarray       Ljava/lang/String;
        //  5054: dup            
        //  5055: iconst_0       
        //  5056: aload           20
        //  5058: aastore        
        //  5059: dup            
        //  5060: iconst_1       
        //  5061: aload           20
        //  5063: aastore        
        //  5064: invokevirtual   android/database/sqlite/SQLiteDatabase.execSQL:(Ljava/lang/String;[Ljava/lang/Object;)V
        //  5067: goto            5092
        //  5070: astore          18
        //  5072: aload_1        
        //  5073: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //  5076: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //  5079: ldc_w           "Failed to remove unused event metadata. appId"
        //  5082: aload           20
        //  5084: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //  5087: aload           18
        //  5089: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //  5092: aload_0        
        //  5093: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  5096: invokevirtual   com/google/android/gms/measurement/internal/zzq.setTransactionSuccessful:()V
        //  5099: aload_0        
        //  5100: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  5103: invokevirtual   com/google/android/gms/measurement/internal/zzq.endTransaction:()V
        //  5106: iconst_1       
        //  5107: ireturn        
        //  5108: aload_0        
        //  5109: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  5112: invokevirtual   com/google/android/gms/measurement/internal/zzq.setTransactionSuccessful:()V
        //  5115: aload_0        
        //  5116: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  5119: invokevirtual   com/google/android/gms/measurement/internal/zzq.endTransaction:()V
        //  5122: iconst_0       
        //  5123: ireturn        
        //  5124: astore_1       
        //  5125: aload           21
        //  5127: astore          18
        //  5129: aload           18
        //  5131: astore          19
        //  5133: aload_1        
        //  5134: astore          18
        //  5136: aload           19
        //  5138: ifnull          5148
        //  5141: aload           19
        //  5143: invokeinterface android/database/Cursor.close:()V
        //  5148: aload           18
        //  5150: athrow         
        //  5151: aload_0        
        //  5152: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzjq:()Lcom/google/android/gms/measurement/internal/zzq;
        //  5155: invokevirtual   com/google/android/gms/measurement/internal/zzq.endTransaction:()V
        //  5158: aload_1        
        //  5159: athrow         
        //  5160: iconst_0       
        //  5161: istore          4
        //  5163: goto            1252
        //  5166: iconst_1       
        //  5167: istore          4
        //  5169: goto            1252
        //  5172: iconst_0       
        //  5173: istore          6
        //  5175: goto            1456
        //  5178: iconst_1       
        //  5179: istore          6
        //  5181: goto            1456
        //  5184: goto            2855
        //  5187: iconst_m1      
        //  5188: istore          6
        //  5190: iload           6
        //  5192: ifeq            5213
        //  5195: iload           6
        //  5197: iconst_1       
        //  5198: if_icmpeq       5213
        //  5201: iload           6
        //  5203: iconst_2       
        //  5204: if_icmpeq       5213
        //  5207: iconst_0       
        //  5208: istore          6
        //  5210: goto            5216
        //  5213: iconst_1       
        //  5214: istore          6
        //  5216: iload           6
        //  5218: ifeq            5224
        //  5221: goto            1631
        //  5224: iload           5
        //  5226: istore          6
        //  5228: iload           4
        //  5230: istore          5
        //  5232: goto            2373
        //  5235: iload           9
        //  5237: iconst_1       
        //  5238: iadd           
        //  5239: istore          9
        //  5241: iload           10
        //  5243: istore          7
        //  5245: goto            1667
        //  5248: iload           5
        //  5250: iconst_1       
        //  5251: iadd           
        //  5252: istore          5
        //  5254: goto            2000
        //  5257: goto            2101
        //  5260: iconst_1       
        //  5261: istore          15
        //  5263: goto            2101
        //  5266: iload           7
        //  5268: iconst_1       
        //  5269: iadd           
        //  5270: istore          7
        //  5272: aload           20
        //  5274: astore          19
        //  5276: goto            2216
        //  5279: iload           4
        //  5281: istore          5
        //  5283: goto            2373
        //  5286: iload           4
        //  5288: iconst_1       
        //  5289: iadd           
        //  5290: istore          4
        //  5292: iload           9
        //  5294: istore          7
        //  5296: goto            2415
        //  5299: iload           8
        //  5301: iconst_m1      
        //  5302: if_icmpne       2551
        //  5305: iconst_1       
        //  5306: istore          4
        //  5308: goto            2616
        //  5311: iconst_0       
        //  5312: istore          4
        //  5314: goto            2575
        //  5317: iconst_0       
        //  5318: istore          4
        //  5320: goto            2616
        //  5323: iconst_1       
        //  5324: istore          4
        //  5326: goto            2616
        //  5329: goto            2665
        //  5332: goto            2674
        //  5335: iload           4
        //  5337: iconst_1       
        //  5338: iadd           
        //  5339: istore          4
        //  5341: goto            3067
        //  5344: iconst_0       
        //  5345: istore          4
        //  5347: goto            3113
        //  5350: goto            3163
        //  5353: aload           22
        //  5355: astore_1       
        //  5356: goto            3209
        //  5359: iconst_1       
        //  5360: istore          5
        //  5362: goto            3687
        //  5365: iload           8
        //  5367: iconst_1       
        //  5368: iadd           
        //  5369: istore          8
        //  5371: goto            3585
        //  5374: lload_2        
        //  5375: lstore          11
        //  5377: iconst_0       
        //  5378: istore          5
        //  5380: lload           11
        //  5382: lstore_2       
        //  5383: goto            3687
        //  5386: iconst_1       
        //  5387: istore          5
        //  5389: goto            3717
        //  5392: iconst_0       
        //  5393: istore          16
        //  5395: goto            3913
        //  5398: iload           4
        //  5400: iconst_1       
        //  5401: iadd           
        //  5402: istore          4
        //  5404: iload           4
        //  5406: istore          5
        //  5408: goto            5436
        //  5411: lload_2        
        //  5412: lload           13
        //  5414: lcmp           
        //  5415: ifeq            5421
        //  5418: goto            5424
        //  5421: goto            5430
        //  5424: iconst_1       
        //  5425: istore          8
        //  5427: goto            4188
        //  5430: iconst_0       
        //  5431: istore          8
        //  5433: goto            4188
        //  5436: iload           7
        //  5438: iconst_1       
        //  5439: iadd           
        //  5440: istore          7
        //  5442: iload           5
        //  5444: istore          4
        //  5446: goto            3306
        //  5449: astore_1       
        //  5450: goto            5151
        //  5453: aload_1        
        //  5454: astore          19
        //  5456: aload           18
        //  5458: astore_1       
        //  5459: goto            4452
        //  5462: iload           4
        //  5464: iconst_1       
        //  5465: iadd           
        //  5466: istore          4
        //  5468: goto            4475
        //  5471: aconst_null    
        //  5472: astore          18
        //  5474: goto            4638
        //  5477: lload           11
        //  5479: lstore_2       
        //  5480: goto            4661
        //  5483: aconst_null    
        //  5484: astore          18
        //  5486: goto            4676
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  13     52     5449   5160   Any
        //  52     65     1190   1197   Landroid/database/sqlite/SQLiteException;
        //  52     65     1182   1190   Any
        //  86     92     129    144    Landroid/database/sqlite/SQLiteException;
        //  86     92     121    129    Any
        //  95     104    129    144    Landroid/database/sqlite/SQLiteException;
        //  95     104    121    129    Any
        //  107    115    129    144    Landroid/database/sqlite/SQLiteException;
        //  107    115    121    129    Any
        //  144    156    1190   1197   Landroid/database/sqlite/SQLiteException;
        //  144    156    1182   1190   Any
        //  170    227    1190   1197   Landroid/database/sqlite/SQLiteException;
        //  170    227    1182   1190   Any
        //  234    243    1161   1169   Landroid/database/sqlite/SQLiteException;
        //  234    243    121    129    Any
        //  256    262    5449   5160   Any
        //  272    282    1161   1169   Landroid/database/sqlite/SQLiteException;
        //  272    282    121    129    Any
        //  293    303    331    339    Landroid/database/sqlite/SQLiteException;
        //  293    303    121    129    Any
        //  314    321    331    339    Landroid/database/sqlite/SQLiteException;
        //  314    321    121    129    Any
        //  348    353    1190   1197   Landroid/database/sqlite/SQLiteException;
        //  348    353    1182   1190   Any
        //  357    365    1190   1197   Landroid/database/sqlite/SQLiteException;
        //  357    365    1182   1190   Any
        //  368    373    1190   1197   Landroid/database/sqlite/SQLiteException;
        //  368    373    1182   1190   Any
        //  391    447    1190   1197   Landroid/database/sqlite/SQLiteException;
        //  391    447    1182   1190   Any
        //  454    464    1161   1169   Landroid/database/sqlite/SQLiteException;
        //  454    464    121    129    Any
        //  482    492    1161   1169   Landroid/database/sqlite/SQLiteException;
        //  482    492    121    129    Any
        //  499    506    1161   1169   Landroid/database/sqlite/SQLiteException;
        //  499    506    121    129    Any
        //  522    567    331    339    Landroid/database/sqlite/SQLiteException;
        //  522    567    5124   5129   Any
        //  567    596    1153   1158   Landroid/database/sqlite/SQLiteException;
        //  567    596    1145   1153   Any
        //  601    608    5449   5160   Any
        //  611    637    1153   1158   Landroid/database/sqlite/SQLiteException;
        //  611    637    1145   1153   Any
        //  637    644    1116   1145   Ljava/io/IOException;
        //  637    644    1153   1158   Landroid/database/sqlite/SQLiteException;
        //  637    644    1145   1153   Any
        //  644    673    1153   1158   Landroid/database/sqlite/SQLiteException;
        //  644    673    1145   1153   Any
        //  673    689    1153   1158   Landroid/database/sqlite/SQLiteException;
        //  673    689    1145   1153   Any
        //  733    749    1153   1158   Landroid/database/sqlite/SQLiteException;
        //  733    749    1145   1153   Any
        //  749    796    1153   1158   Landroid/database/sqlite/SQLiteException;
        //  749    796    1145   1153   Any
        //  807    817    331    339    Landroid/database/sqlite/SQLiteException;
        //  807    817    121    129    Any
        //  828    847    331    339    Landroid/database/sqlite/SQLiteException;
        //  828    847    121    129    Any
        //  869    878    331    339    Landroid/database/sqlite/SQLiteException;
        //  869    878    121    129    Any
        //  889    899    331    339    Landroid/database/sqlite/SQLiteException;
        //  889    899    121    129    Any
        //  910    921    331    339    Landroid/database/sqlite/SQLiteException;
        //  910    921    121    129    Any
        //  932    941    331    339    Landroid/database/sqlite/SQLiteException;
        //  932    941    121    129    Any
        //  952    960    1046   1080   Ljava/io/IOException;
        //  952    960    331    339    Landroid/database/sqlite/SQLiteException;
        //  952    960    121    129    Any
        //  971    984    331    339    Landroid/database/sqlite/SQLiteException;
        //  971    984    121    129    Any
        //  995    1011   331    339    Landroid/database/sqlite/SQLiteException;
        //  995    1011   121    129    Any
        //  1022   1035   331    339    Landroid/database/sqlite/SQLiteException;
        //  1022   1035   121    129    Any
        //  1059   1080   331    339    Landroid/database/sqlite/SQLiteException;
        //  1059   1080   121    129    Any
        //  1091   1100   331    339    Landroid/database/sqlite/SQLiteException;
        //  1091   1100   121    129    Any
        //  1117   1137   1153   1158   Landroid/database/sqlite/SQLiteException;
        //  1117   1137   1145   1153   Any
        //  1200   1221   5124   5129   Any
        //  1228   1249   5449   5160   Any
        //  1257   1299   5449   5160   Any
        //  1310   1398   5449   5160   Any
        //  1398   1453   5449   5160   Any
        //  1461   1504   5449   5160   Any
        //  1507   1529   5449   5160   Any
        //  1534   1556   5449   5160   Any
        //  1583   1593   5449   5160   Any
        //  1599   1609   5449   5160   Any
        //  1615   1625   5449   5160   Any
        //  1631   1648   5449   5160   Any
        //  1648   1658   5449   5160   Any
        //  1680   1703   5449   5160   Any
        //  1713   1736   5449   5160   Any
        //  1760   1851   5449   5160   Any
        //  1856   1947   5449   5160   Any
        //  1947   1997   5449   5160   Any
        //  2000   2047   5449   5160   Any
        //  2052   2065   5449   5160   Any
        //  2072   2092   5449   5160   Any
        //  2092   2098   5449   5160   Any
        //  2101   2112   5449   5160   Any
        //  2117   2207   5449   5160   Any
        //  2229   2243   5449   5160   Any
        //  2254   2268   5449   5160   Any
        //  2288   2313   5449   5160   Any
        //  2321   2340   5449   5160   Any
        //  2343   2370   5449   5160   Any
        //  2373   2394   5449   5160   Any
        //  2399   2406   5449   5160   Any
        //  2415   2440   5449   5160   Any
        //  2451   2468   5449   5160   Any
        //  2488   2548   5449   5160   Any
        //  2551   2560   5449   5160   Any
        //  2564   2572   5449   5160   Any
        //  2575   2600   5449   5160   Any
        //  2603   2613   5449   5160   Any
        //  2624   2662   5449   5160   Any
        //  2665   2671   5449   5160   Any
        //  2685   2716   5449   5160   Any
        //  2719   2736   5449   5160   Any
        //  2740   2751   5449   5160   Any
        //  2756   2769   5449   5160   Any
        //  2769   2777   5449   5160   Any
        //  2783   2791   5449   5160   Any
        //  2794   2805   5449   5160   Any
        //  2810   2823   5449   5160   Any
        //  2826   2832   5449   5160   Any
        //  2864   2897   5449   5160   Any
        //  2902   2918   5449   5160   Any
        //  2922   2929   5449   5160   Any
        //  2932   2979   5449   5160   Any
        //  2982   3018   5449   5160   Any
        //  3018   3064   5449   5160   Any
        //  3067   3107   5449   5160   Any
        //  3118   3160   5449   5160   Any
        //  3172   3203   5449   5160   Any
        //  3209   3297   5449   5160   Any
        //  3320   3366   5449   5160   Any
        //  3374   3403   5449   5160   Any
        //  3403   3447   5449   5160   Any
        //  3447   3489   5449   5160   Any
        //  3511   3564   5449   5160   Any
        //  3572   3582   5449   5160   Any
        //  3601   3636   5449   5160   Any
        //  3636   3657   5449   5160   Any
        //  3660   3668   5449   5160   Any
        //  3671   3684   5449   5160   Any
        //  3692   3714   5449   5160   Any
        //  3722   3748   5449   5160   Any
        //  3764   3781   5449   5160   Any
        //  3789   3811   5449   5160   Any
        //  3819   3884   5449   5160   Any
        //  3884   3902   5449   5160   Any
        //  3913   3920   5449   5160   Any
        //  3933   3962   5449   5160   Any
        //  3962   3984   5449   5160   Any
        //  3987   4009   5449   5160   Any
        //  4013   4030   5449   5160   Any
        //  4040   4060   5449   5160   Any
        //  4060   4088   5449   5160   Any
        //  4097   4133   5449   5160   Any
        //  4136   4157   5449   5160   Any
        //  4160   4185   5449   5160   Any
        //  4193   4230   5449   5160   Any
        //  4234   4251   5449   5160   Any
        //  4261   4284   5449   5160   Any
        //  4284   4312   5449   5160   Any
        //  4325   4354   5449   5160   Any
        //  4365   4391   5449   5160   Any
        //  4391   4405   5449   5160   Any
        //  4412   4449   5449   5160   Any
        //  4452   4472   5449   5160   Any
        //  4475   4522   5449   5160   Any
        //  4522   4550   5449   5160   Any
        //  4553   4574   5449   5160   Any
        //  4579   4606   5449   5160   Any
        //  4609   4623   5449   5160   Any
        //  4629   4635   5449   5160   Any
        //  4638   4651   5449   5160   Any
        //  4667   4673   5449   5160   Any
        //  4676   4742   5449   5160   Any
        //  4742   4775   5449   5160   Any
        //  4780   4788   5449   5160   Any
        //  4791   4798   5449   5160   Any
        //  4798   4804   5449   5160   Any
        //  4807   4829   5449   5160   Any
        //  4832   4859   5449   5160   Any
        //  4859   4870   5449   5160   Any
        //  4870   4908   5449   5160   Any
        //  4911   4923   5449   5160   Any
        //  4928   4937   5449   5160   Any
        //  4937   4958   5449   5160   Any
        //  4967   5034   5449   5160   Any
        //  5034   5045   5449   5160   Any
        //  5045   5067   5070   5092   Landroid/database/sqlite/SQLiteException;
        //  5045   5067   5449   5160   Any
        //  5072   5092   5449   5160   Any
        //  5092   5099   5449   5160   Any
        //  5108   5115   5449   5160   Any
        //  5141   5148   5449   5160   Any
        //  5148   5151   5449   5160   Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private final zzg zzg(final zzh zzh) {
        this.zzaf();
        this.zzlr();
        Preconditions.checkNotNull(zzh);
        Preconditions.checkNotEmpty(zzh.packageName);
        zzg zzbl = this.zzjq().zzbl(zzh.packageName);
        final String zzbz = this.zzadj.zzgp().zzbz(zzh.packageName);
        boolean b = false;
        Label_0139: {
            if (zzbl == null) {
                zzbl = new zzg(this.zzadj, zzh.packageName);
                zzbl.zzam(this.zzadj.zzgm().zzmf());
                zzbl.zzap(zzbz);
            }
            else {
                if (zzbz.equals(zzbl.zzgx())) {
                    b = false;
                    break Label_0139;
                }
                zzbl.zzap(zzbz);
                zzbl.zzam(this.zzadj.zzgm().zzmf());
            }
            b = true;
        }
        if (!TextUtils.equals((CharSequence)zzh.zzafx, (CharSequence)zzbl.getGmpAppId())) {
            zzbl.zzan(zzh.zzafx);
            b = true;
        }
        boolean b2 = b;
        if (!TextUtils.equals((CharSequence)zzh.zzagk, (CharSequence)zzbl.zzgw())) {
            zzbl.zzao(zzh.zzagk);
            b2 = true;
        }
        boolean b3 = b2;
        if (!TextUtils.isEmpty((CharSequence)zzh.zzafz)) {
            b3 = b2;
            if (!zzh.zzafz.equals(zzbl.getFirebaseInstanceId())) {
                zzbl.zzaq(zzh.zzafz);
                b3 = true;
            }
        }
        boolean b4 = b3;
        if (zzh.zzadt != 0L) {
            b4 = b3;
            if (zzh.zzadt != zzbl.zzhc()) {
                zzbl.zzv(zzh.zzadt);
                b4 = true;
            }
        }
        boolean b5 = b4;
        if (!TextUtils.isEmpty((CharSequence)zzh.zzts)) {
            b5 = b4;
            if (!zzh.zzts.equals(zzbl.zzak())) {
                zzbl.setAppVersion(zzh.zzts);
                b5 = true;
            }
        }
        if (zzh.zzagd != zzbl.zzha()) {
            zzbl.zzu(zzh.zzagd);
            b5 = true;
        }
        boolean b6 = b5;
        if (zzh.zzage != null) {
            b6 = b5;
            if (!zzh.zzage.equals(zzbl.zzhb())) {
                zzbl.zzar(zzh.zzage);
                b6 = true;
            }
        }
        boolean b7 = b6;
        if (zzh.zzagf != zzbl.zzhd()) {
            zzbl.zzw(zzh.zzagf);
            b7 = true;
        }
        if (zzh.zzagg != zzbl.isMeasurementEnabled()) {
            zzbl.setMeasurementEnabled(zzh.zzagg);
            b7 = true;
        }
        boolean b8 = b7;
        if (!TextUtils.isEmpty((CharSequence)zzh.zzagv)) {
            b8 = b7;
            if (!zzh.zzagv.equals(zzbl.zzho())) {
                zzbl.zzas(zzh.zzagv);
                b8 = true;
            }
        }
        if (zzh.zzagh != zzbl.zzhq()) {
            zzbl.zzag(zzh.zzagh);
            b8 = true;
        }
        if (zzh.zzagi != zzbl.zzhr()) {
            zzbl.zze(zzh.zzagi);
            b8 = true;
        }
        if (zzh.zzagj != zzbl.zzhs()) {
            zzbl.zzf(zzh.zzagj);
            b8 = true;
        }
        if (b8) {
            this.zzjq().zza(zzbl);
        }
        return zzbl;
    }
    
    private final zzbn zzln() {
        zza(this.zzatd);
        return this.zzatd;
    }
    
    private final zzay zzlp() {
        final zzay zzatg = this.zzatg;
        if (zzatg != null) {
            return zzatg;
        }
        throw new IllegalStateException("Network broadcast receiver not created");
    }
    
    private final zzew zzlq() {
        zza(this.zzath);
        return this.zzath;
    }
    
    private final long zzls() {
        final long currentTimeMillis = this.zzadj.zzbx().currentTimeMillis();
        final zzba zzgp = this.zzadj.zzgp();
        zzgp.zzcl();
        zzgp.zzaf();
        long value;
        if ((value = zzgp.zzani.get()) == 0L) {
            value = zzgp.zzgm().zzmd().nextInt(86400000) + 1L;
            zzgp.zzani.set(value);
        }
        return (currentTimeMillis + value) / 1000L / 60L / 60L / 24L;
    }
    
    private final boolean zzlu() {
        this.zzaf();
        this.zzlr();
        return this.zzjq().zzii() || !TextUtils.isEmpty((CharSequence)this.zzjq().zzid());
    }
    
    private final void zzlv() {
        this.zzaf();
        this.zzlr();
        if (!this.zzlz()) {
            return;
        }
        if (this.zzatl > 0L) {
            final long n = 3600000L - Math.abs(this.zzadj.zzbx().elapsedRealtime() - this.zzatl);
            if (n > 0L) {
                this.zzadj.zzgo().zzjl().zzg("Upload has been suspended. Will update scheduling later in approximately ms", n);
                this.zzlp().unregister();
                this.zzlq().cancel();
                return;
            }
            this.zzatl = 0L;
        }
        if (!this.zzadj.zzkr() || !this.zzlu()) {
            this.zzadj.zzgo().zzjl().zzbx("Nothing to upload or uploading impossible");
            this.zzlp().unregister();
            this.zzlq().cancel();
            return;
        }
        final long currentTimeMillis = this.zzadj.zzbx().currentTimeMillis();
        final long max = Math.max(0L, zzaf.zzakd.get());
        final boolean b = this.zzjq().zzij() || this.zzjq().zzie();
        zzaf.zza<Long> zza;
        if (b) {
            final String zzhy = this.zzadj.zzgq().zzhy();
            if (!TextUtils.isEmpty((CharSequence)zzhy) && !".none.".equals(zzhy)) {
                zza = zzaf.zzajy;
            }
            else {
                zza = zzaf.zzajx;
            }
        }
        else {
            zza = zzaf.zzajw;
        }
        final long max2 = Math.max(0L, zza.get());
        final long value = this.zzadj.zzgp().zzane.get();
        final long value2 = this.zzadj.zzgp().zzanf.get();
        final long max3 = Math.max(this.zzjq().zzig(), this.zzjq().zzih());
        long n5 = 0L;
        Label_0498: {
            if (max3 != 0L) {
                final long n2 = currentTimeMillis - Math.abs(max3 - currentTimeMillis);
                final long abs = Math.abs(value - currentTimeMillis);
                final long n3 = currentTimeMillis - Math.abs(value2 - currentTimeMillis);
                final long max4 = Math.max(currentTimeMillis - abs, n3);
                final long n4 = n5 = n2 + max;
                if (b) {
                    n5 = n4;
                    if (max4 > 0L) {
                        n5 = Math.min(n2, max4) + max2;
                    }
                }
                if (!this.zzjo().zzb(max4, max2)) {
                    n5 = max4 + max2;
                }
                if (n3 == 0L || n3 < n2) {
                    break Label_0498;
                }
                for (int i = 0; i < Math.min(20, Math.max(0, zzaf.zzakf.get())); ++i) {
                    n5 += Math.max(0L, zzaf.zzake.get()) * (1L << i);
                    if (n5 > n3) {
                        break Label_0498;
                    }
                }
            }
            n5 = 0L;
        }
        if (n5 == 0L) {
            this.zzadj.zzgo().zzjl().zzbx("Next upload time is 0");
            this.zzlp().unregister();
            this.zzlq().cancel();
            return;
        }
        if (!this.zzlo().zzfb()) {
            this.zzadj.zzgo().zzjl().zzbx("No network");
            this.zzlp().zzey();
            this.zzlq().cancel();
            return;
        }
        final long value3 = this.zzadj.zzgp().zzang.get();
        final long max5 = Math.max(0L, zzaf.zzaju.get());
        long max6 = n5;
        if (!this.zzjo().zzb(value3, max5)) {
            max6 = Math.max(n5, value3 + max5);
        }
        this.zzlp().unregister();
        long max7;
        if ((max7 = max6 - this.zzadj.zzbx().currentTimeMillis()) <= 0L) {
            max7 = Math.max(0L, zzaf.zzajz.get());
            this.zzadj.zzgp().zzane.set(this.zzadj.zzbx().currentTimeMillis());
        }
        this.zzadj.zzgo().zzjl().zzg("Upload scheduled in approximately ms", max7);
        this.zzlq().zzh(max7);
    }
    
    private final void zzlw() {
        this.zzaf();
        if (this.zzatp || this.zzatq || this.zzatr) {
            this.zzadj.zzgo().zzjl().zzd("Not stopping services. fetch, network, upload", this.zzatp, this.zzatq, this.zzatr);
            return;
        }
        this.zzadj.zzgo().zzjl().zzbx("Stopping uploading service(s)");
        final List<Runnable> zzatm = this.zzatm;
        if (zzatm == null) {
            return;
        }
        final Iterator<Runnable> iterator = zzatm.iterator();
        while (iterator.hasNext()) {
            iterator.next().run();
        }
        this.zzatm.clear();
    }
    
    private final boolean zzlx() {
        this.zzaf();
        final File file = new File(this.zzadj.getContext().getFilesDir(), "google_app_measurement.db");
        zzar zzar;
        String s;
        try {
            final FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            this.zzatt = channel;
            final FileLock tryLock = channel.tryLock();
            this.zzats = tryLock;
            if (tryLock != null) {
                this.zzadj.zzgo().zzjl().zzbx("Storage concurrent access okay");
                return true;
            }
            this.zzadj.zzgo().zzjd().zzbx("Storage concurrent data access panic");
            return false;
        }
        catch (IOException ex) {
            zzar = this.zzadj.zzgo().zzjd();
            s = "Failed to access storage lock file";
        }
        catch (FileNotFoundException ex) {
            zzar = this.zzadj.zzgo().zzjd();
            s = "Failed to acquire storage lock";
        }
        final IOException ex;
        zzar.zzg(s, ex);
        return false;
    }
    
    private final boolean zzlz() {
        this.zzaf();
        this.zzlr();
        return this.zzatk;
    }
    
    public static zzfa zzm(final Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzfa.zzatc == null) {
            synchronized (zzfa.class) {
                if (zzfa.zzatc == null) {
                    zzfa.zzatc = new zzfa(new zzff(context));
                }
            }
        }
        return zzfa.zzatc;
    }
    
    @Override
    public final Context getContext() {
        return this.zzadj.getContext();
    }
    
    protected final void start() {
        this.zzadj.zzgn().zzaf();
        this.zzjq().zzif();
        if (this.zzadj.zzgp().zzane.get() == 0L) {
            this.zzadj.zzgp().zzane.set(this.zzadj.zzbx().currentTimeMillis());
        }
        this.zzlv();
    }
    
    final void zza(final int n, Throwable iterator, byte[] zzatu, String zzjq) {
        this.zzaf();
        this.zzlr();
        byte[] array = zzatu;
        Label_0023: {
            if (zzatu != null) {
                break Label_0023;
            }
        Label_0478_Outer:
            while (true) {
                while (true) {
                    Label_0560: {
                        while (true) {
                            try {
                                array = new byte[0];
                                zzatu = (byte[])(Object)this.zzatu;
                                this.zzatu = null;
                                final boolean b = true;
                                Label_0535: {
                                    if ((n == 200 || n == 204) && iterator == null) {
                                        try {
                                            this.zzadj.zzgp().zzane.set(this.zzadj.zzbx().currentTimeMillis());
                                            this.zzadj.zzgp().zzanf.set(0L);
                                            this.zzlv();
                                            this.zzadj.zzgo().zzjl().zze("Successful upload. Got network response. code, size", n, array.length);
                                            this.zzjq().beginTransaction();
                                            try {
                                                iterator = (Throwable)((List<Object>)(Object)zzatu).iterator();
                                                while (((Iterator)iterator).hasNext()) {
                                                    zzatu = ((Iterator<byte[]>)iterator).next();
                                                    try {
                                                        zzjq = (SQLiteException)this.zzjq();
                                                        final long longValue = (Object)zzatu;
                                                        ((zzco)zzjq).zzaf();
                                                        ((zzez)zzjq).zzcl();
                                                        final SQLiteDatabase writableDatabase = ((zzq)zzjq).getWritableDatabase();
                                                        try {
                                                            if (writableDatabase.delete("queue", "rowid=?", new String[] { String.valueOf(longValue) }) == 1) {
                                                                continue Label_0478_Outer;
                                                            }
                                                            throw new SQLiteException("Deleted fewer rows from queue than expected");
                                                        }
                                                        catch (SQLiteException ex) {
                                                            ((zzco)zzjq).zzgo().zzjd().zzg("Failed to delete a bundle in a queue table", ex);
                                                            throw ex;
                                                        }
                                                    }
                                                    catch (SQLiteException zzjq) {
                                                        if (this.zzatv != null && this.zzatv.contains(zzatu)) {
                                                            continue Label_0478_Outer;
                                                        }
                                                        throw zzjq;
                                                    }
                                                    break;
                                                }
                                                this.zzjq().setTransactionSuccessful();
                                                this.zzjq().endTransaction();
                                                this.zzatv = null;
                                                if (this.zzlo().zzfb() && this.zzlu()) {
                                                    this.zzlt();
                                                }
                                                else {
                                                    this.zzatw = -1L;
                                                    this.zzlv();
                                                }
                                                this.zzatl = 0L;
                                            }
                                            finally {
                                                this.zzjq().endTransaction();
                                            }
                                        }
                                        catch (SQLiteException ex2) {
                                            this.zzadj.zzgo().zzjd().zzg("Database error while trying to delete uploaded bundles", ex2);
                                            this.zzatl = this.zzadj.zzbx().elapsedRealtime();
                                            this.zzadj.zzgo().zzjl().zzg("Disable upload, time", this.zzatl);
                                            break Label_0535;
                                        }
                                    }
                                    this.zzadj.zzgo().zzjl().zze("Network upload failed. Will retry later. code, error", n, iterator);
                                    this.zzadj.zzgp().zzanf.set(this.zzadj.zzbx().currentTimeMillis());
                                    int n2 = b ? 1 : 0;
                                    if (n != 503) {
                                        if (n != 429) {
                                            break Label_0560;
                                        }
                                        n2 = (b ? 1 : 0);
                                    }
                                    if (n2 != 0) {
                                        this.zzadj.zzgp().zzang.set(this.zzadj.zzbx().currentTimeMillis());
                                    }
                                    if (this.zzadj.zzgq().zzaz((String)zzjq)) {
                                        this.zzjq().zzc((List<Long>)(Object)zzatu);
                                    }
                                    this.zzlv();
                                }
                                this.zzatq = false;
                                this.zzlw();
                                return;
                                this.zzatq = false;
                                this.zzlw();
                                throw iterator;
                            }
                            finally {
                                continue;
                            }
                            break;
                        }
                    }
                    int n2 = 0;
                    continue;
                }
            }
        }
    }
    
    public final byte[] zza(final zzad zzad, final String s) {
    Label_1653_Outer:
        while (true) {
            this.zzlr();
            this.zzaf();
            this.zzadj.zzga();
            Preconditions.checkNotNull(zzad);
            Preconditions.checkNotEmpty(s);
            final zzgh zzgh = new zzgh();
            this.zzjq().beginTransaction();
            zzg zzbl;
            zzar zzar;
            Object zzaxu;
            boolean zzax;
            Object o;
            Object value = null;
            Object o2;
            zzgi zzgi;
            long n;
            List<zzfj> zzbk;
            zzfj zzi;
            String s2;
            final zzad zzad2;
            Long value2;
            int zzvu = 0;
            byte[] array;
            zzy zzy;
            zzgg zzgg;
            zzgl zzgl;
            zzgl zzgl2;
            Long value3;
            long zzgy = 0L;
            Block_33_Outer:Label_1637_Outer:
            while (true) {
                Label_1637:Label_0867_Outer:Label_1614_Outer:
                while (true) {
                Label_1614:
                    while (true) {
                        while (true) {
                            Label_1859: {
                                Label_1851: {
                                    Label_1848: {
                                        try {
                                            zzbl = this.zzjq().zzbl(s);
                                            if (zzbl == null) {
                                                zzar = this.zzadj.zzgo().zzjk();
                                                zzaxu = "Log and bundle not available. package_name";
                                            }
                                            else if (!zzbl.isMeasurementEnabled()) {
                                                zzar = this.zzadj.zzgo().zzjk();
                                                zzaxu = "Log and bundle disabled. package_name";
                                            }
                                            else {
                                                if (("_iap".equals(zzad.name) || "ecommerce_purchase".equals(zzad.name)) && !this.zza(s, zzad)) {
                                                    this.zzadj.zzgo().zzjg().zzg("Failed to handle purchase event at single event bundle creation. appId", zzap.zzbv(s));
                                                }
                                                zzax = this.zzadj.zzgq().zzax(s);
                                                value = (o = 0L);
                                                Label_0330: {
                                                    if (zzax) {
                                                        o = value;
                                                        if ("_e".equals(zzad.name)) {
                                                            if (zzad.zzaid != null && zzad.zzaid.size() != 0) {
                                                                if (zzad.zzaid.getLong("_et") != null) {
                                                                    o = zzad.zzaid.getLong("_et");
                                                                    break Label_0330;
                                                                }
                                                                zzaxu = this.zzadj.zzgo().zzjg();
                                                                o = "The engagement event does not include duration. appId";
                                                                o2 = zzap.zzbv(s);
                                                            }
                                                            else {
                                                                zzaxu = this.zzadj.zzgo().zzjg();
                                                                o = "The engagement event does not contain any parameters. appId";
                                                                o2 = zzap.zzbv(s);
                                                            }
                                                            ((zzar)zzaxu).zzg((String)o, o2);
                                                            o = value;
                                                        }
                                                    }
                                                }
                                                zzgi = new zzgi();
                                                zzgh.zzawy = new zzgi[] { zzgi };
                                                zzgi.zzaxa = 1;
                                                zzgi.zzaxi = "android";
                                                zzgi.zztt = zzbl.zzal();
                                                zzgi.zzage = zzbl.zzhb();
                                                zzgi.zzts = zzbl.zzak();
                                                n = zzbl.zzha();
                                                if (n == -2147483648L) {
                                                    zzaxu = null;
                                                }
                                                else {
                                                    zzaxu = (int)n;
                                                }
                                                zzgi.zzaxu = (Integer)zzaxu;
                                                zzgi.zzaxm = zzbl.zzhc();
                                                zzgi.zzafx = zzbl.getGmpAppId();
                                                if (TextUtils.isEmpty((CharSequence)zzgi.zzafx)) {
                                                    zzgi.zzawj = zzbl.zzgw();
                                                }
                                                zzgi.zzaxq = zzbl.zzhd();
                                                if (this.zzadj.isEnabled() && zzn.zzhz() && this.zzadj.zzgq().zzav(zzgi.zztt)) {
                                                    zzgi.zzaya = null;
                                                }
                                                zzaxu = this.zzadj.zzgp().zzby(zzbl.zzal());
                                                if (zzbl.zzhr() && zzaxu != null && !TextUtils.isEmpty((CharSequence)((Pair)zzaxu).first)) {
                                                    zzgi.zzaxo = (String)((Pair)zzaxu).first;
                                                    zzgi.zzaxp = (Boolean)((Pair)zzaxu).second;
                                                }
                                                this.zzadj.zzgk().zzcl();
                                                zzgi.zzaxk = Build.MODEL;
                                                this.zzadj.zzgk().zzcl();
                                                zzgi.zzaxj = Build$VERSION.RELEASE;
                                                zzgi.zzaxl = (int)this.zzadj.zzgk().zzis();
                                                zzgi.zzaia = this.zzadj.zzgk().zzit();
                                                zzgi.zzafw = zzbl.getAppInstanceId();
                                                zzgi.zzafz = zzbl.getFirebaseInstanceId();
                                                zzbk = this.zzjq().zzbk(zzbl.zzal());
                                                zzgi.zzaxc = new zzgl[zzbk.size()];
                                                if (!zzax) {
                                                    break Label_1848;
                                                }
                                                zzi = this.zzjq().zzi(zzgi.zztt, "_lte");
                                                if (zzi == null || zzi.value == null) {
                                                    zzaxu = new zzfj(zzgi.zztt, "auto", "_lte", this.zzadj.zzbx().currentTimeMillis(), o);
                                                    break Label_1851;
                                                }
                                                zzaxu = zzi;
                                                if ((long)o > 0L) {
                                                    zzaxu = new zzfj(zzgi.zztt, "auto", "_lte", this.zzadj.zzbx().currentTimeMillis(), (long)zzi.value + (long)o);
                                                }
                                                break Label_1851;
                                            }
                                            zzar.zzg((String)zzaxu, s);
                                            this.zzjq().endTransaction();
                                            return new byte[0];
                                        Label_1027:
                                            while (true) {
                                                Label_1471: {
                                                    Block_35: {
                                                        Block_25_Outer:Label_1271_Outer:
                                                        while (true) {
                                                            Label_1127: {
                                                                while (true) {
                                                                    Block_31: {
                                                                        Block_34: {
                                                                            while (true) {
                                                                                while (true) {
                                                                                    while (true) {
                                                                                        while (true) {
                                                                                            Block_30: {
                                                                                                while (true) {
                                                                                                    Block_28: {
                                                                                                        break Block_28;
                                                                                                        zzaxu = zzad2.zzaid.zziv();
                                                                                                        break Block_30;
                                                                                                        zzgi.zzaxg = value2;
                                                                                                        zzbl.zzhh();
                                                                                                        zzgi.zzaxr = (int)zzbl.zzhe();
                                                                                                        zzgi.zzaxn = this.zzadj.zzgq().zzhc();
                                                                                                        zzgi.zzaxd = this.zzadj.zzbx().currentTimeMillis();
                                                                                                        zzgi.zzaxs = Boolean.TRUE;
                                                                                                        zzbl.zzs(zzgi.zzaxe);
                                                                                                        zzbl.zzt(zzgi.zzaxf);
                                                                                                        this.zzjq().zza(zzbl);
                                                                                                        this.zzjq().setTransactionSuccessful();
                                                                                                        this.zzjq().endTransaction();
                                                                                                        try {
                                                                                                            zzvu = zzgh.zzvu();
                                                                                                            array = new byte[zzvu];
                                                                                                            zzaxu = zzyy.zzk(array, 0, zzvu);
                                                                                                            zzgh.zza((zzyy)zzaxu);
                                                                                                            ((zzyy)zzaxu).zzyt();
                                                                                                            return this.zzjo().zzb(array);
                                                                                                        }
                                                                                                        catch (IOException ex) {
                                                                                                            this.zzadj.zzgo().zzjd().zze("Data loss. Failed to bundle and serialize. appId", zzap.zzbv(s), ex);
                                                                                                            return null;
                                                                                                        }
                                                                                                        zzy = new zzy(this.zzadj, zzad2.origin, s, zzad2.name, zzad2.zzaip, n, (Bundle)zzaxu);
                                                                                                        zzaxu = new zzgf();
                                                                                                        zzgi.zzaxb = new zzgf[] { (zzgf)zzaxu };
                                                                                                        ((zzgf)zzaxu).zzawu = zzy.timestamp;
                                                                                                        ((zzgf)zzaxu).name = zzy.name;
                                                                                                        ((zzgf)zzaxu).zzawv = zzy.zzaic;
                                                                                                        ((zzgf)zzaxu).zzawt = new zzgg[zzy.zzaid.size()];
                                                                                                        o = zzy.zzaid.iterator();
                                                                                                        zzvu = 0;
                                                                                                        break Label_1471;
                                                                                                        value = ((Iterator<String>)o).next();
                                                                                                        zzgg = new zzgg();
                                                                                                        ((zzgf)zzaxu).zzawt[zzvu] = zzgg;
                                                                                                        zzgg.name = (String)value;
                                                                                                        value = zzy.zzaid.get((String)value);
                                                                                                        this.zzjo().zza(zzgg, value);
                                                                                                        ++zzvu;
                                                                                                        break Label_1471;
                                                                                                        o = new zzz(s, zzad2.name, 1L, 0L, zzad2.zzaip, 0L, null, null, null, null);
                                                                                                        this.zzjq().zza((zzz)o);
                                                                                                        n = 0L;
                                                                                                        continue Block_33_Outer;
                                                                                                        zzgl.zzawx = (Long)((zzfj)zzaxu).value;
                                                                                                        zzgl.zzayl = this.zzadj.zzbx().currentTimeMillis();
                                                                                                        value = zzgl;
                                                                                                        break Label_1859;
                                                                                                    }
                                                                                                    zzgl2 = new zzgl();
                                                                                                    zzgl2.name = "_lte";
                                                                                                    zzgl2.zzayl = this.zzadj.zzbx().currentTimeMillis();
                                                                                                    zzgl2.zzawx = (Long)((zzfj)zzaxu).value;
                                                                                                    (zzgi.zzaxc = Arrays.copyOf(zzgi.zzaxc, zzgi.zzaxc.length + 1))[zzgi.zzaxc.length - 1] = zzgl2;
                                                                                                    break Label_1127;
                                                                                                    value = s2;
                                                                                                    continue Block_25_Outer;
                                                                                                }
                                                                                            }
                                                                                            ((Bundle)zzaxu).putLong("_c", 1L);
                                                                                            this.zzadj.zzgo().zzjk().zzbx("Marking in-app purchase as real-time");
                                                                                            ((Bundle)zzaxu).putLong("_r", 1L);
                                                                                            Label_1203:
                                                                                            ((Bundle)zzaxu).putString("_o", zzad2.origin);
                                                                                            break Block_31;
                                                                                            o = this.zzjq().zzg(s, zzad2.name);
                                                                                            continue Block_25_Outer;
                                                                                        }
                                                                                        Label_1547:
                                                                                        zzgi.zzaxt = this.zza(zzbl.zzal(), zzgi.zzaxc, zzgi.zzaxb);
                                                                                        zzgi.zzaxe = ((zzgf)zzaxu).zzawu;
                                                                                        zzgi.zzaxf = ((zzgf)zzaxu).zzawu;
                                                                                        n = zzbl.zzgz();
                                                                                        break Block_34;
                                                                                        break Block_35;
                                                                                        Label_1330:
                                                                                        n = ((zzz)o).zzaig;
                                                                                        o = ((zzz)o).zzai(zzad2.zzaip).zziu();
                                                                                        this.zzjq().zza((zzz)o);
                                                                                        continue Block_33_Outer;
                                                                                    }
                                                                                    zzgl = new zzgl();
                                                                                    zzgi.zzaxc[zzvu] = zzgl;
                                                                                    zzgl.name = zzbk.get(zzvu).name;
                                                                                    zzgl.zzayl = zzbk.get(zzvu).zzaue;
                                                                                    this.zzjo().zza(zzgl, zzbk.get(zzvu).value);
                                                                                    value = s2;
                                                                                    continue Label_1271_Outer;
                                                                                }
                                                                                continue Label_0867_Outer;
                                                                            }
                                                                        }
                                                                        value3 = n;
                                                                        break Label_1614;
                                                                    }
                                                                    this.zzadj.zzgm().zza((Bundle)zzaxu, "_dbg", 1L);
                                                                    this.zzadj.zzgm().zza((Bundle)zzaxu, "_r", 1L);
                                                                    continue Label_1637_Outer;
                                                                }
                                                            }
                                                            this.zzjq().zza((zzfj)zzaxu);
                                                            continue Label_1653_Outer;
                                                        }
                                                        zzgi.zzaxh = value3;
                                                        zzgy = zzbl.zzgy();
                                                        continue Label_1637;
                                                    }
                                                    value2 = n;
                                                    continue Block_33_Outer;
                                                }
                                                continue Label_1637_Outer;
                                            }
                                        }
                                        // iftrue(Label_1127:, !zzax || s2 != null)
                                        // iftrue(Label_1203:, !"_iap".equals((Object)zzad2.name))
                                        // iftrue(Label_1859:, !"_lte".equals((Object)zzgl.name))
                                        // iftrue(Label_1271:, !this.zzadj.zzgm().zzcw(zzgi.zztt))
                                        // iftrue(Label_1330:, o != null)
                                        // iftrue(Label_1870:, n == 0L)
                                        // iftrue(Label_1882:, n == 0L)
                                        // iftrue(Label_1859:, !zzax)
                                        // iftrue(Label_1027:, zzvu >= zzbk.size())
                                        // iftrue(Label_1147:, o.longValue() <= 0L)
                                        // iftrue(Label_1875:, zzgy != 0L)
                                        // iftrue(Label_1547:, !o.hasNext())
                                        finally {
                                            this.zzjq().endTransaction();
                                        }
                                    }
                                    zzaxu = null;
                                }
                                zzvu = 0;
                                s2 = null;
                                continue Label_1614_Outer;
                            }
                            ++zzvu;
                            s2 = (String)value;
                            continue Label_1614_Outer;
                        }
                        Label_1870: {
                            value3 = null;
                        }
                        continue Label_1614;
                    }
                    Label_1875: {
                        n = zzgy;
                    }
                    continue Label_1637;
                }
                Label_1882: {
                    value2 = null;
                }
                continue Label_1637_Outer;
            }
        }
    }
    
    final void zzb(final zzez zzez) {
        ++this.zzatn;
    }
    
    final void zzb(final zzfh zzfh, final zzh zzh) {
        this.zzaf();
        this.zzlr();
        if (TextUtils.isEmpty((CharSequence)zzh.zzafx) && TextUtils.isEmpty((CharSequence)zzh.zzagk)) {
            return;
        }
        if (!zzh.zzagg) {
            this.zzg(zzh);
            return;
        }
        if (this.zzadj.zzgq().zze(zzh.packageName, zzaf.zzalj) && "_ap".equals(zzfh.name)) {
            final zzfj zzi = this.zzjq().zzi(zzh.packageName, "_ap");
            if (zzi != null && "auto".equals(zzfh.origin) && !"auto".equals(zzi.origin)) {
                this.zzadj.zzgo().zzjk().zzbx("Not setting lower priority ad personalization property");
                return;
            }
        }
        final int zzcs = this.zzadj.zzgm().zzcs(zzfh.name);
        if (zzcs != 0) {
            this.zzadj.zzgm();
            final String zza = zzfk.zza(zzfh.name, 24, true);
            int length;
            if (zzfh.name != null) {
                length = zzfh.name.length();
            }
            else {
                length = 0;
            }
            this.zzadj.zzgm().zza(zzh.packageName, zzcs, "_ev", zza, length);
            return;
        }
        final int zzi2 = this.zzadj.zzgm().zzi(zzfh.name, zzfh.getValue());
        if (zzi2 != 0) {
            this.zzadj.zzgm();
            final String zza2 = zzfk.zza(zzfh.name, 24, true);
            final Object value = zzfh.getValue();
            int length2;
            if (value != null && (value instanceof String || value instanceof CharSequence)) {
                length2 = String.valueOf(value).length();
            }
            else {
                length2 = 0;
            }
            this.zzadj.zzgm().zza(zzh.packageName, zzi2, "_ev", zza2, length2);
            return;
        }
        final Object zzj = this.zzadj.zzgm().zzj(zzfh.name, zzfh.getValue());
        if (zzj == null) {
            return;
        }
        final zzfj zzfj = new zzfj(zzh.packageName, zzfh.origin, zzfh.name, zzfh.zzaue, zzj);
        this.zzadj.zzgo().zzjk().zze("Setting user property", this.zzadj.zzgl().zzbu(zzfj.name), zzj);
        this.zzjq().beginTransaction();
        try {
            this.zzg(zzh);
            final boolean zza3 = this.zzjq().zza(zzfj);
            this.zzjq().setTransactionSuccessful();
            if (zza3) {
                this.zzadj.zzgo().zzjk().zze("User property set", this.zzadj.zzgl().zzbu(zzfj.name), zzfj.value);
            }
            else {
                this.zzadj.zzgo().zzjd().zze("Too many unique user properties are set. Ignoring user property", this.zzadj.zzgl().zzbu(zzfj.name), zzfj.value);
                this.zzadj.zzgm().zza(zzh.packageName, 9, null, null, 0);
            }
        }
        finally {
            this.zzjq().endTransaction();
        }
    }
    
    final void zzb(zzl zzj, final zzh zzh) {
        Preconditions.checkNotNull(zzj);
        Preconditions.checkNotEmpty(zzj.packageName);
        Preconditions.checkNotNull(zzj.origin);
        Preconditions.checkNotNull(zzj.zzahb);
        Preconditions.checkNotEmpty(zzj.zzahb.name);
        this.zzaf();
        this.zzlr();
        if (TextUtils.isEmpty((CharSequence)zzh.zzafx) && TextUtils.isEmpty((CharSequence)zzh.zzagk)) {
            return;
        }
        if (!zzh.zzagg) {
            this.zzg(zzh);
            return;
        }
        final zzl zzl = new zzl(zzj);
        boolean b = false;
        zzl.active = false;
        this.zzjq().beginTransaction();
        try {
            zzj = this.zzjq().zzj(zzl.packageName, zzl.zzahb.name);
            if (zzj != null && !zzj.origin.equals(zzl.origin)) {
                this.zzadj.zzgo().zzjg().zzd("Updating a conditional user property with different origin. name, origin, origin (from DB)", this.zzadj.zzgl().zzbu(zzl.zzahb.name), zzl.origin, zzj.origin);
            }
            if (zzj != null && zzj.active) {
                zzl.origin = zzj.origin;
                zzl.creationTimestamp = zzj.creationTimestamp;
                zzl.triggerTimeout = zzj.triggerTimeout;
                zzl.triggerEventName = zzj.triggerEventName;
                zzl.zzahd = zzj.zzahd;
                zzl.active = zzj.active;
                zzl.zzahb = new zzfh(zzl.zzahb.name, zzj.zzahb.zzaue, zzl.zzahb.getValue(), zzj.zzahb.origin);
            }
            else if (TextUtils.isEmpty((CharSequence)zzl.triggerEventName)) {
                zzl.zzahb = new zzfh(zzl.zzahb.name, zzl.creationTimestamp, zzl.zzahb.getValue(), zzl.zzahb.origin);
                zzl.active = true;
                b = true;
            }
            if (zzl.active) {
                final zzfh zzahb = zzl.zzahb;
                final zzfj zzfj = new zzfj(zzl.packageName, zzl.origin, zzahb.name, zzahb.zzaue, zzahb.getValue());
                zzar zzar;
                String s;
                Object o;
                String s2;
                Object o2;
                if (this.zzjq().zza(zzfj)) {
                    zzar = this.zzadj.zzgo().zzjk();
                    s = "User property updated immediately";
                    o = zzl.packageName;
                    s2 = this.zzadj.zzgl().zzbu(zzfj.name);
                    o2 = zzfj.value;
                }
                else {
                    zzar = this.zzadj.zzgo().zzjd();
                    s = "(2)Too many active user properties, ignoring";
                    o = zzap.zzbv(zzl.packageName);
                    s2 = this.zzadj.zzgl().zzbu(zzfj.name);
                    o2 = zzfj.value;
                }
                zzar.zzd(s, o, s2, o2);
                if (b && zzl.zzahd != null) {
                    this.zzd(new zzad(zzl.zzahd, zzl.creationTimestamp), zzh);
                }
            }
            zzar zzar2;
            String s3;
            Object o3;
            String s4;
            Object o4;
            if (this.zzjq().zza(zzl)) {
                zzar2 = this.zzadj.zzgo().zzjk();
                s3 = "Conditional property added";
                o3 = zzl.packageName;
                s4 = this.zzadj.zzgl().zzbu(zzl.zzahb.name);
                o4 = zzl.zzahb.getValue();
            }
            else {
                zzar2 = this.zzadj.zzgo().zzjd();
                s3 = "Too many conditional properties, ignoring";
                o3 = zzap.zzbv(zzl.packageName);
                s4 = this.zzadj.zzgl().zzbu(zzl.zzahb.name);
                o4 = zzl.zzahb.getValue();
            }
            zzar2.zzd(s3, o3, s4, o4);
            this.zzjq().setTransactionSuccessful();
        }
        finally {
            this.zzjq().endTransaction();
        }
    }
    
    final void zzb(final String s, final int n, Throwable t, byte[] zzbl, final Map<String, List<String>> map) {
        this.zzaf();
        this.zzlr();
        Preconditions.checkNotEmpty(s);
        byte[] array = zzbl;
        Label_0030: {
            if (zzbl != null) {
                break Label_0030;
            }
            boolean b;
            final String s2;
            zzq zzq;
            int n2 = 0;
            Label_0315_Outer:Label_0352_Outer:
            while (true) {
                while (true) {
                    Label_0338_Outer:Label_0288_Outer:
                    while (true) {
                        Label_0572: {
                        Label_0288:
                            while (true) {
                                Label_0229:Label_0266_Outer:
                                while (true) {
                                    while (true) {
                                    Label_0096_Outer:
                                        while (true) {
                                        Label_0513_Outer:
                                            while (true) {
                                                Label_0538: {
                                                    Label_0528: {
                                                        while (true) {
                                                            try {
                                                                array = new byte[0];
                                                                this.zzadj.zzgo().zzjl().zzg("onConfigFetched. Response size", array.length);
                                                                this.zzjq().beginTransaction();
                                                                try {
                                                                    zzbl = (byte[])(Object)this.zzjq().zzbl(s);
                                                                    b = true;
                                                                    if (n == 200 || n == 204) {
                                                                        break Label_0528;
                                                                    }
                                                                    if (n == 304) {
                                                                        break Label_0528;
                                                                    }
                                                                    break Label_0538;
                                                                    // iftrue(Label_0559:, n != 429)
                                                                    // iftrue(Label_0388:, this.zzln().zza(s2, array, (String)t))
                                                                    // iftrue(Label_0388:, this.zzln().zzcf(s2) != null || this.zzln().zza(s2, (byte[])null, (String)null))
                                                                    // iftrue(Label_0229:, n == 503)
                                                                    // iftrue(Label_0544:, zzbl != null)
                                                                    // iftrue(Label_0441:, n != 404)
                                                                    // iftrue(Label_0565:, map == null)
                                                                    // iftrue(Label_0259:, !this.zzlo().zzfb() || !this.zzlu())
                                                                    // iftrue(Label_0570:, t == null || t.size() <= 0)
                                                                    while (true) {
                                                                        Block_17: {
                                                                        Label_0338:
                                                                            while (true) {
                                                                                Label_0488:Block_14_Outer:
                                                                                while (true) {
                                                                                    this.zzlv();
                                                                                    break Label_0488;
                                                                                    Block_20: {
                                                                                    Label_0467:
                                                                                        while (true) {
                                                                                            Block_23: {
                                                                                                Block_13: {
                                                                                                    while (true) {
                                                                                                        while (true) {
                                                                                                            Block_15: {
                                                                                                                break Block_15;
                                                                                                                break Block_20;
                                                                                                                Block_22: {
                                                                                                                    break Block_22;
                                                                                                                    this.zzlt();
                                                                                                                    break Label_0488;
                                                                                                                }
                                                                                                                zzq = this.zzjq();
                                                                                                                break Label_0338;
                                                                                                            }
                                                                                                            n2 = (b ? 1 : 0);
                                                                                                            break Label_0229;
                                                                                                            ((zzg)(Object)zzbl).zzz(this.zzadj.zzbx().currentTimeMillis());
                                                                                                            this.zzjq().zza((zzg)(Object)zzbl);
                                                                                                            this.zzadj.zzgo().zzjl().zze("Fetching config failed. code, error", n, t);
                                                                                                            this.zzln().zzch(s2);
                                                                                                            this.zzadj.zzgp().zzanf.set(this.zzadj.zzbx().currentTimeMillis());
                                                                                                            n2 = (b ? 1 : 0);
                                                                                                            continue Label_0315_Outer;
                                                                                                        }
                                                                                                        break Block_13;
                                                                                                        Label_0441: {
                                                                                                            this.zzadj.zzgo().zzjl().zze("Successfully fetched config. Got network response. code, size", n, array.length);
                                                                                                        }
                                                                                                        break Label_0467;
                                                                                                        Label_0388:
                                                                                                        ((zzg)(Object)zzbl).zzy(this.zzadj.zzbx().currentTimeMillis());
                                                                                                        this.zzjq().zza((zzg)(Object)zzbl);
                                                                                                        break Block_23;
                                                                                                        break Block_17;
                                                                                                        continue Label_0338_Outer;
                                                                                                    }
                                                                                                    break Label_0338;
                                                                                                }
                                                                                                this.zzadj.zzgo().zzjg().zzg("App does not exist in onConfigFetched. appId", zzap.zzbv(s2));
                                                                                                break Label_0488;
                                                                                            }
                                                                                            this.zzadj.zzgo().zzji().zzg("Config not found. Using empty config. appId", s2);
                                                                                            continue Label_0467;
                                                                                        }
                                                                                        zzq.endTransaction();
                                                                                        this.zzatp = false;
                                                                                        this.zzlw();
                                                                                        return;
                                                                                    }
                                                                                    zzq = this.zzjq();
                                                                                    continue Label_0338;
                                                                                    this.zzadj.zzgp().zzang.set(this.zzadj.zzbx().currentTimeMillis());
                                                                                    continue Block_14_Outer;
                                                                                }
                                                                                this.zzjq().setTransactionSuccessful();
                                                                                zzq = this.zzjq();
                                                                                continue Label_0338;
                                                                            }
                                                                            t = (Throwable)((List<String>)t).get(0);
                                                                            break Label_0572;
                                                                        }
                                                                        t = (Throwable)map.get("Last-Modified");
                                                                        continue Label_0288;
                                                                        continue Label_0513_Outer;
                                                                    }
                                                                }
                                                                // iftrue(Label_0259:, n2 == 0)
                                                                finally {
                                                                    this.zzjq().endTransaction();
                                                                }
                                                                this.zzatp = false;
                                                                this.zzlw();
                                                                throw s2;
                                                            }
                                                            finally {
                                                                continue;
                                                            }
                                                            break;
                                                        }
                                                    }
                                                    if (t == null) {
                                                        n2 = 1;
                                                        continue Label_0266_Outer;
                                                    }
                                                }
                                                n2 = 0;
                                                continue Label_0266_Outer;
                                            }
                                            Label_0544: {
                                                if (n2 == 0 && n != 404) {
                                                    continue Label_0096_Outer;
                                                }
                                            }
                                            break;
                                        }
                                        continue Label_0288_Outer;
                                    }
                                    Label_0559: {
                                        n2 = 0;
                                    }
                                    continue Label_0229;
                                }
                                Label_0565: {
                                    t = null;
                                }
                                continue Label_0288;
                            }
                            Label_0570: {
                                t = null;
                            }
                        }
                        if (n != 404 && n != 304) {
                            continue Label_0352_Outer;
                        }
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    @Override
    public final Clock zzbx() {
        return this.zzadj.zzbx();
    }
    
    final void zzc(final zzad zzad, final zzh zzh) {
        Preconditions.checkNotNull(zzh);
        Preconditions.checkNotEmpty(zzh.packageName);
        this.zzaf();
        this.zzlr();
        final String packageName = zzh.packageName;
        final long zzaip = zzad.zzaip;
        if (!this.zzjo().zze(zzad, zzh)) {
            return;
        }
        if (!zzh.zzagg) {
            this.zzg(zzh);
            return;
        }
        this.zzjq().beginTransaction();
        try {
            final zzq zzjq = this.zzjq();
            Preconditions.checkNotEmpty(packageName);
            zzjq.zzaf();
            zzjq.zzcl();
            List<zzl> list;
            if (zzaip < 0L) {
                zzjq.zzgo().zzjg().zze("Invalid time querying timed out conditional properties", zzap.zzbv(packageName), zzaip);
                list = Collections.emptyList();
            }
            else {
                list = zzjq.zzb("active=0 and app_id=? and abs(? - creation_timestamp) > trigger_timeout", new String[] { packageName, String.valueOf(zzaip) });
            }
            for (final zzl zzl : list) {
                if (zzl != null) {
                    this.zzadj.zzgo().zzjk().zzd("User property timed out", zzl.packageName, this.zzadj.zzgl().zzbu(zzl.zzahb.name), zzl.zzahb.getValue());
                    if (zzl.zzahc != null) {
                        this.zzd(new zzad(zzl.zzahc, zzaip), zzh);
                    }
                    this.zzjq().zzk(packageName, zzl.zzahb.name);
                }
            }
            final zzq zzjq2 = this.zzjq();
            Preconditions.checkNotEmpty(packageName);
            zzjq2.zzaf();
            zzjq2.zzcl();
            Object o;
            if (zzaip < 0L) {
                zzjq2.zzgo().zzjg().zze("Invalid time querying expired conditional properties", zzap.zzbv(packageName), zzaip);
                o = Collections.emptyList();
            }
            else {
                o = zzjq2.zzb("active<>0 and app_id=? and abs(? - triggered_timestamp) > time_to_live", new String[] { packageName, String.valueOf(zzaip) });
            }
            final ArrayList list2 = new ArrayList<zzad>(((List)o).size());
            for (final zzl zzl2 : o) {
                if (zzl2 != null) {
                    this.zzadj.zzgo().zzjk().zzd("User property expired", zzl2.packageName, this.zzadj.zzgl().zzbu(zzl2.zzahb.name), zzl2.zzahb.getValue());
                    this.zzjq().zzh(packageName, zzl2.zzahb.name);
                    if (zzl2.zzahe != null) {
                        list2.add(zzl2.zzahe);
                    }
                    this.zzjq().zzk(packageName, zzl2.zzahb.name);
                }
            }
            final ArrayList<zzad> list3 = (ArrayList<zzad>)list2;
            final int size = list3.size();
            int i = 0;
            while (i < size) {
                final zzad value = list3.get(i);
                ++i;
                this.zzd(new zzad(value, zzaip), zzh);
            }
            final zzq zzjq3 = this.zzjq();
            final String name = zzad.name;
            Preconditions.checkNotEmpty(packageName);
            Preconditions.checkNotEmpty(name);
            zzjq3.zzaf();
            zzjq3.zzcl();
            Object o2;
            if (zzaip < 0L) {
                zzjq3.zzgo().zzjg().zzd("Invalid time querying triggered conditional properties", zzap.zzbv(packageName), zzjq3.zzgl().zzbs(name), zzaip);
                o2 = Collections.emptyList();
            }
            else {
                o2 = zzjq3.zzb("active=0 and app_id=? and trigger_event_name=? and abs(? - creation_timestamp) <= trigger_timeout", new String[] { packageName, name, String.valueOf(zzaip) });
            }
            final ArrayList list4 = new ArrayList<zzad>(((List)o2).size());
            for (final zzl zzl3 : o2) {
                if (zzl3 != null) {
                    final zzfh zzahb = zzl3.zzahb;
                    final zzfj zzfj = new zzfj(zzl3.packageName, zzl3.origin, zzahb.name, zzaip, zzahb.getValue());
                    zzar zzar;
                    String s;
                    Object o3;
                    String s2;
                    Object o4;
                    if (this.zzjq().zza(zzfj)) {
                        zzar = this.zzadj.zzgo().zzjk();
                        s = "User property triggered";
                        o3 = zzl3.packageName;
                        s2 = this.zzadj.zzgl().zzbu(zzfj.name);
                        o4 = zzfj.value;
                    }
                    else {
                        zzar = this.zzadj.zzgo().zzjd();
                        s = "Too many active user properties, ignoring";
                        o3 = zzap.zzbv(zzl3.packageName);
                        s2 = this.zzadj.zzgl().zzbu(zzfj.name);
                        o4 = zzfj.value;
                    }
                    zzar.zzd(s, o3, s2, o4);
                    if (zzl3.zzahd != null) {
                        list4.add(zzl3.zzahd);
                    }
                    zzl3.zzahb = new zzfh(zzfj);
                    zzl3.active = true;
                    this.zzjq().zza(zzl3);
                }
            }
            this.zzd(zzad, zzh);
            final ArrayList<zzad> list5 = (ArrayList<zzad>)list4;
            final int size2 = list5.size();
            int j = 0;
            while (j < size2) {
                final zzad value2 = list5.get(j);
                ++j;
                this.zzd(new zzad(value2, zzaip), zzh);
            }
            this.zzjq().setTransactionSuccessful();
        }
        finally {
            this.zzjq().endTransaction();
        }
    }
    
    final void zzc(final zzad zzad, final String s) {
        final zzg zzbl = this.zzjq().zzbl(s);
        if (zzbl != null && !TextUtils.isEmpty((CharSequence)zzbl.zzak())) {
            final Boolean zzc = this.zzc(zzbl);
            if (zzc == null) {
                if (!"_ui".equals(zzad.name)) {
                    this.zzadj.zzgo().zzjg().zzg("Could not find package. appId", zzap.zzbv(s));
                }
            }
            else if (!zzc) {
                this.zzadj.zzgo().zzjd().zzg("App version does not match; dropping event. appId", zzap.zzbv(s));
                return;
            }
            this.zzc(zzad, new zzh(s, zzbl.getGmpAppId(), zzbl.zzak(), zzbl.zzha(), zzbl.zzhb(), zzbl.zzhc(), zzbl.zzhd(), null, zzbl.isMeasurementEnabled(), false, zzbl.getFirebaseInstanceId(), zzbl.zzhq(), 0L, 0, zzbl.zzhr(), zzbl.zzhs(), false, zzbl.zzgw()));
            return;
        }
        this.zzadj.zzgo().zzjk().zzg("No app data available; dropping event", s);
    }
    
    final void zzc(final zzfh zzfh, final zzh zzh) {
        this.zzaf();
        this.zzlr();
        if (TextUtils.isEmpty((CharSequence)zzh.zzafx) && TextUtils.isEmpty((CharSequence)zzh.zzagk)) {
            return;
        }
        if (!zzh.zzagg) {
            this.zzg(zzh);
            return;
        }
        if (this.zzadj.zzgq().zze(zzh.packageName, zzaf.zzalj) && "_ap".equals(zzfh.name)) {
            final zzfj zzi = this.zzjq().zzi(zzh.packageName, "_ap");
            if (zzi != null && "auto".equals(zzfh.origin) && !"auto".equals(zzi.origin)) {
                this.zzadj.zzgo().zzjk().zzbx("Not removing higher priority ad personalization property");
                return;
            }
        }
        this.zzadj.zzgo().zzjk().zzg("Removing user property", this.zzadj.zzgl().zzbu(zzfh.name));
        this.zzjq().beginTransaction();
        try {
            this.zzg(zzh);
            this.zzjq().zzh(zzh.packageName, zzfh.name);
            this.zzjq().setTransactionSuccessful();
            this.zzadj.zzgo().zzjk().zzg("User property removed", this.zzadj.zzgl().zzbu(zzfh.name));
        }
        finally {
            this.zzjq().endTransaction();
        }
    }
    
    final void zzc(final zzl zzl, final zzh zzh) {
        Preconditions.checkNotNull(zzl);
        Preconditions.checkNotEmpty(zzl.packageName);
        Preconditions.checkNotNull(zzl.zzahb);
        Preconditions.checkNotEmpty(zzl.zzahb.name);
        this.zzaf();
        this.zzlr();
        if (TextUtils.isEmpty((CharSequence)zzh.zzafx) && TextUtils.isEmpty((CharSequence)zzh.zzagk)) {
            return;
        }
        if (!zzh.zzagg) {
            this.zzg(zzh);
            return;
        }
        this.zzjq().beginTransaction();
        try {
            this.zzg(zzh);
            final zzl zzj = this.zzjq().zzj(zzl.packageName, zzl.zzahb.name);
            if (zzj != null) {
                this.zzadj.zzgo().zzjk().zze("Removing conditional user property", zzl.packageName, this.zzadj.zzgl().zzbu(zzl.zzahb.name));
                this.zzjq().zzk(zzl.packageName, zzl.zzahb.name);
                if (zzj.active) {
                    this.zzjq().zzh(zzl.packageName, zzl.zzahb.name);
                }
                if (zzl.zzahe != null) {
                    Bundle zziv = null;
                    if (zzl.zzahe.zzaid != null) {
                        zziv = zzl.zzahe.zzaid.zziv();
                    }
                    this.zzd(this.zzadj.zzgm().zza(zzl.packageName, zzl.zzahe.name, zziv, zzj.origin, zzl.zzahe.zzaip, true, false), zzh);
                }
            }
            else {
                this.zzadj.zzgo().zzjg().zze("Conditional user property doesn't exist", zzap.zzbv(zzl.packageName), this.zzadj.zzgl().zzbu(zzl.zzahb.name));
            }
            this.zzjq().setTransactionSuccessful();
        }
        finally {
            this.zzjq().endTransaction();
        }
    }
    
    final void zzd(final zzh zzh) {
        if (this.zzatu != null) {
            (this.zzatv = new ArrayList<Long>()).addAll(this.zzatu);
        }
        final zzq zzjq = this.zzjq();
        final String packageName = zzh.packageName;
        Preconditions.checkNotEmpty(packageName);
        zzjq.zzaf();
        zzjq.zzcl();
        try {
            final SQLiteDatabase writableDatabase = zzjq.getWritableDatabase();
            final String[] array = { packageName };
            final int n = writableDatabase.delete("apps", "app_id=?", array) + 0 + writableDatabase.delete("events", "app_id=?", array) + writableDatabase.delete("user_attributes", "app_id=?", array) + writableDatabase.delete("conditional_properties", "app_id=?", array) + writableDatabase.delete("raw_events", "app_id=?", array) + writableDatabase.delete("raw_events_metadata", "app_id=?", array) + writableDatabase.delete("queue", "app_id=?", array) + writableDatabase.delete("audience_filter_values", "app_id=?", array) + writableDatabase.delete("main_event_params", "app_id=?", array);
            if (n > 0) {
                zzjq.zzgo().zzjl().zze("Reset analytics data. app, records", packageName, n);
            }
        }
        catch (SQLiteException ex) {
            zzjq.zzgo().zzjd().zze("Error resetting analytics data. appId, error", zzap.zzbv(packageName), ex);
        }
        final zzh zza = this.zza(this.zzadj.getContext(), zzh.packageName, zzh.zzafx, zzh.zzagg, zzh.zzagi, zzh.zzagj, zzh.zzagx, zzh.zzagk);
        if (!this.zzadj.zzgq().zzbd(zzh.packageName) || zzh.zzagg) {
            this.zzf(zza);
        }
    }
    
    final void zze(final zzh zzh) {
        this.zzaf();
        this.zzlr();
        Preconditions.checkNotEmpty(zzh.packageName);
        this.zzg(zzh);
    }
    
    final void zze(final zzl zzl) {
        final zzh zzco = this.zzco(zzl.packageName);
        if (zzco != null) {
            this.zzb(zzl, zzco);
        }
    }
    
    final void zzf(final zzh zzh) {
        this.zzaf();
        this.zzlr();
        Preconditions.checkNotNull(zzh);
        Preconditions.checkNotEmpty(zzh.packageName);
        if (TextUtils.isEmpty((CharSequence)zzh.zzafx) && TextUtils.isEmpty((CharSequence)zzh.zzagk)) {
            return;
        }
        final zzg zzbl = this.zzjq().zzbl(zzh.packageName);
        if (zzbl != null && TextUtils.isEmpty((CharSequence)zzbl.getGmpAppId()) && !TextUtils.isEmpty((CharSequence)zzh.zzafx)) {
            zzbl.zzy(0L);
            this.zzjq().zza(zzbl);
            this.zzln().zzci(zzh.packageName);
        }
        if (!zzh.zzagg) {
            this.zzg(zzh);
            return;
        }
        long n;
        if ((n = zzh.zzagx) == 0L) {
            n = this.zzadj.zzbx().currentTimeMillis();
        }
        int zzagy;
        final int n2 = zzagy = zzh.zzagy;
        if (n2 != 0 && (zzagy = n2) != 1) {
            this.zzadj.zzgo().zzjg().zze("Incorrect app type, assuming installed app. appId, appType", zzap.zzbv(zzh.packageName), n2);
            zzagy = 0;
        }
        Object zzbl2;
        Object o;
        zzq zzjq;
        SQLiteDatabase writableDatabase;
        String[] array;
        int n3;
        zzad zzad;
        zzq zzq;
        String s;
        zzz zzg;
        long n4;
        long n5 = 0L;
        PackageInfo packageInfo = null;
        ApplicationInfo applicationInfo;
        zzq zzjq2;
        String packageName;
        long zzn;
        zzad zzad2;
        Bundle bundle;
        Bundle bundle2;
        long n6;
        Label_0745_Outer:Label_1080_Outer:
        while (true) {
            this.zzjq().beginTransaction();
            while (true) {
                Label_1561: {
                Label_1559:
                    while (true) {
                    Label_1553:
                        while (true) {
                            Label_1547: {
                                try {
                                    o = (zzbl2 = this.zzjq().zzbl(zzh.packageName));
                                    if (o != null) {
                                        this.zzadj.zzgm();
                                        zzbl2 = o;
                                        if (zzfk.zza(zzh.zzafx, ((zzg)o).getGmpAppId(), zzh.zzagk, ((zzg)o).zzgw())) {
                                            this.zzadj.zzgo().zzjg().zzg("New GMP App Id passed in. Removing cached database data. appId", zzap.zzbv(((zzg)o).zzal()));
                                            zzjq = this.zzjq();
                                            o = ((zzg)o).zzal();
                                            zzjq.zzcl();
                                            zzjq.zzaf();
                                            Preconditions.checkNotEmpty((String)o);
                                            try {
                                                writableDatabase = zzjq.getWritableDatabase();
                                                array = new String[] { (String)o };
                                                n3 = writableDatabase.delete("events", "app_id=?", array) + 0 + writableDatabase.delete("user_attributes", "app_id=?", array) + writableDatabase.delete("conditional_properties", "app_id=?", array) + writableDatabase.delete("apps", "app_id=?", array) + writableDatabase.delete("raw_events", "app_id=?", array) + writableDatabase.delete("raw_events_metadata", "app_id=?", array) + writableDatabase.delete("event_filters", "app_id=?", array) + writableDatabase.delete("property_filters", "app_id=?", array) + writableDatabase.delete("audience_filter_values", "app_id=?", array);
                                                if (n3 > 0) {
                                                    zzjq.zzgo().zzjl().zze("Deleted application data. app, records", o, n3);
                                                }
                                            }
                                            catch (SQLiteException ex) {
                                                zzjq.zzgo().zzjd().zze("Error deleting application data. appId, error", zzap.zzbv((String)o), ex);
                                            }
                                            break Label_1547;
                                        }
                                    }
                                    Label_0679: {
                                        if (zzbl2 != null) {
                                            if (((zzg)zzbl2).zzha() != -2147483648L) {
                                                if (((zzg)zzbl2).zzha() == zzh.zzagd) {
                                                    break Label_0679;
                                                }
                                                o = new Bundle();
                                                ((Bundle)o).putString("_pv", ((zzg)zzbl2).zzak());
                                                zzad = new zzad("_au", new zzaa((Bundle)o), "auto", n);
                                            }
                                            else {
                                                if (((zzg)zzbl2).zzak() == null || ((zzg)zzbl2).zzak().equals(zzh.zzts)) {
                                                    break Label_0679;
                                                }
                                                o = new Bundle();
                                                ((Bundle)o).putString("_pv", ((zzg)zzbl2).zzak());
                                                zzad = new zzad("_au", new zzaa((Bundle)o), "auto", n);
                                            }
                                            this.zzc(zzad, zzh);
                                        }
                                    }
                                    this.zzg(zzh);
                                    if (zzagy == 0) {
                                        zzq = this.zzjq();
                                        o = zzh.packageName;
                                        s = "_f";
                                    }
                                    else {
                                        if (zzagy != 1) {
                                            break Label_1553;
                                        }
                                        zzq = this.zzjq();
                                        o = zzh.packageName;
                                        s = "_v";
                                    }
                                    zzg = zzq.zzg((String)o, s);
                                    while (true) {
                                        if (zzg == null) {
                                            n4 = 3600000L * (n / 3600000L + 1L);
                                            Label_1431: {
                                                if (zzagy == 0) {
                                                    n5 = 1L;
                                                    this.zzb(new zzfh("_fot", n, n4, "auto"), zzh);
                                                    if (this.zzadj.zzgq().zzbg(zzh.zzafx)) {
                                                        this.zzaf();
                                                        this.zzadj.zzkg().zzcd(zzh.packageName);
                                                    }
                                                    this.zzaf();
                                                    this.zzlr();
                                                    o = new Bundle();
                                                    ((Bundle)o).putLong("_c", n5);
                                                    ((Bundle)o).putLong("_r", n5);
                                                    ((Bundle)o).putLong("_uwa", 0L);
                                                    ((Bundle)o).putLong("_pfo", 0L);
                                                    ((Bundle)o).putLong("_sys", 0L);
                                                    ((Bundle)o).putLong("_sysu", 0L);
                                                    if (this.zzadj.zzgq().zzbd(zzh.packageName) && zzh.zzagz) {
                                                        ((Bundle)o).putLong("_dac", n5);
                                                    }
                                                    if (this.zzadj.getContext().getPackageManager() == null) {
                                                        this.zzadj.zzgo().zzjd().zzg("PackageManager is null, first open report might be inaccurate. appId", zzap.zzbv(zzh.packageName));
                                                    }
                                                    else {
                                                        Label_1036: {
                                                            try {
                                                                packageInfo = Wrappers.packageManager(this.zzadj.getContext()).getPackageInfo(zzh.packageName, 0);
                                                                break Label_1036;
                                                            }
                                                            catch (PackageManager$NameNotFoundException ex2) {
                                                                this.zzadj.zzgo().zzjd().zze("Package info is null, first open report might be inaccurate. appId", zzap.zzbv(zzh.packageName), ex2);
                                                                packageInfo = null;
                                                            }
                                                        }
                                                        if (packageInfo != null && packageInfo.firstInstallTime != 0L) {
                                                            if (packageInfo.firstInstallTime != packageInfo.lastUpdateTime) {
                                                                ((Bundle)o).putLong("_uwa", n5);
                                                                zzagy = 0;
                                                                break Label_1561;
                                                            }
                                                            break Label_1559;
                                                        }
                                                        try {
                                                            applicationInfo = Wrappers.packageManager(this.zzadj.getContext()).getApplicationInfo(zzh.packageName, 0);
                                                        }
                                                        catch (PackageManager$NameNotFoundException ex3) {
                                                            this.zzadj.zzgo().zzjd().zze("Application info is null, first open report might be inaccurate. appId", zzap.zzbv(zzh.packageName), ex3);
                                                            applicationInfo = null;
                                                        }
                                                        if (applicationInfo != null) {
                                                            if ((applicationInfo.flags & 0x1) != 0x0) {
                                                                ((Bundle)o).putLong("_sys", n5);
                                                            }
                                                            if ((applicationInfo.flags & 0x80) != 0x0) {
                                                                ((Bundle)o).putLong("_sysu", n5);
                                                            }
                                                        }
                                                    }
                                                    zzjq2 = this.zzjq();
                                                    packageName = zzh.packageName;
                                                    Preconditions.checkNotEmpty(packageName);
                                                    zzjq2.zzaf();
                                                    zzjq2.zzcl();
                                                    zzn = zzjq2.zzn(packageName, "first_open_count");
                                                    if (zzn >= 0L) {
                                                        ((Bundle)o).putLong("_pfo", zzn);
                                                    }
                                                    zzad2 = new zzad("_f", new zzaa((Bundle)o), "auto", n);
                                                }
                                                else {
                                                    n5 = 1L;
                                                    if (zzagy != 1) {
                                                        break Label_1431;
                                                    }
                                                    this.zzb(new zzfh("_fvt", n, n4, "auto"), zzh);
                                                    this.zzaf();
                                                    this.zzlr();
                                                    bundle = new Bundle();
                                                    bundle.putLong("_c", n5);
                                                    bundle.putLong("_r", n5);
                                                    if (this.zzadj.zzgq().zzbd(zzh.packageName) && zzh.zzagz) {
                                                        bundle.putLong("_dac", n5);
                                                    }
                                                    zzad2 = new zzad("_v", new zzaa(bundle), "auto", n);
                                                }
                                                this.zzc(zzad2, zzh);
                                            }
                                            bundle2 = new Bundle();
                                            bundle2.putLong("_et", 1L);
                                            this.zzc(new zzad("_e", new zzaa(bundle2), "auto", n), zzh);
                                        }
                                        else if (zzh.zzagw) {
                                            this.zzc(new zzad("_cd", new zzaa(new Bundle()), "auto", n), zzh);
                                        }
                                        this.zzjq().setTransactionSuccessful();
                                        return;
                                        this.zzb(new zzfh("_fi", n, n6, "auto"), zzh);
                                        continue Label_1080_Outer;
                                    }
                                }
                                finally {
                                    this.zzjq().endTransaction();
                                }
                            }
                            zzbl2 = null;
                            continue Label_0745_Outer;
                        }
                        zzg = null;
                        continue Label_1080_Outer;
                    }
                    zzagy = 1;
                }
                if (zzagy != 0) {
                    n6 = n5;
                    continue;
                }
                n6 = 0L;
                continue;
            }
        }
    }
    
    final void zzf(final zzl zzl) {
        final zzh zzco = this.zzco(zzl.packageName);
        if (zzco != null) {
            this.zzc(zzl, zzco);
        }
    }
    
    final void zzg(final Runnable runnable) {
        this.zzaf();
        if (this.zzatm == null) {
            this.zzatm = new ArrayList<Runnable>();
        }
        this.zzatm.add(runnable);
    }
    
    public final zzan zzgl() {
        return this.zzadj.zzgl();
    }
    
    public final zzfk zzgm() {
        return this.zzadj.zzgm();
    }
    
    @Override
    public final zzbo zzgn() {
        return this.zzadj.zzgn();
    }
    
    @Override
    public final zzap zzgo() {
        return this.zzadj.zzgo();
    }
    
    public final zzn zzgq() {
        return this.zzadj.zzgq();
    }
    
    @Override
    public final zzk zzgr() {
        return this.zzadj.zzgr();
    }
    
    final String zzh(final zzh zzh) {
        final Future<String> zzb = this.zzadj.zzgn().zzb((Callable<String>)new zzfe(this, zzh));
        try {
            return zzb.get(30000L, TimeUnit.MILLISECONDS);
        }
        catch (TimeoutException | InterruptedException | ExecutionException ex) {
            final Object o;
            this.zzadj.zzgo().zzjd().zze("Failed to get app instance id. appId", zzap.zzbv(zzh.packageName), o);
            return null;
        }
    }
    
    public final zzfg zzjo() {
        zza(this.zzatj);
        return this.zzatj;
    }
    
    public final zzj zzjp() {
        zza(this.zzati);
        return this.zzati;
    }
    
    public final zzq zzjq() {
        zza(this.zzatf);
        return this.zzatf;
    }
    
    public final zzat zzlo() {
        zza(this.zzate);
        return this.zzate;
    }
    
    final void zzlr() {
        if (this.zzvz) {
            return;
        }
        throw new IllegalStateException("UploadController is not initialized");
    }
    
    final void zzlt() {
    Label_0420_Outer:
        while (true) {
            this.zzaf();
            this.zzlr();
            this.zzatr = true;
            while (true) {
                int max = 0;
                Label_1089: {
                    Label_1084: {
                    Label_0492_Outer:
                        while (true) {
                            int zzb = 0;
                        Label_0492:
                            while (true) {
                                Object o = null;
                                Object zzb2 = null;
                                Label_1061: {
                                Label_1058:
                                    while (true) {
                                        Label_1053: {
                                            try {
                                                this.zzadj.zzgr();
                                                final Boolean zzle = this.zzadj.zzgg().zzle();
                                                zzar zzar = null;
                                                Label_0055: {
                                                    if (zzle == null) {
                                                        zzar = this.zzadj.zzgo().zzjg();
                                                        o = "Upload data called on the client side before use of service was decided";
                                                    }
                                                    else {
                                                        if (!zzle) {
                                                            if (this.zzatl <= 0L) {
                                                                this.zzaf();
                                                                if (this.zzatu == null) {
                                                                    break Label_1053;
                                                                }
                                                                zzb = 1;
                                                                if (zzb != 0) {
                                                                    zzar = this.zzadj.zzgo().zzjl();
                                                                    o = "Uploading requested multiple times";
                                                                    break Label_0055;
                                                                }
                                                                if (!this.zzlo().zzfb()) {
                                                                    this.zzadj.zzgo().zzjl().zzbx("Network not connected, ignoring upload request");
                                                                }
                                                                else {
                                                                    final long currentTimeMillis = this.zzadj.zzbx().currentTimeMillis();
                                                                    final long zzhx = zzn.zzhx();
                                                                    final String s = null;
                                                                    this.zzd(null, currentTimeMillis - zzhx);
                                                                    final long value = this.zzadj.zzgp().zzane.get();
                                                                    if (value != 0L) {
                                                                        this.zzadj.zzgo().zzjk().zzg("Uploading events. Elapsed time since last upload attempt (ms)", Math.abs(currentTimeMillis - value));
                                                                    }
                                                                    final String zzid = this.zzjq().zzid();
                                                                    if (!TextUtils.isEmpty((CharSequence)zzid)) {
                                                                        if (this.zzatw == -1L) {
                                                                            this.zzatw = this.zzjq().zzik();
                                                                        }
                                                                        zzb = this.zzadj.zzgq().zzb(zzid, zzaf.zzajj);
                                                                        max = Math.max(0, this.zzadj.zzgq().zzb(zzid, zzaf.zzajk));
                                                                        zzb2 = this.zzjq().zzb(zzid, zzb, max);
                                                                        if (!((List)zzb2).isEmpty()) {
                                                                            final Iterator<Pair> iterator = ((List<Pair>)zzb2).iterator();
                                                                            Block_14: {
                                                                                while (iterator.hasNext()) {
                                                                                    o = iterator.next().first;
                                                                                    if (!TextUtils.isEmpty((CharSequence)((zzgi)o).zzaxo)) {
                                                                                        break Block_14;
                                                                                    }
                                                                                }
                                                                                break Label_1058;
                                                                            }
                                                                            o = ((zzgi)o).zzaxo;
                                                                            break Label_1061;
                                                                        }
                                                                        return;
                                                                    }
                                                                    else {
                                                                        this.zzatw = -1L;
                                                                        final String zzah = this.zzjq().zzah(currentTimeMillis - zzn.zzhx());
                                                                        if (TextUtils.isEmpty((CharSequence)zzah)) {
                                                                            return;
                                                                        }
                                                                        final zzg zzbl = this.zzjq().zzbl(zzah);
                                                                        if (zzbl != null) {
                                                                            this.zzb(zzbl);
                                                                        }
                                                                        return;
                                                                    }
                                                                }
                                                            }
                                                            this.zzlv();
                                                            return;
                                                        }
                                                        zzar = this.zzadj.zzgo().zzjd();
                                                        o = "Upload called in the client side when service should be used";
                                                    }
                                                }
                                                zzar.zzbx((String)o);
                                                return;
                                                List<Pair> subList = (List<Pair>)zzb2;
                                                // iftrue(Label_0492:, zzb >= zzb2.size())
                                                // iftrue(Label_0727:, !this.zzadj.zzgo().isLoggable(2))
                                                // iftrue(Label_1075:, TextUtils.isEmpty((CharSequence)zzgi.zzaxo) || zzgi.zzaxo.equals(o))
                                                // iftrue(Label_1082:, !zzn.zzhz() || !this.zzadj.zzgq().zzav(zzid))
                                                // iftrue(Label_0698:, max >= zzb2.zzawy.length)
                                                // iftrue(Label_1089:, zzb != 0)
                                            Label_0727_Outer:
                                                while (true) {
                                                    final long currentTimeMillis;
                                                Block_20:
                                                    while (true) {
                                                        Block_22: {
                                                            Block_15: {
                                                                break Block_15;
                                                                ((zzgh)zzb2).zzawy[max].zzaya = null;
                                                                break Label_1089;
                                                                Label_0698: {
                                                                    final String s;
                                                                    final String zzb3 = s;
                                                                }
                                                                break Block_22;
                                                                final byte[] zza = this.zzjo().zza((zzgh)zzb2);
                                                                final String s = zzaf.zzajt.get();
                                                                try {
                                                                    final URL url = new URL(s);
                                                                    Preconditions.checkArgument(((List)o).isEmpty() ^ true);
                                                                    if (this.zzatu != null) {
                                                                        this.zzadj.zzgo().zzjd().zzbx("Set uploading progress before finishing the previous upload");
                                                                    }
                                                                    else {
                                                                        this.zzatu = new ArrayList<Long>((Collection<? extends Long>)o);
                                                                    }
                                                                    this.zzadj.zzgp().zzanf.set(currentTimeMillis);
                                                                    o = "?";
                                                                    if (((zzgh)zzb2).zzawy.length > 0) {
                                                                        o = ((zzgh)zzb2).zzawy[0].zztt;
                                                                    }
                                                                    final String zzb3;
                                                                    this.zzadj.zzgo().zzjl().zzd("Uploading data. app, uncompressed size, data", o, zza.length, zzb3);
                                                                    this.zzatq = true;
                                                                    final zzat zzlo = this.zzlo();
                                                                    final String zzid;
                                                                    o = new zzfc(this, zzid);
                                                                    zzlo.zzaf();
                                                                    zzlo.zzcl();
                                                                    Preconditions.checkNotNull(url);
                                                                    Preconditions.checkNotNull(zza);
                                                                    Preconditions.checkNotNull(o);
                                                                    zzlo.zzgn().zzd(new zzax(zzid, url, zza, null, (zzav)o));
                                                                }
                                                                catch (MalformedURLException ex) {
                                                                    final String zzid;
                                                                    this.zzadj.zzgo().zzjd().zze("Failed to parse upload URL. Not uploading. appId", zzap.zzbv(zzid), s);
                                                                }
                                                                return;
                                                            }
                                                            final zzgi zzgi = (zzgi)((List<Pair>)zzb2).get(zzb).first;
                                                            break Label_0727_Outer;
                                                            zzb2 = new zzgh();
                                                            ((zzgh)zzb2).zzawy = new zzgi[subList.size()];
                                                            o = new ArrayList<Long>(subList.size());
                                                            Block_19: {
                                                                break Block_19;
                                                                break Block_20;
                                                            }
                                                            zzb = 1;
                                                            break Label_1084;
                                                        }
                                                        final String zzb3 = this.zzjo().zzb((zzgh)zzb2);
                                                        continue Label_0492_Outer;
                                                    }
                                                    final List<Pair> list;
                                                    ((zzgh)zzb2).zzawy[max] = (zzgi)list.get(max).first;
                                                    ((List<Long>)o).add((Long)list.get(max).second);
                                                    ((zzgh)zzb2).zzawy[max].zzaxn = this.zzadj.zzgq().zzhc();
                                                    ((zzgh)zzb2).zzawy[max].zzaxd = currentTimeMillis;
                                                    final zzgi zzgi2 = ((zzgh)zzb2).zzawy[max];
                                                    this.zzadj.zzgr();
                                                    zzgi2.zzaxs = false;
                                                    continue Label_0727_Outer;
                                                }
                                                subList = ((List<Pair>)zzb2).subList(0, zzb);
                                                continue Label_0492;
                                            }
                                            finally {
                                                this.zzatr = false;
                                                this.zzlw();
                                            }
                                        }
                                        zzb = 0;
                                        continue Label_0420_Outer;
                                    }
                                    o = null;
                                }
                                List<Pair> subList = (List<Pair>)zzb2;
                                if (o != null) {
                                    zzb = 0;
                                    continue Label_0492_Outer;
                                }
                                continue Label_0492;
                            }
                            Label_1075: {
                                ++zzb;
                            }
                            continue Label_0492_Outer;
                        }
                        Label_1082: {
                            final int zzb = 0;
                        }
                    }
                    max = 0;
                    continue;
                }
                ++max;
                continue;
            }
        }
    }
    
    final void zzly() {
        this.zzaf();
        this.zzlr();
        if (!this.zzatk) {
            this.zzadj.zzgo().zzjj().zzbx("This instance being marked as an uploader");
            this.zzaf();
            this.zzlr();
            Label_0170: {
                if (this.zzlz() && this.zzlx()) {
                    final int zza = this.zza(this.zzatt);
                    final int zzja = this.zzadj.zzgf().zzja();
                    this.zzaf();
                    zzar zzar;
                    String s;
                    if (zza > zzja) {
                        zzar = this.zzadj.zzgo().zzjd();
                        s = "Panic: can't downgrade version. Previous, current version";
                    }
                    else {
                        if (zza >= zzja) {
                            break Label_0170;
                        }
                        if (this.zza(zzja, this.zzatt)) {
                            zzar = this.zzadj.zzgo().zzjl();
                            s = "Storage version upgraded. Previous, current version";
                        }
                        else {
                            zzar = this.zzadj.zzgo().zzjd();
                            s = "Storage version upgrade failed. Previous, current version";
                        }
                    }
                    zzar.zze(s, zza, zzja);
                }
            }
            this.zzatk = true;
            this.zzlv();
        }
    }
    
    final void zzma() {
        ++this.zzato;
    }
    
    final zzbt zzmb() {
        return this.zzadj;
    }
    
    final void zzo(final boolean b) {
        this.zzlv();
    }
    
    final class zza implements zzs
    {
        zzgi zzaua;
        List<Long> zzaub;
        List<zzgf> zzauc;
        private long zzaud;
        
        private zza() {
        }
        
        private static long zza(final zzgf zzgf) {
            return zzgf.zzawu / 1000L / 60L / 60L;
        }
        
        @Override
        public final boolean zza(final long n, final zzgf zzgf) {
            Preconditions.checkNotNull(zzgf);
            if (this.zzauc == null) {
                this.zzauc = new ArrayList<zzgf>();
            }
            if (this.zzaub == null) {
                this.zzaub = new ArrayList<Long>();
            }
            if (this.zzauc.size() > 0 && zza(this.zzauc.get(0)) != zza(zzgf)) {
                return false;
            }
            final long zzaud = this.zzaud + zzgf.zzvu();
            if (zzaud >= Math.max(0, zzaf.zzajl.get())) {
                return false;
            }
            this.zzaud = zzaud;
            this.zzauc.add(zzgf);
            this.zzaub.add(n);
            return this.zzauc.size() < Math.max(1, zzaf.zzajm.get());
        }
        
        @Override
        public final void zzb(final zzgi zzaua) {
            Preconditions.checkNotNull(zzaua);
            this.zzaua = zzaua;
        }
    }
}

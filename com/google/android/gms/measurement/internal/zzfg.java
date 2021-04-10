package com.google.android.gms.measurement.internal;

import java.util.*;
import android.content.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.internal.measurement.*;
import java.util.zip.*;
import java.io.*;
import com.google.android.gms.common.util.*;
import android.text.*;

public final class zzfg extends zzez
{
    zzfg(final zzfa zzfa) {
        super(zzfa);
    }
    
    static zzgg zza(final zzgf zzgf, final String s) {
        final zzgg[] zzawt = zzgf.zzawt;
        for (int length = zzawt.length, i = 0; i < length; ++i) {
            final zzgg zzgg = zzawt[i];
            if (zzgg.name.equals(s)) {
                return zzgg;
            }
        }
        return null;
    }
    
    private static void zza(final StringBuilder sb, final int n) {
        for (int i = 0; i < n; ++i) {
            sb.append("  ");
        }
    }
    
    private final void zza(final StringBuilder sb, final int n, final zzfw zzfw) {
        if (zzfw == null) {
            return;
        }
        zza(sb, n);
        sb.append("filter {\n");
        zza(sb, n, "complement", zzfw.zzavm);
        zza(sb, n, "param_name", this.zzgl().zzbt(zzfw.zzavn));
        final int n2 = n + 1;
        final zzfz zzavk = zzfw.zzavk;
        if (zzavk != null) {
            zza(sb, n2);
            sb.append("string_filter");
            sb.append(" {\n");
            if (zzavk.zzavw != null) {
                String s = null;
                switch (zzavk.zzavw) {
                    default: {
                        s = "UNKNOWN_MATCH_TYPE";
                        break;
                    }
                    case 6: {
                        s = "IN_LIST";
                        break;
                    }
                    case 5: {
                        s = "EXACT";
                        break;
                    }
                    case 4: {
                        s = "PARTIAL";
                        break;
                    }
                    case 3: {
                        s = "ENDS_WITH";
                        break;
                    }
                    case 2: {
                        s = "BEGINS_WITH";
                        break;
                    }
                    case 1: {
                        s = "REGEXP";
                        break;
                    }
                }
                zza(sb, n2, "match_type", s);
            }
            zza(sb, n2, "expression", zzavk.zzavx);
            zza(sb, n2, "case_sensitive", zzavk.zzavy);
            if (zzavk.zzavz.length > 0) {
                zza(sb, n2 + 1);
                sb.append("expression_list {\n");
                final String[] zzavz = zzavk.zzavz;
                for (int length = zzavz.length, i = 0; i < length; ++i) {
                    final String s2 = zzavz[i];
                    zza(sb, n2 + 2);
                    sb.append(s2);
                    sb.append("\n");
                }
                sb.append("}\n");
            }
            zza(sb, n2);
            sb.append("}\n");
        }
        this.zza(sb, n2, "number_filter", zzfw.zzavl);
        zza(sb, n);
        sb.append("}\n");
    }
    
    private final void zza(final StringBuilder sb, final int n, String s, final zzfx zzfx) {
        if (zzfx == null) {
            return;
        }
        zza(sb, n);
        sb.append(s);
        sb.append(" {\n");
        if (zzfx.zzavo != null) {
            final int intValue = zzfx.zzavo;
            if (intValue != 1) {
                if (intValue != 2) {
                    if (intValue != 3) {
                        if (intValue != 4) {
                            s = "UNKNOWN_COMPARISON_TYPE";
                        }
                        else {
                            s = "BETWEEN";
                        }
                    }
                    else {
                        s = "EQUAL";
                    }
                }
                else {
                    s = "GREATER_THAN";
                }
            }
            else {
                s = "LESS_THAN";
            }
            zza(sb, n, "comparison_type", s);
        }
        zza(sb, n, "match_as_float", zzfx.zzavp);
        zza(sb, n, "comparison_value", zzfx.zzavq);
        zza(sb, n, "min_comparison_value", zzfx.zzavr);
        zza(sb, n, "max_comparison_value", zzfx.zzavs);
        zza(sb, n);
        sb.append("}\n");
    }
    
    private static void zza(final StringBuilder sb, int n, final String s, final zzgj zzgj) {
        if (zzgj == null) {
            return;
        }
        zza(sb, 3);
        sb.append(s);
        sb.append(" {\n");
        final long[] zzayf = zzgj.zzayf;
        final int n2 = 0;
        if (zzayf != null) {
            zza(sb, 4);
            sb.append("results: ");
            final long[] zzayf2 = zzgj.zzayf;
            int length;
            int i;
            long n3;
            for (length = zzayf2.length, i = 0, n = 0; i < length; ++i, ++n) {
                n3 = zzayf2[i];
                if (n != 0) {
                    sb.append(", ");
                }
                sb.append((Object)n3);
            }
            sb.append('\n');
        }
        if (zzgj.zzaye != null) {
            zza(sb, 4);
            sb.append("status: ");
            final long[] zzaye = zzgj.zzaye;
            final int length2 = zzaye.length;
            n = 0;
            for (int j = n2; j < length2; ++j, ++n) {
                final long n4 = zzaye[j];
                if (n != 0) {
                    sb.append(", ");
                }
                sb.append((Object)n4);
            }
            sb.append('\n');
        }
        zza(sb, 3);
        sb.append("}\n");
    }
    
    private static void zza(final StringBuilder sb, final int n, final String s, final Object o) {
        if (o == null) {
            return;
        }
        zza(sb, n + 1);
        sb.append(s);
        sb.append(": ");
        sb.append(o);
        sb.append('\n');
    }
    
    static boolean zza(final long[] array, final int n) {
        return n < array.length << 6 && (1L << n % 64 & array[n / 64]) != 0x0L;
    }
    
    static long[] zza(final BitSet set) {
        final int n = (set.length() + 63) / 64;
        final long[] array = new long[n];
        for (int i = 0; i < n; ++i) {
            array[i] = 0L;
            for (int j = 0; j < 64; ++j) {
                final int n2 = (i << 6) + j;
                if (n2 >= set.length()) {
                    break;
                }
                if (set.get(n2)) {
                    array[i] |= 1L << j;
                }
            }
        }
        return array;
    }
    
    static zzgg[] zza(final zzgg[] array, final String name, final Object o) {
        final int length = array.length;
        int i = 0;
        while (i < length) {
            final zzgg zzgg = array[i];
            if (name.equals(zzgg.name)) {
                zzgg.zzawx = null;
                zzgg.zzamp = null;
                zzgg.zzauh = null;
                if (o instanceof Long) {
                    zzgg.zzawx = (Long)o;
                    return array;
                }
                if (o instanceof String) {
                    zzgg.zzamp = (String)o;
                    return array;
                }
                if (o instanceof Double) {
                    zzgg.zzauh = (Double)o;
                }
                return array;
            }
            else {
                ++i;
            }
        }
        final zzgg[] array2 = new zzgg[array.length + 1];
        System.arraycopy(array, 0, array2, 0, array.length);
        final zzgg zzgg2 = new zzgg();
        zzgg2.name = name;
        if (o instanceof Long) {
            zzgg2.zzawx = (Long)o;
        }
        else if (o instanceof String) {
            zzgg2.zzamp = (String)o;
        }
        else if (o instanceof Double) {
            zzgg2.zzauh = (Double)o;
        }
        array2[array.length] = zzgg2;
        return array2;
    }
    
    static Object zzb(final zzgf zzgf, final String s) {
        final zzgg zza = zza(zzgf, s);
        if (zza != null) {
            if (zza.zzamp != null) {
                return zza.zzamp;
            }
            if (zza.zzawx != null) {
                return zza.zzawx;
            }
            if (zza.zzauh != null) {
                return zza.zzauh;
            }
        }
        return null;
    }
    
    static boolean zzcp(final String s) {
        return s != null && s.matches("([+-])?([0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]+)") && s.length() <= 310;
    }
    
    final <T extends Parcelable> T zza(final byte[] array, final Parcelable$Creator<T> parcelable$Creator) {
        if (array == null) {
            return null;
        }
        final Parcel obtain = Parcel.obtain();
        try {
            try {
                obtain.unmarshall(array, 0, array.length);
                obtain.setDataPosition(0);
                final Parcelable parcelable = (Parcelable)parcelable$Creator.createFromParcel(obtain);
                obtain.recycle();
                return (T)parcelable;
            }
            finally {}
        }
        catch (SafeParcelReader.ParseException ex) {
            this.zzgo().zzjd().zzbx("Failed to load parcelable from buffer");
            obtain.recycle();
            return null;
        }
        obtain.recycle();
    }
    
    final String zza(final zzfv zzfv) {
        if (zzfv == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("\nevent_filter {\n");
        final Integer zzave = zzfv.zzave;
        int i = 0;
        zza(sb, 0, "filter_id", zzave);
        zza(sb, 0, "event_name", this.zzgl().zzbs(zzfv.zzavf));
        this.zza(sb, 1, "event_count_filter", zzfv.zzavi);
        sb.append("  filters {\n");
        for (zzfw[] zzavg = zzfv.zzavg; i < zzavg.length; ++i) {
            this.zza(sb, 2, zzavg[i]);
        }
        zza(sb, 1);
        sb.append("}\n}\n");
        return sb.toString();
    }
    
    final String zza(final zzfy zzfy) {
        if (zzfy == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("\nproperty_filter {\n");
        zza(sb, 0, "filter_id", zzfy.zzave);
        zza(sb, 0, "property_name", this.zzgl().zzbu(zzfy.zzavu));
        this.zza(sb, 1, zzfy.zzavv);
        sb.append("}\n");
        return sb.toString();
    }
    
    final void zza(final zzgg zzgg, final Object o) {
        Preconditions.checkNotNull(o);
        zzgg.zzamp = null;
        zzgg.zzawx = null;
        zzgg.zzauh = null;
        if (o instanceof String) {
            zzgg.zzamp = (String)o;
            return;
        }
        if (o instanceof Long) {
            zzgg.zzawx = (Long)o;
            return;
        }
        if (o instanceof Double) {
            zzgg.zzauh = (Double)o;
            return;
        }
        this.zzgo().zzjd().zzg("Ignoring invalid (type) event param value", o);
    }
    
    final void zza(final zzgl zzgl, final Object o) {
        Preconditions.checkNotNull(o);
        zzgl.zzamp = null;
        zzgl.zzawx = null;
        zzgl.zzauh = null;
        if (o instanceof String) {
            zzgl.zzamp = (String)o;
            return;
        }
        if (o instanceof Long) {
            zzgl.zzawx = (Long)o;
            return;
        }
        if (o instanceof Double) {
            zzgl.zzauh = (Double)o;
            return;
        }
        this.zzgo().zzjd().zzg("Ignoring invalid (type) user attribute value", o);
    }
    
    final byte[] zza(final zzgh zzgh) {
        try {
            final int zzvu = zzgh.zzvu();
            final byte[] array = new byte[zzvu];
            final zzyy zzk = zzyy.zzk(array, 0, zzvu);
            zzgh.zza(zzk);
            zzk.zzyt();
            return array;
        }
        catch (IOException ex) {
            this.zzgo().zzjd().zzg("Data loss. Failed to serialize batch", ex);
            return null;
        }
    }
    
    final byte[] zza(byte[] byteArray) throws IOException {
        try {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            final GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final byte[] array = new byte[1024];
            while (true) {
                final int read = gzipInputStream.read(array);
                if (read <= 0) {
                    break;
                }
                byteArrayOutputStream.write(array, 0, read);
            }
            gzipInputStream.close();
            byteArrayInputStream.close();
            byteArray = byteArrayOutputStream.toByteArray();
            return byteArray;
        }
        catch (IOException ex) {
            this.zzgo().zzjd().zzg("Failed to ungzip content", ex);
            throw ex;
        }
    }
    
    final String zzb(final zzgh zzgh) {
        final StringBuilder sb = new StringBuilder();
        sb.append("\nbatch {\n");
        if (zzgh.zzawy != null) {
            final zzgi[] zzawy = zzgh.zzawy;
            for (int length = zzawy.length, i = 0; i < length; ++i) {
                final zzgi zzgi = zzawy[i];
                if (zzgi != null && zzgi != null) {
                    zza(sb, 1);
                    sb.append("bundle {\n");
                    zza(sb, 1, "protocol_version", zzgi.zzaxa);
                    zza(sb, 1, "platform", zzgi.zzaxi);
                    zza(sb, 1, "gmp_version", zzgi.zzaxm);
                    zza(sb, 1, "uploading_gmp_version", zzgi.zzaxn);
                    zza(sb, 1, "config_version", zzgi.zzaxy);
                    zza(sb, 1, "gmp_app_id", zzgi.zzafx);
                    zza(sb, 1, "admob_app_id", zzgi.zzawj);
                    zza(sb, 1, "app_id", zzgi.zztt);
                    zza(sb, 1, "app_version", zzgi.zzts);
                    zza(sb, 1, "app_version_major", zzgi.zzaxu);
                    zza(sb, 1, "firebase_instance_id", zzgi.zzafz);
                    zza(sb, 1, "dev_cert_hash", zzgi.zzaxq);
                    zza(sb, 1, "app_store", zzgi.zzage);
                    zza(sb, 1, "upload_timestamp_millis", zzgi.zzaxd);
                    zza(sb, 1, "start_timestamp_millis", zzgi.zzaxe);
                    zza(sb, 1, "end_timestamp_millis", zzgi.zzaxf);
                    zza(sb, 1, "previous_bundle_start_timestamp_millis", zzgi.zzaxg);
                    zza(sb, 1, "previous_bundle_end_timestamp_millis", zzgi.zzaxh);
                    zza(sb, 1, "app_instance_id", zzgi.zzafw);
                    zza(sb, 1, "resettable_device_id", zzgi.zzaxo);
                    zza(sb, 1, "device_id", zzgi.zzaxx);
                    zza(sb, 1, "ds_id", zzgi.zzaya);
                    zza(sb, 1, "limited_ad_tracking", zzgi.zzaxp);
                    zza(sb, 1, "os_version", zzgi.zzaxj);
                    zza(sb, 1, "device_model", zzgi.zzaxk);
                    zza(sb, 1, "user_default_language", zzgi.zzaia);
                    zza(sb, 1, "time_zone_offset_minutes", zzgi.zzaxl);
                    zza(sb, 1, "bundle_sequential_index", zzgi.zzaxr);
                    zza(sb, 1, "service_upload", zzgi.zzaxs);
                    zza(sb, 1, "health_monitor", zzgi.zzagv);
                    if (zzgi.zzaxz != null && zzgi.zzaxz != 0L) {
                        zza(sb, 1, "android_id", zzgi.zzaxz);
                    }
                    if (zzgi.zzayc != null) {
                        zza(sb, 1, "retry_counter", zzgi.zzayc);
                    }
                    final zzgl[] zzaxc = zzgi.zzaxc;
                    if (zzaxc != null) {
                        for (int length2 = zzaxc.length, j = 0; j < length2; ++j) {
                            final zzgl zzgl = zzaxc[j];
                            if (zzgl != null) {
                                zza(sb, 2);
                                sb.append("user_property {\n");
                                zza(sb, 2, "set_timestamp_millis", zzgl.zzayl);
                                zza(sb, 2, "name", this.zzgl().zzbu(zzgl.name));
                                zza(sb, 2, "string_value", zzgl.zzamp);
                                zza(sb, 2, "int_value", zzgl.zzawx);
                                zza(sb, 2, "double_value", zzgl.zzauh);
                                zza(sb, 2);
                                sb.append("}\n");
                            }
                        }
                    }
                    final zzgd[] zzaxt = zzgi.zzaxt;
                    if (zzaxt != null) {
                        for (int length3 = zzaxt.length, k = 0; k < length3; ++k) {
                            final zzgd zzgd = zzaxt[k];
                            if (zzgd != null) {
                                zza(sb, 2);
                                sb.append("audience_membership {\n");
                                zza(sb, 2, "audience_id", zzgd.zzauy);
                                zza(sb, 2, "new_audience", zzgd.zzawo);
                                zza(sb, 2, "current_data", zzgd.zzawm);
                                zza(sb, 2, "previous_data", zzgd.zzawn);
                                zza(sb, 2);
                                sb.append("}\n");
                            }
                        }
                    }
                    final zzgf[] zzaxb = zzgi.zzaxb;
                    if (zzaxb != null) {
                        for (int length4 = zzaxb.length, l = 0; l < length4; ++l) {
                            final zzgf zzgf = zzaxb[l];
                            if (zzgf != null) {
                                zza(sb, 2);
                                sb.append("event {\n");
                                zza(sb, 2, "name", this.zzgl().zzbs(zzgf.name));
                                zza(sb, 2, "timestamp_millis", zzgf.zzawu);
                                zza(sb, 2, "previous_timestamp_millis", zzgf.zzawv);
                                zza(sb, 2, "count", zzgf.count);
                                final zzgg[] zzawt = zzgf.zzawt;
                                if (zzawt != null) {
                                    for (int length5 = zzawt.length, n = 0; n < length5; ++n) {
                                        final zzgg zzgg = zzawt[n];
                                        if (zzgg != null) {
                                            zza(sb, 3);
                                            sb.append("param {\n");
                                            zza(sb, 3, "name", this.zzgl().zzbt(zzgg.name));
                                            zza(sb, 3, "string_value", zzgg.zzamp);
                                            zza(sb, 3, "int_value", zzgg.zzawx);
                                            zza(sb, 3, "double_value", zzgg.zzauh);
                                            zza(sb, 3);
                                            sb.append("}\n");
                                        }
                                    }
                                }
                                zza(sb, 2);
                                sb.append("}\n");
                            }
                        }
                    }
                    zza(sb, 1);
                    sb.append("}\n");
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }
    
    final boolean zzb(final long n, final long n2) {
        return n == 0L || n2 <= 0L || Math.abs(this.zzbx().currentTimeMillis() - n) > n2;
    }
    
    final byte[] zzb(byte[] byteArray) throws IOException {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(byteArray);
            gzipOutputStream.close();
            byteArrayOutputStream.close();
            byteArray = byteArrayOutputStream.toByteArray();
            return byteArray;
        }
        catch (IOException ex) {
            this.zzgo().zzjd().zzg("Failed to gzip content", ex);
            throw ex;
        }
    }
    
    final boolean zze(final zzad zzad, final zzh zzh) {
        Preconditions.checkNotNull(zzad);
        Preconditions.checkNotNull(zzh);
        if (TextUtils.isEmpty((CharSequence)zzh.zzafx) && TextUtils.isEmpty((CharSequence)zzh.zzagk)) {
            this.zzgr();
            return false;
        }
        return true;
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
}

package com.google.android.gms.internal.measurement;

import android.net.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.database.sqlite.*;
import android.database.*;
import java.util.*;

public final class zzsi
{
    private static final Object zzbqp;
    private static final Map<Uri, zzsi> zzbqq;
    private static final String[] zzbqw;
    private final Uri uri;
    private final ContentResolver zzbqr;
    private final Object zzbqs;
    private volatile Map<String, String> zzbqt;
    private final Object zzbqu;
    private final List<zzsk> zzbqv;
    
    static {
        zzbqp = new Object();
        zzbqq = new HashMap<Uri, zzsi>();
        zzbqw = new String[] { "key", "value" };
    }
    
    private zzsi(final ContentResolver zzbqr, final Uri uri) {
        this.zzbqs = new Object();
        this.zzbqu = new Object();
        this.zzbqv = new ArrayList<zzsk>();
        (this.zzbqr = zzbqr).registerContentObserver(this.uri = uri, false, (ContentObserver)new zzsj(this, null));
    }
    
    public static zzsi zza(final ContentResolver contentResolver, final Uri uri) {
        synchronized (zzsi.zzbqp) {
            zzsi zzsi;
            if ((zzsi = com.google.android.gms.internal.measurement.zzsi.zzbqq.get(uri)) == null) {
                zzsi = new zzsi(contentResolver, uri);
                com.google.android.gms.internal.measurement.zzsi.zzbqq.put(uri, zzsi);
            }
            return zzsi;
        }
    }
    
    private final Map<String, String> zztb() {
        try {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final Cursor query = this.zzbqr.query(this.uri, zzsi.zzbqw, (String)null, (String[])null, (String)null);
            if (query != null) {
                try {
                    while (query.moveToNext()) {
                        hashMap.put(query.getString(0), query.getString(1));
                    }
                    return hashMap;
                }
                finally {
                    query.close();
                }
            }
            return hashMap;
        }
        catch (SecurityException | SQLiteException ex) {
            Log.e("ConfigurationContentLoader", "PhenotypeFlag unable to load ContentProvider, using default values");
            return null;
        }
    }
    
    private final void zztc() {
        synchronized (this.zzbqu) {
            final Iterator<zzsk> iterator = this.zzbqv.iterator();
            while (iterator.hasNext()) {
                iterator.next().zztd();
            }
        }
    }
    
    public final Map<String, String> zzsz() {
        Map<String, String> map;
        if (zzsl.zzd("gms:phenotype:phenotype_flag:debug_disable_caching", false)) {
            map = this.zztb();
        }
        else {
            map = this.zzbqt;
        }
        Map<String, String> map2 = map;
        if (map == null) {
            synchronized (this.zzbqs) {
                Map<String, String> zzbqt;
                map2 = (zzbqt = this.zzbqt);
                if (map2 == null) {
                    zzbqt = this.zztb();
                    this.zzbqt = zzbqt;
                }
                // monitorexit(this.zzbqs)
                map2 = zzbqt;
            }
        }
        if (map2 != null) {
            return map2;
        }
        return Collections.emptyMap();
    }
    
    public final void zzta() {
        synchronized (this.zzbqs) {
            this.zzbqt = null;
        }
    }
}
